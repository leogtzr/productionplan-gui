package com.production.util.html;

import com.production.domain.Day;
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
    
    @MissingTests
    public static String workOrderToTrForTurnPlan(final WorkOrderInformation wo) {
        final StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        sb.append("<td>").append(wo.getTurn()).append("</td>");
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
    
    @MissingTests
    public static String generateHTMLContentForTwoTurns(
        final String htmlTemplate
        , final String workCenter
        , final List<WorkOrderInformation> items
    ) {

        if (items.isEmpty()) {
            return "";
        }
        
        /*
public static final String DAY_CONTENT = "@DAY@\n@TABLE.DAY.CONTENT@";
public static final String DAY_CONTENT_MARK = "@TABLE.DAY.CONTENT@";
public static final String DAY_MARK = "@DAY@";
        */
        
        final StringBuilder tableRows = new StringBuilder();
        final StringBuilder result = new StringBuilder();
        Day day = items.get(0).getDay();
        
//        for (int i = 0; i < (items.size() - 1); i++) {
//            final WorkOrderInformation current = items.get(i);
//            final WorkOrderInformation next = items.get(i + 1);
//            
//            // Check day change ... 
//            if (current.getDay().equals(next.getDay())) {
//                String dayContent = getTableDayTemplateContent(day);
//                dayContent = dayContent.replace(TABLE_ROWS_MARK, tableRows.toString());
//                result.append(dayContent).append("\n");
//                tableRows.delete(0, tableRows.length());
//            } else {
//                final String tr = HTMLFormat.workOrderToTrForTurnPlan(current);
//                tableRows.append(tr).append("\n");
//            }
//        }
        
        for (final WorkOrderInformation wo : items) {
            if (wo.getDay().equals(day)) {
                final String tr = HTMLFormat.workOrderToTrForTurnPlan(wo);
                tableRows.append(tr).append("\n");
            } else {
                String dayContent = getTableDayTemplateContent(day);
                dayContent = dayContent.replace(TABLE_ROWS_MARK, tableRows.toString());
                result.append(dayContent).append("\n");
                day = wo.getDay();
                tableRows.delete(0, tableRows.length());
            }
        }
        
        System.out.println(tableRows.toString());
        
        return result.toString();
    }
    
    @MissingTests
    public static String getTableDayTemplateContent(final Day day) {
        String dayContent = HTMLConstants.DAY_CONTENT;
        dayContent = dayContent.replace(HTMLConstants.DAY_MARK, String.format("<h1>%s</h1>", day.toString()));
        dayContent = dayContent.replace(HTMLConstants.DAY_CONTENT_MARK, HTMLConstants.TABLE_DAY_CONTENT);
        dayContent = dayContent.replace(HTMLConstants.TABLE_HEAD_MARK, HTMLConstants.TR_HEAD_TURNS_PLAN);
        return dayContent;
    }
    
}
