/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tesseract.example;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import readers.FormularInvestitie;
import utils.FileUtils;

/**
 *
 * @author robert_damian
 */
public class TesseractExample {

    
    private static final String ROMANIAN = "ron";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TesseractException {
        // System.setProperty("jna.library.path", "32".equals(System.getProperty("sun.arch.data.model")) ? "lib/win32-x86" : "lib/win32-x86-64");
        
        Tesseract instance = Tesseract.getInstance();
        instance.setDatapath("/usr/share/tesseract-ocr/");
        instance.setLanguage(ROMANIAN);
        File parentFolder = FileUtils.getInputFolder();
        File[] files = parentFolder.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pdf");
            }
        });
        
        for (File pdfFile : files) {
            FormularInvestitie formular = new FormularInvestitie(pdfFile);
            formular.exportResults(instance);
        }
    }
}

