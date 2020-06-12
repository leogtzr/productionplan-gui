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

    public WorkOrderInformation() { }
    
    public WorkOrderInformation(final String partNumber, final String workOrder) {
        this.partNumber = partNumber;
        this.workOrder = workOrder;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public void setWorkOrder(String workOrder) {
        this.workOrder = workOrder;
    }

    public double getRunHours() {
        return runHours;
    }

    public void setRunHours(double runHours) {
        this.runHours = runHours;
    }

    public double getSetupHours() {
        return setupHours;
    }

    public void setSetupHours(double setupHours) {
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

    public void setWcDescription(String wcDescription) {
        this.wcDescription = wcDescription;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(final Turn turn) {
        this.turn = turn;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.wcDescription);
        hash = 17 * hash + Objects.hashCode(this.partNumber);
        hash = 17 * hash + Objects.hashCode(this.workOrder);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.runHours) ^ (Double.doubleToLongBits(this.runHours) >>> 32));
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.setupHours) ^ (Double.doubleToLongBits(this.setupHours) >>> 32));
        hash = 17 * hash + this.qty;
        hash = 17 * hash + this.age;
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.salesPrice) ^ (Double.doubleToLongBits(this.salesPrice) >>> 32));
        hash = 17 * hash + Objects.hashCode(this.turn);
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
        return true;
    }

    @Override
    public String toString() {
        return "WorkOrderInformation{" + "wcDescription=" + wcDescription + ", partNumber=" + partNumber + 
                ", workOrder=" + workOrder + ", runHours=" + runHours + ", setupHours=" + setupHours + 
                ", qty=" + qty + ", age=" + age + ", salesPrice=" + salesPrice + ", turn=" + turn + '}';
    }
    
}
