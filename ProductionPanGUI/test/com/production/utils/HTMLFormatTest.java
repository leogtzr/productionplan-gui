package com.production.utils;

import com.production.domain.Day;
import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.util.Constants;
import com.production.util.Utils;
import com.production.util.html.HTMLConstants;
import org.junit.Test;

import com.production.util.html.HTMLFormat;
import java.util.ArrayList;
import org.junit.Assert;

import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Ignore;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class HTMLFormatTest {
    
    @Test
    public void shouldFormatWorkOrderAsTRForListPlan() {
        
        final String EXPECTED_HTML_OUTPUT = 
                "<tr><td></td><td>pt1</td><td>wo1</td><td>0.0</td><td>1.2</td><td>13</td><td>mch1</td></tr>\n";
        
        final WorkOrderInformation wo = new WorkOrderInformation.Builder("pt1", "wo1")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .build()
                ;
               
        final String tr = HTMLFormat.workOrderToTRForListPlan(wo);
        Assert.assertFalse("tr shouldn't be empty", tr.isEmpty());
        Assert.assertEquals(EXPECTED_HTML_OUTPUT, tr);
    }
    
    @Test
    public void shouldReplaceTableHeaderInHTMLContent() {
        final String HTML_CONTENT = "<table class=\"table\">\n" +
"                <thead>\n" +
"                  @TABLE.HEAD@\n" +
"                </thead>\n" +
"                <tbody>\n" +
"                  @TABLE.ROWS@\n" +
"                </tbody>\n" +
"              </table>";
        
        final String html = HTMLFormat.replaceTableHeaderInTemplate(HTML_CONTENT, HTMLConstants.TABLE_HEAD_MARK, 
                HTMLConstants.TR_HEAD_LIST_PLAN);
        Assert.assertFalse("html content shouldn't be empty", html.isEmpty());
        Assert.assertTrue(String.format("[%s] shoudl be in the replaced content", HTMLConstants.TR_HEAD_LIST_PLAN), 
                html.contains(HTMLConstants.TR_HEAD_LIST_PLAN));
    }
    
    @Test
    public void shouldReturnTRsForWorkOrderItem() {
        final String EXPECTED_HTML_OUTPUT = 
                "<tr><td></td><td>pt1</td><td>wo1</td><td>0.0</td><td>1.2</td><td>13</td><td>mch1</td></tr>\n"
              + "<tr><td></td><td>pt2</td><td>wo2</td><td>2.3</td><td>3.5</td><td>8</td><td>mch2</td></tr>\n";
        
        final WorkOrderInformation wo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .runHours(0.0D)
                .setupHours(1.2D)
                .qty(13)
                .machine("mch1")
                .build()
                ;
        
        final WorkOrderInformation wo2 = new WorkOrderInformation.Builder("pt2", "wo2")
                .runHours(2.3D)
                .setupHours(3.5D)
                .qty(8)
                .machine("mch2")
                .build()
                ;
        
        final List<WorkOrderInformation> items = List.of(wo1, wo2);
        final String html = HTMLFormat.getTRForWorkOrderItemsLitPlan(items);
        
        Assert.assertFalse("html content shouldn't be empty", html.isEmpty());
        Assert.assertEquals(EXPECTED_HTML_OUTPUT, html);
    }
    
    // replaceTableRowsContentInTemplate
    @Test
    public void shouldReplaceTableHTMLRowContentInTemplate() {
        final String HTML_CONTENT = "<table class=\"table\">\n" +
"                <thead>\n" +
"                  @TABLE.HEAD@\n" +
"                </thead>\n" +
"                <tbody>\n" +
"                  @TABLE.ROWS@\n" +
"                </tbody>\n" +
"              </table>";
        
        final String EXPECTED_HTML_OUTPUT = 
                "<tr><td></td><td>pt1</td><td>wo1</td><td>0.0</td><td>1.2</td><td>13</td><td>mch1</td></tr>\n"
              + "<tr><td></td><td>pt2</td><td>wo2</td><td>2.3</td><td>3.5</td><td>8</td><td>mch2</td></tr>\n";
        
        final WorkOrderInformation wo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .runHours(0.0D)
                .setupHours(1.2D)
                .qty(13)
                .machine("mch1")
                .build()
                ;
        
        final WorkOrderInformation wo2 = new WorkOrderInformation.Builder("pt2", "wo2")
                .runHours(2.3D)
                .setupHours(3.5D)
                .qty(8)
                .machine("mch2")
                .build()
                ;
        
        final List<WorkOrderInformation> items = List.of(wo1, wo2);
        
        final String html = HTMLFormat.replaceTableRowsContentInTemplate(HTML_CONTENT, HTMLConstants.TABLE_ROWS_MARK, items);
        System.out.println(html);
        Assert.assertFalse("html content shouldn't be empty", html.isEmpty());
        Assert.assertTrue(String.format("[%s] should be in the content replaced.", EXPECTED_HTML_OUTPUT), 
                html.contains(EXPECTED_HTML_OUTPUT));        
    }
    
    @Test
    public void shouldGenerateHTMLContentForListPlan() {
        final WorkOrderInformation wo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .runHours(0.0D)
                .setupHours(1.2D)
                .qty(13)
                .machine("mch1")
                .build()
                ;
        
        final WorkOrderInformation wo2 = new WorkOrderInformation.Builder("pt2", "wo2")
                .runHours(2.3D)
                .setupHours(3.5D)
                .qty(8)
                .machine("mch2")
                .build()
                ;
        
        final List<WorkOrderInformation> items = List.of(wo1, wo2);
        
        final StringBuilder sb = new StringBuilder();
        sb.append(HTMLConstants.TABLE_HEAD_MARK);
        sb.append(HTMLConstants.TABLE_ROWS_MARK);
        sb.append(HTMLConstants.TABLE_TITLE_MARK);
        
        final String html = HTMLFormat.generateHTMLContentForListPlan(sb.toString(), "EMPAQUE A PROVEEDOR", items);
        Assert.assertFalse("HTML content shouldn't be empty", html.isEmpty());        
        Assert.assertThat(html, not(containsString(HTMLConstants.TABLE_HEAD_MARK)));
        Assert.assertThat(html, not(containsString(HTMLConstants.TABLE_ROWS_MARK)));
        Assert.assertThat(html, not(containsString(HTMLConstants.TABLE_TITLE_MARK)));
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
    
    @Test
    public void shouldGenerateTrForTurnPlans() {
        final String EXPECTED_HTML_OUTPUT = 
                "<tr><td>THIRD</td><td>pt1</td><td>wo1</td><td>0.0</td><td>1.2</td><td>13</td><td>mch1</td></tr>\n";
        
        final WorkOrderInformation wo = new WorkOrderInformation.Builder("pt1", "wo1")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.THIRD)
                .build()
                ;
               
        final String tr = HTMLFormat.workOrderToTrForTurnPlan(wo);
        Assert.assertFalse("tr shouldn't be empty", tr.isEmpty());
        Assert.assertEquals(EXPECTED_HTML_OUTPUT, tr);
    }
    
    @Test
    public void shouldGenerateHTMLContentForTwoTurns() {
//        final List<WorkOrderInformation> items = testItems();
//        final List<Priority> priorities = List.of();
//        final List<WorkOrderInformation> planForTwoTurns = Utils.buildPlanForTwoTurns(Constants.DOBLADO, items, priorities);

        final WorkOrderInformation wo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.FIRST)
                .day(Day.MONDAY)
                .build()
                ;
        
        final WorkOrderInformation wo2 = new WorkOrderInformation.Builder("pt1", "wo1")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.FIRST)
                .day(Day.TUESDAY)
                .build()
                ;
        
        final WorkOrderInformation wo3 = new WorkOrderInformation.Builder("pt1", "wo1")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.SECOND)
                .day(Day.TUESDAY)
                .build()
                ;
        
        final WorkOrderInformation wo4 = new WorkOrderInformation.Builder("pt1", "wo1")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.FIRST)
                .day(Day.WEDNESDAY)
                .build()
                ;
        
        final List<WorkOrderInformation> items = new ArrayList<>();
        items.add(wo1);
        items.add(wo2);
        items.add(wo3);
        items.add(wo4);
        
        final String html = HTMLFormat.generateHTMLContentForTwoTurns("", Constants.DOBLADO, items);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        System.out.println(html);
    }
    
    @Test
    public void shouldGroupWorkItemsByDay() {
        final List<WorkOrderInformation> items = new ArrayList<>();
        final WorkOrderInformation wo1 = new WorkOrderInformation.Builder("pt1", "wo1").day(Day.MONDAY).build();
        final WorkOrderInformation wo2 = new WorkOrderInformation.Builder("pt2", "wo2").day(Day.TUESDAY).build();
        final WorkOrderInformation wo3 = new WorkOrderInformation.Builder("pt3", "wo3").day(Day.TUESDAY).build();
        final WorkOrderInformation wo4 = new WorkOrderInformation.Builder("pt4", "wo4").day(Day.WEDNESDAY).build();
        final WorkOrderInformation wo5 = new WorkOrderInformation.Builder("pt5", "wo5").day(Day.THURSDAY).build();
        final WorkOrderInformation wo6 = new WorkOrderInformation.Builder("pt6", "wo6").day(Day.FRIDAY).build();
        final WorkOrderInformation wo7 = new WorkOrderInformation.Builder("pt7", "wo7").day(Day.SATURDAY).build();
        final WorkOrderInformation wo8 = new WorkOrderInformation.Builder("pt8", "wo8").day(Day.SUNDAY).build();
        final WorkOrderInformation wo9 = new WorkOrderInformation.Builder("pt9", "wo9").day(Day.MONDAY).build();
        final WorkOrderInformation wo10 = new WorkOrderInformation.Builder("pt10", "wo10").day(Day.TUESDAY).build();
        
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
            // index, day, size
            {0, Day.MONDAY, 1},
            {1, Day.TUESDAY, 2},
            {2, Day.WEDNESDAY, 1},
            {3, Day.THURSDAY, 1},
            {4, Day.FRIDAY, 1},
            {5, Day.SATURDAY, 1},
            {6, Day.SUNDAY, 1},
            {7, Day.MONDAY, 1},
            {8, Day.TUESDAY, 1},
        };
        
        final int EXPECTED_RESULT_SIZE = 9;
        final List<List<WorkOrderInformation>> result = Utils.groupWorkItemsByDay(items);
        Assert.assertEquals(EXPECTED_RESULT_SIZE, result.size());
        
        for (final Object[] test : tests) {
            final int idx = Integer.parseInt(test[0].toString());
            final Day expectedDay = (Day)test[1];
            final int expectedSize = Integer.parseInt(test[2].toString());
            
            final List<WorkOrderInformation> got = result.get(idx);
            final WorkOrderInformation gotWO = got.get(0);
            
            Assert.assertEquals(expectedSize, got.size());
            Assert.assertEquals(expectedDay, gotWO.getDay());
        }
    }
    
    
    @Ignore
    @Test
    public void _testAlgoForgroupWorkItemsByDay() {
        final List<Day> days = new ArrayList<>();
        
        days.add(Day.MONDAY);
        days.add(Day.TUESDAY);
        days.add(Day.WEDNESDAY);
        days.add(Day.THURSDAY);
        days.add(Day.FRIDAY);
        days.add(Day.SATURDAY);
        days.add(Day.SUNDAY);
        days.add(Day.MONDAY);
        days.add(Day.TUESDAY);
        days.add(Day.WEDNESDAY);
        
        final List<List<Day>> result = new ArrayList<>();
        List<Day> lst = new ArrayList<>();
        
        for (int i = 0; i < (days.size() - 1); i++) {
            Day c = days.get(i);
            Day nxt = days.get(i + 1);
            lst.add(c);
            
            if (!c.equals(nxt)) {
                result.add(new ArrayList(lst));
                lst.clear();
            }
        }
        
        lst.add(days.get(days.size() - 1));
        result.add(new ArrayList(lst));
        result.forEach(System.out::println);
        System.out.println(">>>>>>>>>>>>>>>");
    }
    
}
