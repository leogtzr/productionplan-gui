package com.production.util;

import com.production.domain.WorkOrderInformation;
import com.production.lang.MissingTests;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public final class HTMLFormat {
    
    private HTMLFormat() {}
    
    @MissingTests
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
    
}
