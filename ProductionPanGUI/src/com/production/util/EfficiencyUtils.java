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
    public static EfficiencyInformation splitWithEfficiency2(
            final WorkOrderInformation workOrderInfo
            , double initHours
            , Turn currTurn) {
        
        final EfficiencyInformation efficiencyInformation = new EfficiencyInformation();
        final List<WorkOrderInformation> orders = new ArrayList<>();
        
        //double available = Utils.turnHours(currTurn) - initHours;
        double available = initHours == 0.0D ? Utils.turnHours(currTurn) : (Utils.turnHours(currTurn) - initHours);
        double r = available;
        double stp = workOrderInfo.getSetupHours();
        boolean savingSetupHours = true;
        pln("available: " + available);
        boolean done = false;
        
        int it = 0;
        
        while (!done) {
            
            if (it >= 50) {
                pln("ERROR: reach max");
                break;
            }
            
            // Podemos abarcar todo sin problemas y al final actualizamos available
            if ((workOrderInfo.getRunHours() + workOrderInfo.getSetupHours()) < available) {
                pln("DEBUG | case 1)");
                if (savingSetupHours) {
                    pln("DEBUG | case 2)");
                    pln("X(2) saving hours");
                    savingSetupHours = false;
                    // TODO: algoritmo
                    // TODO: no olvidar actualizar 
                    final double newRunHours = workOrderInfo.getRunHours();
                    final WorkOrderInformation wo = Utils
                            .workOrderInfoWithSetup(workOrderInfo, newRunHours, workOrderInfo.getSetupHours(), currTurn);
                    
                    orders.add(wo);
                    
                    //available -= (workOrderInfo.getRunHours() + workOrderInfo.getSetupHours());
                    // r = available;
                    r = initHours + workOrderInfo.getRunHours() + workOrderInfo.getSetupHours();
                    pln("X(2) r is: " + r);
                    efficiencyInformation.setInitHours(r);
                    available = 0.0D;           // or a break ...
                    done = true;
                    pln("X(2): available is: " + available);
                    break;
                } else {
                    // TODO: algoritmo
                    pln("DEBUG | case 3)");
                }
                // Actualizar available e initHours
                
            } else if ((workOrderInfo.getRunHours() + workOrderInfo.getSetupHours()) > available) {
                pln("DEBUG | case 4)");
                pln("X(4), runHours is: " + workOrderInfo.getRunHours());
                pln("X(4), setupHours is: " + workOrderInfo.getSetupHours());
                pln("X(4): available is: " + available);
                
                // NOTE: to stop it just available = 0.0D;
                
                // ¿Qué casos puede haber aquí? 

                // 1) Que solo parte de runHours quepa y setup completo.
                // 2) Que ni runHours ni setup quepan.
                //  2.1) tomar como prioridad setup para ocupar
                if ((workOrderInfo.getRunHours() > available) && (workOrderInfo.getSetupHours() < available)) {     // 1)
                    pln("\tX(5)");
                    pln("X(5), runHours is: " + workOrderInfo.getRunHours());
                    pln("X(5), setupHours is: " + workOrderInfo.getSetupHours());
                    pln("X(5): available is: " + available);
                } else if ((workOrderInfo.getRunHours() > available) && (workOrderInfo.getSetupHours() > available)) {
                    pln("\tX(6)");
                    pln("X(6), runHours is: " + workOrderInfo.getRunHours());
                    pln("X(6), setupHours is: " + workOrderInfo.getSetupHours());
                    pln("X(6): available is: " + available);
                    // Darle prioridad al setup
                    // Nada nos detiene de introducir un loop aquí para salir del atolladero ... I guess ... 
                    // La otra alternativa es abarcar el espacio que tenemos disponible y actualizar variables ...
                    // me inclino más hacia esta última ... 
                }
                
                // Actualizar available e initHours
            }
            
            it++;
            
        }
        
        efficiencyInformation.setOrders(orders);
        efficiencyInformation.setOutputTurn(currTurn);
        return efficiencyInformation;
        
    }
    
    // TODO: numberOfTurns as parameter?
    @MissingTests
    public static EfficiencyInformation splitWithEfficiency(
            final WorkOrderInformation workOrderInfo
            , double initHours
            , Turn currTurn) {
        
        final EfficiencyInformation efficiencyInformation = new EfficiencyInformation();
        final List<WorkOrderInformation> orders = new ArrayList<>();
        
        double r = workOrderInfo.getRunHours();
        boolean savingSetupHours = true;
        int it = 0;
        
        while (r > 0.0D) {
            it++;
            pln("");
            pln(String.format("Starting %d it", it));
            pln("Curr turn is: " + currTurn);
            
            final double currTurnHours = Utils.turnHours(currTurn);
            if (initHours > 0.0D) {
                pln("initHours > 0.0D -> initHours is " + initHours);
                pln("currTurnHours: " + currTurnHours);
                
                final double availableHoursInTurn = currTurnHours - initHours;
                final double offset = currTurnHours - initHours;
                pln("availableHoursInTurn: " + availableHoursInTurn);
                pln("offset is: " + offset);
                pln("r is: " + r);
                
                // are we saving hours and initHours > 0.0D
                if (savingSetupHours) {
                    if (r > offset) {
                        pln("r > offset ... ");
                        pln("offset is: " + offset);
                        final double newRunHours = offset - workOrderInfo.getSetupHours();
                        pln("newRunHours is: " + newRunHours);
                        pln("setupHours is: " + workOrderInfo.getSetupHours());
                        
                        final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, newRunHours, workOrderInfo.getSetupHours(), currTurn);
                        orders.add(wo);
                        
                        r = r - (offset - workOrderInfo.getSetupHours());
                        pln("r now is: " + r);
                        initHours = r;
                        savingSetupHours = false;
                        pln("Turn is ... " + currTurn);
                        pln("- initHours is ... " + initHours);
                        // Do I need to update the 
                        currTurn = Utils.nextTurn(currTurn, 3);
                        
                    } else {
                        savingSetupHours = false;
                        pln("r < offset ... ");
                        pln("r is: " + r + ", offset is: " + offset);
                        pln("setup hours is: " + workOrderInfo.getSetupHours());
                        // Necesitamos tomar en consideración la suma de runHours y 
                        if ((r + workOrderInfo.getSetupHours()) > offset) {
                            final double newRunHours = offset - workOrderInfo.getSetupHours();
                            pln("(r + workOrderInfo.getSetupHours()) > offset");
                            pln("newRunHours will be: " + newRunHours);
                            final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, newRunHours, workOrderInfo.getSetupHours(), currTurn);
                            orders.add(wo);
                            pln("====================..");
                            pln(wo.toString());
                            pln("..====================");
                            
                            r = r - newRunHours;
                            initHours = 0.0D;
                            currTurn = Utils.nextTurn(currTurn, 3);
                            pln("done with this block 1)");
                            
                        } else {
                            pln("(r + workOrderInfo.getSetupHours()) < offset");
                        }
                        // r = 0.0D;
                    }
                    //r = 0.0D;
                } else {
                    pln("not saving hours ... ");
                    pln("Aquiii");
                    pln("initHours is: " + initHours);
                    pln("offset is: " + offset);
                    
                    if (r > offset) {
                        pln("$1 r > offset");
                    } else {
                        pln("$2 r < offset");
                        pln("r is: " + r);
                        final double newRunHours = r;
                        double stp = workOrderInfo.getSetupHours();
                        if ((r + stp) > offset) {
                            pln("we need to adjust here ... ");
                        } else {
                            stp = workOrderInfo.getSetupHours();
                        }
                        final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, newRunHours, stp, currTurn);
                        orders.add(wo);
                        pln("create an order with the stuff we have available ... ");
                        initHours = r;
                        pln("r was: " + r);
                        r = 0.0D;
                    }
                    
                    r = 0.0D;
                }
                
                pln("Bye ... ");
            } else {
                // Especial para es la primera orden.
                if (savingSetupHours) {
                    pln("savingSetup hours ... ");
                    // TODO: missing the if r > currTurn
                    if (r > currTurnHours) {
                        pln("r > currTurnHours");
                        savingSetupHours = false;
                        final double newRunHours = currTurnHours - workOrderInfo.getSetupHours();
                        pln("Curr turn hours is: " + currTurnHours);
                        System.out.printf("newRunHours = %f\n", newRunHours);
                        r = r - newRunHours;
                        final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, newRunHours, workOrderInfo.getSetupHours(), currTurn);
                        orders.add(wo);

                        currTurn = Utils.nextTurn(currTurn, 3);
                    } else {
                        pln("r < currTurnHours");
                        if ((r + workOrderInfo.getSetupHours()) > currTurnHours) {
                            pln("(r + workOrderInfo.getSetupHours()) > currTurnHours");
                        } else {
                            // Creo que podemos abarcar todo el turno con la info actual ...
                            savingSetupHours = false;
                            final double newRunHours = r;
                            final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, newRunHours, workOrderInfo.getSetupHours(), currTurn);
                            orders.add(wo);
                            initHours = r + workOrderInfo.getSetupHours();
                            r = 0.0D;
                        }
                        // r = 0.0D;
                    }
                    
                } else {
                    pln("not saving hours ... ");
                    if (r > currTurnHours) {
                        pln("r > currTurnHours");
                        final double newRunHours = currTurnHours;
                        pln("Curr turn hours is: " + currTurnHours);
                        System.out.printf("newRunHours = %f\n", newRunHours);
                        r = r - newRunHours;
                        final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, newRunHours, 0.0D, currTurn);
                        orders.add(wo);

                        currTurn = Utils.nextTurn(currTurn, 3);
                    } else {
                        pln("r < currTurnHours");
                        initHours = r;
                        final WorkOrderInformation wo = Utils.workOrderInfoWithSetup(workOrderInfo, r, 0.0D, currTurn);
                        orders.add(wo);
                        r = 0.0D;
                    }
                }
            }
        }
        
        pln("initHours is: " + initHours);
        pln("finishing with: " + currTurn);
        efficiencyInformation.setInitHours(initHours);
        efficiencyInformation.setOutputTurn(currTurn);
        
        efficiencyInformation.setOrders(orders);
        
        return efficiencyInformation;
    }
    
    private EfficiencyUtils() {}
    
}
