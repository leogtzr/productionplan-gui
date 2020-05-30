package com.production.domain;

import java.util.Objects;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class WorkOrderInformation {

    private String partNumber;
    private String workOrder;
    private double runHours;
    private double setupHours;
    private int qty;

    private int age;
    public double salesPrice;

    // Not feeling really good having this constructor with all these parameters ... a builder pattern would be a good
    // fit here ... - Leo
    public WorkOrderInformation(
            final String partNumber
            , final String workOrder
            , final double runHours
            , final double setupHours
            , final int qty
            , final int age
            , final double salesPrice
    ) {
        this.partNumber = partNumber;
        this.workOrder = workOrder;
        this.runHours = runHours;
        this.setupHours = setupHours;
        this.qty = qty;
        this.age = age;
        this.salesPrice = salesPrice;
    }

    public WorkOrderInformation() { }

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

    @Override
    public String toString() {
        return "WorkOrderInformation{" +
                "partNumber='" + partNumber + '\'' +
                ", workOrder='" + workOrder + '\'' +
                ", runHours=" + runHours +
                ", setupHours=" + setupHours +
                ", qty=" + qty +
                ", age=" + age +
                ", salesPrice=" + salesPrice +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WorkOrderInformation that = (WorkOrderInformation) o;
        return Double.compare(that.runHours, runHours) == 0 &&
                Double.compare(that.setupHours, setupHours) == 0 &&
                qty == that.qty &&
                age == that.age &&
                Double.compare(that.salesPrice, salesPrice) == 0 &&
                Objects.equals(partNumber, that.partNumber) &&
                Objects.equals(workOrder, that.workOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partNumber, workOrder, runHours, setupHours, qty, age, salesPrice);
    }
}
