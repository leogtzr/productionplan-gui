package com.production.domain;

import java.util.Objects;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class Priority {
    
    private String partNumber;
    private int order;

    public Priority(final String partNumber, final int order) {
        this.partNumber = partNumber;
        this.order = order;
    }
    
    public Priority() {}

    public int getOrder() {
        return order;
    }

    public void setOrder(final int order) {
        this.order = order;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(final String partNumber) {
        this.partNumber = partNumber;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.order;
        hash = 97 * hash + Objects.hashCode(this.partNumber);
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
        final Priority other = (Priority) obj;
        if (this.order != other.order) {
            return false;
        }
        if (!Objects.equals(this.partNumber, other.partNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Priority{" + "order=" + order + ", partNumber=" + partNumber + '}';
    }
    
}
