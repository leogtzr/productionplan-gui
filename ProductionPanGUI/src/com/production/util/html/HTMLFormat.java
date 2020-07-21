package com.production.util.html;

import com.production.domain.Day;
import com.production.domain.WorkOrderInformation;
import com.production.lang.MissingTests;
import com.production.lang.Validated;
import com.production.util.Utils;
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
    public static String workOrderToTrForTurnPlan(final WorkOrderInformation wo) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        final String tdTurn = HTMLFormat.getTurnTDWithCSSTurnClass(wo);
        sb.append(tdTurn);
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
    
    @MissingTests
    public static String getTurnTDWithCSSTurnClass(final WorkOrderInformation wo) {
        switch (wo.getTurn()) {
            case FIRST -> {
                return "<td class='first-turn'>" + wo.getTurn() + "</td>";
            }
            case SECOND -> {
                return "<td class='second-turn'>" + wo.getTurn() + "</td>";
            }
            case THIRD -> {
                return "<td class='third-turn'>" + wo.getTurn() + "</td>";
            }
        }
        return "<td>" + wo.getTurn() + "</td>";
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
    
    @Validated
    public static String generateHTMLContentForListPlan(
            final String htmlTemplate
            , final String workCenter
            , final List<WorkOrderInformation> items) {
        final String header = replaceTableHeaderInTemplate(htmlTemplate, TABLE_HEAD_MARK, TR_HEAD_LIST_PLAN);
        final String trs = getTRForWorkOrderItemsLitPlan(items);
        final String html = header.replace(TABLE_ROWS_MARK, trs);
        
        return html.replace(TABLE_TITLE_MARK, workCenter);
    }
    
    @Validated
    public static String generateHTMLContentForTwoTurns(
        String htmlTemplate
        , final String workCenter
        , final List<WorkOrderInformation> items
    ) {

        if (items.isEmpty()) {
            return "";
        }
        
        final List<List<WorkOrderInformation>> byDay = Utils.groupWorkItemsByDay(items);
        htmlTemplate = htmlTemplate.replace(HTMLConstants.TABLE_TITLE_MARK, String.format("<h1>%s</h1>", workCenter));
        
        final StringBuilder tableRows = new StringBuilder();
        final StringBuilder result = new StringBuilder();
        
        for (final List<WorkOrderInformation> woItems : byDay) {
            final Day day = woItems.get(0).getDay();
            String dayContent = getTableDayTemplateContent(day);
            
            tableRows.delete(0, tableRows.length());
            // Generate the <TR>'s:
            for (final WorkOrderInformation wo : woItems) {
                final String tr = workOrderToTrForTurnPlan(wo);
                tableRows.append("\t").append(tr);
            }
            dayContent = dayContent.replace(HTMLConstants.TABLE_ROWS_MARK, tableRows.toString());
            result.append(dayContent).append("\n\n");
        }
        
        htmlTemplate = htmlTemplate.replace(HTMLConstants.CONTENT_MARK, result.toString());
        return htmlTemplate;
    }
    
    @MissingTests
    public static String getTableDayTemplateContent(final Day day) {
        String dayContent = HTMLConstants.DAY_CONTENT;
        dayContent = dayContent.replace(HTMLConstants.DAY_MARK, String.format("<h2>%s</h2>", day.toString()));
        dayContent = dayContent.replace(HTMLConstants.DAY_CONTENT_MARK, HTMLConstants.TABLE_DAY_CONTENT);
        dayContent = dayContent.replace(HTMLConstants.TABLE_HEAD_MARK, HTMLConstants.TR_HEAD_TURNS_PLAN);
        return dayContent;
    }
    
}
