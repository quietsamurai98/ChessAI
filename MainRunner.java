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
    	JFrame newGameFrame = new JFrame();
    	newGameFrame.setTitle("Chess Interface");
        
        newGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newGameFrame.setFocusable(true);
        newGameFrame.requestFocus();
    	newGameFrame.setLayout(new GridLayout(2,2,0,0));
    	JButton humanVhuman = new JButton("Human vs Human");
    	humanVhuman.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent e) {
        		ChessGUI gui=new ChessGUI(false,false);
        	}          
      	});
    	JButton aiVai = new JButton("AI vs AI");
    	aiVai.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent e) {
        		ChessGUI gui=new ChessGUI(true,true);
        	}          
      	});
    	JButton humanVai = new JButton("Human vs AI \r(Play as White)");
    	humanVai.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent e) {
        		ChessGUI gui=new ChessGUI(false,true);
        	}          
      	});
    	JButton aiVhuman = new JButton("Human vs AI \r(Play as Black)");
    	aiVhuman.addMouseListener(new java.awt.event.MouseAdapter() {
        	public void mouseClicked(java.awt.event.MouseEvent e) {
        		ChessGUI gui=new ChessGUI(true,false);
        	}          
      	});
    	newGameFrame.add(humanVhuman);
    	newGameFrame.add(aiVai);
    	newGameFrame.add(humanVai);
    	newGameFrame.add(aiVhuman);
    	newGameFrame.pack();
        newGameFrame.setResizable(false);
        newGameFrame.setVisible(true);
        
    }
}
