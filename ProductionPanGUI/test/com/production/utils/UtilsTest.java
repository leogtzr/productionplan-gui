package com.production.utils;

import com.production.domain.WorkOrderInformation;
import com.production.util.Utils;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.junit.Assert;

import org.junit.Test;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public class UtilsTest {
    
    public UtilsTest() {}
    
    @Test
    public void shouldReturnNumberOfTurnsFromWorkCenter() {
        final Object[][] tests = {
            {"DOBLADO", 3},
            {"PUNZONADO", 3},
            {"MAQUINADO_MANUAL", 2},
        };
        
        for (final Object[] test : tests) {
            final int turns = Utils.numberOfTurnsFromWorkCenter(test[0].toString());
            Assert.assertEquals(turns, Integer.parseInt(test[1].toString()));
        }
    }
        
    @Test
    public void shouldReturnWorkCenterName() {
        final Object[][] tests = {
            {"Laser 1", "LASER"},
            {"PUNZONADO 2", "PUNZONADO"},
        };
        
        for (final Object[] test : tests) {
            final String got = Utils.workCenterName(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldSanitizeWorkCenterName() {
        final Object[][] tests = {
            {"Laser", true},
            {"Arroz", false},
        };
        
        for (final Object[] test : tests) {
            final Boolean got = Utils.allowedWC(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldValidateAllowedWorkCenters() {
        final Object[][] tests = {
            {"Laser 1", "LASER_1"},
            {"PUNZONADO-2", "PUNZONADO_2"},
        };
        
        for (final Object[] test : tests) {
            final String got = Utils.sanitizeWorkCenterName(test[0].toString());
            Assert.assertEquals(got, test[1]);
        }
    }
    
    @Test
    public void shouldReturnWorkCenterOccurrenceCount() {
        final List<WorkOrderInformation> items = List.of(
                new WorkOrderInformation("A", "o1"),
                new WorkOrderInformation("A", "o1"),
                new WorkOrderInformation("B", "o2"),
                new WorkOrderInformation("C", "o3"),
                new WorkOrderInformation("C", "o3"),
                new WorkOrderInformation("C", "o3")
        );
        
        final Map<String, Integer> workCenterOccurrenceCount = Utils.workCenterOccurrenceCount(items);
        
        final Object[][] tests = {
            {"A", 2},
            {"B", 1},
            {"C", 3},
        };
        
        for (final Object[] test : tests) {
            final int got = workCenterOccurrenceCount.get(test[0].toString());
            Assert.assertEquals(got, Integer.parseInt(test[1].toString()));
        }
        
    }
    
    @Test
    public void shouldReturnPartNumberFromRowInTableModel() {
        
        final Object[][] data = {
            {"ABC", "A", "B"},
            {"ABCD", "A", "B"},
        };
        
        final String[] columnNames = {
                "#Part", "Hr", "Stup",
        };
        
        final TableModel model = new DefaultTableModel(data, columnNames);
        
        final Object[][] tests = {
            {0, "ABC"},
            {1, "ABCD"},
        };
        
        for (final Object[] test : tests) {
            final var got = Utils.getPartNumberFromRow(model, Integer.parseInt(test[0].toString()));
            Assert.assertEquals(got, test[1].toString());
        }
        
    }
    
    @Test
    public void shouldUpdateStatusLabel() {
        final JLabel statusLabel = new JLabel();
        
        final Object[][] tests = {
            {new File("a"), new File("b"), "Files ready."},
            {null, new File("b"), "Please open the file containing the 'FAB Load by WC' information."},
            {null, null, "Please open the required files."},
            {new File("b"), null, "Please open the file containing the 'Age  by WC' information."},
        };
        
        for (final Object[] test : tests) {
            Utils.updateStatusBar(statusLabel, (File)test[0], (File)test[1]);
            Assert.assertEquals(test[2].toString(), statusLabel.getText());
        }
    }
    
    @Test
    public void shouldBuildPlanForTwoTurns() {
        // Prepare the test here ... 
    }
    
}
