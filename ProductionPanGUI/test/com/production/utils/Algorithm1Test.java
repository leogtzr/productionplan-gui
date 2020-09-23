package com.production.utils;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import com.production.domain.efficiency.Progress;
import com.production.util.EfficiencyUtils;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class Algorithm1Test {
    
    @Test
    public void shouldRunAlgo1() {
        
        class testCase {
            
        }
        
    }
    
    @Test
    public void shouldRunAlgo2() {
        
        final WorkOrderInformation testOrder = new WorkOrderInformation.Builder("pt1", "wo1")
        .runHours(53.0D)
        .setupHours(7.2D)
        .qty(220)
        .build();
        
        final Progress progress = new Progress(Turn.FIRST, 0.0D);
        final EfficiencyInformation efficiency = EfficiencyUtils.efficiency(testOrder, progress);
        
        printDebugOrders(efficiency.getOrders());
        
    }
    
    private static void printDebugOrders(final List<WorkOrderInformation> orders) {
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        orders.forEach(System.out::println);
        
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/>");
    }
    
}
