package com.production.util;

import com.production.domain.Day;
import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import com.production.domain.efficiency.Progress;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class OrderUtilsTest {

    @Test
    public void shouldCopyOrderWithNewRunHoursAndSetupHours() {
        
        class testCase {
            WorkOrderInformation order;
            double newRunHours;
            double newSetupHours;
            Progress progress;
            WorkOrderInformation want;

            testCase(WorkOrderInformation order, double newRunHours, double newSetupHours, Progress progress, WorkOrderInformation want) {
                this.order = order;
                this.newRunHours = newRunHours;
                this.newSetupHours = newSetupHours;
                this.progress = progress;
                this.want = want;
            }
        }
        
        final List<testCase> tests = List.of(
            new testCase(
                new WorkOrderInformation.Builder("pt1", "wo1").workCenter("a").qty(1).age(3)
                .salesPrice(2.5D).machine("m1").build(),
                2.5D, 3.5D, new Progress(Turn.THIRD, -1D, Day.WEDNESDAY),
                new WorkOrderInformation.Builder("pt1", "wo1").workCenter("a").runHours(2.5D).setupHours(3.5D).qty(1).age(3)
                .salesPrice(2.5D).machine("m1").turn(Turn.THIRD).day(Day.WEDNESDAY).build()
            ),
            new testCase(
                new WorkOrderInformation.Builder("xxx", "yyy").workCenter("wc1").qty(1).age(23)
                .salesPrice(2.5D).machine("m1").build(),
                2.5D, 3.5D, new Progress(Turn.THIRD, -1D, Day.WEDNESDAY),
                new WorkOrderInformation.Builder("xxx", "yyy").workCenter("wc1").runHours(2.5D).setupHours(3.5D).qty(1).age(23)
                .salesPrice(2.5D).machine("m1").turn(Turn.THIRD).day(Day.WEDNESDAY).build()
            )
        );
        
        for (final testCase tc : tests) {
            final WorkOrderInformation order = tc.order;
            final WorkOrderInformation got = OrderUtils.from(order, tc.newRunHours, tc.newSetupHours, tc.progress);
            Assert.assertEquals(tc.want, got);
        }
        
    }
    
}
