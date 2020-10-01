package com.production.utils;

import com.production.domain.Day;
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
 * @author lgutierr (leogutierrezramirez@gmail.com)
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

        testCaseSingleOrder(final WorkOrderInformation order, final List<WorkOrderInformation> want) {
            this.order = order;
            this.want = want;
        }
    }
    
    @Test
    public void validateCase1() {
        
        final List<testCase> tests = List.of(
            new testCase(
                List.of(
                    new WorkOrderInformation.Builder("p1", "o1").runHours(4.3D).qty(37).setupHours(1.2D).build(),
                    new WorkOrderInformation.Builder("p2", "o2").runHours(2.2D).qty(22).setupHours(0.4D).build(),
                    new WorkOrderInformation.Builder("p3", "o3").runHours(5.4D).qty(16).setupHours(0.8D).build(),
                    new WorkOrderInformation.Builder("p4", "o4").runHours(6.8D).qty(10).setupHours(3.0D).build()
                ),
                List.of(
                    new WorkOrderInformation.Builder("p1", "o1").runHours(4.3D).setupHours(1.2D).qty(37).turn(Turn.FIRST).day(Day.MONDAY).build(),
                    new WorkOrderInformation.Builder("p2", "o2").runHours(2.2D).setupHours(0.4D).qty(22).turn(Turn.FIRST).day(Day.MONDAY).build(),
                    new WorkOrderInformation.Builder("p3", "o3").runHours(0.0D).setupHours(0.40000000000000036D).qty(0).day(Day.MONDAY).turn(Turn.FIRST).build(),
                    new WorkOrderInformation.Builder("p3", "o3").runHours(5.4D).setupHours(0.3999999999999997D).qty(16).day(Day.MONDAY).turn(Turn.SECOND).build(),
                    new WorkOrderInformation.Builder("p4", "o4").runHours(0.0D).setupHours(2.3D).turn(Turn.SECOND).qty(0).day(Day.MONDAY).build(),
                    new WorkOrderInformation.Builder("p4", "o4").runHours(4.8D).setupHours(0.7000000000000002D).qty(7).turn(Turn.THIRD).day(Day.MONDAY).build(),
                    new WorkOrderInformation.Builder("p4", "o4").runHours(2.0D).setupHours(0.0D).turn(Turn.FIRST).qty(3).day(Day.TUESDAY).build()

                )
            )
        );
        
        Progress progress = new Progress(Turn.FIRST, 0.0D, Day.MONDAY);
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
    public void validateCase2() {
        
        final List<testCaseSingleOrder> tests = List.of(
                new testCaseSingleOrder(
                    new WorkOrderInformation.Builder("p1", "o1")
                        .runHours(53.0D)
                        .setupHours(7.2D)
                        .day(Day.MONDAY)
                        .qty(200)
                        .build(),
                    List.of(
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(1.2999999999999998D)
                                .setupHours(7.2D)
                                .qty(5)
                                .turn(Turn.FIRST)
                                .day(Day.MONDAY)
                                .build(),
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(8.1D)
                                .setupHours(0.0D)
                                .qty(31)
                                .turn(Turn.SECOND)
                                .day(Day.MONDAY)
                                .build(),
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(5.5D)
                                .setupHours(0.0D)
                                .qty(21)
                                .turn(Turn.THIRD)
                                .day(Day.MONDAY)
                                .build(),
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(8.5D)
                                .setupHours(0.0D)
                                .qty(32)
                                .turn(Turn.FIRST)
                                .day(Day.TUESDAY)
                                .build(),
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(8.1D)
                                .setupHours(0.0D)
                                .qty(31)
                                .turn(Turn.SECOND)
                                .day(Day.TUESDAY)
                                .build(),
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(5.5D)
                                .setupHours(0.0D)
                                .qty(21)
                                .turn(Turn.THIRD)
                                .day(Day.TUESDAY)
                                .build(),
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(8.5D)
                                .setupHours(0.0D)
                                .qty(32)
                                .turn(Turn.FIRST)
                                .day(Day.WEDNESDAY)
                                .build(),
                        new WorkOrderInformation.Builder("p1", "o1")
                                .runHours(7.5D)
                                .setupHours(0.0D)
                                .qty(28)
                                .turn(Turn.SECOND)
                                .day(Day.WEDNESDAY)
                                .build()
                    )
                )
        );
        
        for (final testCaseSingleOrder tc : tests) {
            
            final Progress progress = new Progress(Turn.FIRST, 0.0D, tc.order.getDay());
            final EfficiencyInformation efficiency = EfficiencyUtils.efficiency(tc.order, progress);
            printDebugOrders(efficiency.getOrders());
           
            Assert.assertEquals(tc.want.size(), efficiency.getOrders().size());
            Assert.assertEquals(tc.want, efficiency.getOrders());
        }
        
    }
    
    @Test
    public void validateCase3() {
        
        final List<testCaseSingleOrder> tests = List.of(
                new testCaseSingleOrder(
                    new WorkOrderInformation.Builder("p1", "o1")
                        .runHours(15.9D)
                        .setupHours(2.0D)
                        .day(Day.MONDAY)
                        .qty(220)
                        .build(),
                    List.of(
                        new WorkOrderInformation.Builder("p1", "o1").runHours(6.5D).setupHours(2.0D).qty(90).turn(Turn.FIRST).day(Day.MONDAY).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(8.1D).setupHours(0.0D).qty(112).turn(Turn.SECOND).day(Day.MONDAY).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(1.3000000000000007D).setupHours(0.0D).qty(18).turn(Turn.THIRD).day(Day.MONDAY).build()
                    )
                )
        );
        
        for (final testCaseSingleOrder tc : tests) {
            
            final Progress progress = new Progress(Turn.FIRST, 0.0D, tc.order.getDay());
            final EfficiencyInformation efficiency = EfficiencyUtils.efficiency(tc.order, progress);
            printDebugOrders(efficiency.getOrders());
           
            Assert.assertEquals(tc.want.size(), efficiency.getOrders().size());
            Assert.assertEquals(tc.want, efficiency.getOrders());
        }
        
    }
    
    @Test
    public void validateCase4() {
        
        final List<testCaseSingleOrder> tests = List.of(
                new testCaseSingleOrder(
                    new WorkOrderInformation.Builder("p1", "o1")
                        .runHours(7.5D)
                        .setupHours(14.3D)
                        .day(Day.MONDAY)
                        .qty(60)
                        .build(),
                    List.of(
                        new WorkOrderInformation.Builder("p1", "o1").runHours(0.0D).setupHours(8.5D).qty(0).turn(Turn.FIRST).day(Day.MONDAY).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(2.299999999999999D).setupHours(5.800000000000001D).qty(18).turn(Turn.SECOND).day(Day.MONDAY).build(),
                        new WorkOrderInformation.Builder("p1", "o1").runHours(5.200000000000001D).setupHours(0.0D).qty(42).turn(Turn.THIRD).day(Day.MONDAY).build()
                    )
                )
        );
        
        for (final testCaseSingleOrder tc : tests) {
            
            final Progress progress = new Progress(Turn.FIRST, 0.0D, tc.order.getDay());
            final EfficiencyInformation efficiency = EfficiencyUtils.efficiency(tc.order, progress);
            printDebugOrders(efficiency.getOrders());
           
            Assert.assertEquals(tc.want.size(), efficiency.getOrders().size());
            Assert.assertEquals(tc.want, efficiency.getOrders());
        }
        
    }

    @Test
    public void validatePunzonado1() {
        final List<testCase> tests = List.of(
              new testCase(
                      List.of(
                       new WorkOrderInformation.Builder("M14838B001", "o1")
                               .runHours(8.895999999999864D).setupHours(0.65D).qty(512).machine("4").build(),
                
                        new WorkOrderInformation.Builder("ENC-1500-120-20", "o2")
                                .runHours(15.89500000000006D).setupHours(2.0D).qty(220).machine("5").build(),

                        new WorkOrderInformation.Builder("ENC-1421-37-20", "o3")
                                .runHours(6.449999999999956D).setupHours(2.4D).qty(300).machine("").build(),

                        new WorkOrderInformation.Builder("M14836A003", "o4")
                                .runHours(22.374999999999833D).setupHours(1.3D).qty(500).machine("4").build(),

                        new WorkOrderInformation.Builder("M11353A001", "o5")
                                .runHours(26.06249999999984D).setupHours(1.5D).qty(500).machine("4").build(),

                        new WorkOrderInformation.Builder("M11588A001", "o6")
                                .runHours(5.147999999999945D).setupHours(0.93D).qty(528).machine("4").build(),

                        new WorkOrderInformation.Builder("ENC-1083-130-20", "o7")
                                .runHours(15.187499999999888D).setupHours(1.0D).qty(405).machine("6").build(),

                        new WorkOrderInformation.Builder("ENC-0983-121-20", "o8")
                                .runHours(26.881250000000147D).setupHours(0.7D).qty(550).machine("3").build(),
                        new WorkOrderInformation.Builder("ENC-0983-121-20", "o9")
                                .runHours(24.43750000000007D).setupHours(0.7D).qty(500).machine("3").build(),
                        new WorkOrderInformation.Builder("ENC-0983-121-20", "o10")
                            .runHours(24.43750000000007D).setupHours(0.7D).qty(500).machine("3").build(),

                        new WorkOrderInformation.Builder("M14837A001", "o11")
                            .runHours(2.3287499999999786D).setupHours(0.48D).qty(270).machine("4").build(),


                        new WorkOrderInformation.Builder("4022.635.52983", "o12")
                            .runHours(0.4850000000000021D).setupHours(1.15D).qty(40).machine("2").build(),

                        new WorkOrderInformation.Builder("4022.636.27932", "o13")
                            .runHours(0.5602499999999982D).setupHours(0.75D).qty(54).machine("2").build(),

                        new WorkOrderInformation.Builder("ENC-0983-131-00", "o14")
                            .runHours(22.93199999999985D).setupHours(0.64D).qty(3276).machine("DURMA").build(),

                        new WorkOrderInformation.Builder("ENC-0983-132-20", "o15")
                            .runHours(6.569999999999913D).setupHours(1.0D).qty(720).machine("3").build(),

                        new WorkOrderInformation.Builder("M16270B001", "o16")
                            .runHours(3.78000000000003D).setupHours(1.5D).qty(480).machine("DURMA").build(),

                        new WorkOrderInformation.Builder("ENC-1291-10-20", "o17")
                            .runHours(4.85100000000001D).setupHours(1.12D).qty(392).machine("3").build(),

                        new WorkOrderInformation.Builder("ENC-0983-121-20", "o18")
                            .runHours(12.218749999999995D).setupHours(0.7D).qty(250).machine("3").build(),

                        new WorkOrderInformation.Builder("T18674", "o19")
                            .runHours(4.233000000000057D).setupHours(0.75D).qty(408).machine("3").build(),

                        new WorkOrderInformation.Builder("ENC-0983-141-20", "o20")
                            .runHours(3.0960000000000023D).setupHours(0.0D).qty(72).machine("6").build(),

                        new WorkOrderInformation.Builder("4022.436.71481", "o21")
                            .runHours(1.0912500000000047D).setupHours(0.75D).qty(90).machine("2").build(),

                        new WorkOrderInformation.Builder("M16270B002", "o22")
                            .runHours(0.4775227272727263D).setupHours(0.0D).qty(510).machine("DURMA").build(),

                        new WorkOrderInformation.Builder("M11353A001", "o23")
                            .runHours(13.969500000000009D).setupHours(0.0D).qty(268).machine("4").build()
                      ), List.of()
              )  
        );
        
        Progress progress = new Progress(Turn.FIRST, 0.0D, Day.MONDAY);
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
            // Assert.assertEquals(tc.want, result);
        }
        
    }
    
    @Test
    public void validateMaquinadoManual() {
        final List<testCase> tests = List.of(
            new testCase(
//                    List.of(
//                     new WorkOrderInformation.Builder("4022.637.70091", "o1")
//                             .runHours(6.974999999999994D).setupHours(0.1667D).qty(62).machine("").build(),
//                     new WorkOrderInformation.Builder("4022.637.70102", "o2")
//                             .runHours(104.171875D).setupHours(0.0D).qty(26).machine("").build(),
//                     new WorkOrderInformation.Builder("4022.637.70091", "o3")
//                             .runHours(48.4375D).setupHours(0.15D).qty(31).machine("").build(),
//                     new WorkOrderInformation.Builder("4022.637.70091", "o4")
//                             .runHours(3.487499999999999D).setupHours(0.1667D).qty(31).machine("").build(),
//                     new WorkOrderInformation.Builder("4022.637.70091", "o5")
//                             .runHours(3.487499999999999D).setupHours(0.1667D).qty(31).machine("").build(),
//                     new WorkOrderInformation.Builder("4022.673.47211", "o6")
//                             .runHours(3.125D).setupHours(0.25D).qty(10).machine("2").build(),
//                     new WorkOrderInformation.Builder("4022.673.47221", "o7")
//                             .runHours(3.125D).setupHours(0.25D).qty(10).machine("2").build(),
//                     new WorkOrderInformation.Builder("4022.637.70091", "o8")
//                             .runHours(1.1249999999999998D).setupHours(0.0D).qty(17).machine("").build()
//                    ),
                    // Use Maquinado Manual items ...
                    List.of(
                       new WorkOrderInformation.Builder("4022.637.70111", "o1")
                             .runHours(84.375D).setupHours(0.75D).qty(90).machine("").build()
                            
//                       new WorkOrderInformation.Builder("4022.639.17101", "o2")
//                             .runHours(0.9730000000000016D).setupHours(0.25D).qty(56).machine("2").build(),
//                      
//                       new WorkOrderInformation.Builder("ENC-1010-18-20", "o3")
//                             .runHours(9.205875000000063D).setupHours(0.25D).qty(438).machine("3").build(),
//                       
//                       new WorkOrderInformation.Builder("ENC-1291-14-21", "o4")
//                             .runHours(10.008000000000008D).setupHours(0.17D).qty(288).machine("3").build(),
//                       
//                       new WorkOrderInformation.Builder("ENC-1291-14-21", "o5")
//                             .runHours(10.008000000000008D).setupHours(0.17D).qty(288).machine("3").build()
                    ),
                    List.of()
            )
        );
        
        Progress progress = new Progress(Turn.FIRST, 0.0D, Day.MONDAY);
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
            // Assert.assertEquals(tc.want, result);
        }
        
    }
    
    private static void printDebugOrders(final List<WorkOrderInformation> orders) {
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~.>");
        orders.forEach(System.out::println);
        System.out.println("DEBUG ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/.>");
    }
    
}
