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
        final JFrame setupFrame = new JFrame();
        setupFrame.setTitle("New Game Setup");
        setupFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame.setFocusable(true);
        setupFrame.requestFocus();
        setupFrame.setLayout(new GridLayout(2,2,0,0));
        
        JButton aiDuel = new JButton("AI vs AI");
        aiDuel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	setupFrame.dispose();
                ChessGUI gui=new ChessGUI(true,true);
            }
        });
        setupFrame.add(aiDuel);
        
        JButton humanDuel = new JButton("Human vs Human");
        humanDuel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	setupFrame.dispose();
                ChessGUI gui=new ChessGUI(false,false);
            }
        });
        setupFrame.add(humanDuel);
        
        JButton humanVai = new JButton("Human vs AI");
        humanVai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	setupFrame.dispose();
                ChessGUI gui=new ChessGUI(false,true);
            }
        });
        setupFrame.add(humanVai);
        
        JButton aiVhuman = new JButton("AI vs Human");
        aiVhuman.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
            	setupFrame.dispose();
                ChessGUI gui=new ChessGUI(true,false);
            }
        });
        setupFrame.add(aiVhuman);
        
        setupFrame.pack();
        setupFrame.setResizable(false);
        setupFrame.setVisible(true);
    }
}
