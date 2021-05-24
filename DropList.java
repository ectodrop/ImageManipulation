
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.image.BufferedImage;
/**
 * DropList contains a list of text buttons that show to form a makeshift drop down list
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DropList extends Actor
{
    /**
     * Act - do whatever the DropList wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    private ArrayList<TextButton> buttons = new ArrayList<TextButton>();
    private GreenfootImage myImage;
    private String buttonText;
    private BufferedImage original;
    
    private boolean highlighted = false;
    private int maxWidth = 0;
    public DropList(String name){
        this(name, 15);
    }
    public DropList (String text, int textSize)
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
    public void act() 
    {
        // Add your action code here.
        mouseInputs();
    }    
    /**
     * Checks if the mouse is hovering over the list
     */
    private void mouseInputs(){
        MouseInfo m = Greenfoot.getMouseInfo();
        if(m != null){
            List list = getWorld().getObjectsAt(m.getX(), m.getY(), DropList.class);
            list.addAll(getWorld().getObjectsAt(m.getX(), m.getY(), TextButton.class));
            if(list.size() > 0){
                for(var l: list){
                    if(this == l || buttons.contains(l)){//also show the list if the mouse is over the buttons in the list
                        this.show();
                    }else{
                        this.hide();
                    }
                }
            }
            else this.hide();
        }
    }
    /**
     * adds a button to the droplist
     */
    public void add(TextButton btn){
        buttons.add(btn);
        for(var b: buttons){
            maxWidth = Processor.max(b.getImage().getWidth(), maxWidth);
        }
        for(var b: buttons){
            b.widen(maxWidth);
        }
        hide();
    }
    /**
     * Moves the buttons in the list the their proper location
     */
    public void show(){
        for(int i = 0; i < buttons.size(); i++){
            TextButton btn = buttons.get(i);
            btn.setLocation(getX()+maxWidth/2-getImage().getWidth()/2, getY() + btn.getImage().getHeight()*(i+1));
        }//adjusts the buttons to they appear left justified
        if(!highlighted){
            highlighted = true;
            Processor.colorFilter(getBufferedImage(), Processor.packagePixel(0,0,0,50));
        }

    }
    /**
     * Sets the buttons back to their hidden position
     */
    public void hide(){
        for(int i = 0; i < buttons.size(); i++){
            buttons.get(i).setLocation(-20,-20);
        }
        highlighted = false;
        Processor.overwriteImage(getBufferedImage(), original);
    }
    public BufferedImage getBufferedImage ()
    {
        return this.getImage().getAwtImage();
    }
}
