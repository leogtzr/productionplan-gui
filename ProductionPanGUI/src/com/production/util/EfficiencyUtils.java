package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class EfficiencyUtils {
    
    public static EfficiencyInformation splitWithEfficiency(
            final WorkOrderInformation workOrderInfo
            , final double initHours
            , final Turn initTurn) {
        
        final EfficiencyInformation efficiencyInformation = new EfficiencyInformation();
        
        double r = workOrderInfo.getRunHours();
        boolean savingSetupHours = true;
        
        return efficiencyInformation;
    }
    
    private EfficiencyUtils() {}
    
}
