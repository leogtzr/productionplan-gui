package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.lang.MissingTests;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class OrderUtils {
    
    // PENDING: validate this.
    @MissingTests
    public static WorkOrderInformation from(
            final WorkOrderInformation wo, 
            final double newRunHours,
            final double newSetupHours,
            final Turn newTurn
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
        newOrder.setTurn(wo.getTurn());
        newOrder.setDay(wo.getDay());
        newOrder.setMachine(wo.getMachine());
        newOrder.setTurn(newTurn);
        
        return newOrder;
    }
    
    private OrderUtils() {}
    
}
