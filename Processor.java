/**
 * Starter code for Processor - the class that processes images.
 * <p>
 * This class manipulated Java BufferedImages, which are effectively 2d arrays
 * of pixels. Each pixel is a single integer packed with 4 values inside it.
 * <p>
 * I have included two useful methods for dealing with bit-shift operators so
 * you don't have to. These methods are unpackPixel() and packagePixel() and do
 * exactly what they say - extract red, green, blue and alpha values out of an
 * int, and put the same four integers back into a special packed integer. 
 * 
 * @author Jordan Cohen 
 * @version November 2013
 */
import greenfoot.*;
import java.awt.image.*;
import java.awt.Graphics2D;
import java.util.*;

public class Processor  
{
    
    
    public static void overwriteImage(BufferedImage toOverwrite, BufferedImage overwriteWith){
        for (int x = 0; x < overwriteWith.getWidth(); x++)
        {
            for (int y = 0; y < overwriteWith.getHeight(); y++)
            {
                try{
                    toOverwrite.setRGB(x, y, overwriteWith.getRGB(x, y));
                }
                catch(ArrayIndexOutOfBoundsException e){}
            }
        }
    }
    public static <T extends Comparable<T>> T max(T a, T b){
        return a.compareTo(b) > 0 ? a:b;
    }
    public static <T extends Comparable<T>> T clamp(T value, T min, T max){
        if(value.compareTo(min) < 0)return min;
        else if(value.compareTo(max) > 0)return max;
        return value;
    }
    /**
     * Blurs and image using a kernal
     */
    public static void blur(BufferedImage bi){
        int size = 9;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];
    
        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }

        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        BufferedImage i = op.filter(bi, null);
        overwriteImage(bi,i);
    }
    /**
     * Converts a BufferedImage to a GreenfootImage
     */
    public static GreenfootImage bufferToGreenfoot(BufferedImage bi){
        GreenfootImage gImage = new GreenfootImage(bi.getWidth(), bi.getHeight());
        BufferedImage gBufImg = gImage.getAwtImage();
        Graphics2D graphics = (Graphics2D)gBufImg.getGraphics();
        graphics.drawImage(bi, null, 0, 0);
        
        return gImage;
    }
    /**
     * Draws an image with a flat color over the base image with concern of position in the base image
     */
    public static void colorFilter(ImageHolder image, int color){
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage bi = image.getBufferedImage();
        BufferedImage filter = new BufferedImage(width, height, bi.getType());
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                filter.setRGB(i, j, color);
            }
        }
        Graphics2D gfx = bi.createGraphics();
        gfx.drawImage(filter, null, image.getXPos(),image.getYPos());
        
    }
    /**
     * Draws an image with a flat color over the base image without concern of position in the base image
     */
    public static BufferedImage colorFilter(BufferedImage image, int color){
        int width = image.getWidth();
        int height = image.getHeight();
        
        BufferedImage filter = new BufferedImage(width, height, image.getType());
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                filter.setRGB(i, j, color);
            }
        }
        Graphics2D gfx = image.createGraphics();
        gfx.drawImage(filter, null, 0,0);
        return copyImage(image);
    }
    /**
     * Uses a Kernal and ConvolveOp to brighten the image
     * @param bi The source image to change
     */
    public static void highpass(BufferedImage bi){
        int size = 3;
        
        float[] data = {
            0,-1/4,0,
            -1/4,2,-1/4,
            0,-1/4,0
        };
        Kernel kernel = new Kernel(size, size, data);
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_ZERO_FILL, null);
        BufferedImage i = op.filter(bi, null);
        overwriteImage(bi,i);
    }
    /**
     * Average the colors in square of pixels for the entire image
     * @param bi The source image to change
     */
    public static void pixilate(BufferedImage bi, int psize){
        
        int w = bi.getWidth();
        int h = bi.getHeight();
        for(int k = 0; k < w/psize; k++){//loop through the squares of the image
            for(int l = 0; l < h/psize; l++){
                
                
                int r = 0;
                int g = 0;
                int b = 0;
                int a = 0;
                for (int x = 0; x < psize; x++)
                {
                    for (int y = 0; y < psize; y++)
                    {
                        
                        try{
                            int[] pc = unpackPixel(bi.getRGB(x + (psize*k),y + (psize*l)));
                            a+=pc[0];//sum up all the rgb values to get the average
                            r+= pc[1];
                            g+=pc[2];
                            b+=pc[3];
                        }catch(ArrayIndexOutOfBoundsException e){
                            //Catches edge cases
                        }
                    }
                }
                a/=psize*psize;
                r/=psize*psize;
                g/=psize*psize;
                b/=psize*psize;
                int newRGB = packagePixel(r,g,b,a);
                for (int x = 0; x < psize; x++)
                {
                    for (int y = 0; y < psize; y++)
                    {
                        try{
                            bi.setRGB(x + (psize*k),y + (psize*l), newRGB);
                        }
                        catch(ArrayIndexOutOfBoundsException e){
                            //Catches edge cases
                        }
                    }
                }
            }
        }
    }
    /**
     * Rotates the bufferedimage counter-clockwise by swapping (x,y) to (y,-x)
     * @param bi The source image to change
     */
    public static void rotateCCW(BufferedImage bi){
        int w = bi.getWidth();
        int h = bi.getHeight();
        BufferedImage newBi = new BufferedImage(h, w, bi.getType());
        BufferedImage blank = new BufferedImage(w, h, bi.getType());
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                int rgb = bi.getRGB(x,y);
                newBi.setRGB(y,-x+w-1, rgb);

            }
        }
        overwriteImage(bi, blank);
        overwriteImage(bi, newBi);
    }
    /**
     * Rotates the bufferedimage clockwise by swapping (x,y) to (-y,x) 
     * @param bi The source image to change
     */
    public static void rotateCW(BufferedImage bi){
        int w = bi.getWidth();
        int h = bi.getHeight();
        BufferedImage newBi = new BufferedImage(h, w, bi.getType());
        BufferedImage blank = new BufferedImage(w, h, bi.getType());
        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < h; y++)
            {
                int rgb = bi.getRGB(x,y);
                newBi.setRGB(-y+w-1,x, rgb);// shift the negative coordinate to be positive

            }
        }
        overwriteImage(bi, blank);
        overwriteImage(bi, newBi);
    }
    /**
     * Example colour altering method by Mr. Cohen. This method will
     * increase the blue value while reducing the red and green values.
     * 
     * Demonstrates use of packagePixel() and unpackPixel() methods.
     * 
     * @param bi    The BufferedImage (passed by reference) to change.
     */
    public static void blueify (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic BLUE-er
                if (blue < 254)
                    blue += 2;
                if (red >= 20)
                    red--;
                if (green >= 20)
                    green--;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }
    public static void redify (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                
                int alpha = rgbValues[0];
                int red = rgbValues[1];
                int green = rgbValues[2];
                int blue = rgbValues[3];

                // make the pic BLUE-er
                if (red < 254)
                    red += 2;
                if (blue >= 20)
                    blue--;
                if (green >= 20)
                    green--;

                int newColour = packagePixel (red, green, blue, alpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }
    /**
     * Averages the rgb values of every pixel
     * @param bi The source image to change
     */
    public static void greyscale (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                
                int average = (rgbValues[0] + rgbValues[2] + rgbValues[3])/3;

                int newColour = packagePixel (average, average, average, rgbValues[0]);
                bi.setRGB (x, y, newColour);
            }
        }

    }
    /**
     * Loops through every pixel and changes its transparency
     * 
     * @param bi The source image to change
     * @param newAlpha an integer value from 0-255 
     */
    public static void changeAlpha (BufferedImage bi, int newAlpha)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                int newColour = packagePixel (rgbValues[1], rgbValues[2], rgbValues[3], newAlpha);
                bi.setRGB (x, y, newColour);
            }
        }

    }
    /**
     * inverts the color of pixels, high values in rgb become low, low becomes high
     * @param bi The source image to change
     */
    public static void invert (BufferedImage bi)
    {
        // Get image size to use in for loops
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Using array size as limit
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                // Call the unpackPixel method to retrieve the four integers for
                // R, G, B and alpha and assign them each to their own integer
                int[] rgbValues = unpackPixel (rgb);
                
                int average = (rgbValues[0] + rgbValues[2] + rgbValues[3])/3;

                int newColour = packagePixel (255-rgbValues[0], 255-rgbValues[1], 255-rgbValues[2], rgbValues[0]);
                bi.setRGB (x, y, newColour);
            }
        }

    }
    /**
     * flips the image on the horizontal axis
     * 
     * @param bi The source image to change
     */
    public static void flipHorizontal (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                
                newBi.setRGB((xSize-1)-x, y, rgb);
            }
        }
        overwriteImage(bi, newBi);
    }
    /**
     * flips the image on the vertical axis
     * 
     * @param bi The source image to change
     */
    public static void flipVertical (BufferedImage bi)
    {
        int xSize = bi.getWidth();
        int ySize = bi.getHeight();

        // Temp image, to store pixels as we reverse everything
        BufferedImage newBi = new BufferedImage (xSize, ySize, 3);
        for (int x = 0; x < xSize; x++)
        {
            for (int y = 0; y < ySize; y++)
            {
                // Calls method in BufferedImage that returns R G B and alpha values
                // encoded together in an integer
                int rgb = bi.getRGB(x, y);

                
                newBi.setRGB(x, (ySize-1)-y, rgb);
            }
        }
        overwriteImage(bi, newBi);

    }
    /**
     * Returns an image that points to different but identical image of the source
     * @param source The source image to copy
     */
    public static BufferedImage copyImage(BufferedImage source){
        ColorModel cm = source.getColorModel();
        boolean isAlphaPremultip = cm.isAlphaPremultiplied();
        WritableRaster raster = source.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultip, null);
    }
    /**
     * Converts the image into one without an alpha layer
     */
    public static BufferedImage removeAlpha(BufferedImage source){
        BufferedImage newBi = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_RGB);
        overwriteImage(newBi, source);
        
        return newBi;
    }
    /**
     * Takes in an rgb value - the kind that is returned from BufferedImage's
     * getRGB() method - and returns 4 integers for easy manipulation.
     * 
     * By Jordan Cohen
     * Version 0.2
     * 
     * @param rgbaValue The value of a single pixel as an integer, representing<br>
     *                  8 bits for red, green and blue and 8 bits for alpha:<br>
     *                  <pre>alpha   red     green   blue</pre>
     *                  <pre>00000000000000000000000000000000</pre>
     * @return int[4]   Array containing 4 shorter ints<br>
     *                  <pre>0       1       2       3</pre>
     *                  <pre>alpha   red     green   blue</pre>
     */
    public static int[] unpackPixel (int rgbaValue)
    {
        int[] unpackedValues = new int[4];
        // alpha
        unpackedValues[0] = (rgbaValue >> 24) & 0xFF;
        // red
        unpackedValues[1] = (rgbaValue >> 16) & 0xFF;
        // green
        unpackedValues[2] = (rgbaValue >>  8) & 0xFF;
        // blue
        unpackedValues[3] = (rgbaValue) & 0xFF;

        return unpackedValues;
    }

    /**
     * Takes in a red, green, blue and alpha integer and uses bit-shifting
     * to package all of the data into a single integer.
     * 
     * @param   int red value (0-255)
     * @param   int green value (0-255)
     * @param   int blue value (0-255)
     * @param   int alpha value (0-255)
     * 
     * @return int  Integer representing 32 bit integer pixel ready
     *              for BufferedImage
     */
    public static int packagePixel (int r, int g, int b, int a)
    {
        int newRGB = (a << 24) | (r << 16) | (g << 8) | b;
        return newRGB;
    }
}

