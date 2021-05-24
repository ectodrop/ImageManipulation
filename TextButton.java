import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.image.BufferedImage;
import java.util.*;
/**
 * A Generic Button to display text that is clickable. Owned by a World, which controls click
 * capturing.
 * 
 * @author Jordan Cohen
 * @version v0.1.5
 */
public class TextButton extends Actor
{
    // Declare private variables
    private GreenfootImage myImage;
    private BufferedImage original;
    private String buttonText;
    private int textSize;
    private boolean highlighted = false;
    /**
     * Construct a TextButton with a given String at the default size
     * 
     * @param text  String value to display
     * 
     */
    public TextButton (String text)
    {
        this(text, 15);
    }

    /**
     * Construct a TextButton with a given String and a specified size
     * 
     * @param text  String value to display
     * @param textSize  size of text, as an integer
     * 
     */
    public TextButton (String text, int textSize)
    {
        buttonText = text;
        GreenfootImage tempTextImage = new GreenfootImage (text, textSize, Color.BLACK, Color.GRAY);
        myImage = new GreenfootImage (tempTextImage.getWidth() + 8, tempTextImage.getHeight() + 8);
        myImage.setColor (Color.GRAY);
        myImage.fill();
        myImage.drawImage (tempTextImage, 4, 4);

        myImage.setColor (Color.BLACK);
        //myImage.drawRect (0,0,tempTextImage.getWidth() + 7, tempTextImage.getHeight() + 7);
        setImage(myImage);
        
        original = Processor.copyImage(getBufferedImage());
    }
    public void act(){
        checkMouse();
    }
    
    /**
     * changes the width of the button
     * 
     * @param newWidth the new Width of the button
     */
    public void widen(int newWidth){
        GreenfootImage tempTextImage = new GreenfootImage (buttonText, 15, Color.BLACK, Color.GRAY);

        myImage = new GreenfootImage (newWidth, getImage().getHeight());
        myImage.setColor (Color.GRAY);
        myImage.fill();
        myImage.drawImage (tempTextImage, 4, 4);

        myImage.setColor (Color.BLACK);
        //myImage.drawRect (0,0,newWidth, tempTextImage.getHeight() );
        setImage(myImage);
        original = Processor.copyImage(getBufferedImage());
    }
    /**
     * checks if the mouse is currently over the button
     */
    public void checkMouse(){
        MouseInfo m = Greenfoot.getMouseInfo();
        if(m !=null){
            List button = getWorld().getObjectsAt(m.getX(), m.getY(), TextButton.class);
            if(button.size() > 0 && button.get(0) == this && !highlighted){
                highlighted = true;
                Processor.colorFilter(getBufferedImage(), Processor.packagePixel(255,255,255,50));
            }else if(button.size() == 0 || button.get(0) != this){
                highlighted = false;
                Processor.overwriteImage(getBufferedImage(), original);
            }
        }
        
    }
    public BufferedImage getBufferedImage ()
    {
        return this.getImage().getAwtImage();
    }

}
