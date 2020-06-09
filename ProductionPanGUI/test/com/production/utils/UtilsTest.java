package com.production.utils;

import com.production.util.Utils;
import org.junit.Assert;

import org.junit.Test;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class UtilsTest {
    
    public UtilsTest() {}
    
    @Test
    public void shouldReturnNumberOfTurnsFromWorkCenter() {
        final Object[][] tests = {
            {"DOBLADO", 3},
            {"PUNZONADO", 3},
            {"MAQUINADO_MANUAL", 2},
        };
        
        for (final Object[] test : tests) {
            final int turns = Utils.numberOfTurnsFromWorkCenter(test[0].toString());
            Assert.assertEquals(turns, Integer.parseInt(test[1].toString()));
        }
    }
        
    @Test
    public void shouldReturnWorkCenterName() {
        final Object[][] tests = {
            {"Laser 1", "LASER"},
            {"PUNZONADO 2", "PUNZONADO"},
        };
        
        for (final Object[] test : tests) {
            final String got = Utils.workCenterName(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldSanitizeWorkCenterName() {
        final Object[][] tests = {
            {"Laser", true},
            {"Arroz", false},
        };
        
        for (final Object[] test : tests) {
            final boolean got = Utils.allowedWC(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldValidateAllowedWorkCenters() {
        final Object[][] tests = {
            {"Laser 1", "LASER_1"},
            {"PUNZONADO-2", "PUNZONADO_2"},
        };
        
        for (final Object[] test : tests) {
            final String got = Utils.sanitizeWorkCenterName(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
}
