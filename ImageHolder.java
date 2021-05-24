
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.Graphics2D;
/**
 * Simple class that serves to be an Actor to display the image.
 * 
 * (Revised 11/14 to avoid crashing if user cancels import operation).
 * 
 * @author Jordan Cohen
 * @version November 2013, revised
 */
public class ImageHolder extends Actor
{
    private final int maxHeight = 700;//canvas is a square to simplify rotations
    private final int maxWidth = 700;
    private BufferedImage imageToDisplay; 
    private BufferedImage imageContainer;
    private int width, height;
    
    private int xpos;
    private int ypos;
    /**
     * Construct an ImageHolder with a file name. If there is an error, 
     * show a blank GreenfootImage.
     * 
     * @param fileName  Name of image file to be displayed.
     */
    public ImageHolder (File fileName)
    {
        openFile (fileName);
    }
    /**
     * Attempt to open a file and assign it as this Actor's image
     * Crops any image bigger than maxWidthxmaxHeight
     *
     * @param fileName  Name of the image file to open (must be in this directory)
     * @return boolean  True if operation successful, otherwise false
     */
    public boolean openFile (File fileName)
    {
        try {
            imageToDisplay = FileHandler.fileToImage(fileName);
            width = Processor.clamp(imageToDisplay.getWidth(), 0 , maxHeight);
            height = Processor.clamp(imageToDisplay.getHeight(), 0 , maxWidth);
            imageContainer = new BufferedImage(maxHeight,maxWidth, 2);
            Graphics2D temp = imageContainer.createGraphics();
            
            xpos = imageContainer.getWidth()/2-width/2;
            ypos = imageContainer.getHeight()/2-height/2;
            //position of the image is adjusted to appear centered
            
            temp.drawImage(imageToDisplay,null, xpos, ypos);
            temp.dispose();
            setImage(Processor.bufferToGreenfoot(imageContainer));
        }
        catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }
    public int getXPos(){
        return xpos;
    }
    public int getYPos(){
        return ypos;
    }
    public void updatePos(){
        xpos = imageContainer.getWidth()/2-getWidth()/2;
        ypos = imageContainer.getHeight()/2-getHeight()/2;
    }
    /**
     * Draws an image on top with a certain transparency
     * 
     * @param bi the image to overlay with
     * @param alpha a value from 0-255, the transparency of the overlayed image
     */
    public void overlay(BufferedImage bi, int alpha){
        Processor.changeAlpha(bi, alpha);
        Graphics2D gfx = getBufferedImage().createGraphics();
        gfx.drawImage(bi, null, getImage().getWidth()/2-bi.getWidth()/2,getImage().getHeight()/2-bi.getHeight()/2);
        setWidth(bi.getWidth());
        setHeight(bi.getHeight());
    }
    public void swapDimensions(){
        int temp = width;
        width = height;
        height = temp;
    }
    /**
     * changes the internal width of the holder, if the width is greater than the current width
     */
    public void setWidth(int newWidth){
        if(width < newWidth){
            width = Processor.clamp(newWidth, 0 , 700);
            updatePos();
        }
    }
    /**
     * changes the internal height of the holder, if the width is greater than the current width
     */
    public void setHeight(int newHeight){
        if(height < newHeight){
            height = Processor.clamp(newHeight, 0 , 700);
            updatePos();
        }
    }
    
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    /**
     * Allows access to my awtImage - the backing data underneath the GreenfootImage class.
     * 
     * @return BufferedImage returns the backing image for this Actor as an AwtImage
     */
    public BufferedImage getBufferedImage ()
    {
        return this.getImage().getAwtImage();
    }

}
