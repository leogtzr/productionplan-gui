package com.production.util;

import com.production.domain.Day;
import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.EfficiencyInformation;
import com.production.domain.efficiency.Progress;
import com.production.lang.MissingTests;
import com.production.lang.Validated;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class EfficiencyUtils {
    
    // PENDING: add more tests here and change the annotation here.
    @MissingTests
    public static EfficiencyInformation efficiency(
            final WorkOrderInformation ord, final Progress progress) {
        
        final int qty = ord.getQty();
        final double totalHours = ord.getRunHours();
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
        
        double xs;
        double xr;
        
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

            final WorkOrderInformation o = OrderUtils.from(ord, xr, xs, progress);
            final long qtyGoalPerTurn = qtyGoalPerTurn(qty, xr, totalHours);
            o.setQty((int)qtyGoalPerTurn);
            debugOrderCreation("c", o);
            orders.add(o);

            if (hInTurn == 0.0) {
                System.out.println("d5 -> hInTurn is 0.0, changing turn.");
                final Turn beforeTurn = progress.getTurn();
                final Turn nextTurn = Utils.nextTurn(progress.getTurn(), numberOfTurns);
                progress.setTurn(nextTurn);
                
                if (nextTurn.equals(Turn.FIRST) && beforeTurn.equals(Turn.THIRD)) {
                    System.out.printf("A change of day!\n");
                    final Day day = progress.getDay();
                    final Day nextDay = Utils.nextDay(day);
                    System.out.printf("Day change (turns): %s:beforeTurn, nextTurn:%s!\n", beforeTurn, nextTurn);
                    System.out.printf("Day change: %s:before, next:%s!\n", day, nextDay);
                    o.setDay(day);
                    progress.setDay(nextDay);
                }
                
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
	System.out.printf("\t\t#Case-%s - Order to be created with runHours: %.3f, setupHours: %.3f (%s)\n", suffix, 
                ord.getRunHours(), ord.getSetupHours(), ord.getTurn());
    }
    
    /**
     *
     * @param qty
     * @param hoursWithEfficiency
     * @param totalHours
     * @return
     */
    @Validated
    public static long qtyGoalPerTurn(final int qty, final double hoursWithEfficiency, final double totalHours) {
        return Math.round((double)qty * hoursWithEfficiency / totalHours);
    }
    
    private EfficiencyUtils() {}
    
}
