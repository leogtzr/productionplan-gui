package com.production.domain;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public enum FormatType {
    HTML("html")
    , CSV("csv")
    ;
    private final String type;

    FormatType(String type) {
        this.type = type;
    }
    
    public String get() {
        return this.type;
    }
}
