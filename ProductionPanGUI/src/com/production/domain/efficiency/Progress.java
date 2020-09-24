package com.production.domain.efficiency;

import com.production.domain.Day;
import com.production.domain.Turn;
import java.util.Objects;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class Progress {
    
    private Turn turn;
    private double factor;
    private Day day;

    public Progress(final Turn turn, final double factor, final Day day) {
        this.turn = turn;
        this.factor = factor;
        this.day = day;
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

    public Day getDay() {
        return day;
    }

    public void setDay(final Day day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Progress{" + "turn=" + turn + ", factor=" + factor + ", day=" + day + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.turn);
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.factor) ^ (Double.doubleToLongBits(this.factor) >>> 32));
        hash = 37 * hash + Objects.hashCode(this.day);
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
        final Progress other = (Progress) obj;
        if (Double.doubleToLongBits(this.factor) != Double.doubleToLongBits(other.factor)) {
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
    
}
