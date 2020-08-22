package com.production.domain.efficiency;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import java.util.List;
import java.util.Objects;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class EfficiencyInformation {
    
    private List<WorkOrderInformation> orders;
    private Turn outputTurn;
    private double initHours;

    public EfficiencyInformation(
            final List<WorkOrderInformation> orders, 
            final Turn outputTurn, final double initHours) {
        this.orders = orders;
        this.outputTurn = outputTurn;
        this.initHours = initHours;
    }

    public EfficiencyInformation() {}
    
    public List<WorkOrderInformation> getOrders() {
        return orders;
    }

    public void setOrders(final List<WorkOrderInformation> orders) {
        this.orders = orders;
    }

    public Turn getOutputTurn() {
        return outputTurn;
    }

    public void setOutputTurn(final Turn outputTurn) {
        this.outputTurn = outputTurn;
    }

    public double getInitHours() {
        return initHours;
    }

    public void setInitHours(final double initHours) {
        this.initHours = initHours;
    }

    @Override
    public String toString() {
        return "EfficiencyInformation{" + "orders=" + orders + ", outputTurn=" + outputTurn + ", initHours=" + initHours + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.orders);
        hash = 89 * hash + Objects.hashCode(this.outputTurn);
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.initHours) ^ (Double.doubleToLongBits(this.initHours) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EfficiencyInformation other = (EfficiencyInformation) obj;
        if (Double.doubleToLongBits(this.initHours) != Double.doubleToLongBits(other.initHours)) {
            return false;
        }
        if (!Objects.equals(this.orders, other.orders)) {
            return false;
        }
        if (this.outputTurn != other.outputTurn) {
            return false;
        }
        return true;
    }
    
    
    
}
