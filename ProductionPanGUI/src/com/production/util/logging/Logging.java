package com.production.util.logging;

import java.util.Date;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class Logging {
    
    public static void trace(final String message) {
        System.out.printf("[%s] - %s\n", new Date(), message);
    }
    
    private Logging() {}
    
}
