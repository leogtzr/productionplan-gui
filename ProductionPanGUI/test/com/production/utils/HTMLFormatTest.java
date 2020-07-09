package com.production.utils;

import com.production.domain.WorkOrderInformation;
import org.junit.Test;

import com.production.util.HTMLFormat;
import org.junit.Assert;

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
    
}
