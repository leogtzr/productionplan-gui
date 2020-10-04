/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
package com.production.util.csv;

import com.production.domain.WorkOrderInformation;
import com.production.lang.MissingTests;
import java.util.List;

public class CSVFormat {
    
    @MissingTests
    public static String createWithoutTurns(final List<WorkOrderInformation> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(CSVConstants.HEADER_NO_TURNS).append("\n");
        
        items.forEach(item -> sb.append(item.toCSVWithoutTurns()).append("\n"));
        return sb.toString();
    }
    
    @MissingTests
    public static String createWithTurns(final List<WorkOrderInformation> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(CSVConstants.HEADER_TURNS).append("\n");
        
        items.forEach(item -> sb.append(item.toCSVWithTurns()).append("\n"));
        return sb.toString();
    }
    
    private CSVFormat() {}
    
}
