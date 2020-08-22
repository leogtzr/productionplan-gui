package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lgutierr
 */
public class EfficiencyUtilsTest {
    
    @Test
    public void testSplitWithEfficiency() {
        
        final WorkOrderInformation workOrderInfo = new WorkOrderInformation.Builder("pt1", "wo1")
                .runHours(15.9D)
                .setupHours(2.0D)
                .qty(220)
                .build();
        
        final EfficiencyInformation splitOrders = EfficiencyUtils.splitWithEfficiency(workOrderInfo, 0.0D, Turn.NA);
                
                // List<WorkOrderInformation> expResult = null;
                // List<WorkOrderInformation> result = EfficiencyUtils.splitWithEfficiency(workOrderInfo);
                // assertEquals(expResult, result);
                // TODO review the generated test code and remove the default call to fail.
                // fail("The test case is a prototype.");
;
    }
    
}
