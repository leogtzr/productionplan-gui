package com.production.util;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;

/**
 * @author lgutierr
 */
public final class Utils {
    
    /**
     * "factory" method to build a FileChooser for .XLS files.
     * @return a JFileChooser to select .XLS files
     */
    public static JFileChooser genericXLSFileChooser() {
        final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a .XLS file");
        jfc.setAcceptAllFileFilterUsed(false);
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls");
        jfc.addChoosableFileFilter(filter);
        return jfc;
    }
    
    public static String[] dataFromModelAt(final TableModel model, final int rowIndex) {
        
        final int columnCount = model.getColumnCount();
        final String[] data = new String[columnCount];
        
        for (int i = 0; i < columnCount; i++) {
            data[i] = model.getValueAt(rowIndex, i).toString();
        }
        
        return data;
    }
    
    private Utils() {}
    
}
