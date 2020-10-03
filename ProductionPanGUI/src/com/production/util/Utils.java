package com.production.util;

import com.production.domain.AgeByWCFields;
import com.production.domain.AgeComparator;
import com.production.domain.Day;
import com.production.domain.FabLoadByWCFields;
import com.production.domain.Priority;
import com.production.domain.SimpleWorkOrderInformation;
import com.production.domain.Turn;
import com.production.domain.WorkCenter;
import com.production.domain.WorkOrderInformation;
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
import static com.production.util.Constants.FIRST_TURN_LENGTH;
import static com.production.util.Constants.SECOND_TURN_LENGTH;
import static com.production.util.Constants.THIRD_TURN_LENGTH;

import static com.production.domain.Turn.*;
import static com.production.domain.Day.*;
import com.production.domain.efficiency.EfficiencyInformation;
import com.production.domain.efficiency.Progress;
import com.production.util.html.HTMLFormat;
import com.production.util.logging.Logging;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import java.util.LinkedHashMap;
import java.util.Optional;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public final class Utils {
    
    public static DateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy.MM.dd");
    
    /**
     * "factory" method to build a FileChooser for .XLS files.
     * @return a JFileChooser to select .XLS files
     */
    @Validated
    public static JFileChooser genericXLSFileChooser() {
        final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a .XLS file");
        jfc.setAcceptAllFileFilterUsed(false);
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls");
        jfc.addChoosableFileFilter(filter);
        return jfc;
    }
    
    @Validated
    public static JFileChooser createFileChooser(final String dialogTitle, final String saveDirectory) {
        final JFileChooser jfc = new JFileChooser(saveDirectory);
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jfc.setDialogTitle(dialogTitle);
        jfc.setAcceptAllFileFilterUsed(false);
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

                final WorkOrderInformation wo = to(wcDescription, partCell, workOrderCell, runCell, setupCell, qtyCell);
                
                if (wo.getRunHours() != 0.0 || wo.getSetupHours() != 0.0) {
                    workOrderInfoItems.add(wo);
                } else {                // ERROR handling or logging
                    // System.out.printf("[%s] has been filtered due to the 0.0 restriction.\n", wo);
                }
                
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
        final WorkCenter wc = WorkCenter.valueOf(sanitizeWorkCenterName(workCenterName));
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
            String workCenter = tokens[1].trim();
            machineInfo.put(tokens[0], workCenter);
        }
            return machineInfo;
    }
    
    @Validated
    // PENDING: this might not be needed.
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
    
    /**
     * @param workOrderItems items
     * @return a map containing a list of WorkOrderInformation items as values, and the machine as key.
     */
    @Validated
    public static Map<String, List<WorkOrderInformation>> workOrderItemsPerMachine(
            final List<WorkOrderInformation> workOrderItems) {
        final Map<String, List<WorkOrderInformation>> machines = new LinkedHashMap<>();
        workOrderItems.stream()
                .filter(woInfo -> !woInfo.getMachine().isBlank())
                .forEach(woInfo -> machines.computeIfAbsent(woInfo.getMachine(), items -> new ArrayList<>()).add(woInfo));
        return machines;
    }
    
    @Validated
    public static Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber(
            final List<WorkOrderInformation> workOrderItems) {
        final Map<String, List<WorkOrderInformation>> parts = new LinkedHashMap<>();
        workOrderItems.forEach(woInfo -> parts.computeIfAbsent(woInfo.getPartNumber(), items -> new ArrayList<>()).add(woInfo));
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
    
    @Validated
    public static String getMachineFromWorkCenter(
            final Map<String, String> doblado
            , final Map<String, String> laserAndPunch
            , final String partNumber
            , final String workCenter
    ) {
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
            , final List<Priority> priorities) throws IOException {
        
        // Items in the "priorities" list will go first.
        final int numberOfTurns = numberOfTurnsFromWorkCenter(workCenter);
        
        if (numberOfTurns == 0) {
            final List<WorkOrderInformation> planItems = buildPlanList(workCenter, workOrderItems, priorities);
            final Path templateHTMLPath = TemplateFileUtils.getTemplateFilePath("list-template.html");

            if (templateHTMLPath.toFile().exists()) {
                final String templateHTMLContent = Files.readString(templateHTMLPath);
                final String htmlPlan = HTMLFormat.generateHTMLContentForListPlan(templateHTMLContent, workCenter, planItems);
                return htmlPlan;
            }
        } else {
            // PENDING: analyze this.
            // final List<WorkOrderInformation> planItems = buildPlanForTwoTurns(workCenter, workOrderItems, priorities);
            final List<WorkOrderInformation> planItems = Utils.buildPlan(workCenter, workOrderItems, priorities);
            
            //System.out.printf("DEBUG HTML PLAN - begin\n");
            //planItems.forEach(System.out::println);
            //System.out.printf("DEBUG HTML PLAN - end\n");
            
            final Path templateHTMLPath = TemplateFileUtils.getTemplateFilePath("turns-template.html");
            
            if (templateHTMLPath.toFile().exists()) {
                final String templateHTMLContent = Files.readString(templateHTMLPath);
                final String htmlPlan = HTMLFormat.generateHTMLContentForTwoTurns(templateHTMLContent, workCenter, planItems);
                return htmlPlan;
            } else {
                throw new IOException("Template HTML files not found.");
            }
        }
        return "";
    }
    
    @Validated
    public static Day nextDay(final Day day) {
        switch (day) {
            case MONDAY -> {
                return TUESDAY;
            }
            case TUESDAY -> {
                return WEDNESDAY;
            }
            case WEDNESDAY -> {
                return THURSDAY;
            }
            case THURSDAY -> {
                return FRIDAY;
            }
            case FRIDAY -> {
                return SATURDAY;
            }
            case SATURDAY -> {
                return SUNDAY;
            }
            case SUNDAY -> {
                return MONDAY;
            }
        }
        return MONDAY;
    }
    
    private static void removePrioritizedItemsFromWorkOrderItems(
            final WorkOrderInformation toBeRemoved
            , List<WorkOrderInformation> workOrderItems
    ) {
        final Iterator<WorkOrderInformation> iterator = workOrderItems.iterator();
        while (iterator.hasNext()) {
            final WorkOrderInformation wo = iterator.next();
            if (wo.getPartNumber().equalsIgnoreCase(toBeRemoved.getPartNumber())) {
                iterator.remove();
            }
        }
    }
    
    @Validated
    public static List<WorkOrderInformation> buildPlanList(
            final String workCenter
            , List<WorkOrderInformation> workOrderItems
            , final List<Priority> priorities
    ) {
        // Before sorting ... 
        List<WorkOrderInformation> priorityWorkOrderItems = new ArrayList<>();
        for (final Priority priority : priorities) {
            priorityWorkOrderItems.addAll(
                workOrderItems.stream()
                    .filter(wo -> wo.getPartNumber().equalsIgnoreCase(priority.getPartNumber()))
                    .collect(Collectors.toList())
            );
        }
        
        for (final WorkOrderInformation wo : priorityWorkOrderItems) {
            removePrioritizedItemsFromWorkOrderItems(wo, workOrderItems);
        }
        
        priorityWorkOrderItems = sortAndGroup(priorityWorkOrderItems, new AgeComparator());
        workOrderItems = sortAndGroup(workOrderItems, new AgeComparator());
        
        final List<WorkOrderInformation> joined = new ArrayList<>(priorityWorkOrderItems);
        joined.addAll(workOrderItems);
        
        // Before this the lists have to be joined.
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(joined);
        
        for (final WorkOrderInformation woInfo : joined) {
            
            final String partNumber = woInfo.getPartNumber();
            final List<WorkOrderInformation> partNumbers = workOrderItemsPerPartNumber.get(partNumber);
            // Check if a part number has more than one element so we can adjust the setup time after the first element.
            if (partNumbers.size() > 1) {
                for (int i = 1; i < partNumbers.size(); i++) {
                    final WorkOrderInformation wo = partNumbers.get(i);
                    wo.setSetupHours(0.0D);
                }
            }
        }
        
        return joined;
    }
    
    @MissingTests
    public static List<WorkOrderInformation> buildPlan(
        final String workCenter
        , List<WorkOrderInformation> workOrderItems
        , final List<Priority> priorities
    ) {
       
        // PENDING: here the old algorithm is used, it needs to be fixed.
        // Before sorting ... 
        /*
            En el siguiente código, de las prioridades que el cliente provee (la tabla a la derecha) 
            tomamos esos items y los guardamos en una lista.
        
            Como tenemos esos mismos items en "workOrderItems" (el cual recibimos como argumento), necesitamos removerlo de ahí.
        
            Ver el código a continuación: 
            for (final WorkOrderInformation wo : priorityWorkOrderItems) {
                removePrioritizedItemsFromWorkOrderItems(wo, workOrderItems);
            }
        */
        
        List<WorkOrderInformation> priorityWorkOrderItems = new ArrayList<>();
        for (final Priority priority : priorities) {
            priorityWorkOrderItems.addAll(
                workOrderItems.stream()
                    .filter(wo -> wo.getPartNumber().equalsIgnoreCase(priority.getPartNumber()))
                    .collect(Collectors.toList())
            );
        }
        
        for (final WorkOrderInformation wo : priorityWorkOrderItems) {
            removePrioritizedItemsFromWorkOrderItems(wo, workOrderItems);
        }
        
        priorityWorkOrderItems = sortAndGroup(priorityWorkOrderItems, new AgeComparator());
        workOrderItems = sortAndGroup(workOrderItems, new AgeComparator());
        
        final List<WorkOrderInformation> joined = new ArrayList<>(priorityWorkOrderItems);
        joined.addAll(workOrderItems);
        
        // Before this the lists have to be joined.
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(joined);
        
        // The following variable will be used to accumulate
        // PENDING: Use the algorithm here, check how to adapt it.
        
        final Progress progress = new Progress(Turn.FIRST, 0.0D, Day.MONDAY);
        
        final List<WorkOrderInformation> result = new ArrayList<>();
        
        for (final WorkOrderInformation woInfo : joined) {
            // PENDING: analyze if the following line is necessary or not.
            // PENDING: woInfo.setDay(day);
            
            final String partNumber = woInfo.getPartNumber();
            final List<WorkOrderInformation> partNumbers = workOrderItemsPerPartNumber.get(partNumber);
            // Check if a part number has more than one element so we can adjust the setup time after the first element.
            if (partNumbers.size() > 1) {
                for (int i = 1; i < partNumbers.size(); i++) {
                    final WorkOrderInformation wo = partNumbers.get(i);
                    wo.setSetupHours(0.0D);
                }
            }
            
            // Call the algo here:
            final EfficiencyInformation ordersWithEfficiency = EfficiencyUtils.efficiency(woInfo, progress);
            
            if (ordersWithEfficiency.getOrders().isEmpty()) {
                // System.out.println("Analysis for: [%s] was empty ..., moving on");
                continue;
            }
            
            //System.out.printf("Efficiency for: [%s]\n", woInfo);
            //System.out.printf("With progress: %s\n", progress);
            //printDebugOrders(ordersWithEfficiency.getOrders());
            result.addAll(ordersWithEfficiency.getOrders());
            int numberOfOrdersInResult = ordersWithEfficiency.getOrders().size();
            
            numberOfOrdersInResult = numberOfOrdersInResult == 0 ? 0 : numberOfOrdersInResult - 1;
            
            //System.out.printf("CHECK: The size is: %d\n", numberOfOrdersInResult);
            
            final Turn lastTurn = ordersWithEfficiency.getOrders().get(numberOfOrdersInResult).getTurn();

            //System.out.printf("lastTu/rn to use is: %s\n", lastTurn);
            //System.out.printf("Calculating factor with: %s\n", result);
            final double factor = Utils.progressFactor(result);

            progress.setTurn(lastTurn);
            progress.setFactor(factor);
        }
        
        return result;
    }
    
    @Validated
    public static List<WorkOrderInformation> buildPlanForTwoTurns(
        final String workCenter
        , List<WorkOrderInformation> workOrderItems
        , final List<Priority> priorities
    ) { 
        // PENDING: here the old algorithm is used, it needs to be fixed.
        // Before sorting ... 
        /*
            En el siguiente código, de las prioridades que el cliente provee (la tabla a la derecha) 
            tomamos esos items y los guardamos en una lista.
        
            Como tenemos esos mismos items en "workOrderItems" (el cual recibimos como argumento), necesitamos removerlo de ahí.
        
            Ver el código a continuación: 
            for (final WorkOrderInformation wo : priorityWorkOrderItems) {
                removePrioritizedItemsFromWorkOrderItems(wo, workOrderItems);
            }
        */
        List<WorkOrderInformation> priorityWorkOrderItems = new ArrayList<>();
        for (final Priority priority : priorities) {
            priorityWorkOrderItems.addAll(
                workOrderItems.stream()
                    .filter(wo -> wo.getPartNumber().equalsIgnoreCase(priority.getPartNumber()))
                    .collect(Collectors.toList())
            );
        }
        
        for (final WorkOrderInformation wo : priorityWorkOrderItems) {
            removePrioritizedItemsFromWorkOrderItems(wo, workOrderItems);
        }
        
        priorityWorkOrderItems = sortAndGroup(priorityWorkOrderItems, new AgeComparator());
        workOrderItems = sortAndGroup(workOrderItems, new AgeComparator());
        
        final List<WorkOrderInformation> joined = new ArrayList<>(priorityWorkOrderItems);
        joined.addAll(workOrderItems);
        
        // Before this the lists have to be joined.
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(joined);
        
        Day day = MONDAY;
        // The following variable will be used to accumulate
        // PENDING: Use the algorithm here, check how to adapt it.
        
        double turnHours = 0.0D;
        for (final WorkOrderInformation woInfo : joined) {
            woInfo.setDay(day);
            
            final String partNumber = woInfo.getPartNumber();
            final List<WorkOrderInformation> partNumbers = workOrderItemsPerPartNumber.get(partNumber);
            // Check if a part number has more than one element so we can adjust the setup time after the first element.
            if (partNumbers.size() > 1) {
                for (int i = 1; i < partNumbers.size(); i++) {
                    final WorkOrderInformation wo = partNumbers.get(i);
                    wo.setSetupHours(0.0D);
                }
            }
            
            // Get the total hours for the individual list:
            final double hours = woInfo.getRunHours() + woInfo.getSetupHours();
            turnHours += hours;
            
            final Turn turn = hoursTo2Turns(turnHours);
            switch (turn) {
                case FIRST -> woInfo.setTurn(FIRST);
                case SECOND -> woInfo.setTurn(SECOND);
                case FIRST_NEXT_DAY -> {
                    woInfo.setTurn(FIRST);
                    day = nextDay(day);
                    woInfo.setDay(day);
                    turnHours = 0.0D;
                }
            }
        }
        
        return joined;
    }
    
    @Validated
    public static Turn hoursTo2Turns(final double turnHours) {
        if (turnHours < FIRST_TURN_LENGTH) {
            return FIRST;
        } else if ((turnHours > FIRST_TURN_LENGTH) && (turnHours < (FIRST_TURN_LENGTH + SECOND_TURN_LENGTH))) {
            return SECOND;
        } else if (turnHours > (FIRST_TURN_LENGTH + SECOND_TURN_LENGTH)) {
            return FIRST_NEXT_DAY;
        }
        return NA;
    }
    
    @Validated
    public static Turn hoursTo3Turns(final double turnHours) {
        if (turnHours < FIRST_TURN_LENGTH) {
            return FIRST;
        } else if ((turnHours > FIRST_TURN_LENGTH) && (turnHours < (FIRST_TURN_LENGTH + SECOND_TURN_LENGTH))) {
            return SECOND;
        } else if ((turnHours > (FIRST_TURN_LENGTH + SECOND_TURN_LENGTH)) && 
                (turnHours < (FIRST_TURN_LENGTH + SECOND_TURN_LENGTH + THIRD_TURN_LENGTH))) {
            return THIRD;
        } else if ((turnHours > (FIRST_TURN_LENGTH + SECOND_TURN_LENGTH + THIRD_TURN_LENGTH))) {
            return FIRST_NEXT_DAY;
        }
        return NA;
    }
    
    @Validated
    public static List<WorkOrderInformation> buildPlanForThreeTurns(
        final String workCenter
        , final List<WorkOrderInformation> workOrderItems
        , final List<Priority> priorities
    ) {
        
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(workOrderItems);
        
        Day day = MONDAY;
        // The following variable will be used to accumulate
        double turnHours = 0.0D;
        for (final WorkOrderInformation woInfo : workOrderItems) {
            
            woInfo.setDay(day);
            
            final String partNumber = woInfo.getPartNumber();
            final List<WorkOrderInformation> partNumbers = workOrderItemsPerPartNumber.get(partNumber);
            // Check if a part number has more than one element so we can adjust the setup time after the first element.
            if (partNumbers.size() > 1) {
                for (int i = 1; i < partNumbers.size(); i++) {
                    final WorkOrderInformation wo = partNumbers.get(i);
                    wo.setSetupHours(0.0D);
                }
            }
            
            // Get the total hours for the individual list:
            final double hours = woInfo.getRunHours() + woInfo.getSetupHours();
            turnHours += hours;
            
            final Turn turn = hoursTo3Turns(turnHours);
            switch (turn) {
                case FIRST -> woInfo.setTurn(FIRST);
                case SECOND -> woInfo.setTurn(SECOND);
                case THIRD -> woInfo.setTurn(THIRD);
                case FIRST_NEXT_DAY -> {
                    woInfo.setTurn(FIRST);
                    day = nextDay(day);
                    woInfo.setDay(day);
                    turnHours = 0.0D;
                }
            }
            
        }
        
        return workOrderItems;
    }
    
    @Validated
    public static double sumTurnHoursFromWorkOrderItems(final List<WorkOrderInformation> items) {
        return items.stream().mapToDouble(wo -> wo.getRunHours() + wo.getSetupHours()).sum();
    }
    
    @Validated
    public static boolean updateMachine(
            final int row
            , final String newMachine
            , final String workCenter
            , final List<WorkOrderInformation> workOrderInformationItems) {
        
        boolean updated = false;
        
        final List<WorkOrderInformation> filteredItems = workOrderInformationItems
                .stream()
                .filter(wo -> wo.getWcDescription().equalsIgnoreCase(workCenter))
                .collect(Collectors.toList());
        
        if (row < filteredItems.size()) {
            final WorkOrderInformation toUpdate = filteredItems.get(row);
            toUpdate.setMachine(newMachine);
            updated = true;
        }
        
        return updated;       
    }
    
    @Validated
    public static List<WorkOrderInformation> sortAndGroup(
            final List<WorkOrderInformation> items, 
            final Comparator<WorkOrderInformation> comparator) {
        Collections.sort(items, comparator);
        final List<WorkOrderInformation> other = new ArrayList<>();

        final Map<String, List<WorkOrderInformation>> group = workOrderItemsPerPartNumber(items);
        group.forEach((pt, wos) -> other.addAll(wos));
        
        return other;
    }
    
    @Validated
    public static void copyWorkOrderItems(final List<WorkOrderInformation> dest, final List<WorkOrderInformation> source) {
        dest.clear();
        source.forEach(woItem -> dest.add(new WorkOrderInformation(woItem)));
    }
    
    @Validated
    public static boolean shouldTheFilesBeSplit(final String workCenter) {
        final String sanitizedWorkCenter = sanitizeWorkCenterName(workCenter);
        return sanitizedWorkCenter.equalsIgnoreCase(Constants.DOBLADO) ||
                sanitizedWorkCenter.equalsIgnoreCase(Constants.PUNZONADO);
    }
    
    @MissingTests
    // TODO: call this method.
    public static void updateMachineInMaps(
        final String workCenter
        , final String partNumber
        , final String machine
        , final Map<String, String> dobladoPartInformation
        , final Map<String, String> laserAndPunchPartInformation
    ) {
        machineMapByWorkCenter(workCenter, dobladoPartInformation, laserAndPunchPartInformation)
            .ifPresentOrElse(machines -> {
                Logging.info("Updating %s part number with %s as machine.", partNumber, machine);
                machines.put(partNumber, machine);
            }, () -> {
                // Logging ... 
            });
        
    }
    
    @MissingTests
    public static Optional<Map<String, String>> machineMapByWorkCenter(
        final String workCenter
        , final Map<String, String> dobladoPartInformation
        , final Map<String, String> laserAndPunchPartInformation
    ) {
        final String sanitizedWorkCenter = sanitizeWorkCenterName(workCenter);
        switch (sanitizedWorkCenter) {
            case Constants.DOBLADO:
                return Optional.of(dobladoPartInformation);
            case Constants.PUNZONADO:
            case Constants.LASER:
                return Optional.of(laserAndPunchPartInformation);
        }
        return Optional.empty();
    }
    
    @Validated
    public static String getMachineFromMachineInfoMaps(
            final String workCenter
            , final String partNumber
            , final Map<String, String> dobladoPartInformation
            , final Map<String, String> laserAndPunchPartInformation
    ) {
        final String sanitizedWorkCenter = sanitizeWorkCenterName(workCenter);
        String result = "";
        switch (sanitizedWorkCenter) {
            case Constants.DOBLADO:
                result = dobladoPartInformation.getOrDefault(partNumber, "");
                break;
            case Constants.PUNZONADO:
            case Constants.LASER:
                result = laserAndPunchPartInformation.getOrDefault(partNumber, "");
                break;
        }
        return result;
    }
    
    @Validated
    public static List<List<WorkOrderInformation>> groupWorkItemsByDay(final List<WorkOrderInformation> items) {
        final List<List<WorkOrderInformation>> result = new ArrayList<>();
        List<WorkOrderInformation> lst = new ArrayList<>();
        
        for (int i = 0; i < (items.size() - 1); i++) {
            final WorkOrderInformation c = items.get(i);
            final WorkOrderInformation nxt = items.get(i + 1);
            lst.add(c);
            
            if (!c.getDay().equals(nxt.getDay())) {
                result.add(new ArrayList(lst));
                lst.clear();
            }
        }
        
        lst.add(items.get(items.size() - 1));
        result.add(new ArrayList(lst));
        
        return result;
    }
    
    @Validated
    public static Turn nextTurn(final Turn turn, final int numberOfTurns) {
        if (numberOfTurns == 2) {
            switch (turn) {
                case NA:
                    return FIRST;
                case FIRST:
                    return SECOND;
                case SECOND:
                    return FIRST;
            }
        } else {
            switch (turn) {
                case NA:
                    return FIRST;
                case FIRST:
                    return SECOND;
                case SECOND:
                    return THIRD;
                case THIRD:
                    return FIRST;
            }
        }
        
        return NA;
    }
    
    @Validated
    public static double turnHours(final Turn turn) {
        if (turn == null) {
            return Constants.FIRST_TURN_LENGTH;
        }
        switch (turn) {
            case FIRST:
                return Constants.FIRST_TURN_LENGTH;
            case SECOND:
                return Constants.SECOND_TURN_LENGTH;
            case THIRD:
                return Constants.THIRD_TURN_LENGTH;
        }
        return 0.0D;
    }
    
    @Validated
    public static WorkOrderInformation workOrderInfoWithSetup(
        final WorkOrderInformation wo
        , final double newRunHours
        , final double newSetupHours
        , final Turn newTurn
    ) {
        
        final WorkOrderInformation newWorkOrderInfo = new WorkOrderInformation(wo);
        newWorkOrderInfo.setTurn(newTurn);
        newWorkOrderInfo.setRunHours(newRunHours);
        newWorkOrderInfo.setSetupHours(newSetupHours);
        
        return newWorkOrderInfo;
    }
    
    @MissingTests
    public static double availableHoursInTurn(final Turn turn, final double initHours) {
        double available;
        if (initHours == 0.0D) {
            available = Utils.turnHours(turn);
        } else {
            available = Utils.turnHours(turn) - initHours;
        }
        return available;
    }
    
    @Validated
    public static double progressFactor(final List<WorkOrderInformation> orders) {
        
        int n = orders.size();
        final Turn lastTurn = orders.get(n - 1).getTurn();
        
        double sum = 0.0;
        
        for (int i = n - 1; i >= 0 && lastTurn == orders.get(i).getTurn(); i--) {
            sum += orders.get(i).getRunHours() + orders.get(i).getSetupHours();
        }
        
        return sum;
    }
    
    public static void printDebugOrders(final List<WorkOrderInformation> orders) {
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        orders.forEach(System.out::println);
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/>");
    }
    
    @MissingTests
    public static Path buildOutputPlanFile(final String workCenter, final String saveDirectory, final Date date) {
        final String today = Utils.DATE_FORMATTER.format(date);
        final String listOutputPlanFileName = String.format("%s-%s.html", workCenter, today);
        final Path planFile = Paths.get(saveDirectory, listOutputPlanFileName);
        return planFile;
    }
    
    private Utils() {}
    
}
