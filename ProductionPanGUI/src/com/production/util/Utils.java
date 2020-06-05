package com.production.util;

import com.production.domain.AgeByWCFields;
import com.production.domain.FabLoadByWCFields;
import com.production.domain.SimpleWorkOrderInformation;
import com.production.domain.WorkOrderInformation;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

/**
 * @author lgutierr
 */
public final class Utils {
    
    /**
     * "factory" method to build a FileChooser for .XLS files.
     * @return a JFileChooser to select .XLS files
     */
    public static JFileChooser genericXLSFileChooser() {
        final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a .XLS file");
        jfc.setAcceptAllFileFilterUsed(false);
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls");
        jfc.addChoosableFileFilter(filter);
        return jfc;
    }
    
    public static String[] dataFromModelAt(final TableModel model, final int rowIndex) {
        
        final int columnCount = model.getColumnCount();
        final String[] data = new String[columnCount];
        
        for (int i = 0; i < columnCount; i++) {
            data[i] = model.getValueAt(rowIndex, i).toString();
        }
        
        return data;
    }
    
    public static String getPartNumberFromRow(final TableModel model, final int rowIndex) {
        if (rowIndex < 0) {
            return "";
        }
        return model.getValueAt(rowIndex, 0).toString();
    }

    public static List<WorkOrderInformation> extractWorkOrdersFromSheetFile(final String filePath)
            throws IOException, InvalidFormatException {
        final List<WorkOrderInformation> workOrderInfoItems = new ArrayList<>();
        // Change this for something like a constant ...

        try (final Workbook workbook = WorkbookFactory.create(new File(filePath))) {
            final Sheet fabLoadByWCSheet = workbook.getSheet(Constants.TAB_SHEET_NAME);
            final Iterator<Row> rowIterator = fabLoadByWCSheet.rowIterator();
            while (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                final Cell firstCell = row.getCell(0);
                final String firstCellStringCellValue = firstCell.getStringCellValue();
                switch (firstCellStringCellValue.trim()) {
                    case "WC Name":
                    case "Count":
                    case "Sum":
                        continue;
                }

                final Cell wcDescription = row.getCell(FabLoadByWCFields.WC_DESCRIPTION_INDEX.get());
                
                if (!allowedWC(wcDescription.getStringCellValue())) {
                    continue;
                }
                
                final Cell partCell = row.getCell(FabLoadByWCFields.PART_CELL_INDEX.get());
                final Cell workOrderCell = row.getCell(FabLoadByWCFields.WORKORDER_CELL_INDEX.get());
                final Cell runCell = row.getCell(FabLoadByWCFields.RUN_CELL_INDEX.get());
                final Cell setupCell = row.getCell(FabLoadByWCFields.SETUP_CELL_INDEX.get());
                final Cell qtyCell = row.getCell(FabLoadByWCFields.QTY_CELL_INDEX.get());

                workOrderInfoItems.add(to(wcDescription, partCell, workOrderCell, runCell, setupCell, qtyCell));
            }
        }
        return workOrderInfoItems;
    }
    
    private static String sanitizeWorkCenterName(String wc) {
        wc = wc.trim().toUpperCase().replace(" ", "_");
        return wc.replace("-", "_");
    }
    
    public static boolean allowedWC(final String wc) {
        boolean ok = false;
        switch (sanitizeWorkCenterName(wc)) {
            case "DOBLADO":
            case "EMPAQUE_A_PROVEEDOR":
            case "EMPAQUE_FINAL":
            case "ENSAMBLE":
            case "INSERTOS_PEM":
            case "INSPECCION_DE_ACABADOS":
            case "LASER":
            case "LIMPIEZA":
            case "LIMPIEZA_LUZ_NEGRA":
            case "MAQUINADO_CNC":
            case "MAQUINADO_MANUAL":
            case "PINTURA_EN_POLVO":
            case "PULIDO":
            case "PUNZONADO":
            case "REBABEO":
            case "SERIGRAFIA":
            case "SOLDADURA":
            case "SPOT_WELD":
            case "SURTIR_MATERIAL":
            case "TIME_SAVER":
            case "TRATAMIENTO_QUIMICO":
                ok = true;
                break;  
        }
        return ok;
    }

    public static void reconcileInformationFromAgeFile(
            final String ageAndPriceFilePath, final List<WorkOrderInformation> workOrderItems) 
            throws IOException, InvalidFormatException {

        final Map<String, SimpleWorkOrderInformation> workOrdersFromAgeFile = new HashMap<>();

        // Generate a map from the spread sheet ...
        try (final Workbook workbook = WorkbookFactory.create(new File(ageAndPriceFilePath))) {
            final Sheet fabLoadByWCSheet = workbook.getSheet(Constants.AGE_BY_WC_SHEET_NAME);
            final Iterator<Row> rowIterator = fabLoadByWCSheet.rowIterator();
            while (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                final Cell firstCell = row.getCell(0);
                final String firstCellStringCellValue = firstCell.getStringCellValue();
                switch (firstCellStringCellValue.trim()) {
                    case "WC Name":
                    case "Count":
                    case "Sum":
                        continue;
                }

                final SimpleWorkOrderInformation workOrderInfo = extractSimpleWorkOrderFromAgeRow(row);
                workOrdersFromAgeFile.put(workOrderInfo.getWorkOrder(), workOrderInfo);
            }
        }

        // Reconcile the information ...
        workOrderItems.forEach(workOrder -> {
            if (workOrdersFromAgeFile.containsKey(workOrder.getWorkOrder())) {
                final SimpleWorkOrderInformation simpleWorkOrderInformation = workOrdersFromAgeFile.get(workOrder.getWorkOrder());
                workOrder.setAge(simpleWorkOrderInformation.getAge());
                workOrder.setSalesPrice(simpleWorkOrderInformation.getSalesPrice());
            }
        });
    }

    private static SimpleWorkOrderInformation extractSimpleWorkOrderFromAgeRow(final Row row) {
        final Cell workOrderCell = row.getCell(AgeByWCFields.WORK_ORDER_NUMBER.get());
        final Cell ageCell = row.getCell(AgeByWCFields.AGE.get());
        final Cell salesPriceCell = row.getCell(AgeByWCFields.SALES_PRICE.get());

        final SimpleWorkOrderInformation workOrderInfo = new SimpleWorkOrderInformation(workOrderCell.getStringCellValue());

        workOrderInfo.setAge((int)ageCell.getNumericCellValue());
        workOrderInfo.setSalesPrice(salesPriceCell.getNumericCellValue());

        return workOrderInfo;
    }

    private static WorkOrderInformation to(
            final Cell wcDescriptionCell
            , final Cell partCell
            , final Cell workOrderCell
            , final Cell runCell
            , final Cell setupCell
            , final Cell qtyCell
    ) {
        final WorkOrderInformation workOrderInformation = new WorkOrderInformation();
        workOrderInformation.setWcDescription(wcDescriptionCell.getStringCellValue());
        workOrderInformation.setPartNumber(partCell.getStringCellValue().trim());
        workOrderInformation.setWorkOrder(workOrderCell.getStringCellValue().trim());

        final double runNumericCellValue = runCell.getNumericCellValue();

        workOrderInformation.setRunHours(runNumericCellValue / Constants.RUN_EFFICIENCY);
        workOrderInformation.setSetupHours(setupCell.getNumericCellValue());
        workOrderInformation.setQty((int)qtyCell.getNumericCellValue());

        return workOrderInformation;
    }
    
    private Utils() {}
    
}
