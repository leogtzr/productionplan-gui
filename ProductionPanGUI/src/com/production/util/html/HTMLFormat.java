package com.production.util.html;

import com.production.domain.WorkOrderInformation;
import com.production.lang.MissingTests;
import com.production.lang.Validated;
import java.util.List;

import static com.production.util.html.HTMLConstants.*;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public final class HTMLFormat {
    
    private HTMLFormat() {}
    
    @Validated
    public static String workOrderToTRForListPlan(final WorkOrderInformation wo) {
        
        final StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td></td>");
        // Part number:
        sb.append("<td>").append(wo.getPartNumber()).append("</td>");
        // Work Order:
        sb.append("<td>").append(wo.getWorkOrder()).append("</td>");
        // Run hours:
        sb.append("<td>").append(wo.getRunHours()).append("</td>");
        // Setup hours:
        sb.append("<td>").append(wo.getSetupHours()).append("</td>");
        // Qty or Piezas x Hacer
        sb.append("<td>").append(wo.getQty()).append("</td>");
        // "Comentarios" o machine:
        sb.append("<td>").append(wo.getMachine()).append("</td>");
        sb.append("</tr>");
        sb.append("\n");
        return sb.toString();
    }
    
    @Validated
    public static String replaceTableHeaderInTemplate(
            final String htmlTemplateContent
            , final String mark
    , final String tableHeader) {
        return htmlTemplateContent.replace(mark, tableHeader);
    }
    
    @Validated
    public static String replaceTableRowsContentInTemplate(
            final String htmlTemplateContent
            , final String mark
    , final List<WorkOrderInformation> woItems) {
        final String html = getTRForWorkOrderItemsLitPlan(woItems);
        return htmlTemplateContent.replace(mark, html);
    }
    
    @Validated
    public static String getTRForWorkOrderItemsLitPlan(final List<WorkOrderInformation> woItems) {
        final StringBuilder sb = new StringBuilder();
        woItems.forEach(item -> sb.append(workOrderToTRForListPlan(item)));
        return sb.toString();
    }
    
    /*
    @Validated
    public static String replaceTableHeaderInTemplate(
            final String htmlTemplateContent
            , final String mark
    , final String tableHeader) {
        return htmlTemplateContent.replace(mark, tableHeader);
    }
    */
    
    @MissingTests
    public static String generateHTMLContentForListPlan(
            final String htmlTemplate
            , final String workCenter
            , final List<WorkOrderInformation> items) {
        final String header = replaceTableHeaderInTemplate(htmlTemplate, TABLE_HEAD_MARK, TR_HEAD_LIST_PLAN);
        final String trs = getTRForWorkOrderItemsLitPlan(items);
        final String html = header.replace(TABLE_ROWS_MARK, trs);
        
        return html.replace(TABLE_TITLE_MARK, workCenter);
    }
    
}