package com.production.utils;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import com.production.domain.efficiency.Progress;
import com.production.util.EfficiencyUtils;
import com.production.util.Utils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class Algorithm1Test {
    
    class testCase {
        List<WorkOrderInformation> orders;
        List<WorkOrderInformation> want;
        testCase(final List<WorkOrderInformation> orders, final List<WorkOrderInformation> want) {
            this.orders = orders;
            this.want = want;
        }
    }
    
    class testCaseSingleOrder {
        WorkOrderInformation order;
        List<WorkOrderInformation> want;

        public testCaseSingleOrder(final WorkOrderInformation order, final List<WorkOrderInformation> want) {
            this.order = order;
            this.want = want;
        }
    }
    
    @Test
    public void shouldRunAlgo1() {
        
        // PENDING: fix these tests to include the day ...
        final List<testCase> tests = List.of(
                new testCase(
                    List.of(
                        new WorkOrderInformation.Builder("p1", "o1").runHours(4.3D).setupHours(1.2D).build(),
                        new WorkOrderInformation.Builder("p2", "o2").runHours(2.2D).setupHours(0.4D).build(),
                        new WorkOrderInformation.Builder("p3", "o3").runHours(5.4D).setupHours(0.8D).build(),
                        new WorkOrderInformation.Builder("p4", "o4").runHours(6.8D).setupHours(3.0D).build()
                    ),
                    List.of(
                        new WorkOrderInformation.Builder("p1", "o1").runHours(4.3D).setupHours(1.2D).turn(Turn.FIRST).build(),
                        new WorkOrderInformation.Builder("p2", "o2").runHours(2.2D).setupHours(0.4D).turn(Turn.FIRST).build(),
                        new WorkOrderInformation.Builder("p3", "o3").runHours(0.0D).setupHours(0.40000000000000036D).turn(Turn.FIRST).build(),
                        new WorkOrderInformation.Builder("p3", "o3").runHours(5.4D).setupHours(0.3999999999999997D).turn(Turn.SECOND).build(),
                        new WorkOrderInformation.Builder("p4", "o4").runHours(0.0D).setupHours(2.3D).turn(Turn.SECOND).build(),
                        new WorkOrderInformation.Builder("p4", "o4").runHours(4.8D).setupHours(0.7000000000000002D).turn(Turn.THIRD).build(),
                        new WorkOrderInformation.Builder("p4", "o4").runHours(2.0D).setupHours(0.0D).turn(Turn.FIRST).build()
                            
                    )
                )
        );
        
        Progress progress = new Progress(Turn.FIRST, 0.0D);
        for (final testCase tc : tests) {
            final List<WorkOrderInformation> result = new ArrayList<>();
            
            for (final WorkOrderInformation order : tc.orders) {
                final EfficiencyInformation ordersWithEfficiency = EfficiencyUtils.efficiency(order, progress);
                printDebugOrders(ordersWithEfficiency.getOrders());
                result.addAll(ordersWithEfficiency.getOrders());
                final int numberOfOrdersInResult = ordersWithEfficiency.getOrders().size();
                final Turn lastTurn = ordersWithEfficiency.getOrders().get(numberOfOrdersInResult - 1).getTurn();
                
                System.out.printf("lastTurn to use is: %s\n", lastTurn);
                System.out.printf("Calculating factor with: %s\n", result);
		final double factor = Utils.progressFactor(result);
                System.out.printf("(f) factor to use: %.1f\n", factor);

                progress.setTurn(lastTurn);
                progress.setFactor(factor);
            }
            
            printDebugOrders(result);
            
            Assert.assertEquals(tc.want, result);
            
        }
        
    }
    
    @Test
    public void shouldRunAlgo2() {
        
        
        final List<testCaseSingleOrder> tests = List.of(
                new testCaseSingleOrder(
                    new WorkOrderInformation.Builder("p1", "o1")
                        .runHours(53.0D)
                        .setupHours(7.2D)
                        .build(),
                    List.of(
                        new WorkOrderInformation.Builder("p1", "o1").runHours(1.2999999999999998D).setupHours(7.2D).turn(Turn.FIRST).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(8.1D).setupHours(0.0D).turn(Turn.SECOND).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(5.5D).setupHours(0.0D).turn(Turn.THIRD).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(8.5D).setupHours(0.0D).turn(Turn.FIRST).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(8.1D).setupHours(0.0D).turn(Turn.SECOND).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(5.5D).setupHours(0.0D).turn(Turn.THIRD).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(8.5D).setupHours(0.0D).turn(Turn.FIRST).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(7.5D).setupHours(0.0D).turn(Turn.SECOND).build()
                    )
                )
        );
        
        for (final testCaseSingleOrder tc : tests) {
            final Progress progress = new Progress(Turn.FIRST, 0.0D);
            final EfficiencyInformation efficiency = EfficiencyUtils.efficiency(tc.order, progress);
            printDebugOrders(efficiency.getOrders());
           
            Assert.assertEquals(tc.want.size(), efficiency.getOrders().size());
            Assert.assertEquals(tc.want, efficiency.getOrders());
        }
        
    }
    
    private static void printDebugOrders(final List<WorkOrderInformation> orders) {
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>");
        orders.forEach(System.out::println);
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/>");
    }
    
}
