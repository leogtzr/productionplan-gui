package com.production.utils;

import com.production.domain.WorkOrderInformation;
import com.production.util.html.HTMLConstants;
import org.junit.Test;

import com.production.util.html.HTMLFormat;
import org.junit.Assert;

import java.util.List;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

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
    
}
