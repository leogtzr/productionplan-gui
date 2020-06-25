package com.production.domain;

import java.util.Comparator;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class AgeComparator implements Comparator<WorkOrderInformation> {

    @Override
    public int compare(final WorkOrderInformation o1, final WorkOrderInformation o2) {
        return Integer.compare(o2.getAge(), o1.getAge());
    }
    
}
