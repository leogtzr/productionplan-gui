package com.production.utils;

import com.production.domain.Priority;
import com.production.domain.WorkOrderInformation;
import com.production.domain.WorkOrderWrapper;
import com.production.util.Constants;
import com.production.util.Utils;
import static com.production.util.Utils.workOrderItemsPerPartNumber;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.junit.Assert;

import org.junit.Test;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class UtilsTest {
    
    public UtilsTest() {}
    
    @Test
    public void shouldReturnNumberOfTurnsFromWorkCenter() {
        final Object[][] tests = {
            {"DOBLADO", 3},
            {"PUNZONADO", 3},
            {"MAQUINADO_MANUAL", 2},
        };
        
        for (final Object[] test : tests) {
            final int turns = Utils.numberOfTurnsFromWorkCenter(test[0].toString());
            Assert.assertEquals(turns, Integer.parseInt(test[1].toString()));
        }
    }
        
    @Test
    public void shouldReturnWorkCenterName() {
        final Object[][] tests = {
            {"Laser 1", "LASER"},
            {"PUNZONADO 2", "PUNZONADO"},
        };
        
        for (final Object[] test : tests) {
            final String got = Utils.workCenterName(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldSanitizeWorkCenterName() {
        final Object[][] tests = {
            {"Laser", true},
            {"Arroz", false},
        };
        
        for (final Object[] test : tests) {
            final Boolean got = Utils.allowedWC(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldValidateAllowedWorkCenters() {
        final Object[][] tests = {
            {"Laser 1", "LASER_1"},
            {"PUNZONADO-2", "PUNZONADO_2"},
        };
        
        for (final Object[] test : tests) {
            final String got = Utils.sanitizeWorkCenterName(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldReturnPartNumbersOccurrenceCount() {
        final List<WorkOrderInformation> items = List.of(
                new WorkOrderInformation("A", "o1"),
                new WorkOrderInformation("A", "o1"),
                new WorkOrderInformation("B", "o2"),
                new WorkOrderInformation("C", "o3"),
                new WorkOrderInformation("C", "o3"),
                new WorkOrderInformation("C", "o3")
        );
        
        final Map<String, Integer> workCenterOccurrenceCount = Utils.partsNumbersOccurrenceCount(items);
        
        final Object[][] tests = {
            {"A", 2},
            {"B", 1},
            {"C", 3},
        };
        
        for (final Object[] test : tests) {
            final int got = workCenterOccurrenceCount.get(test[0].toString());
            Assert.assertEquals(got, Integer.parseInt(test[1].toString()));
        }
        
    }
    
    @Test
    public void shouldReturnPartNumberFromRowInTableModel() {
        
        final Object[][] data = {
            {"ABC", "A", "B"},
            {"ABCD", "A", "B"},
        };
        
        final String[] columnNames = {
                "#Part", "Hr", "Stup",
        };
        
        final TableModel model = new DefaultTableModel(data, columnNames);
        
        final Object[][] tests = {
            {0, "ABC"},
            {1, "ABCD"},
        };
        
        for (final Object[] test : tests) {
            final var got = Utils.getPartNumberFromRow(model, Integer.parseInt(test[0].toString()));
            Assert.assertEquals(got, test[1].toString());
        }
        
    }
    
    @Test
    public void shouldUpdateStatusLabel() {
        final JLabel statusLabel = new JLabel();
        
        final Object[][] tests = {
            {new File("a"), new File("b"), "Files ready."},
            {null, new File("b"), "Please open the file containing the 'FAB Load by WC' information."},
            {null, null, "Please open the required files."},
            {new File("b"), null, "Please open the file containing the 'Age  by WC' information."},
        };
        
        for (final Object[] test : tests) {
            Utils.updateStatusBar(statusLabel, (File)test[0], (File)test[1]);
            Assert.assertEquals(test[2].toString(), statusLabel.getText());
        }
    }
    
    @Test
    public void shouldBuildPlanForTwoTurns() {
        final List<WorkOrderInformation> workOrderItems = testWorkOrderItems();
        final Map<String, Integer> partsNumbersOccurrenceCount = Utils.partsNumbersOccurrenceCount(workOrderItems);
        final String workCenter = Constants.DOBLADO;
        final List<Priority> priorities = List.of();
        
        final List<WorkOrderWrapper> buildPlanForTwoTurns = Utils
                .buildPlanForTwoTurns(workCenter, workOrderItems, priorities);
        
        // The list shouldn't be empty ...
        Assert.assertFalse(buildPlanForTwoTurns.isEmpty());
    }
    
    private static List<WorkOrderInformation> testWorkOrderItems() {
        // Prepare the data, some WorkOrderInformation items to build a list.
        final WorkOrderInformation wo1 = new WorkOrderInformation();
        wo1.setPartNumber("M11353A001");
        wo1.setRunHours(38.10037500000014D);
        wo1.setSetupHours(0.0D);
        wo1.setQty(477);
        
        final WorkOrderInformation wo2 = new WorkOrderInformation();
        wo2.setPartNumber("M14836A003");
        wo2.setRunHours(17.374999999999773D);
        wo2.setSetupHours(0.5D);
        wo2.setQty(500);
        
        final WorkOrderInformation wo3 = new WorkOrderInformation();
        wo3.setPartNumber("M11588A001");
        wo3.setRunHours(11.022000000000087D);
        wo3.setSetupHours(1.0D);
        wo3.setQty(528);
        
        final WorkOrderInformation wo4 = new WorkOrderInformation();
        wo4.setPartNumber("M14836A003");
        wo4.setRunHours(6.950000000000012D);
        wo4.setSetupHours(0.5D);
        wo4.setQty(200);
        
        final WorkOrderInformation wo5 = new WorkOrderInformation();
        wo5.setPartNumber("M11588A001");
        wo5.setRunHours(10.980250000000078D);
        wo5.setSetupHours(0.0D);
        wo5.setQty(527);
        
        final WorkOrderInformation wo6 = new WorkOrderInformation();
        wo6.setPartNumber("4022.482.54242");
        wo6.setRunHours(2.0831249999999994D);
        wo6.setSetupHours(1.5D);
        wo6.setQty(15);
        
        final WorkOrderInformation wo7 = new WorkOrderInformation();
        wo7.setPartNumber("ENC-1284-4-20");
        wo7.setRunHours(0.7469999999999977D);
        wo7.setSetupHours(0.5D);
        wo7.setQty(72);
        
        final WorkOrderInformation wo8 = new WorkOrderInformation();
        wo8.setPartNumber("M11588A001");
        wo8.setRunHours(9.519000000000073D);
        wo8.setSetupHours(1.0D);
        wo8.setQty(528);
        
        final WorkOrderInformation wo9 = new WorkOrderInformation();
        wo9.setPartNumber("4022.470.25863");
        wo9.setRunHours(1.942500000000013D);
        wo9.setSetupHours(1.5D);
        wo9.setQty(70);
        
        final WorkOrderInformation wo10 = new WorkOrderInformation();
        wo10.setPartNumber("4022.639.17091");
        wo10.setRunHours(1.168999999999999D);
        wo10.setSetupHours(1.25D);
        wo10.setQty(56);
        
        return List.of(
                wo1
                , wo2
                , wo3
                , wo4
                , wo5
                , wo6
                , wo7
                , wo8
                , wo9
                , wo10
        );
    }
    
    // Note: this is not a very strict test, we only check counts, not the value of the lists.
    @Test
    public void shouldReturnWorkOrderItemsPerPartNumber() {
        final List<WorkOrderInformation> workOrderItems = testWorkOrderItems();
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = 
                Utils.workOrderItemsPerPartNumber(workOrderItems);
        
        final Object[][] tests = {
            {"M11353A001", 1},
            {"M14836A003", 2},
            {"M11588A001", 3},
            {"4022.482.54242", 1},
            {"ENC-1284-4-20", 1},
            {"4022.470.25863", 1},
            {"4022.639.17091", 1}
        };
        
        for (final Object[] test : tests) {
            final List<WorkOrderInformation> items = workOrderItemsPerPartNumber.getOrDefault(test[0].toString(), List.of());
            Assert.assertEquals((Integer)test[1], (Integer)items.size());
        }
    }
    
    @Test
    public void shouldReturnSumTurnHoursFromWorkOrderItems() {
        final List<WorkOrderInformation> workOrderItems = testWorkOrderItems();
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = 
            Utils.workOrderItemsPerPartNumber(workOrderItems);
        
        final Object[][] tests = {
            {"M14836A003", 25.324999999999783D},
            {"M11588A001", 33.521250D},
        };
        
        for (final Object[] test : tests) {
            final List<WorkOrderInformation> items = workOrderItemsPerPartNumber.get(test[0].toString());
            final double got = Utils.sumTurnHoursFromWorkOrderItems(items);
            Assert.assertEquals((double)test[1], got, 0.001);
        }
        
    }
}
    
