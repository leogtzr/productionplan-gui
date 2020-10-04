package com.production.domain;

import com.production.lang.MissingTests;
import com.production.lang.Validated;
import java.util.Objects;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public final class WorkOrderInformation implements Cloneable {
    
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
    private String machine = "";
    
    @Override
    public WorkOrderInformation clone() throws CloneNotSupportedException {
        return (WorkOrderInformation)super.clone();
    }
    
    public WorkOrderInformation(final WorkOrderInformation other) {
        this.wcDescription = other.wcDescription;
        this.partNumber = other.partNumber;
        this.workOrder = other.workOrder;
        this.runHours = other.runHours;
        this.setupHours = other.setupHours;
        this.qty = other.qty;
        this.age = other.age;
        this.salesPrice = other.salesPrice;
        this.turn = other.turn;
        this.day = other.day;
        this.machine = other.machine;
    }

    public WorkOrderInformation() { }
    
    public WorkOrderInformation(final String partNumber, final String workOrder) {
        this.partNumber = partNumber;
        this.workOrder = workOrder;
    }
    
    private WorkOrderInformation(final Builder builder) {
        this.wcDescription = builder.wcDescription;
        this.partNumber = builder.partNumber;
        this.workOrder = builder.workOrder;
        this.runHours = builder.runHours;
        this.setupHours = builder.setupHours;
        this.qty = builder.qty;
        this.age = builder.age;
        this.salesPrice = builder.salesPrice;
        this.turn = builder.turn;
        this.day = builder.day;
        this.machine = builder.machine;
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

    public String getMachine() {
        return machine;
    }

    public void setMachine(final String machine) {
        this.machine = machine;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.wcDescription);
        hash = 97 * hash + Objects.hashCode(this.partNumber);
        hash = 97 * hash + Objects.hashCode(this.workOrder);
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.runHours) ^ (Double.doubleToLongBits(this.runHours) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.setupHours) ^ (Double.doubleToLongBits(this.setupHours) >>> 32));
        hash = 97 * hash + this.qty;
        hash = 97 * hash + this.age;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.salesPrice) ^ (Double.doubleToLongBits(this.salesPrice) >>> 32));
        hash = 97 * hash + Objects.hashCode(this.turn);
        hash = 97 * hash + Objects.hashCode(this.day);
        hash = 97 * hash + Objects.hashCode(this.machine);
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
        if (!Objects.equals(this.machine, other.machine)) {
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
        return "WOInfo{" + "wcDescription=" + wcDescription + ", partNumber=" + partNumber + 
                ", workOrder=" + workOrder + ", runHours=" + runHours + ", setupHours=" + setupHours + ", qty=" + 
                qty + ", age=" + age + ", salesPrice=" + salesPrice + ", turn=" + turn + ", day=" + day + 
                ", machine=" + machine + '}';
    }
    
    public String toShortString() {
        return String.format("WO[pt: %s, order: %s, runHours: %.2f, setupHours: %.2f, turn: %s, day: %s]"
                , this.partNumber, this.workOrder, this.runHours, this.setupHours, this.turn, this.day
                );
    }
    
    public static class Builder {
        
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
        private String machine = "";
        
        public Builder(final String partNumber, final String workOrder) {
            this.partNumber = partNumber;
            this.workOrder = workOrder;
        }
        
        public Builder workCenter(final String wcDescription) {
            this.wcDescription = wcDescription;
            return this;
        }
        
        public Builder partNumber(final String partNumber) {
            this.partNumber = partNumber;
            return this;
        }
        
        public Builder workOrder(final String workOrder) {
            this.workOrder = workOrder;
            return this;
        }
        
        public Builder runHours(final double runHours) {
            this.runHours = runHours;
            return this;
        }
        
        public Builder setupHours(final double setupHours) {
            this.setupHours = setupHours;
            return this;
        }
        
        public Builder qty(final int qty) {
            this.qty = qty;
            return this;
        }
        
        public Builder age(final int age) {
            this.age = age;
            return this;
        }
        
        public Builder salesPrice(final double salesPrice) {
            this.salesPrice = salesPrice;
            return this;
        }
        
        public Builder turn(final Turn turn) {
            this.turn = turn;
            return this;
        }
        
        public Builder day(final Day day) {
            this.day = day;
            return this;
        }
        
        public Builder machine(final String machine) {
            this.machine = machine;
            return this;
        }
        
        public WorkOrderInformation build() {
            return new WorkOrderInformation(this);
        }
    }
    
    @Validated
    public String toCSVWithoutTurns() {
        return String.format("%s, %s, %.2f, %.2f, %d, %s", partNumber, workOrder, runHours, setupHours, qty, machine);
    }
    
    @MissingTests
    public String toCSVWithTurns() {
        return String.format("%s, %s, %s, %.2f, %.2f, %d, %s", turn, partNumber, workOrder, runHours, setupHours, qty, machine);
    }

}