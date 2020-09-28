package com.production.util;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public final class Constants {
    
    public static final String TAB_SHEET_NAME = "FAB Load by WC";
    public static final String AGE_BY_WC_SHEET_NAME = "Age  by WC";
    public static final String DOBLADO_PART_MACHINE_FILE_NAME = "doblado-part-machines.csv";
    public static final String LASER_AND_PUNCH_PART_MACHINE_FILE_NAME = "laserpunch-part-machines.csv";
    
    // TODO: the value of following constant will have to be changed eventually:
    public static final String FAB_LOAD_FILE_NAME = "FAB Load by WC Leo.xls";
    public static final String AGE_BY_WC_FILE_NAME = "Age  by WC.xls";
    
    public static final double RUN_EFFICIENCY = 0.8;
    
    public static final double FIRST_TURN_LENGTH = 8.5D;
    public static final double SECOND_TURN_LENGTH = 8.1D;
    public static final double THIRD_TURN_LENGTH = 5.5D;
    
    public static final String DOBLADO = "DOBLADO";
    public static final String PUNZONADO = "PUNZONADO";
    public static final String LASER = "LASER";
    
    public static final int ALLOWED_COLUMN_NUMBER_TO_BE_EDITED = 4;
        
    public static final String CONFIG_PROPERTIES_FILE_NAME = "prodplan.properties";
    
    private Constants() {}
    
}
