package com.production.util.csv;

import com.production.domain.Turn;
import com.production.domain.WorkOrderInformation;
import java.util.List;
import org.junit.Test;
import org.junit.Assert;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public class CSVFormatTest {
    
    @Test
    public void shouldReturnCSVWithoutTurns() {
        
        class testCase {
            WorkOrderInformation item;
            String want;
            testCase(WorkOrderInformation item, String want) {
                this.item = item;
                this.want = want;
            }
        }
        
        final List<testCase> tests = List.of(
            new testCase(
                new WorkOrderInformation.Builder("pt1", "wo1")
                    .setupHours(1.2)
                    .qty(13)
                    .machine("mch1")
                    .build(),
                    "pt1, wo1, 0.00, 1.20, 13, mch1"
            ),
            new testCase(
                new WorkOrderInformation.Builder("pt2", "wo2")
                    .setupHours(1.2)
                    .qty(13)
                    .machine("mch2")
                    .build(),
                    "pt2, wo2, 0.00, 1.20, 13, mch2"
            )
        );
        
        tests.forEach(tc -> Assert.assertEquals(tc.want, tc.item.toCSVWithoutTurns()));
    }
    
    @Test
    public void shouldReturnCSVWithTurns() {
        
        class testCase {
            WorkOrderInformation item;
            String want;
            testCase(WorkOrderInformation item, String want) {
                this.item = item;
                this.want = want;
            }
        }
        
        final List<testCase> tests = List.of(
            new testCase(
                new WorkOrderInformation.Builder("pt1", "wo1")
                    .setupHours(1.2)
                    .qty(13)
                    .machine("mch1")
                    .turn(Turn.THIRD)
                    .build(),
                    "THIRD, pt1, wo1, 0.00, 1.20, 13, mch1"
            ),
            new testCase(
                new WorkOrderInformation.Builder("pt2", "wo2")
                    .setupHours(1.2)
                    .qty(13)
                    .machine("mch2")
                     .turn(Turn.FIRST)
                    .build(),
                    "FIRST, pt2, wo2, 0.00, 1.20, 13, mch2"
            )
        );
        
        tests.forEach(tc -> Assert.assertEquals(tc.want, tc.item.toCSVWithTurns()));
    }

}