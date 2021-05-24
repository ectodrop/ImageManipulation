import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;
    /**
     * Starter code for Image Manipulation Array Assignment.
     * 
     * The class Processor contains all of the code to actually perform
     * transformation. The rest of the classes serve to support that
     * capability. This World allows the user to choose transformations
     * and open files.
     * 
     * Add to it and make it your own!
     * 
     * @author Jordan Cohen
     * @version November 2013
     */
    
    public class Background extends World
{
    // Constants:
    private final File STARTING_FILE = new File(".\\images\\paintedbird.jpg");

    // Objects and Variables:
    private ImageHolder image;
    //drop down lists
    private DropList file;
    private DropList colorFilters;
    private DropList effects;
    private DropList transformations;
    //buttons
    private TextButton openFile;
    private TextButton saveButton;
    private TextButton undoButton;
    private TextButton redoButton;
    private TextButton overlayButton;
    
    private TextButton greyButton;
    private TextButton invertButton;
    private TextButton blurButton;
    private TextButton pixilateButton;
    private TextButton brightenButton;
    
    private TextButton rotateCWButton;
    private TextButton rotateCCWButton;
    private TextButton hRevButton;
    private TextButton vRevButton;
    
    private TextButton blueButton;
    private TextButton redButton;
    private TextButton greenButton;
    private TextButton coolButton;
    private TextButton warmButton;
    
    private TextButton customColor;

    private Stack<BufferedImage> undoImages = new Stack<BufferedImage>();
    private Stack<BufferedImage> redoImages = new Stack<BufferedImage>();
    /**
     * Constructor for objects of class Background.
     * 
     */
    public Background()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(700, 725, 1, false); 
        getBackground().setColor(Color.GRAY);
        getBackground().fill();
        getBackground().setColor(Color.LIGHT_GRAY);
        getBackground().fillRect(0, 25 , 700, 725);
        getBackground().setColor(Color.BLACK);
        
        initObjects();
    }
    

    /**
     * Act() method just checks for mouse input
     */
    public void act ()
    {
        checkInputs();
    }

    public void undo(BufferedImage bi){
        BufferedImage undoImage = undoImages.pop();
        redoImages.push(Processor.copyImage(bi));
        Processor.overwriteImage(bi, undoImage);
    }

    public void redo(BufferedImage bi){
        BufferedImage redoImage = redoImages.pop();
        undoImages.push(Processor.copyImage(bi));
        Processor.overwriteImage(bi, redoImage);
    }

    /**
     * Check for user clicking on a button
     */
    private void checkInputs ()
    {
        // Avoid excess mouse checks - only check mouse if somethething is clicked.
        if (Greenfoot.mouseClicked(null))
        {
            BufferedImage bi = image.getBufferedImage();
            if (Greenfoot.mouseClicked(blueButton)){
                undoImages.push(Processor.copyImage(bi));
                int color = Processor.packagePixel(0,0,255,100);
                Processor.colorFilter(image, color);
            }
            else if (Greenfoot.mouseClicked(redButton)){
                undoImages.push(Processor.copyImage(bi));
                int color = Processor.packagePixel(255,0,0,100);
                Processor.colorFilter(image, color);
            }
            else if (Greenfoot.mouseClicked(greenButton)){
                undoImages.push(Processor.copyImage(bi));
                int color = Processor.packagePixel(0,255,0,100);
                Processor.colorFilter(image, color);
            }
            else if (Greenfoot.mouseClicked(customColor)){
                undoImages.push(Processor.copyImage(bi));
                FileHandler.customFilter(image);
            }
            else if (Greenfoot.mouseClicked(hRevButton)){
                undoImages.push(Processor.copyImage(bi));
                Processor.flipHorizontal(bi);
            }
            else if (Greenfoot.mouseClicked(vRevButton)){
                undoImages.push(Processor.copyImage(bi));
                Processor.flipVertical(bi);
            }
            else if (Greenfoot.mouseClicked(greyButton)){
                undoImages.push(Processor.copyImage(bi));
                Processor.greyscale(bi);
            }
            else if (Greenfoot.mouseClicked(invertButton)){
                undoImages.push(Processor.copyImage(bi));
                Processor.invert(bi);
            }
            else if (Greenfoot.mouseClicked(openFile))
            {
                undoImages.push(Processor.copyImage(bi));
                FileHandler.openFile (image);
            }
            else if (Greenfoot.mouseClicked(saveButton))
            {
                FileHandler.saveFile(image);
            }
            else if(Greenfoot.mouseClicked(pixilateButton)){
                int size = Integer.parseInt(JOptionPane.showInputDialog("enter a tile size"));
                undoImages.push(Processor.copyImage(bi));
                Processor.pixilate(bi, size);
            }
            else if(Greenfoot.mouseClicked(blurButton)){
                undoImages.push(Processor.copyImage(bi));
                Processor.blur(bi);
            }
            else if(Greenfoot.mouseClicked(brightenButton)){
                undoImages.push(Processor.copyImage(bi));
                Processor.highpass(bi);
            }
            else if(Greenfoot.mouseClicked(rotateCWButton)){
                image.swapDimensions();
                undoImages.push(Processor.copyImage(bi));
                Processor.rotateCW(bi);
            }
            else if(Greenfoot.mouseClicked(rotateCCWButton)){
                image.swapDimensions();
                undoImages.push(Processor.copyImage(bi));
                Processor.rotateCCW(bi);
            }
            else if(Greenfoot.mouseClicked(undoButton) && !undoImages.empty()){
                undo(bi);
            }
            else if(Greenfoot.mouseClicked(redoButton) && !redoImages.empty()){
                redo(bi);
            }
            else if(Greenfoot.mouseClicked(overlayButton)){
                undoImages.push(Processor.copyImage(bi));
                FileHandler.overlayImage(image);
            }
            else if(Greenfoot.mouseClicked(coolButton)){
                Processor.blueify(bi);
            }
            else if(Greenfoot.mouseClicked(warmButton)){
                Processor.redify(bi);
            }
        }
        String key = Greenfoot.getKey();
        if(key != null){
            BufferedImage bi = image.getBufferedImage();
            if(key.equals("z") && !undoImages.empty()){
                undo(bi);
            }
            if(key.equals("y") && !redoImages.empty()){
                redo(bi);
            }
            if(key.equals("s")){
                FileHandler.saveFile(image);
            }
            
        }
    }
    private void initObjects(){
        file = new DropList("File");
        colorFilters = new DropList("Color Filters");
        effects = new DropList("Effects");
        transformations = new DropList("Transformation");
        
        addObject(file, 15, 13);
        addObject(colorFilters, 85, 13);
        addObject(effects, 170,13);
        addObject(transformations, 280, 13);
        
        undoButton = new TextButton("Undo");
        redoButton = new TextButton("Redo");
        addObject(undoButton,620,13);
        addObject(redoButton,660,13);
        // Initialize buttons and the image
        image = new ImageHolder(STARTING_FILE);
        
        
        blueButton = new TextButton("Blue");
        redButton = new TextButton("Red");
        greenButton = new TextButton("Green");
        customColor = new TextButton("Custom Filter");
        hRevButton = new TextButton("Flip Horizontal");
        vRevButton = new TextButton("Flip Vertical");
        openFile = new TextButton("New File");
        greyButton = new TextButton("Grey scale");
        invertButton = new TextButton("Invert");
        
        rotateCWButton = new TextButton("Rotate Clockwise 90");
        rotateCCWButton = new TextButton("Rotate Counter-Clockwise 90");
        saveButton = new TextButton("Save");
        blurButton = new TextButton("Blur");
        pixilateButton = new TextButton("Pixilate");
        brightenButton = new TextButton("Brighten");
        overlayButton = new TextButton("Overlay Image");
        coolButton = new TextButton("Cool");
        warmButton = new TextButton("Warm");
        // Add objects to the screen
        addObject (image, 700/2, 700/2+25);
        
        addObject(redButton, 0,0);
        addObject(blueButton, 0,0);
        addObject(greenButton, 0,0);
        addObject(customColor, 0,0);
        addObject (hRevButton, 100, 300);
        addObject (vRevButton, 90, 340);
        addObject (greyButton, 85, 380);
        addObject (invertButton, 70, 420);
        addObject (openFile, 400, 24);
        
        addObject(rotateCWButton, 0, 0);
        addObject(rotateCCWButton, 0, 0);
        addObject(saveButton, 0, 0);
        addObject(blurButton, 0, 0);
        addObject(pixilateButton, 0, 0);
        addObject(brightenButton, 0, 0);
        addObject(overlayButton, 0, 0);
        addObject(coolButton, 0, 0);
        addObject(warmButton, 0, 0);
        
        file.add(openFile);
        file.add(overlayButton);
        file.add(saveButton);
        
        
        colorFilters.add(redButton);
        colorFilters.add(greenButton);
        colorFilters.add(blueButton);
        colorFilters.add(coolButton);
        colorFilters.add(warmButton);
        colorFilters.add(customColor);
        
        
        effects.add(blurButton);
        effects.add(pixilateButton);
        effects.add(brightenButton);
        effects.add(invertButton);
        effects.add(greyButton);
        
        transformations.add(hRevButton);
        transformations.add(vRevButton);
        transformations.add(rotateCWButton);
        transformations.add(rotateCCWButton);
    }

}

