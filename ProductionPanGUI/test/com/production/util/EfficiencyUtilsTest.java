package com.production.util;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class EfficiencyUtilsTest {
    
    @Test
    public void shouldReturnQtyGoalPerHour() {
        
        class testCase {
            int qty;
            double hoursWithEfficiency;
            double totalHours;
            long want;

            testCase(final int qty, final double hoursWithEfficiency, final double totalHours, final long want) {
                this.qty = qty;
                this.hoursWithEfficiency = hoursWithEfficiency;
                this.totalHours = totalHours;
                this.want = want;
            }
        }
        
        List<testCase> tests = List.of(
            new testCase(220, 6.5D, 15.9D, 90L),
            new testCase(220, 8.1D, 15.9D, 112L),
            new testCase(220, 1.3D, 15.9D, 18L)
        );
        
        tests.forEach(tc -> 
                Assert.assertEquals(tc.want, EfficiencyUtils.qtyGoalPerTurn(tc.qty, tc.hoursWithEfficiency, tc.totalHours)));
        
    }
        
}
