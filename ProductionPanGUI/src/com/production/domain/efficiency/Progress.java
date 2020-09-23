package com.production.domain.efficiency;

import com.production.domain.Turn;
import java.util.Objects;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class Progress {
    
    private Turn turn;
    private double factor;

    public Progress(final Turn turn, final double factor) {
        this.turn = turn;
        this.factor = factor;
    }

    public Progress() {}

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(final Turn turn) {
        this.turn = turn;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(final double factor) {
        this.factor = factor;
    }

    @Override
    public String toString() {
        return "Progress{" + "turn=" + turn + ", factor=" + factor + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.turn);
        hash = 83 * hash + (int) (Double.doubleToLongBits(this.factor) ^ (Double.doubleToLongBits(this.factor) >>> 32));
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
        final Progress other = (Progress) obj;
        if (Double.doubleToLongBits(this.factor) != Double.doubleToLongBits(other.factor)) {
            return false;
        }
        if (this.turn != other.turn) {
            return false;
        }
        return true;
    }
    
    
    
}
