/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package readers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import utils.FileUtils;
import utils.PDPageUtils;

/**
 *
 * @author robert_damian
 */
public class FormularInvestitie {
    private File formularFile;
    private String folderName;
    public FormularInvestitie(File formularFile) {
        this.formularFile = formularFile;
        folderName = formularFile.getName().substring(0, formularFile.getName().lastIndexOf("."));
    }
    
    public void exportResults(Tesseract tesseractInstance) throws IOException, TesseractException {
        PDDocument document = PDDocument.load(formularFile);
        List<PDPage> documentPages = document.getDocumentCatalog().getAllPages();
        int pageCount = 0;
        for (PDPage page : documentPages) {
            exportResultFromPage(tesseractInstance, page, pageCount);
            pageCount++;
        }
        document.close();
    }
    
    private void exportResultFromPage(Tesseract tesseractInstance, PDPage page, int index) throws IOException, TesseractException {
            File pageImageFile = FileUtils.getImageFile(index, folderName);
            File pageTextFile = FileUtils.getTextFile(index, folderName);

            BufferedImage image = PDPageUtils.getImageFromPage(page);
            if (image == null) {
                return ;
            }
            ImageIO.write(image, "png", pageImageFile);
            String result = tesseractInstance.doOCR(image);

            OutputStream os = new FileOutputStream(pageTextFile);
            os.write(result.getBytes());
            os.flush();
            os.close();        
    }
}
