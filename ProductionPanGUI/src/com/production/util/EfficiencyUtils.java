package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import com.production.domain.efficiency.Progress;
import com.production.lang.MissingTests;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class EfficiencyUtils {
    
    private static void p(final String msg) {
        System.out.print(msg);
    }
    
    private static void pln(final String msg) {
        System.out.println(msg);
    }
    
    private static void pln() {
        System.out.println();
    }
    
    private static void p() {
        System.out.print("");
    }
    
    @MissingTests
    public static EfficiencyInformation algo2(
            final WorkOrderInformation workOrderInfo
            , double initHours
            , Turn currTurn) {
        
        final EfficiencyInformation efficiencyInformation = new EfficiencyInformation();
        final List<WorkOrderInformation> orders = new ArrayList<>();
        int it = 0;
        
        double availableHoursInTurn = Utils.availableHoursInTurn(currTurn, initHours);
        pln("X(0), availableHoursInTurn: " + availableHoursInTurn);
        
        // TODO: need some calculations here to check where do we need to start.
        // TODO: perhaps based on initHours ...
        double remHours = availableHoursInTurn;
        double remSetup = workOrderInfo.getSetupHours();
        boolean shouldChangeTurn = false;
        
        pln("remHours: " + remHours);
        
        outer:
        while ((remHours != 0.0D) || (remSetup != 0.0D)) {
            it++;
            
            if (it >= 50) {
                pln("ERROR: reach max");
                pln("DIAGNOSTICS, remHours: " + remHours);
                pln("DIAGNOSTICS, remSetup: " + remSetup);
                break;
            }
            
            double turnHours = Utils.turnHours(currTurn);
            
            pln("DIAGNOSTICS, remHours: " + remHours);
            pln("DIAGNOSTICS, remSetup: " + remSetup);
            
            if ((remHours != 0.0D) && (remSetup == 0.0D)) {
                pln("X(1) need to adjust runHours");
                //              Qué casos podría haber aquí?
                // Que remHours sea menor a turnHours
                // Que remHours sea mayor a turnHours
                if (remHours > turnHours) {
                    pln("X(1.1) remHours > turnHours, remHours: " + remHours + ", remHours: " + remHours);
                    
                    pln("X(1.1), turnHours: " + turnHours);
                    final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, turnHours, 0.0D, currTurn);
                    orders.add(wo);
                    System.out.printf("X(1.1) created: %s\n", wo.toShortString());
                    
                    remHours -= turnHours;
                    
                    currTurn = Utils.nextTurn(currTurn, 3);
                    initHours = remHours;
                    
                } else if (remHours < turnHours) {
                    pln("X(1.2) remHours < turnHours, remHours: " + remHours + ", remHours: " + remHours);
                    final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, remHours, 0.0D, currTurn);
                    orders.add(wo);
                    System.out.printf("X(1.2) created: %s\n", wo.toShortString());
                    initHours += 0.0D;
                    remHours = 0.0D;
                } else {
                    // we might have all the turn available ...
                    pln("X(1.3) other ... ");
                    pln("X(1.3)DIAGNOSTICS, remHours: " + remHours);
                    pln("X(1.3)DIAGNOSTICS, remSetup: " + remSetup);
                    double runHours = workOrderInfo.getRunHours();
                    if (runHours > remHours) {
                        pln("X(1.4) runHours > remHours, runHours: " + runHours + ", remHours: " + remHours);
                    } else if (runHours < remHours) {
                        pln("X(1.5) runHours < remHours, runHours: " + runHours + ", remHours: " + remHours);
                        pln("X(1.5) creating order");
                        final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, runHours, 0.0D, currTurn);
                        orders.add(wo);
                        System.out.printf("X(1.5) created: %s\n", wo.toShortString());
                        
                    } else {
                        pln("X(1.6) runHours == remHours?, runHours: " + runHours + ", remHours: " + remHours);
                    }
                }
                
            } else if ((remHours == 0.0D) && (remSetup != 0.0D)) {
                pln("X(2) need to adjust setup hours.");
                
                if (remHours > turnHours) {
                    pln("X(2.1) remHours > turnHours, remHours: " + remHours + ", remHours: " + remHours);
                } else if (remHours < turnHours) {
                    // pln("X(2.2) remHours < turnHours, remHours: " + remHours + ", remHours: " + remHours);
                    System.out.printf("X(2.2) remHours < turnHours, remHours: %f, turnHours: %f\n", remHours, turnHours);
                } else {
                    pln("X(2.3) other ... ");
                    pln("X(2.3) DIAGNOSTICS, remHours: " + remHours);
                    pln("X(2.3) DIAGNOSTICS, remSetup: " + remSetup);
                }
            } else {
                // In this specific case we have to take into consideration runHours and setupHours together for the available time.
                pln("X(3) other");
                
                double runHours = workOrderInfo.getRunHours();
                double stp = workOrderInfo.getSetupHours();
                double both = runHours + stp;
                
                pln("X(3) DIAGNOSTICS, runHours: " + runHours);
                pln("X(3) DIAGNOSTICS, stp: " + stp);
                pln("X(3) DIAGNOSTICS, both: " + both);
                
                // What scenarios could we have?
                // runHours > remHours and both > remHours - 3.1
                // runHours < remHours and both > remHours - 3.2
                // runHours < remHours and both < remHours - 3.3
                    // 3.3 could mean that we are good to use runHours and setup ... 
                if ((runHours > remHours) && (both > remHours)) {
                    pln("X(3.1), runHours: " + runHours + ", remHours: " + remHours + ", both: " + both + ", setup: " + stp);
                    // Need more analysis here ...
                    // Based on above's conditions we might need to check these scenarios:
                        // can we allocate only the setup?
                        if (stp < remHours) {
                            // EXPL: being here means that we have room for the setup but not for both
                            // meaning that we need to make some calculations ...
                            pln("X(3.1.1), stp: " + stp + ", remHours: " + remHours + ", remSetup: " + remSetup);
                            if (remSetup < remHours) {
                                pln("X(3.1.1.2), remSetup < remHours, remSetup: " + remSetup + ", remHours: " + remHours);
                                // Podemos ocupar todo el setup.
                                pln("X(3.1.1.2) creating order");
                                final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, remHours - remSetup, remSetup, currTurn);
                                orders.add(wo);
                                
                                System.out.printf("X(3.1.1.2) created: %s\n", wo.toShortString());
                                
                                
                                pln("X(3.1.1.2), remHours: " + remHours);
                                pln("X(3.1.1.2), remSetup: " + remSetup);
                                pln("X(3.1.1.2), runHours: " + runHours);
                                
                                runHours -= (remHours - remSetup);
                                remHours = runHours;
                                // remHours = 0.0D;
                                // remHours = runHours;
                                remSetup = 0.0D;
                                
                                pln("X(3.1.1.2)");
                                pln("X(3.1.1.2), remHours: " + remHours);
                                pln("X(3.1.1.2), remSetup: " + remSetup);
                                pln("X(3.1.1.2), runHours: " + runHours);
                                
                                initHours = 0.0D;
                                currTurn = Utils.nextTurn(currTurn, 3);
                                
                            } else {
                                pln("X(3.1.1.3) ... ");
                            }
                        } else if (stp > remHours) {
                            // Let's allocate only what we have available.
                            pln("X(3.1.2), stp: " + stp + ", remHours: " + remHours + ", remSetup: " + remSetup);
                            pln("X(3.1.2) creating order");
                            final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, 0.0D, remHours, currTurn);
                            orders.add(wo);
                            
                            System.out.printf("X(3.1.2) created: %s\n", wo.toShortString());
                            // We ran out of hours in the current turn so we have to move to the next one ... 
                            // currTurn = Utils.nextTurn(currTurn, 3);
                            // break;
                            remSetup -= remHours;
                            pln("X(3.1.2), remSetup is now: " + remSetup);
                            pln("X(3.1.2), stp: " + stp + ", remHours: " + remHours + ", remSetup: " + remSetup);
                            
                            // I have basically covered all the turn so now I just have to move to the next turn and update the variables.
                            currTurn = Utils.nextTurn(currTurn, 3);
                            double available = Utils.availableHoursInTurn(currTurn, 0.0D); // 0 because I have used everything in this turn.
                            remHours = available;
                            initHours = 0.0D;
                            pln("X(3.1.2), remHours is now: " + remHours);
                            pln("X(3.1.2), turn is now: " + currTurn);
                        } else {
                            pln("X(3.1.3), stp: " + stp + ", remHours: " + remHours);
                        }
                } else if ((runHours < remHours) && (both > remHours)) {
                    pln("X(3.2), runHours: " + runHours + ", remHours: " + remHours + ", both: " + both);
                } else if ((runHours < remHours) && (both < remHours)) {
                    pln("X(3.3), runHours: " + runHours + ", remHours: " + remHours + ", both: " + both);
                    pln("X(3.3), stp is: " + stp);
                    pln("X(3.3), remSetup is: " + remSetup);
                    pln("X(3.3) creating order");
                    final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, runHours, remSetup, currTurn);
                    System.out.printf("X(3.3) created: %s\n", wo.toShortString());
                    // TODO: need to update initHours here ... 
                    pln("X(3.3) initHours before update is: " + initHours + ", remHours: " + remHours + ", remSetup: " + remSetup);
                    initHours += runHours + remSetup;
                    pln("X(3.3) initHours after update is: " + initHours);
                    orders.add(wo);
                    
                    remHours = 0.0D;
                    remSetup = 0.0D;
                } else {
                    pln("X(4)");
                    pln("X(4) DIAGNOSTICS, runHours: " + runHours);
                    pln("X(4) DIAGNOSTICS, remHours: " + remHours);
                    pln("X(4) DIAGNOSTICS, stp: " + stp);
                    pln("X(4) DIAGNOSTICS, both: " + both);
                    pln("X(4) DIAGNOSTICS, initHours: " + initHours);
                }
            }
            // break;
        }
        
        efficiencyInformation.setOrders(orders);
        efficiencyInformation.setOutputTurn(currTurn);
        // TODO: re-check this later ... 
//        if (shouldChangeTurn) {
//            currTurn = Utils.nextTurn(currTurn, 3);
//            efficiencyInformation.setOutputTurn(currTurn);
//        }
        // TODO: check a good way of calculating this ... 
        efficiencyInformation.setInitHours(initHours);
        return efficiencyInformation;
    }
    
    // PENDING: add more tests here and change the annotation here.
    @MissingTests
    public static EfficiencyInformation efficiency(
            final WorkOrderInformation ord, final Progress progress) {
        
        final EfficiencyInformation efficiencyInformation = new EfficiencyInformation();
        final List<WorkOrderInformation> orders = new ArrayList<>();
        final int numberOfTurns = 3;
        
        final double whereWeAre = progress.getFactor();
        
        System.out.printf("\t@@@@@@@ Progress received (%s turn): %f\n", progress.getTurn(), whereWeAre);        
        double hInTurn = Utils.turnHours(progress.getTurn());
        
        hInTurn -= whereWeAre;
        
        System.out.printf("\tWhat we have left: %f (%s)\n", hInTurn, progress.getTurn());
	System.out.printf("\tWorking with: O('%s'), rh: %f, stp: %f\n", ord.getWorkOrder(), ord.getRunHours(), ord.getSetupHours());
        
        int it = 0;
        
        double xs = 0.0;
        double xr = 0.0;
        
        System.out.printf("Before to work: %s\n", ord);
        
        while (ord.getRunHours() != 0.0 || ord.getSetupHours() != 0.0) {

            it++;
            if (it >= 10) {
                System.out.printf("MAX point reached. ord.RunHours(%.3f), ord.Setup(%.3f)\n", ord.getRunHours(), ord.getSetupHours());
                break;
            }

            if ((hInTurn - ord.getSetupHours()) >= 0.0) {
                System.out.printf("d1. hInTurn(%.2f) - ord.Setup(%.2f) (= %.2f) > 0.0\n", hInTurn, ord.getSetupHours(), hInTurn-ord.getSetupHours());
                xs = ord.getSetupHours();
                hInTurn -= ord.getSetupHours();
            } else {
                xs = hInTurn;
                hInTurn = 0.0;
            }

            System.out.printf("d2. hInTurn=%.2f\n", hInTurn);

            if ((hInTurn - ord.getRunHours()) >= 0.0) {
                System.out.printf("d3. hInTurn(%.2f) - ord.RunHours(%.2f) (= %.2f) > 0.0\n", hInTurn, ord.getRunHours(), hInTurn-ord.getRunHours());
                hInTurn -= ord.getRunHours();
                xr = ord.getRunHours();
            } else {
                xr = hInTurn;
                hInTurn = 0.0;
            }

            System.out.printf("d4. hInTurn=%.2f\n", hInTurn);

            final WorkOrderInformation o = OrderUtils.from(ord, xr, xs, progress.getTurn());
            debugOrderCreation("c", o);
            orders.add(o);

            if (hInTurn == 0.0) {
                System.out.println("d5 -> hInTurn is 0.0, changing turn.");
                // nextTurn(progress);
                progress.setTurn(Utils.nextTurn(progress.getTurn(), numberOfTurns));
                hInTurn = Utils.turnHours(progress.getTurn());
                System.out.printf("d6. Turn is now: %.1f\n", hInTurn);
            }

            System.out.printf("1) O{r: %.2f, s: %.2f}\n", ord.getRunHours(), ord.getSetupHours());

            ord.setRunHours(ord.getRunHours() - xr);
            ord.setSetupHours(ord.getSetupHours() - xs);
            
            System.out.printf("2) O{r: %.2f, s: %.2f}\n", ord.getRunHours(), ord.getSetupHours());
	}

        
        efficiencyInformation.setOrders(orders);
        return efficiencyInformation;
    }
    
    private static void debugOrderCreation(String suffix, WorkOrderInformation ord) {
	System.out.printf("\t\t#Case-%s - Order to be created with runHours: %.3f, setupHours: %.3f (%s)\n", suffix, ord.getRunHours(), ord.getSetupHours(), ord.getTurn());
    }
    
    private EfficiencyUtils() {}
    
}
