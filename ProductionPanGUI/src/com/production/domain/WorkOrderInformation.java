package com.production.domain;

import java.util.Objects;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public final class WorkOrderInformation {
    
    private String wcDescription;
    private String partNumber;
    private String workOrder;
    private double runHours;
    private double setupHours;
    private int qty;
    private int age;
    private double salesPrice;
    private Turn turn = Turn.NA;
    private Day day = Day.MONDAY;

    public WorkOrderInformation() { }
    
    public WorkOrderInformation(final String partNumber, final String workOrder) {
        this.partNumber = partNumber;
        this.workOrder = workOrder;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(final String partNumber) {
        this.partNumber = partNumber;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(final String workOrder) {
        this.workOrder = workOrder;
    }

    public double getRunHours() {
        return runHours;
    }

    public void setRunHours(final double runHours) {
        this.runHours = runHours;
    }

    public double getSetupHours() {
        return setupHours;
    }

    public void setSetupHours(final double setupHours) {
        this.setupHours = setupHours;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public String getWcDescription() {
        return wcDescription;
    }

    public void setWcDescription(final String wcDescription) {
        this.wcDescription = wcDescription;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(final Turn turn) {
        this.turn = turn;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(final Day day) {
        this.day = day;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.wcDescription);
        hash = 73 * hash + Objects.hashCode(this.partNumber);
        hash = 73 * hash + Objects.hashCode(this.workOrder);
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.runHours) ^ (Double.doubleToLongBits(this.runHours) >>> 32));
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.setupHours) ^ (Double.doubleToLongBits(this.setupHours) >>> 32));
        hash = 73 * hash + this.qty;
        hash = 73 * hash + this.age;
        hash = 73 * hash + (int) (Double.doubleToLongBits(this.salesPrice) ^ (Double.doubleToLongBits(this.salesPrice) >>> 32));
        hash = 73 * hash + Objects.hashCode(this.turn);
        hash = 73 * hash + Objects.hashCode(this.day);
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
        final WorkOrderInformation other = (WorkOrderInformation) obj;
        if (Double.doubleToLongBits(this.runHours) != Double.doubleToLongBits(other.runHours)) {
            return false;
        }
        if (Double.doubleToLongBits(this.setupHours) != Double.doubleToLongBits(other.setupHours)) {
            return false;
        }
        if (this.qty != other.qty) {
            return false;
        }
        if (this.age != other.age) {
            return false;
        }
        if (Double.doubleToLongBits(this.salesPrice) != Double.doubleToLongBits(other.salesPrice)) {
            return false;
        }
        if (!Objects.equals(this.wcDescription, other.wcDescription)) {
            return false;
        }
        if (!Objects.equals(this.partNumber, other.partNumber)) {
            return false;
        }
        if (!Objects.equals(this.workOrder, other.workOrder)) {
            return false;
        }
        if (this.turn != other.turn) {
            return false;
        }
        if (this.day != other.day) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "{partNumber=" + partNumber + ", workOrder=" + workOrder + ", runHours=" + runHours + ", setupHours=" + setupHours + ", qty=" + qty + ", age=" + age + ", salesPrice=" + salesPrice + ", turn=" + turn + ", day=" + day + '}';
    }

    
    

}
