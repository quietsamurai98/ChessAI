/**
 * @(#)Resizer.java
 *
 *
 * @author 
 * @version 1.00 2016/11/1
 */
import javax.swing.*;
import java.awt.Color;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.*;
public class Resizer {
        
    /**
     * Creates a new instance of <code>Resizer</code>.
     */
    public Resizer() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        	BufferedImage[][] rawPieceSprites = new BufferedImage[2][7];
        	BufferedImage[][] pieceSprites = new BufferedImage[2][7];
	    	try{
		    	rawPieceSprites[0][1] = ImageIO.read(new File("white pawn.png"));
		    	rawPieceSprites[0][2] = ImageIO.read(new File("white knight.png"));
		    	rawPieceSprites[0][3] = ImageIO.read(new File("white bishop.png"));
		    	rawPieceSprites[0][4] = ImageIO.read(new File("white rook.png"));
		    	rawPieceSprites[0][5] = ImageIO.read(new File("white queen.png"));
		    	rawPieceSprites[0][6] = ImageIO.read(new File("white king.png"));
		    	
		    	rawPieceSprites[1][1] = ImageIO.read(new File("black pawn.png"));
		    	rawPieceSprites[1][2] = ImageIO.read(new File("black knight.png"));
		    	rawPieceSprites[1][3] = ImageIO.read(new File("black bishop.png"));
		    	rawPieceSprites[1][4] = ImageIO.read(new File("black rook.png"));
		    	rawPieceSprites[1][5] = ImageIO.read(new File("black queen.png"));
		    	rawPieceSprites[1][6] = ImageIO.read(new File("black king.png"));	
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
	    	for(int i=0;i<2;i++){
	    		for(int j=1;j<7;j++){
	    			pieceSprites[i][j] = resize(rawPieceSprites[i][j],100,100);
	    		}
	    	}
	    	try{
		    	ImageIO.write(pieceSprites[0][1], "png", new File("white pawn.png"));
		    	ImageIO.write(pieceSprites[0][2], "png", new File("white knight.png"));
		    	ImageIO.write(pieceSprites[0][3], "png", new File("white bishop.png"));
		    	ImageIO.write(pieceSprites[0][4], "png", new File("white rook.png"));
		    	ImageIO.write(pieceSprites[0][5], "png", new File("white queen.png"));
		    	ImageIO.write(pieceSprites[0][6], "png", new File("white king.png"));
		    	
		    	ImageIO.write(pieceSprites[1][1], "png", new File("black pawn.png"));
		    	ImageIO.write(pieceSprites[1][2], "png", new File("black knight.png"));
		    	ImageIO.write(pieceSprites[1][3], "png", new File("black bishop.png"));
		    	ImageIO.write(pieceSprites[1][4], "png", new File("black rook.png"));
		    	ImageIO.write(pieceSprites[1][5], "png", new File("black queen.png"));
		    	ImageIO.write(pieceSprites[1][6], "png", new File("black king.png"));	
	    	} catch (IOException e) {
	    		e.printStackTrace();
	    	}
    }
    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
	
	    return dimg;
	}  
}
