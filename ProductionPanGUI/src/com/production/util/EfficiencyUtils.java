package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class EfficiencyUtils {
    
    public static EfficiencyInformation splitWithEfficiency(
            final WorkOrderInformation workOrderInfo
            , final double initHours
            , Turn currTurn) {
        
        final EfficiencyInformation efficiencyInformation = new EfficiencyInformation();
        final List<WorkOrderInformation> orders = new ArrayList<>();
        
        double r = workOrderInfo.getRunHours();
        boolean savingSetupHours = true;
        
        while (r > 0.0D) {
            if (initHours > 0.0D) {
                
            } else {
                // Especial para es la primera orden.
                if (savingSetupHours) {
                    savingSetupHours = false;
                    final double newRunHours = Utils.turnHours(currTurn) - workOrderInfo.getSetupHours();
                    r = r - newRunHours;
                    
                } else {
                    
                }
            }
        }
        
        efficiencyInformation.setOrders(orders);
        
        return efficiencyInformation;
    }
    
    private EfficiencyUtils() {}
    
}
