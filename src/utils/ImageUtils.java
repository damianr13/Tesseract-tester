/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author robert_damian
 */
public class ImageUtils {
            
    private static final int WEIRD_THING_LOOKS_CONSTANT = 344;

    public static BufferedImage rotateImage(BufferedImage image) {
        int width = image.getHeight();
        int height = image.getWidth();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D graphics = result.createGraphics();
        AffineTransform at = AffineTransform.getRotateInstance(-Math.PI/2, width/2, height/2);
        graphics.transform(at);
        graphics.drawImage(image, -WEIRD_THING_LOOKS_CONSTANT, WEIRD_THING_LOOKS_CONSTANT, null);
        graphics.dispose();
        return result;
    }
}
