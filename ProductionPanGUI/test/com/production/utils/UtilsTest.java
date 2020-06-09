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
}
