package com.production.util.logging;

import java.util.Date;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class Logging {
    
    public static void trace(final String message) {
        System.out.printf("[%s] - %s\n", new Date(), message);
    }
    
    public static void info(final String format, Object ... args) {
        final String msg = String.format(format, args);
        printToStdout("INFO", msg);
    }
    
    public static void debug(final String format, Object ... args) {
        final String msg = String.format(format, args);
        printToStdout("DEBUG", msg);
    }
    
    public static void warn(final String format, Object ... args) {
        final String msg = String.format(format, args);
        printToStdout("WARN", msg);
    }
    
    public static void error(final String format, Object ... args) {
        final String msg = String.format(format, args);
        printToStdout("ERROR", msg);
    }
    
    private static void printToStdout(final String suffix, final String msg) {
        System.out.printf("[%s] - %s\n", suffix, msg);
    }
    
    private Logging() {}
    
}
