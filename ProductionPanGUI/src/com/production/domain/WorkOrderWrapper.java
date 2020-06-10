package com.production.domain;

import java.util.Objects;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class WorkOrderWrapper {
    
    private SimpleWorkOrderInformation workOrderInfo;
    private Day day;

    public WorkOrderWrapper(final SimpleWorkOrderInformation workOrderInfo, final Day day) {
        this.workOrderInfo = workOrderInfo;
        this.day = day;
    }

    public WorkOrderWrapper() {}

    public SimpleWorkOrderInformation getWorkOrderInfo() {
        return workOrderInfo;
    }

    public void setWorkOrderInfo(final SimpleWorkOrderInformation workOrderInfo) {
        this.workOrderInfo = workOrderInfo;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(final Day day) {
        this.day = day;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.workOrderInfo);
        hash = 37 * hash + Objects.hashCode(this.day);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WorkOrderWrapper other = (WorkOrderWrapper) obj;
        if (!Objects.equals(this.workOrderInfo, other.workOrderInfo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WOWrapper{" + "workOrderInfo=" + workOrderInfo + ", day=" + day + '}';
    }
    
}
