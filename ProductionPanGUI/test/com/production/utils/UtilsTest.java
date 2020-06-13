package com.production.utils;

import com.production.domain.Day;
import com.production.domain.Priority;
import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.util.Constants;
import com.production.util.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.junit.Assert;
import org.junit.Test;

import static com.production.util.Utils.sumTurnHoursFromWorkOrderItems;
import static com.production.util.Utils.workOrderItemsPerPartNumber;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class UtilsTest {

    public UtilsTest() {
    }

    @Test
    public void shouldReturnNumberOfTurnsFromWorkCenter() {
        final Object[][] tests = {
            {"DOBLADO", 3},
            {"PUNZONADO", 3},
            {"MAQUINADO_MANUAL", 2},};

        for (final Object[] test : tests) {
            final int turns = Utils.numberOfTurnsFromWorkCenter(test[0].toString());
            Assert.assertEquals(turns, Integer.parseInt(test[1].toString()));
        }
    }

    @Test
    public void shouldReturnWorkCenterName() {
        final Object[][] tests = {
            {"Laser 1", "LASER"},
            {"PUNZONADO 2", "PUNZONADO"},};

        for (final Object[] test : tests) {
            final String got = Utils.workCenterName(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }

    @Test
    public void shouldSanitizeWorkCenterName() {
        final Object[][] tests = {
            {"Laser", true},
            {"Arroz", false},};

        for (final Object[] test : tests) {
            final Boolean got = Utils.allowedWC(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }

    @Test
    public void shouldValidateAllowedWorkCenters() {
        final Object[][] tests = {
            {"Laser 1", "LASER_1"},
            {"PUNZONADO-2", "PUNZONADO_2"},};

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
            {"C", 3},};

        for (final Object[] test : tests) {
            final int got = workCenterOccurrenceCount.get(test[0].toString());
            Assert.assertEquals(got, Integer.parseInt(test[1].toString()));
        }

    }

    @Test
    public void shouldReturnPartNumberFromRowInTableModel() {

        final Object[][] data = {
            {"ABC", "A", "B"},
            {"ABCD", "A", "B"},};

        final String[] columnNames = {
            "#Part", "Hr", "Stup",};

        final TableModel model = new DefaultTableModel(data, columnNames);

        final Object[][] tests = {
            {0, "ABC"},
            {1, "ABCD"},};

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
            {new File("b"), null, "Please open the file containing the 'Age  by WC' information."},};

        for (final Object[] test : tests) {
            Utils.updateStatusBar(statusLabel, (File) test[0], (File) test[1]);
            Assert.assertEquals(test[2].toString(), statusLabel.getText());
        }
    }

    @Test
    public void shouldBuildPlanForTwoTurns() {

        // The following value might change:
        final int EXPECTED_NUMBER_OF_ITEMS_IN_PLAN = 11;

        final List<WorkOrderInformation> workOrderItems = dobladora2TestItems();
        final String workCenter = Constants.DOBLADO;
        final List<Priority> priorities = List.of();

        final List<WorkOrderInformation> planForTwoTurns = Utils
                .buildPlanForTwoTurns(workCenter, workOrderItems, priorities);

        // The list shouldn't be empty ...
        Assert.assertFalse(planForTwoTurns.isEmpty());
        Assert.assertEquals(EXPECTED_NUMBER_OF_ITEMS_IN_PLAN, planForTwoTurns.size());

        final Object[][] tests = {
            // Index, setup hours, turn, day
            {0, 0.3D, Turn.FIRST, Day.MONDAY},
            {1, 2.3D, Turn.FIRST, Day.MONDAY},
            {2, 0.5D, Turn.SECOND, Day.MONDAY},
            {3, 0.0D, Turn.SECOND, Day.MONDAY},
            {4, 3.3D, Turn.FIRST, Day.TUESDAY},
            {5, 0.0D, Turn.FIRST, Day.TUESDAY},
            {6, 0.5D, Turn.FIRST, Day.TUESDAY},
        };

        // int idx = 0;
        for (final Object[] test : tests) {
            final WorkOrderInformation wo = planForTwoTurns.get(Integer.parseInt(test[0].toString()));
            // System.out.println(wo);
            System.out.printf("-> [%s] <-\n", wo);
            Assert.assertEquals((double) test[1], wo.getSetupHours(), 0.001D);
            Assert.assertEquals((Turn) test[2], wo.getTurn());
            Assert.assertEquals((Day) test[3], wo.getDay());
        }

        //System.out.println(planForTwoTurns);
        planForTwoTurns.forEach(System.out::println);
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
                wo1,
                 wo2,
                 wo3,
                 wo4,
                 wo5,
                 wo6,
                 wo7,
                 wo8,
                 wo9,
                 wo10
        );
    }

    /*
        This factory test method contains the 
     */
    private static List<WorkOrderInformation> dobladora2TestItems() {
        final List<WorkOrderInformation> items = new ArrayList<>();

        final WorkOrderInformation wo1 = new WorkOrderInformation();
        wo1.setPartNumber("PT_1");
        wo1.setWorkOrder("WO_1");
        wo1.setRunHours(4.2D);
        wo1.setSetupHours(0.3D);
        wo1.setQty(7);
        wo1.setTurn(Turn.NA);

        final WorkOrderInformation wo2 = new WorkOrderInformation();
        wo2.setPartNumber("PT_2");
        wo2.setWorkOrder("WO_2");
        wo2.setRunHours(1.1D);
        wo2.setSetupHours(2.3D);
        wo2.setQty(20);
        wo2.setTurn(Turn.NA);

        final WorkOrderInformation wo3 = new WorkOrderInformation();
        wo3.setPartNumber("PT_3");
        wo3.setWorkOrder("WO_3");
        wo3.setRunHours(0.2D);
        wo3.setSetupHours(0.5D);
        wo3.setQty(14);
        wo3.setTurn(Turn.NA);

        final WorkOrderInformation wo4 = new WorkOrderInformation();
        wo4.setPartNumber("PT_3");
        wo4.setWorkOrder("WO_3");
        wo4.setRunHours(1.4D);
        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        wo4.setSetupHours(0.5D);
        wo4.setQty(94);
        wo4.setTurn(Turn.NA);

        final WorkOrderInformation wo5 = new WorkOrderInformation();
        wo5.setPartNumber("PT_4");
        wo5.setWorkOrder("WO_4");
        wo5.setRunHours(3.4D);
        wo5.setSetupHours(3.3D);
        wo5.setQty(14);
        wo5.setTurn(Turn.NA);

        final WorkOrderInformation wo6 = new WorkOrderInformation();
        wo6.setPartNumber("PT_4");
        wo6.setWorkOrder("WO_4");
        wo6.setRunHours(5.7D);
        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        wo6.setSetupHours(3.3D);
        wo6.setQty(24);
        wo6.setTurn(Turn.NA);

        // Some of the following items should be set to Day.TUESDAY:
        final WorkOrderInformation wo7 = new WorkOrderInformation();
        wo7.setPartNumber("PT_5");
        wo7.setWorkOrder("WO_5");
        wo7.setRunHours(0.7D);
        wo7.setSetupHours(0.5D);
        wo7.setQty(240);
        wo7.setTurn(Turn.NA);

        final WorkOrderInformation wo8 = new WorkOrderInformation();
        wo8.setPartNumber("PT_6");
        wo8.setWorkOrder("WO_6");
        wo8.setRunHours(2.5D);
        wo8.setSetupHours(2.0D);
        wo8.setQty(55);
        wo8.setTurn(Turn.NA);

        final WorkOrderInformation wo9 = new WorkOrderInformation();
        wo9.setPartNumber("PT_7");
        wo9.setWorkOrder("WO_7");
        wo9.setRunHours(0.8D);
        wo9.setSetupHours(0.5D);
        wo9.setQty(40);
        wo9.setTurn(Turn.NA);

        final WorkOrderInformation wo10 = new WorkOrderInformation();
        wo10.setPartNumber("PT_8");
        wo10.setWorkOrder("WO_8");
        wo10.setRunHours(1.3D);
        wo10.setSetupHours(1.0D);
        wo10.setQty(39);

        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        final WorkOrderInformation wo11 = new WorkOrderInformation();
        wo11.setPartNumber("PT_8");
        wo11.setWorkOrder("WO_8");
        wo11.setRunHours(1.0D);
        wo11.setSetupHours(1.0D);
        wo11.setQty(30);
        wo11.setTurn(Turn.NA);

        items.add(wo1);
        items.add(wo2);
        items.add(wo3);
        items.add(wo4);
        items.add(wo5);
        items.add(wo6);
        items.add(wo7);
        items.add(wo8);
        items.add(wo9);
        items.add(wo10);
        items.add(wo11);

        return items;
    }

    // Note: this is not a very strict test, we only check counts, not the value of the lists.
    @Test
    public void shouldReturnWorkOrderItemsPerPartNumber() {
        final List<WorkOrderInformation> workOrderItems = testWorkOrderItems();
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(workOrderItems);

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
            Assert.assertEquals((Integer) test[1], (Integer) items.size());
        }
    }

    @Test
    public void shouldReturnSumTurnHoursFromWorkOrderItems() {
        final List<WorkOrderInformation> workOrderItems = testWorkOrderItems();
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(workOrderItems);

        final Object[][] tests = {
            {"M14836A003", 25.324999999999783D},
            {"M11588A001", 33.521250D},};

        for (final Object[] test : tests) {
            final List<WorkOrderInformation> items = workOrderItemsPerPartNumber.get(test[0].toString());
            final double got = sumTurnHoursFromWorkOrderItems(items);
            Assert.assertEquals((double) test[1], got, 0.001);
        }
    }
}
