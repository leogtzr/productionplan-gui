package com.production.util;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
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
                } else if (remHours < turnHours) {
                    pln("X(1.2) remHours < turnHours, remHours: " + remHours + ", remHours: " + remHours);
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
                        final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, runHours, 0.0D, currTurn);
                        orders.add(wo);
                        
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
                            pln("X(3.1.1), stp: " + stp + ", remHours: " + remHours);
                        } else if (stp > remHours) {
                            // Let's allocate only what we have available.
                            pln("X(3.1.2), stp: " + stp + ", remHours: " + remHours + ", remSetup: " + remSetup);
                            final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, 0.0D, remHours, currTurn);
                            orders.add(wo);
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
                    final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, runHours, stp, currTurn);
                    // TODO: need to update initHours here ... 
                    pln("X(3.3) initHours before update is: " + initHours);
                    initHours += both;
                    pln("X(3.3) initHours after update is: " + initHours);
                    orders.add(wo);
                    // currTurn = Utils.nextTurn(currTurn, 3);
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
    
    private EfficiencyUtils() {}
    
}
