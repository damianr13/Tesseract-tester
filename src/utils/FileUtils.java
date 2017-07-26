/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author robert_damian
 */
public class FileUtils {
    private static final String PARENT_FOLDER = "/home/robert_damian/Workspace/devizExpres/output_tesseract2/";
    private static final String INPUT_FOLDER = "/home/robert_damian/Workspace/devizExpres/investitii_ocr";
    
    private static String fromatPathString(String subParent) {
        if (!subParent.equals("") && !subParent.endsWith("//")) {
            return subParent.concat("//");
        }
        
        return subParent;
    }
    
    private static File getSpecificFile(int index, String subParent, String identification) throws IOException {
        subParent = fromatPathString(subParent);
        File folder = new File(PARENT_FOLDER + subParent);
        folder.mkdir();
        File result = new File(folder,  identification + index);
        return createIfNotExists(result);
    }
    
    public static File getImageFile(int index) throws IOException {
        return getImageFile(index, "");
    }
    
    public static File getTextFile(int index) throws IOException {
        return getTextFile(index, "");
    }
    
    public static File getImageFile(int index, String subParent) throws IOException {
        return getSpecificFile(index, subParent, "pageImage");
    }
    
    public static File getTextFile(int index, String subParent) throws IOException {
        return getSpecificFile(index, subParent, "pageText");
    }
    
    private static File createIfNotExists(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        
        return file;
    }

    public static File getInputFolder() {
        return new File(INPUT_FOLDER);
    }
}
