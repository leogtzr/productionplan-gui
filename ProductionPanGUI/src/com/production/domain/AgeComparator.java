package com.production.domain;

import java.util.Comparator;

/**
 *
 * @author lgutierr
 */
public class AgeComparator implements Comparator<WorkOrderInformation> {

    @Override
    public int compare(final WorkOrderInformation o1, final WorkOrderInformation o2) {
        /*
            
            final int x = o1.getAge();
            final int y = o2.getAge();
            return (x < y) ? -1 : ((x == y) ? 0 : 1);
        */
        return Integer.compare(o2.getAge(), o1.getAge());
    }
    
}
