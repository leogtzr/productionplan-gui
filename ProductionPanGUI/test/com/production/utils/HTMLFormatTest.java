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
import org.junit.Ignore;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
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
    
    @Test
    public void shouldGenerateTrForTurnPlans() {
        final String EXPECTED_HTML_OUTPUT = 
                "<tr><td class='third-turn'>THIRD</td><td>pt1</td><td>wo1</td><td>0.0</td><td>1.2</td><td>13</td><td>mch1</td></tr>\n";
        
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
        final WorkOrderInformation wo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.FIRST)
                .day(Day.MONDAY)
                .build()
                ;
        
        final WorkOrderInformation wo2 = new WorkOrderInformation.Builder("pt2", "wo2")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.FIRST)
                .day(Day.TUESDAY)
                .build()
                ;
        
        final WorkOrderInformation wo3 = new WorkOrderInformation.Builder("pt3", "wo3")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.SECOND)
                .day(Day.TUESDAY)
                .build()
                ;
        
        final WorkOrderInformation wo4 = new WorkOrderInformation.Builder("pt4", "wo4")
                .setupHours(1.2)
                .qty(13)
                .machine("mch1")
                .turn(Turn.FIRST)
                .day(Day.WEDNESDAY)
                .build()
                ;
        
        // MONDAY
        // TUESDAY
        // TUESDAY
        // WEDNESDAY
        
        final List<WorkOrderInformation> items = new ArrayList<>();
        items.add(wo1);
        items.add(wo2);
        items.add(wo3);
        items.add(wo4);
        
        final String html = HTMLFormat.generateHTMLContentForTwoTurns("@TITLE@\n@CONTENT@", Constants.DOBLADO, items);
        
        Assert.assertThat(html, not(containsString(HTMLConstants.TABLE_HEAD_MARK)));
        Assert.assertThat(html, not(containsString(HTMLConstants.TABLE_ROWS_MARK)));
        Assert.assertThat(html, not(containsString(HTMLConstants.TABLE_TITLE_MARK)));
        Assert.assertThat(html, not(containsString(HTMLConstants.TABLE_TITLE_MARK)));
        Assert.assertThat(html, not(containsString(HTMLConstants.CONTENT_MARK)));
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
    
    @Test
    public void shouldReturnTRForPlanWithCSSClass() {
        final Object[][] tests = {
            // WorkOrderInformation, expected html TD
            {
                new WorkOrderInformation.Builder("pt1", "wo1").turn(Turn.THIRD).build(), "<td class='third-turn'>THIRD</td>",
            },
            {
                new WorkOrderInformation.Builder("pt2", "wo2").turn(Turn.SECOND).build(), "<td class='second-turn'>SECOND</td>",
            },
            {
                new WorkOrderInformation.Builder("pt3", "wo3").turn(Turn.FIRST).build(), "<td class='first-turn'>FIRST</td>",
            },
            {
                new WorkOrderInformation.Builder("pt4", "wo4").turn(Turn.NA).build(), "<td>NA</td>",
            },
            {
                new WorkOrderInformation.Builder("pt5", "wo5").turn(Turn.FIRST_NEXT_DAY).build(), "<td>FIRST_NEXT_DAY</td>",
            },
        };
        
        for (final Object[] test : tests) {
            final WorkOrderInformation wo = (WorkOrderInformation) test[0];
            final String got = HTMLFormat.getTurnTDWithCSSTurnClass(wo);
            Assert.assertEquals(test[1].toString(), got);
        }
        
    }
    
    // getTableDayTemplateContent
    
    @Test
    public void shouldGetTableDayTemplateContent() {
        
        class testCase {
            Day day;
            String want;
            testCase(Day day, String want) {
                this.day = day;
                this.want = want;
            }
        }
        
        final List<testCase> tests = List.of(
            new testCase(Day.MONDAY, "<h2>MONDAY</h2>"),
            new testCase(Day.WEDNESDAY, "<h2>WEDNESDAY</h2>")
        );
        
        for (final testCase tc : tests) {
            final var got = HTMLFormat.getTableDayTemplateContent(tc.day);
            Assert.assertTrue(got.contains(tc.want));
        }
        
    }
    
}
