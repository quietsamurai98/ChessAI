/**
 * @(#)MainRunner.java
 *
 *
 * @author 
 * @version 1.00 2016/11/2
 */
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
public class MainRunner {
        
    /**
     * Creates a new instance of <code>MainRunner</code>.
     */
    public MainRunner() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ChessGUI gui=new ChessGUI(false,true);
    }
}
