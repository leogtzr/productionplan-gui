package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import java.util.ArrayList;
import java.util.List;
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
    
//    @Test
//    public void testSplitWithEfficiency() {
//        
//        final WorkOrderInformation workOrderInfo1 = new WorkOrderInformation.Builder("pt1", "wo1")
//                .runHours(15.9D)
//                .setupHours(2.0D)
//                .qty(220)   
//                .build();
//        
//        final WorkOrderInformation workOrderInfo2 = new WorkOrderInformation.Builder("pt2", "wo2")
//                .runHours(6.7D)
//                .setupHours(1.1D)
//                .qty(88)   
//                .build();
//        
//        final WorkOrderInformation workOrderInfo3 = new WorkOrderInformation.Builder("pt3", "wo3")
//                .runHours(4.5D)
//                .setupHours(0.7D)
//                .qty(45)
//                .build();
//        
//        System.out.println("1) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
//        
//        final EfficiencyInformation splitOrdersForOrder1 = EfficiencyUtils.splitWithEfficiency(workOrderInfo1, 0.0D, Turn.FIRST);
//        System.out.println(splitOrdersForOrder1);
//        
//        System.out.println("2) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
//        
//        final EfficiencyInformation splitOrdersForOrder2 = EfficiencyUtils.splitWithEfficiency(workOrderInfo2, 1.3D, Turn.THIRD);
//        System.out.println(splitOrdersForOrder2);
//        
//        System.out.println("3) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
//        
//        final EfficiencyInformation splitOrdersForOrder3 = EfficiencyUtils.splitWithEfficiency(workOrderInfo3, 3.6D, Turn.FIRST);
//        System.out.println(splitOrdersForOrder3);
//
//    }
//    
//    @Test
//    public void scenarioWithSavingSetup() {
//        
//        final WorkOrderInformation workOrderInfo1 = new WorkOrderInformation.Builder("pt1", "wo1")
//                .runHours(4.3D)
//                .setupHours(1.2D)
//                .qty(37)
//                .build();
//        
//        final WorkOrderInformation workOrderInfo2 = new WorkOrderInformation.Builder("pt2", "wo2")
//                .runHours(2.2D)
//                .setupHours(0.4D)
//                .qty(22)
//                .build();
//        
//        final WorkOrderInformation workOrderInfo3 = new WorkOrderInformation.Builder("pt3", "wo3")
//                .runHours(5.4D)
//                .setupHours(0.8D)
//                .qty(16)
//                .build();
//        
//        final WorkOrderInformation workOrderInfo4 = new WorkOrderInformation.Builder("pt4", "wo4")
//                .runHours(6.8D)
//                .setupHours(3.0D)
//                .qty(10)
//                .build();
//        
//        System.out.println("1) ~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
//        
//        final EfficiencyInformation splitOrdersForOrder1 = EfficiencyUtils.splitWithEfficiency2(workOrderInfo1, 0.0D, Turn.FIRST);
//        System.out.println(splitOrdersForOrder1);
//        
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
//        final EfficiencyInformation splitOrdersForOrder2 = EfficiencyUtils.
//                splitWithEfficiency2(workOrderInfo2, splitOrdersForOrder1.getInitHours(), splitOrdersForOrder1.getOutputTurn());
//        System.out.println(splitOrdersForOrder2);
//        
//        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
//        final EfficiencyInformation splitOrdersForOrder3 = EfficiencyUtils.
//                splitWithEfficiency2(workOrderInfo3, splitOrdersForOrder2.getInitHours(), splitOrdersForOrder1.getOutputTurn());
//        System.out.println(splitOrdersForOrder3);
//        
//        
//        
////        System.out.println("2) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
////        
////        final EfficiencyInformation splitOrdersForOrder2 = EfficiencyUtils.
////                splitWithEfficiency2(workOrderInfo2, splitOrdersForOrder1.getInitHours(), splitOrdersForOrder1.getOutputTurn());
////        System.out.println(splitOrdersForOrder2);
//        
//    }
//    
    @Test
    public void algo_two_variables_remHours_and_remSetup() {
        
        final int EXPECTED_NUMBER_OF_SPLIT_ORDERS = 4;
        
        final WorkOrderInformation workOrderInfo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .runHours(4.3D)
                .setupHours(1.2D)
                .qty(37)
                .build();
        
        final WorkOrderInformation workOrderInfo2 = new WorkOrderInformation.Builder("pt2", "wo2")
                .runHours(2.2D)
                .setupHours(0.4D)
                .qty(22)
                .build();
        
        final WorkOrderInformation workOrderInfo3 = new WorkOrderInformation.Builder("pt3", "wo3")
                .runHours(5.4D)
                .setupHours(0.8D)
                .qty(16)
                .build();
        
        final WorkOrderInformation workOrderInfo4 = new WorkOrderInformation.Builder("pt4", "wo4")
                .runHours(6.8D)
                .setupHours(3.0D)
                .qty(10)
                .build();
        
//        final List<WorkOrderInformation> inputWorkOrders = List.of(workOrderInfo1, workOrderInfo2, workOrderInfo3, workOrderInfo4);
//        final List<EfficiencyInformation> results = new ArrayList<>();
//        
//        final EfficiencyInformation splitOrdersForOrder1 = EfficiencyUtils.algo2(inputWorkOrders.get(0), 0D, Turn.FIRST);
//        results.add(splitOrdersForOrder1);
//        
//        for (int i = 1; i < inputWorkOrders.size(); i++) {
//            final EfficiencyInformation split = EfficiencyUtils.algo2(inputWorkOrders.get(i), results.get(i - 1).getInitHours(), results.get(i - 1).getOutputTurn());
//            results.add(split);
//            // System.out.println(split);
//        }
//        
//        // System.out.println(results);
//        results.forEach(ef -> {
//            ef.getOrders().forEach(System.out::println);
//        });
        
        System.out.println("1) ~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        final EfficiencyInformation splitOrdersForOrder1 = EfficiencyUtils.algo2(workOrderInfo1, 0D, Turn.FIRST);
        System.out.println(splitOrdersForOrder1);
        
        System.out.println("\n2) ~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        final EfficiencyInformation splitOrdersForOrder2 = 
                EfficiencyUtils.algo2(workOrderInfo2, splitOrdersForOrder1.getInitHours(), splitOrdersForOrder1.getOutputTurn());
        System.out.println(splitOrdersForOrder2);
//        
        System.out.println("\n4) ~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        final EfficiencyInformation splitOrdersForOrder3 = 
                EfficiencyUtils.algo2(workOrderInfo3, splitOrdersForOrder2.getInitHours(), splitOrdersForOrder2.getOutputTurn());
        System.out.println(splitOrdersForOrder3);
        
        final EfficiencyInformation splitOrdersForOrder4 = 
                EfficiencyUtils.algo2(workOrderInfo4, splitOrdersForOrder3.getInitHours(), splitOrdersForOrder3.getOutputTurn());
        System.out.println(splitOrdersForOrder4);
        
    }
    
    // This test checks the scenario given here:
    // https://github.com/leogtzr/productionplan-gui/issues/12
    @Test
    public void algo_scenario_raro_15895() {
        final WorkOrderInformation workOrderInfo1 = new WorkOrderInformation.Builder("pt1", "wo1")
                .runHours(15.895D)
                .setupHours(2.0D)
                .qty(220)
                .build();
        
        System.out.println("1) ~~~~~~~~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        
        final EfficiencyInformation splitOrdersForOrder1 = EfficiencyUtils.algo2(workOrderInfo1, 0D, Turn.FIRST);
        System.out.println(splitOrdersForOrder1);
        
    }
    
}
