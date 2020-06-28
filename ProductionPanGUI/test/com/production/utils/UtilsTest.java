package com.production.utils;

import com.production.domain.AgeComparator;
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

import static com.production.domain.WorkOrderInformation.Builder;
import static com.production.util.Constants.DOBLADO;
import java.util.Collections;

import static java.util.stream.Collectors.toList;

import static java.lang.Integer.parseInt;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class UtilsTest {
    
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
    public void shouldConvertHoursTo2Turns() {
        final Object[][] tests = {
            {1.3D, Turn.FIRST},
            {8.6D, Turn.SECOND},
            {16.7, Turn.THIRD},
            {22.2, Turn.FIRST_NEXT_DAY},};

        for (final Object[] test : tests) {
            final Turn got = Utils.hoursTo3Turns((double) test[0]);
            Assert.assertEquals(test[1], got);
        }
    }

    @Test
    public void shouldBuildPlanForThreeTurns() {
        // The following value might change:
        final int EXPECTED_NUMBER_OF_ITEMS_IN_PLAN = 30;

        final List<WorkOrderInformation> workOrderItems = testItems();
        final String workCenter = Constants.DOBLADO;
        final List<Priority> priorities = List.of();

        // Build the plan:
        final List<WorkOrderInformation> planForTwoTurns = Utils.buildPlanForThreeTurns(workCenter, workOrderItems, priorities);

        // The list shouldn't be empty ...
        Assert.assertFalse(planForTwoTurns.isEmpty());
        Assert.assertEquals(EXPECTED_NUMBER_OF_ITEMS_IN_PLAN, planForTwoTurns.size());

        final Object[][] tests = {
            // Index, setup hours, turn, day
            {0, 0.3D, Turn.FIRST, Day.MONDAY, "PT_1"}, // 0
            {1, 2.3D, Turn.FIRST, Day.MONDAY, "PT_2"}, // 1
            {2, 0.5, Turn.SECOND, Day.MONDAY, "PT_3"}, // 2
            {3, 0.0, Turn.SECOND, Day.MONDAY, "PT_3"}, // 3
            {4, 3.3, Turn.THIRD, Day.MONDAY, "PT_4"}, // 4
            {5, 0.0, Turn.FIRST, Day.TUESDAY, "PT_4"}, // 5
            {6, 0.5, Turn.FIRST, Day.TUESDAY, "PT_5"}, // 6
            {7, 2.0, Turn.FIRST, Day.TUESDAY, "PT_6"}, // 7
            {8, 0.5, Turn.FIRST, Day.TUESDAY, "PT_7"}, // 8
            {9, 1.0, Turn.SECOND, Day.TUESDAY, "PT_8"}, // 9
            {10, 0.0, Turn.SECOND, Day.TUESDAY, "PT_8"}, // 10
            {11, 1.5, Turn.SECOND, Day.TUESDAY, "PT_9"}, // 11
            {12, 2.0, Turn.SECOND, Day.TUESDAY, "PT_10"}, // 12
            {13, 0.0, Turn.SECOND, Day.TUESDAY, "PT_10"}, // 13
            {14, 0.8, Turn.THIRD, Day.TUESDAY, "PT_11"}, // 14
            {15, 0.8, Turn.THIRD, Day.TUESDAY, "PT_12"}, // 15
            {16, 0.0, Turn.FIRST, Day.WEDNESDAY, "PT_12"}, // 16
            {17, 0.5, Turn.FIRST, Day.WEDNESDAY, "PT_13"}, // 17
            {18, 0.0, Turn.FIRST, Day.WEDNESDAY, "PT_13"}, // 18
            {19, 0.5, Turn.FIRST, Day.WEDNESDAY, "PT_14"}, // 19
            {20, 0.8, Turn.FIRST, Day.WEDNESDAY, "PT_15"}, // 20
            {21, 0.8, Turn.SECOND, Day.WEDNESDAY, "PT_16"}, // 21
            {22, 0.0, Turn.SECOND, Day.WEDNESDAY, "PT_16"}, // 22
            {23, 0.8, Turn.SECOND, Day.WEDNESDAY, "PT_17"}, // 23
            {24, 0.0, Turn.THIRD, Day.WEDNESDAY, "PT_18"}, // 24
            {25, 0.0, Turn.THIRD, Day.WEDNESDAY, "PT_18"}, // 25
            {26, 0.5, Turn.FIRST, Day.THURSDAY, "PT_19"}, // 26
            {27, 0.0, Turn.FIRST, Day.THURSDAY, "PT_19"}, // 27
            {28, 0.5, Turn.FIRST, Day.THURSDAY, "PT_20"}, // 28
            {29, 0.5, Turn.FIRST, Day.THURSDAY, "PT_21"}, // 29
        };

        for (final Object[] test : tests) {
            final WorkOrderInformation wo = planForTwoTurns.get(Integer.parseInt(test[0].toString()));
            Assert.assertEquals((double) test[1], wo.getSetupHours(), 0.001D);
            Assert.assertEquals((Turn) test[2], wo.getTurn());
            Assert.assertEquals((Day) test[3], wo.getDay());
        }
    }

    @Test
    public void shouldBuildPlanForTwoTurns() {

        // The following value might change:
        final int EXPECTED_NUMBER_OF_ITEMS_IN_PLAN = 30;

        final List<WorkOrderInformation> workOrderItems = testItems();
        final String workCenter = Constants.DOBLADO;
        final List<Priority> priorities = List.of();

        // Build the plan:
        final List<WorkOrderInformation> planForTwoTurns = Utils.buildPlanForTwoTurns(workCenter, workOrderItems, priorities);

        // The list shouldn't be empty ...
        Assert.assertFalse(planForTwoTurns.isEmpty());
        Assert.assertEquals(EXPECTED_NUMBER_OF_ITEMS_IN_PLAN, planForTwoTurns.size());

        final Object[][] tests = {
            // Index, setup hours, turn, day
            {0, 0.3D, Turn.FIRST, Day.MONDAY, "PT_1"}, // 0
            {1, 2.3D, Turn.FIRST, Day.MONDAY, "PT_2"}, // 1
            {2, 0.5D, Turn.SECOND, Day.MONDAY, "PT_3"}, // 2
            {3, 0.0D, Turn.SECOND, Day.MONDAY, "PT_3"}, // 3
            {4, 3.3D, Turn.FIRST, Day.TUESDAY, "PT_4"}, // 4
            {5, 0.0D, Turn.FIRST, Day.TUESDAY, "PT_4"}, // 5
            {6, 0.5D, Turn.FIRST, Day.TUESDAY, "PT_5"}, // 6
            {7, 2.0D, Turn.SECOND, Day.TUESDAY, "PT_6"}, // 7
            {8, 0.5D, Turn.SECOND, Day.TUESDAY, "PT_7"}, // 8
            {9, 1.0D, Turn.SECOND, Day.TUESDAY, "PT_8"}, // 9
            // setup hours for the following item should be 0.0
            {10, 0.0D, Turn.SECOND, Day.TUESDAY, "PT_8"}, //10
            {11, 1.5D, Turn.FIRST, Day.WEDNESDAY, "PT_9"}, //11
            {12, 2.0D, Turn.FIRST, Day.WEDNESDAY, "PT_10"}, //12
            {13, 0.0D, Turn.FIRST, Day.WEDNESDAY, "PT_10"}, //13
            {14, 0.8D, Turn.FIRST, Day.WEDNESDAY, "PT_11"}, //14
            {15, 0.8D, Turn.FIRST, Day.WEDNESDAY, "PT_12"}, //15
            {16, 0.0D, Turn.SECOND, Day.WEDNESDAY, "PT_12"},//16
            {17, 0.5D, Turn.SECOND, Day.WEDNESDAY, "PT_13"},//17
            {18, 0.0D, Turn.SECOND, Day.WEDNESDAY, "PT_13"},//18
            {19, 0.5D, Turn.SECOND, Day.WEDNESDAY, "PT_14"},//19
            {20, 0.8D, Turn.SECOND, Day.WEDNESDAY, "PT_15"},//20
            {21, 0.8D, Turn.FIRST, Day.THURSDAY, "PT_16"}, //21
            {22, 0.0D, Turn.FIRST, Day.THURSDAY, "PT_16"}, //22
            {23, 0.8D, Turn.FIRST, Day.THURSDAY, "PT_17"}, //23
            {24, 0.0D, Turn.SECOND, Day.THURSDAY, "PT_18"}, //24
            {25, 0.0D, Turn.SECOND, Day.THURSDAY, "PT_18"}, //25
            {26, 0.5D, Turn.SECOND, Day.THURSDAY, "PT_19"}, //26
            {27, 0.0D, Turn.SECOND, Day.THURSDAY, "PT_19"}, //27
            {28, 0.5D, Turn.SECOND, Day.THURSDAY, "PT_20"}, //28
            {29, 0.5D, Turn.FIRST, Day.FRIDAY, "PT_21"}, //29
        };

        for (final Object[] test : tests) {
            final WorkOrderInformation wo = planForTwoTurns.get(Integer.parseInt(test[0].toString()));
            Assert.assertEquals((double) test[1], wo.getSetupHours(), 0.01D);
            Assert.assertEquals((Turn) test[2], wo.getTurn());
            Assert.assertEquals((Day) test[3], wo.getDay());
        }

    }

    private static List<WorkOrderInformation> testWorkOrderItems() {
        // Prepare the data, some WorkOrderInformation items to build a list.
        final WorkOrderInformation wo1 = new WorkOrderInformation();
        wo1.setPartNumber("PT1");
        wo1.setRunHours(38.10037500000014D);
        wo1.setSetupHours(0.0D);
        wo1.setQty(477);

        final WorkOrderInformation wo2 = new WorkOrderInformation();
        wo2.setPartNumber("PT2");
        wo2.setRunHours(17.374999999999773D);
        wo2.setSetupHours(0.5D);
        wo2.setQty(500);

        final WorkOrderInformation wo3 = new WorkOrderInformation();
        wo3.setPartNumber("PT3");
        wo3.setRunHours(11.022000000000087D);
        wo3.setSetupHours(1.0D);
        wo3.setQty(528);

        final WorkOrderInformation wo4 = new WorkOrderInformation();
        wo4.setPartNumber("PT2");
        wo4.setRunHours(6.950000000000012D);
        wo4.setSetupHours(0.5D);
        wo4.setQty(200);

        final WorkOrderInformation wo5 = new WorkOrderInformation();
        wo5.setPartNumber("PT3");
        wo5.setRunHours(10.980250000000078D);
        wo5.setSetupHours(0.0D);
        wo5.setQty(527);

        final WorkOrderInformation wo6 = new WorkOrderInformation();
        wo6.setPartNumber("PT4");
        wo6.setRunHours(2.0831249999999994D);
        wo6.setSetupHours(1.5D);
        wo6.setQty(15);

        final WorkOrderInformation wo7 = new WorkOrderInformation();
        wo7.setPartNumber("PT5");
        wo7.setRunHours(0.7469999999999977D);
        wo7.setSetupHours(0.5D);
        wo7.setQty(72);

        final WorkOrderInformation wo8 = new WorkOrderInformation();
        wo8.setPartNumber("PT6");
        wo8.setRunHours(9.519000000000073D);
        wo8.setSetupHours(1.0D);
        wo8.setQty(528);

        final WorkOrderInformation wo9 = new WorkOrderInformation();
        wo9.setPartNumber("PT7");
        wo9.setRunHours(1.942500000000013D);
        wo9.setSetupHours(1.5D);
        wo9.setQty(70);

        final WorkOrderInformation wo10 = new WorkOrderInformation();
        wo10.setPartNumber("PT8");
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

    private static List<WorkOrderInformation> testItemsWithAgeShort() {
        final List<WorkOrderInformation> items = new ArrayList<>();

        final WorkOrderInformation wo2 = new WorkOrderInformation.Builder("pt2", "wo2").workCenter(DOBLADO).age(3).build();
        final WorkOrderInformation wo3 = new WorkOrderInformation.Builder("pt3", "wo3").workCenter(DOBLADO).age(6).build();
        final WorkOrderInformation wo4 = new WorkOrderInformation.Builder("pt1", "wo9").workCenter(DOBLADO).age(5).build();
        final WorkOrderInformation wo1 = new WorkOrderInformation.Builder("pt1", "wo1").workCenter(DOBLADO).age(9).build();

        items.add(wo1);
        items.add(wo2);
        items.add(wo3);
        items.add(wo4);

        return items;
    }

    @Test
    public void shouldSortByAge() {
        final List<WorkOrderInformation> items = testItemsWithAgeShort();

        Collections.sort(items, new AgeComparator());

        final Object[][] tests = {
            // Index, Expected Age
            {0, 9},
            {1, 6},
            {2, 5},
            {3, 3},};

        final Object[][] testsPtOrder = {
            // Index, expected age, pt number
            {0, 9, "pt1"},
            {1, 5, "pt1"},
            {2, 6, "pt3"},
            {3, 3, "pt2"},};

        for (final Object[] test : tests) {
            final WorkOrderInformation wo = items.get(Integer.parseInt(test[0].toString()));
            final int got = wo.getAge();
            final int expectedAge = Integer.parseInt(test[1].toString());
            Assert.assertEquals(expectedAge, got);
        }

        // let's group the elements ... 
        final List<WorkOrderInformation> other = new ArrayList<>();

        final Map<String, List<WorkOrderInformation>> group = Utils.workOrderItemsPerPartNumber(items);
        group.forEach((pt, wos) -> other.addAll(wos));

        for (final Object[] test : testsPtOrder) {
            final WorkOrderInformation wo = other.get(Integer.parseInt(test[0].toString()));
            final int got = wo.getAge();
            final int expectedAge = Integer.parseInt(test[1].toString());
            Assert.assertEquals(expectedAge, got);
            Assert.assertEquals(test[2].toString(), wo.getPartNumber());
        }
    }
    
    @Test
    public void shouldSortByAgeAndGroupByEqualPartNumbers() {
        final List<WorkOrderInformation> items = testItemsWithAge();
        final int EXPECTED_ITEMS_SIZE = 17;
        Assert.assertEquals(EXPECTED_ITEMS_SIZE, items.size());
        
        final List<WorkOrderInformation> itemsSortedByAge = Utils.sortAndGroup(items, new AgeComparator());
        
        final Object[][] tests = {
            // Index, expected age, expected part number
            {0, 12, "PT_9"},
            {1, 8, "PT_6"},
            {2, 6, "PT_3"},
            {3, 4, "PT_3"},
            {4, 6, "PT_5"},
            {5, 6, "PT_10"},
            {6, 5, "PT_10"},
            {7, 5, "PT_4"},
            {8, 1, "PT_4"},
            {9, 1, "PT_4"},
            {10, 5, "PT_7"},
            {11, 5, "PT_12"},
            {12, 4, "PT_8"},
            {13, 3, "PT_8"},
            {14, 3, "PT_1"},
            {15, 3, "PT_11"},
            {16, 2, "PT_2"},
        };
        
        for (final Object[] test : tests) {
            final int idx = parseInt(test[0].toString());
            final int expectedAge = parseInt(test[1].toString());
            final String expectedPartNumber = test[2].toString();
            
            final WorkOrderInformation wo = itemsSortedByAge.get(idx);
            Assert.assertEquals(expectedAge, wo.getAge());
            Assert.assertEquals(expectedPartNumber, wo.getPartNumber());
        }
        
    }

    private static List<WorkOrderInformation> testItemsWithAge() {
        final List<WorkOrderInformation> items = new ArrayList<>();
        
        final WorkOrderInformation wo1 = new WorkOrderInformation("PT_1", "WO_1");
        wo1.setRunHours(4.2D);
        wo1.setSetupHours(0.3D);
        wo1.setQty(7);
        wo1.setAge(3);

        final WorkOrderInformation wo2 = new WorkOrderInformation("PT_2", "WO_2");
        wo2.setRunHours(1.1D);
        wo2.setSetupHours(2.3D);
        wo2.setQty(20);
        wo2.setAge(2);

        final WorkOrderInformation wo3 = new WorkOrderInformation("PT_3", "WO_3");
        wo3.setRunHours(0.2D);
        wo3.setSetupHours(0.5D);
        wo3.setQty(14);
        wo3.setAge(6);

        final WorkOrderInformation wo4 = new WorkOrderInformation("PT_4", "WO_4");
        wo4.setRunHours(3.4D);
        wo4.setSetupHours(3.3D);
        wo4.setQty(14);
        wo4.setAge(5);

        final WorkOrderInformation wo5 = new WorkOrderInformation("PT_4", "WO_4");
        wo5.setRunHours(5.7D);
        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        wo5.setSetupHours(3.3D);
        wo5.setQty(24);
        wo5.setAge(1);

        // Some of the following items should be set to Day.TUESDAY:
        final WorkOrderInformation wo6 = new WorkOrderInformation("PT_5", "WO_5");
        wo6.setRunHours(0.7D);
        wo6.setSetupHours(0.5D);
        wo6.setQty(240);
        wo6.setAge(6);

        final WorkOrderInformation wo7 = new WorkOrderInformation("PT_6", "WO_6");
        wo7.setRunHours(2.5D);
        wo7.setSetupHours(2.0D);
        wo7.setQty(55);
        wo7.setAge(8);

        final WorkOrderInformation wo8 = new WorkOrderInformation("PT_7", "WO_7");
        wo8.setRunHours(0.8D);
        wo8.setSetupHours(0.5D);
        wo8.setQty(40);
        wo8.setAge(5);

        final WorkOrderInformation wo9 = new WorkOrderInformation("PT_8", "WO_8");
        wo9.setRunHours(1.3D);
        wo9.setSetupHours(1.0D);
        wo9.setQty(39);
        wo9.setAge(3);

        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        final WorkOrderInformation wo10 = new WorkOrderInformation("PT_8", "WO_8");
        wo10.setRunHours(1.0D);
        wo10.setSetupHours(1.0D);
        wo10.setQty(30);
        wo10.setAge(4);

        final WorkOrderInformation wo11 = new WorkOrderInformation("PT_9", "WO_9");
        wo11.setRunHours(1.0D);
        wo11.setSetupHours(1.5D);
        wo11.setQty(45);
        wo11.setAge(12);

        final WorkOrderInformation wo12 = new WorkOrderInformation("PT_10", "WO_10");
        wo12.setRunHours(0.5D);
        wo12.setSetupHours(2.0D);
        wo12.setQty(12);
        wo12.setAge(5);

        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        final WorkOrderInformation wo13 = new WorkOrderInformation("PT_10", "WO_11");
        wo13.setRunHours(0.5D);
        wo13.setSetupHours(2.5D);
        wo13.setQty(96);
        wo13.setAge(6);

        // Some of the following items may appear on Wednesday:
        final WorkOrderInformation wo14 = new WorkOrderInformation("PT_11", "WO_12");
        wo14.setRunHours(1.1D);
        wo14.setSetupHours(0.8D);
        wo14.setQty(22);
        wo14.setAge(3);

        final WorkOrderInformation wo15 = new WorkOrderInformation("PT_12", "WO_13");
        wo15.setRunHours(1.5D);
        wo15.setSetupHours(0.8D);
        wo15.setQty(47);
        wo15.setAge(5);

        final WorkOrderInformation wo16 = new WorkOrderInformation("PT_3", "WO_3");
        wo16.setRunHours(1.4D);
        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        wo16.setSetupHours(0.5D);
        wo16.setQty(94);
        wo16.setAge(4);

        final WorkOrderInformation wo17 = new WorkOrderInformation("PT_4", "WO_4");
        wo17.setRunHours(5.7D);
        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        wo17.setSetupHours(3.3D);
        wo17.setQty(24);
        wo17.setAge(1);

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
        items.add(wo12);
        items.add(wo13);
        items.add(wo14);
        items.add(wo15);
        items.add(wo16);
        items.add(wo17);

        return items;
    }
    
    @Test
    public void shouldBuilPlanForTwoTurnsWithSortedItemsByAgeAndGroupedByPartNumbers() {
        final List<WorkOrderInformation> items = testItemsWithAge();
        final int EXPECTED_ITEMS_SIZE = 17;
        Assert.assertEquals(EXPECTED_ITEMS_SIZE, items.size());
        
        final int EXPECTED_NUMBER_OF_ITEMS_IN_PLAN = items.size();
        final String workCenter = Constants.DOBLADO;
        final List<Priority> priorities = List.of();

        // Build the plan:
        final List<WorkOrderInformation> planForTwoTurns = Utils.buildPlanForTwoTurns(workCenter, items, priorities);

        // The list shouldn't be empty ...
        Assert.assertFalse(planForTwoTurns.isEmpty());
        Assert.assertEquals(EXPECTED_NUMBER_OF_ITEMS_IN_PLAN, planForTwoTurns.size());

        final Object[][] tests = {
            // Index, setup hours, turn, day
            {0, 1.5D, Turn.FIRST, Day.MONDAY, "PT_9"},      // 0
            {1, 2.0D, Turn.FIRST, Day.MONDAY, "PT_6"},      // 1
            {2, 0.5D, Turn.FIRST, Day.MONDAY, "PT_3"},      // 2
            {3, 0.0D, Turn.SECOND, Day.MONDAY, "PT_3"},     // 3
            {4, 0.5D, Turn.SECOND, Day.MONDAY, "PT_5"},     // 4
            {5, 2.5D, Turn.SECOND, Day.MONDAY, "PT_10"},    // 5
            {6, 0.0D, Turn.SECOND, Day.MONDAY, "PT_10"},    // 6
            {7, 3.3D, Turn.FIRST, Day.TUESDAY, "PT_4"},     // 7
            {8, 0.0D, Turn.FIRST, Day.TUESDAY, "PT_4"},     // 8
            {9, 0.0D, Turn.SECOND, Day.TUESDAY, "PT_4"},    // 9
            {10, 0.5D, Turn.SECOND, Day.TUESDAY, "PT_7"},   // 10
            {11, 0.8D, Turn.SECOND, Day.TUESDAY, "PT_12"},  // 11
            {12, 1.0D, Turn.FIRST, Day.WEDNESDAY, "PT_8"},  // 12
            {13, 0.0D, Turn.FIRST, Day.WEDNESDAY, "PT_8"},  // 13
            {14, 0.3D, Turn.FIRST, Day.WEDNESDAY, "PT_1"},  // 14
            {15, 0.8D, Turn.FIRST, Day.WEDNESDAY, "PT_11"}, // 15
            {16, 2.3D, Turn.SECOND, Day.WEDNESDAY, "PT_2"}, // 16
        };

        for (final Object[] test : tests) {
            final WorkOrderInformation wo = planForTwoTurns.get(Integer.parseInt(test[0].toString()));
            Assert.assertEquals((double) test[1], wo.getSetupHours(), 0.01D);
            Assert.assertEquals((Turn) test[2], wo.getTurn());
            Assert.assertEquals((Day) test[3], wo.getDay());
            Assert.assertEquals(test[4].toString(), wo.getPartNumber());
        }
    }
    
    @Test
    public void shouldBuilPlanForTwoTurnsWithSortedItemsByAgeAndGroupedByPartNumbersAndPriorities() {
        final List<WorkOrderInformation> items = testItemsWithAge();
        final int EXPECTED_ITEMS_SIZE = 17;
        Assert.assertEquals(EXPECTED_ITEMS_SIZE, items.size());
        
        final int EXPECTED_NUMBER_OF_ITEMS_IN_PLAN = items.size();
        final String workCenter = Constants.DOBLADO;
        
        // Our priorities ... 
        final List<Priority> priorities = List.of(
            new Priority("PT_2", -1)
            , new Priority("PT_5", -1)
        );
        
        // Build the plan:
        final List<WorkOrderInformation> planForTwoTurns = Utils.buildPlanForTwoTurns(workCenter, items, priorities);

        // The list shouldn't be empty ...
        Assert.assertFalse(planForTwoTurns.isEmpty());
        // Assert.assertEquals(EXPECTED_NUMBER_OF_ITEMS_IN_PLAN, planForTwoTurns.size());
    }

    /*
        This factory test method contains items that will be used to test how the plans are built.
     */
    private static List<WorkOrderInformation> testItems() {
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

        final WorkOrderInformation wo12 = new WorkOrderInformation("PT_9", "WO_9");
        wo12.setRunHours(1.0D);
        wo12.setSetupHours(1.5D);
        wo12.setQty(45);
        wo12.setTurn(Turn.NA);

        final WorkOrderInformation wo13 = new WorkOrderInformation("PT_10", "WO_10");
        wo13.setRunHours(0.5D);
        wo13.setSetupHours(2.0D);
        wo13.setQty(12);
        wo13.setTurn(Turn.NA);

        // The following property should be modified and adjusted to 0.0 since the previous item shares the
        // same part number
        final WorkOrderInformation wo14 = new WorkOrderInformation("PT_10", "WO_11");
        wo14.setRunHours(0.5D);
        wo14.setSetupHours(2.5D);
        wo14.setQty(96);
        wo14.setTurn(Turn.NA);

        // Some of the following items may appear on Wednesday:
        final WorkOrderInformation wo15 = new WorkOrderInformation("PT_11", "WO_12");
        wo15.setRunHours(1.1D);
        wo15.setSetupHours(0.8D);
        wo15.setQty(22);
        wo15.setTurn(Turn.NA);

        final WorkOrderInformation wo16 = new WorkOrderInformation("PT_12", "WO_13");
        wo16.setRunHours(1.5D);
        wo16.setSetupHours(0.8D);
        wo16.setQty(47);
        wo16.setTurn(Turn.NA);

        final WorkOrderInformation wo17 = new WorkOrderInformation("PT_12", "WO_14");
        wo17.setRunHours(3.6);
        // This one should be adjusted since the previous part number is the same.
        wo17.setSetupHours(12.0D);
        wo17.setQty(54);
        wo17.setTurn(Turn.NA);

        final WorkOrderInformation wo18 = new WorkOrderInformation("PT_13", "WO_15");
        wo18.setRunHours(0.2D);
        wo18.setSetupHours(0.5D);
        wo18.setQty(5);
        wo18.setTurn(Turn.NA);

        final WorkOrderInformation wo19 = new WorkOrderInformation("PT_13", "WO_15");
        wo19.setRunHours(1.4D);
        // This one should be adjusted since the previous part number is the same.
        wo19.setSetupHours(Math.PI);
        wo19.setQty(31);
        wo19.setTurn(Turn.NA);

        final WorkOrderInformation wo20 = new WorkOrderInformation("PT_14", "WO_16");
        wo20.setRunHours(0.6D);
        wo20.setSetupHours(0.5D);
        wo20.setQty(50);
        wo20.setTurn(Turn.NA);

        final WorkOrderInformation wo21 = new WorkOrderInformation("PT_15", "WO_17");
        wo21.setRunHours(1.1D);
        wo21.setSetupHours(0.8D);
        wo21.setQty(22);
        wo21.setTurn(Turn.NA);

        final WorkOrderInformation wo22 = new WorkOrderInformation("PT_16", "WO_18");
        wo22.setRunHours(3.0D);
        wo22.setSetupHours(0.8D);
        wo22.setQty(75);
        wo22.setTurn(Turn.NA);

        final WorkOrderInformation wo23 = new WorkOrderInformation("PT_16", "WO_18");
        wo23.setRunHours(5.8D);
        // This one should be adjusted since the previous part number is the same.
        wo23.setSetupHours(Math.PI);
        wo23.setQty(145);
        wo23.setTurn(Turn.NA);

        final WorkOrderInformation wo24 = new WorkOrderInformation("PT_17", "WO_19");
        wo24.setRunHours(1.1D);
        wo24.setSetupHours(0.8D);
        wo24.setQty(170);
        wo24.setTurn(Turn.NA);

        final WorkOrderInformation wo25 = new WorkOrderInformation("PT_18", "WO_20");
        wo25.setRunHours(1.5D);
        wo25.setSetupHours(0.0D);
        wo25.setQty(47);
        wo25.setTurn(Turn.NA);

        final WorkOrderInformation wo26 = new WorkOrderInformation("PT_18", "WO_20");
        wo26.setRunHours(3.6D);
        // This one should be adjusted since the previous part number is the same.
        wo26.setSetupHours(Math.PI);
        wo26.setQty(54);
        wo26.setTurn(Turn.NA);

        final WorkOrderInformation wo27 = new WorkOrderInformation("PT_19", "WO_21");
        wo27.setRunHours(0.2D);
        wo27.setSetupHours(0.5D);
        wo27.setQty(15);
        wo27.setTurn(Turn.NA);

        final WorkOrderInformation wo28 = new WorkOrderInformation("PT_19", "WO_21");
        wo28.setRunHours(0.7D);
        // This one should be adjusted since the previous part number is the same.
        wo28.setSetupHours(Math.E);
        wo28.setQty(51);
        wo28.setTurn(Turn.NA);

        final WorkOrderInformation wo29 = new WorkOrderInformation("PT_20", "WO_22");
        wo29.setRunHours(1.6D);
        wo29.setSetupHours(0.5D);
        wo29.setQty(36);
        wo29.setTurn(Turn.NA);

        final WorkOrderInformation wo30 = new WorkOrderInformation.Builder("PT_21", "WO_23")
                .runHours(2.9D).setupHours(0.5D).qty(99).turn(Turn.NA).build();

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
        items.add(wo12);
        items.add(wo13);
        items.add(wo14);
        items.add(wo15);
        items.add(wo16);
        items.add(wo17);
        items.add(wo18);
        items.add(wo19);
        items.add(wo20);
        items.add(wo21);
        items.add(wo22);
        items.add(wo23);
        items.add(wo24);
        items.add(wo25);
        items.add(wo26);
        items.add(wo27);
        items.add(wo28);
        items.add(wo29);
        items.add(wo30);

        return items;
    }

    // Note: this is not a very strict test, we only check counts, not the value of the lists.
    @Test
    public void shouldReturnWorkOrderItemsPerPartNumber() {
        final List<WorkOrderInformation> workOrderItems = testWorkOrderItems();
        final Map<String, List<WorkOrderInformation>> workOrderItemsPerPartNumber = workOrderItemsPerPartNumber(workOrderItems);

        final Object[][] tests = {
            {"PT1", 1},
            {"PT2", 2},
            {"PT3", 2},
            {"PT4", 1},
            {"PT5", 1},
            {"PT6", 1},
            {"PT7", 1},
            {"PT8", 1}
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
            {"PT1", 38.10037500000014D},
            {"PT2", 25.324999999999783D},
            {"PT3", 23.00225D},
            {"PT4", 3.5831249999999994D},
            {"PT5", 1.2469999999999977D},
            {"PT6", 10.519000000000073D},
            {"PT7", 3.442500000000013D},
            {"PT8", 1.168999999999999D + 1.25D},};

        for (final Object[] test : tests) {
            final List<WorkOrderInformation> items = workOrderItemsPerPartNumber.get(test[0].toString());
            final double got = sumTurnHoursFromWorkOrderItems(items);
            Assert.assertEquals((double) test[1], got, 0.001);
        }
    }

    @Test
    public void shouldBuildForTwoTurnsWithPriorities() {

    }

    @Test
    public void shouldUpdateMachineByWorkCenter() {

        // Test items:
        final List<WorkOrderInformation> items = new ArrayList<>();
        final WorkOrderInformation wo1 = new Builder("PT1", "W1").workCenter("DOBLADO").machine("toupdate1").build();
        final WorkOrderInformation wo2 = new Builder("PT2", "W2").workCenter("OTRO1").machine("toupdate2").build();
        final WorkOrderInformation wo3 = new Builder("PT3", "W3").workCenter("OTRO2").machine("toupdate3").build();
        final WorkOrderInformation wo4 = new Builder("PT4", "W4").workCenter("DOBLADO").machine("toupdate4").build();
        final WorkOrderInformation wo5 = new Builder("PT5", "W5").workCenter("DOBLADO").machine("toupdate5").build();
        final WorkOrderInformation wo6 = new Builder("PT6", "W6").workCenter("DOBLADO").machine("toupdate6").build();
        final WorkOrderInformation wo7 = new Builder("PT7", "W7").workCenter("PUNCH").machine("toupdate7").build();
        final WorkOrderInformation wo8 = new Builder("PT8", "W8").workCenter("DOBLADO").machine("toupdate8").build();
        final WorkOrderInformation wo9 = new Builder("PT9", "W9").workCenter("MAQUINADO").machine("toupdate9").build();
        final WorkOrderInformation wo10 = new Builder("PT10", "W10").workCenter("MAQUINADO").machine("toupdate10").build();

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

        final Object[][] tests = {
            {3, "DOBLADO", "new-value", "new-value", true},};

        for (final Object[] test : tests) {
            final int idx = Integer.parseInt(test[0].toString());
            final String workCenter = test[1].toString();
            final String newMachineValue = test[2].toString();
            final String expectedMachineValue = test[3].toString();
            final boolean updated = Utils.updateMachine(idx, newMachineValue, workCenter, items);
            Assert.assertEquals(Boolean.valueOf(test[4].toString()), updated);

            final var filtered = items.stream().filter(w -> w.getWcDescription().equalsIgnoreCase(workCenter)).collect(toList());
            final WorkOrderInformation wo = filtered.get(idx);
            Assert.assertEquals(expectedMachineValue, wo.getMachine());
        }

    }
    
    @Test
    public void shouldReturnNextDay() {
        
        final Object[][] tests = {
            // current day, expected day
            {Day.MONDAY, Day.TUESDAY},
            {Day.TUESDAY, Day.WEDNESDAY},
            {Day.WEDNESDAY, Day.THURSDAY},
            {Day.THURSDAY, Day.FRIDAY},
            {Day.FRIDAY, Day.SATURDAY},
            {Day.SATURDAY, Day.SUNDAY},
            {Day.SUNDAY, Day.MONDAY}
        };
        
        for (final Object[] test : tests) {
            final Day got = Utils.nextDay((Day)test[0]);
            Assert.assertEquals((Day)test[1], got);
        }
        
    }

}
