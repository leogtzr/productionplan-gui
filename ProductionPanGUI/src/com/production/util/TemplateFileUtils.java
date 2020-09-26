package com.production.util;

import com.production.lang.MissingTests;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

/**
 * @author lgutierr (leogutierrezramirez@gmail.com)
 */
public final class TemplateFileUtils {
    
    @MissingTests
    public static Path getTemplateFilePath(final String templateFileName) throws IOException {
        final String jarFilepath = TemplateFileUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        final File jf = new File(jarFilepath);
        final String jarDirectoryPath = URLDecoder.decode(jf.getParent(), "UTF-8");
        final Path templateHTMLPath = Paths.get(jarDirectoryPath, "templates", templateFileName);
        return templateHTMLPath;
    }
    
    @MissingTests
    public static void copyFileFromTemplatesDirectoryTo(final File dest, final File staticFile) throws IOException {
        FileUtils.copyFileToDirectory(staticFile, dest, true);
    }
    
    private TemplateFileUtils() {}
    
}
