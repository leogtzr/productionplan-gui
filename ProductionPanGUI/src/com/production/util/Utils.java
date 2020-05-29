package com.production.util;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author lgutierr
 */
public final class Utils {
    
    public static JFileChooser genericXLSFileChooser() {
        final JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Select a .XLS file");
        jfc.setAcceptAllFileFilterUsed(false);
        final FileNameExtensionFilter filter = new FileNameExtensionFilter("XLS files", "xls");
        jfc.addChoosableFileFilter(filter);
        return jfc;
    }
    
    private Utils() {}
    
}
