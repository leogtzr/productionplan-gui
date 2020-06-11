package com.production.util;

import com.production.domain.AgeByWCFields;
import com.production.domain.FabLoadByWCFields;
import com.production.domain.Priority;
import com.production.domain.SimpleWorkOrderInformation;
import com.production.domain.WorkCenterTurns;
import com.production.domain.WorkOrderInformation;
import com.production.domain.WorkOrderWrapper;
import com.production.lang.MissingTests;
import com.production.lang.Validated;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Workbook;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import static com.production.util.Constants.DOBLADO;
import static com.production.util.Constants.TAB_SHEET_NAME;
import static com.production.util.Constants.AGE_BY_WC_SHEET_NAME;
import static com.production.util.Constants.RUN_EFFICIENCY;
import java.util.LinkedHashMap;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
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
    
    @Validated
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
            final Sheet fabLoadByWCSheet = workbook.getSheet(TAB_SHEET_NAME);
            final Iterator<Row> rowIterator = fabLoadByWCSheet.rowIterator();
            while (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                final Cell firstCell = row.getCell(0);
                final String firstCellStringCellValue = firstCell.getStringCellValue();
                switch (firstCellStringCellValue.trim()) {
                    case "WC Name", "Count", "Sum" -> {
                        continue;
                    }
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
    
    @Validated
    public static String sanitizeWorkCenterName(String wc) {
        wc = wc.trim().toUpperCase().replace(" ", "_");
        return wc.replace("-", "_");
    }
    
    @Validated
    public static boolean allowedWC(final String wc) {
        boolean ok = false;
        switch (sanitizeWorkCenterName(wc)) {
            case "DOBLADO", "EMPAQUE_A_PROVEEDOR", "EMPAQUE_FINAL", "ENSAMBLE", 
                    "INSERTOS_PEM", "INSPECCION_DE_ACABADOS", "LASER", "LIMPIEZA", 
                    "LIMPIEZA_LUZ_NEGRA", "MAQUINADO_CNC", "MAQUINADO_MANUAL", 
                    "PINTURA_EN_POLVO", "PULIDO", "PUNZONADO", "REBABEO", "SERIGRAFIA", 
                    "SOLDADURA", "SPOT_WELD", "SURTIR_MATERIAL", "TIME_SAVER", 
                    "TRATAMIENTO_QUIMICO" -> 
                ok = true;
        }
        return ok;
    }

    public static void reconcileInformationFromAgeFile(
            final String ageAndPriceFilePath, final List<WorkOrderInformation> workOrderItems) 
            throws IOException, InvalidFormatException {

        final Map<String, SimpleWorkOrderInformation> workOrdersFromAgeFile = new HashMap<>();

        // Generate a map from the spread sheet ...
        try (final Workbook workbook = WorkbookFactory.create(new File(ageAndPriceFilePath))) {
            final Sheet fabLoadByWCSheet = workbook.getSheet(AGE_BY_WC_SHEET_NAME);
            final Iterator<Row> rowIterator = fabLoadByWCSheet.rowIterator();
            while (rowIterator.hasNext()) {
                final Row row = rowIterator.next();
                final Cell firstCell = row.getCell(0);
                final String firstCellStringCellValue = firstCell.getStringCellValue();
                switch (firstCellStringCellValue.trim()) {
                    case "WC Name", "Count", "Sum" -> {
                        continue;
                    }
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

        workOrderInformation.setRunHours(runNumericCellValue / RUN_EFFICIENCY);
        workOrderInformation.setSetupHours(setupCell.getNumericCellValue());
        workOrderInformation.setQty((int)qtyCell.getNumericCellValue());

        return workOrderInformation;
    }
    
    @Validated
    public static int numberOfTurnsFromWorkCenter(final String workCenterName) {
        final WorkCenterTurns wc = WorkCenterTurns.valueOf(sanitizeWorkCenterName(workCenterName));
        return wc.turns();
    }
    
    public static Map<String, String> loadCSVFile(final String filePath) throws IOException {
        
        final Map<String, String> machineInfo = new HashMap<>();
        
        final List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 0; i < lines.size(); i++) {
            // Skip the header:
            if (i == 0) {
                continue;
            }
            final String line = lines.get(i);
            final String[] tokens = line.split(",");
            String workCenter = workCenterName(tokens[1].trim());
            machineInfo.put(tokens[0], workCenter);
        }
            return machineInfo;
    }
    
    @Validated
    public static String workCenterName(final String wc) {
        final String[] possibleTokens = wc.trim().toUpperCase().split("\\s+");
        return possibleTokens[0];
    }
    
    @Validated
    public static Map<String, Integer> partsNumbersOccurrenceCount(final List<WorkOrderInformation> workOrderItems) {
        final Map<String, Integer> partsNumbersOccurrenceCount = workOrderItems.stream().collect(Collectors.toMap(
                k -> k.getPartNumber(),
                v -> 1,
                Integer::sum
        ));
        return partsNumbersOccurrenceCount;
    }
    
    @Validated
    public static Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber(
            final List<WorkOrderInformation> workOrderItems) {
        
        final Map<String, List<WorkOrderInformation>> parts = new LinkedHashMap<>();
        
        workOrderItems.forEach(woInfo -> 
                parts.computeIfAbsent(woInfo.getPartNumber(), items -> new ArrayList<>()).add(woInfo));
        
        return parts;
    }
    
    @Validated
    public static void updateStatusBar(final JLabel statusLabel, final File fabLoadFilePath, final File ageByWCFilePath) {
        if (fabLoadFilePath == null && ageByWCFilePath == null) {
            statusLabel.setText("Please open the required files.");
        } else if (fabLoadFilePath == null && ageByWCFilePath != null) {
            statusLabel.setText("Please open the file containing the 'FAB Load by WC' information.");
        } else if (fabLoadFilePath != null && ageByWCFilePath == null) {
            statusLabel.setText("Please open the file containing the 'Age  by WC' information.");
        } else if (fabLoadFilePath != null && ageByWCFilePath != null) {
            statusLabel.setText("Files ready.");
        }
    }
    
    @MissingTests
    public static String getMachineFromWorkCenter(
            final Map<String, String> doblado
            , final Map<String, String> laserAndPunch
            , final String partNumber
            , final String workCenter
    ) {
        // Nasty code ...
        final String machine = 
                workCenter.toUpperCase().trim().equalsIgnoreCase(DOBLADO)
                ? doblado.getOrDefault(partNumber, "")
                : laserAndPunch.getOrDefault(partNumber, "");
        return machine;
    }
    
    @MissingTests
    public static String buildHtmlContent(
            final String workCenter
            , final List<WorkOrderInformation> workOrderItems
            , final List<Priority> priorities) {
        
        // Items in the "priorities" list will go first.
        
        final int numberOfTurns = numberOfTurnsFromWorkCenter(workCenter);
        /*
tomar en cuenta que cuando hay partes iguales en un WorkCenter ... un mismo setup aplica para ello.
Es decir: si hay dos part numbers iguales, solo el primero tendrïa un setup ...
el segundo se aprovecha
        */
        
        switch (numberOfTurns) {
            case 0:                 // Build a simple list ... 
                                    // TODO: How is the list built?
                // HOW: alv
                break;
            case 2:                 // Only two turns ...
                buildPlanForTwoTurns(workCenter, workOrderItems, priorities);
                break;
            case 3:                 // Use three turns ...
                break;
        }
        return "";
    }
    
    public static List<WorkOrderWrapper> buildPlanForTwoTurns(
            final String workCenter
            , final List<WorkOrderInformation> workOrderItems
            , final List<Priority> priorities
    ) {
        
        final List<WorkOrderWrapper> plan = new ArrayList<>();
        
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(workOrderItems);
        // We will iterate in order ... 
        for (final WorkOrderInformation woInfo : workOrderItems) {
            final String partNumber = woInfo.getPartNumber();
            // TODO: ... algorithm to build the shit ... 
        }
        
        return plan;
    }
    
    private Utils() {}
    
}
