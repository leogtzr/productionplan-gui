package com.production.config;

import java.util.Properties;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class ConfigUtils {
    
    public static String get(final Properties props, final String key, final String defaultValue) {
        if (props == null) {
            return defaultValue;
        }
        return props.contains(key) ? props.get(key).toString() : defaultValue;
    }
    
    private ConfigUtils() {}
    
}
