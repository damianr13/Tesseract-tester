/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import linedetection.pdf.LineCountingPageDrawer;
import linedetection.pdf.LinePdfDetectParameters;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import tabledetection.geometry.BasicLineSegment;
import tabledetection.geometry.ClippingRectangle2D_F32;
import tabledetection.utils.LineUtils;

/**
 *
 * @author robert_damian
 */
public class PDPageUtils {
    private static final int DEFAULT_USER_SPACE_UNIT_DPI = 72;
    private static final Color TRANSPARENT_WHITE = new Color( 255, 255, 255, 0 );
    private static final float MINIMUM_LINE_SIZE = 10;
    
    
    public static BufferedImage getImageFromPage(PDPage page) throws IOException {
        int resolution = 300;
        linedetection.pdf.LinePdfDetectParameters algorithmParameters = new LinePdfDetectParameters();
        PDRectangle cropBox = page.findCropBox();
        float widthPt = cropBox.getWidth();
        float heightPt = cropBox.getHeight();
        float scaling = resolution / (float)DEFAULT_USER_SPACE_UNIT_DPI;
        int widthPx = Math.round(widthPt * scaling);
        int heightPx = Math.round(heightPt * scaling);
        //The following reduces accuracy. It should really be a Dimension2D.Float.
        Dimension pageDimension = new Dimension( (int)widthPt, (int)heightPt );
        BufferedImage retval = null;
        float rotation = (float)Math.toRadians(page.findRotation());
        if (rotation != 0)
        {
            retval = new BufferedImage( heightPx, widthPx, BufferedImage.TYPE_BYTE_BINARY );
        }
        else
        {
        retval = new BufferedImage( widthPx, heightPx, BufferedImage.TYPE_BYTE_BINARY );
        }
        Graphics2D graphics = retval.createGraphics();
        graphics.setBackground( TRANSPARENT_WHITE );
        graphics.clearRect( 0, 0, retval.getWidth(), retval.getHeight() );
        if (rotation != 0)
        {
            graphics.translate(retval.getWidth(), 0f);
            graphics.rotate(-rotation);
            
            AffineTransform at = AffineTransform.getRotateInstance(
                    Math.PI, widthPx/2, heightPx/2.0);
            graphics.transform(at);
        }
        graphics.scale( scaling, scaling );
        
        //Identify the lines and paint the image
        LineCountingPageDrawer drawer = new LineCountingPageDrawer(algorithmParameters);
        drawer.drawPage( graphics, page, pageDimension );
        graphics.dispose();
        List<BasicLineSegment> lines = drawer.getFoundLines();
        
        //Filter the obtained lines by size
        List<BasicLineSegment> lengthyLines = LineUtils.filterLinesByLength(lines, MINIMUM_LINE_SIZE);
        
        //Filter by region
        ClippingRectangle2D_F32 clippingRectangle2D = new ClippingRectangle2D_F32(0, 0, widthPx, heightPx);
        List<BasicLineSegment> validLines = LineUtils.filterLinesByRegion(lengthyLines, clippingRectangle2D);
        return retval;
    }
}
