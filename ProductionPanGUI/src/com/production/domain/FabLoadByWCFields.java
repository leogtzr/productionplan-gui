package com.production.domain;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public enum FabLoadByWCFields {
    WC_DESCRIPTION_INDEX(1),
    PART_CELL_INDEX(4),
    WORKORDER_CELL_INDEX(6),
    RUN_CELL_INDEX(10),
    SETUP_CELL_INDEX(9),
    QTY_CELL_INDEX(12),
    ;
    
    private final int index;
    
    FabLoadByWCFields(final int index) {
        this.index = index;
    }
    
    public int get() {
        return this.index;
    }

}
