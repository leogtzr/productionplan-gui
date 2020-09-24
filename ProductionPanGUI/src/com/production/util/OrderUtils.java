package com.production.util;

import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.Progress;
import com.production.lang.Validated;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class OrderUtils {
    
    @Validated
    public static WorkOrderInformation from(
            final WorkOrderInformation wo, 
            final double newRunHours,
            final double newSetupHours,
            final Progress progress
            ) {
        
        final WorkOrderInformation newOrder = new WorkOrderInformation();
        newOrder.setWcDescription(wo.getWcDescription());
        newOrder.setPartNumber(wo.getPartNumber());
        newOrder.setWorkOrder(wo.getWorkOrder());
        newOrder.setRunHours(newRunHours);
        newOrder.setSetupHours(newSetupHours);
        newOrder.setQty(wo.getQty());
        newOrder.setAge(wo.getAge());
        newOrder.setSalesPrice(wo.getSalesPrice());
        newOrder.setMachine(wo.getMachine());
        newOrder.setTurn(progress.getTurn());
        newOrder.setDay(progress.getDay());
        
        return newOrder;
    }
    
    private OrderUtils() {}
    
}
