package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import org.junit.Test;
//import java.util.List;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import static org.junit.Assert.*;

/**
 *
 * @author lgutierr
 */
public class EfficiencyUtilsTest {
    
    @Test
    public void testSplitWithEfficiency() {
        
        final WorkOrderInformation workOrderInfo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .runHours(15.9D)
                .setupHours(2.0D)
                .qty(220)   
                .build();
        
        final WorkOrderInformation workOrderInfo2 = new WorkOrderInformation.Builder("pt2", "wo2")
                .runHours(6.7D)
                .setupHours(1.1D)
                .qty(88)   
                .build();
        
        final WorkOrderInformation workOrderInfo3 = new WorkOrderInformation.Builder("pt3", "wo3")
                .runHours(4.5D)
                .setupHours(0.7D)
                .qty(45)
                .build();
        
        System.out.println("1) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        final EfficiencyInformation splitOrdersForOrder1 = EfficiencyUtils.splitWithEfficiency(workOrderInfo1, 0.0D, Turn.FIRST);
        System.out.println(splitOrdersForOrder1);
        
        System.out.println("2) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        final EfficiencyInformation splitOrdersForOrder2 = EfficiencyUtils.splitWithEfficiency(workOrderInfo2, 1.3D, Turn.THIRD);
        System.out.println(splitOrdersForOrder2);
        
        System.out.println("3) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        final EfficiencyInformation splitOrdersForOrder3 = EfficiencyUtils.splitWithEfficiency(workOrderInfo3, 3.6D, Turn.FIRST);
        System.out.println(splitOrdersForOrder3);

    }
    
}
