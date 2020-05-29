package com.production.domain;

public enum AgeByWCFields {
    WORK_ORDER_NUMBER(7)
    , AGE(9)
    , SALES_PRICE(11)
    ;
    private final int index;

    AgeByWCFields(final int index) {
        this.index = index;
    }

    public int get() {
        return index;
    }
}
