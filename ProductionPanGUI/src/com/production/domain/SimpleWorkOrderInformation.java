package com.production.domain;

import java.util.Objects;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public final class SimpleWorkOrderInformation {

    private final String workOrder;
    private int age = -1;
    private double salesPrice;

    public SimpleWorkOrderInformation(final String workOrder) {
        this.workOrder = workOrder;
    }

    public String getWorkOrder() {
        return workOrder;
    }

    public int getAge() {
        return age;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(final double salesPrice) {
        this.salesPrice = salesPrice;
    }

    @Override
    public String toString() {
        return "SimpleWorkOrderInformation{" +
                "workOrder='" + workOrder + '\'' +
                ", age=" + age +
                ", salesPrice='" + salesPrice + '\'' +
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
        final SimpleWorkOrderInformation that = (SimpleWorkOrderInformation) o;
        return age == that.age &&
                Objects.equals(workOrder, that.workOrder) &&
                Objects.equals(salesPrice, that.salesPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workOrder, age, salesPrice);
    }
}
