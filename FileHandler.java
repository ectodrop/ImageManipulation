import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.File;
import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.imageio.ImageIO;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
public class FileHandler  
{
    /**
     * Opens an image and draws it over the current image with a chosen alpha value
     */
    public static void overlayImage(ImageHolder image){
        JFileChooser chooser = new JFileChooser(new File(".\\images"));
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           try{
               File f = chooser.getSelectedFile();
               BufferedImage newImage = fileToImage(f);
               
               int a = Integer.parseInt(JOptionPane.showInputDialog("Input an alpha value", null));
               image.overlay(newImage, a);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Please enter an integer value from 0-255");
            }
        }
    }
    /**
     * User chooses a directory to save a file to and an extention to save with
     */
    public static void saveFile(ImageHolder image){
        
        JFileChooser chooser = new JFileChooser(new File(".\\Saved Images"));
        
        chooser.setDialogType(JFileChooser.SAVE_DIALOG); 
        FileNameExtensionFilter png = new FileNameExtensionFilter("png", "png");
        chooser.addChoosableFileFilter(png);
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
        chooser.setFileFilter(png);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setDialogTitle("Type a file name with or without the extension");
        
        int returnVal = chooser.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           try{
               File fileName = chooser.getSelectedFile();
               
               String ext = chooser.getFileFilter().getDescription();
               File output = new File(removeExtension(fileName.getAbsolutePath()) + "." + ext);
               
               BufferedImage buffer = image.getBufferedImage();
               BufferedImage bi = buffer.getSubimage(image.getXPos(), image.getYPos(),image.getWidth(), image.getHeight());;
               
               if(ext == "jpg") bi = Processor.removeAlpha(bi);
               ImageIO.write(bi, ext, output);
           }
           catch(Exception e){}
        }
        
    }
    /**
     * user can choose rgba value and get a preview of the color
     */
    public static void customFilter(ImageHolder image){
        JColorChooser chooser = new JColorChooser(new java.awt.Color(0,0,0,100));
        AbstractColorChooserPanel[] oldPanels = chooser.getChooserPanels();
        chooser.removeChooserPanel(oldPanels[0]);
        chooser.removeChooserPanel(oldPanels[1]);
        chooser.removeChooserPanel(oldPanels[2]);
        JDialog dialog = JColorChooser.createDialog(null,"Choose a Color",true,chooser,
        new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                java.awt.Color c= chooser.getColor();
                Processor.colorFilter(image, c.getRGB());
            }
        },null);
        dialog.setVisible(true);
    }
    /**
     * Allows the user to open a new image file.
     */
    public static void openFile (ImageHolder image)
    {
        // Use a JOptionPane to get file name from user
        JFileChooser chooser = new JFileChooser(new File(".\\images"));
        chooser.setDialogTitle("Images over 700x700 will be cropped");
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
           File f = chooser.getSelectedFile();
           image.openFile(f);
        }
        // If the file opening operation is successful, update the text in the open file button
        
    }
    public static String removeExtension(String filename){
        if(filename.contains(".")){
            int index = filename.indexOf('.');
            return filename.substring(0,index);
        }
        return filename;
    }
    /**
     * converts a file object into a bufferedimage
     */
    public static BufferedImage fileToImage(File f){
        try{
            
            String fileName = f.getName();
            BufferedImage in = ImageIO.read(f);
    
            BufferedImage newImage = new BufferedImage(
            in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D g = newImage.createGraphics();
            g.drawImage(in, 0, 0, null);
            g.dispose();
            return newImage;
            //openImage(newImage);
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null,"There was an error loading the image");
        }
        return null;
    }
}
