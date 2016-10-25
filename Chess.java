/**
 * @(#)Chess.java
 *
 *
 * @author 
 * @version 1.00 2016/10/21
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
public class Chess {
        
    /**
     * Creates a new instance of <code>Chess</code>.
     */
    JFrame chessInterface;
    JPanel boardPanel;
    JLabel squaresPanels[][];
    ImageIcon pieceSprites[][] = new ImageIcon[2][7];
    static final int reset[][] = {{24,22,23,25,26,23,22,24},{21,21,21,21,21,21,21,21},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{11,11,11,11,11,11,11,11},{14,12,13,15,16,13,12,14}};
    int board[][];
    boolean moving;
    int lastI;
    int lastJ;
    boolean AIvsAI=true;
        
    public Chess() {
    }
    public static void main(String[] args) {
        // TODO code application logic here
        Chess chess = new Chess();
        chess.pseudoMain();
        
    }
    public void createBoardWindow(){
    	JFrame chessInterface = new JFrame();
        chessInterface.setTitle("Chess Interface");
        
        chessInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessInterface.setFocusable(true);
        chessInterface.requestFocus();

        boardPanel = new JPanel();
        boardPanel.setMinimumSize(new Dimension(400,400));
        boardPanel.setMaximumSize(new Dimension(400,400));
        boardPanel.setPreferredSize(new Dimension(400,400));
        boardPanel.setSize(400,400);
        boardPanel.setLayout(new GridLayout(8,8,0,0));
        
    	squaresPanels = new JLabel[8][8];
    	boolean squareColor = true;
        for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		squaresPanels[i][j] = new JLabel();
        		squaresPanels[i][j].setOpaque(true);
        		squaresPanels[i][j].setSize(new Dimension(50,50));
        		squaresPanels[i][j].setMinimumSize(new Dimension(50,50));
        		squaresPanels[i][j].setMaximumSize(new Dimension(50,50));
        		squaresPanels[i][j].setPreferredSize(new Dimension(50,50));
        		if(squareColor){
        			squaresPanels[i][j].setBackground(Color.WHITE);
        		} else {
        			squaresPanels[i][j].setBackground(Color.BLACK);
        		}
        		squareColor=!squareColor;
        		final int tempI = i;
        		final int tempJ = j;
        		squaresPanels[i][j].addMouseListener(new java.awt.event.MouseAdapter() {
        			final int myI=tempI;
        			final int myJ=tempJ;
		            public void mouseClicked(java.awt.event.MouseEvent evt) {
		                clickedOn(myI,myJ);
		            }
		        });
        		boardPanel.add(squaresPanels[i][j]);
        		
        	}
        	squareColor=!squareColor;
        }
        chessInterface.add(boardPanel);
        chessInterface.pack();
        chessInterface.setResizable(false);
        chessInterface.setVisible(true);
    }
    public void pseudoMain() {
        // TODO code application logic here
        createBoardWindow();
        loadPieceSprites();
        board=reset;
        updatePieceDisplay();
        moving = false;
        lastI=0;
        lastJ=0;
        boolean kingsAreAlive=true;
        int kingCount=0;
        while(AIvsAI&&kingsAreAlive){
        	kingCount=0;
			for(int[] foo:board){
				for(int bar:foo){
					if(bar%10==5){
						kingCount++;
					}
				}
			}
			kingsAreAlive=kingCount==2;
			if(kingsAreAlive){
				int[] coords = NoPlanningMove(1,board);
				board[coords[2]][coords[3]] = board[coords[0]][coords[1]];
    			board[coords[0]][coords[1]]=0;
    			System.out.println("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
    			System.out.println("    Move score was " + analyzeMove(coords[2],coords[3],coords[0],coords[1],1,board));
    			updatePieceDisplay();
			}
			kingCount=0;
			for(int[] foo:board){
				for(int bar:foo){
					if(bar%10==5){
						kingCount++;
					}
				}
			}
			kingsAreAlive=kingCount==2;
			if(kingsAreAlive){
				int[] coords = NoPlanningMove(2,board);
				board[coords[2]][coords[3]] = board[coords[0]][coords[1]];
    			board[coords[0]][coords[1]]=0;
    			System.out.println("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
    			System.out.println("    Move score was " + analyzeMove(coords[2],coords[3],coords[0],coords[1],2,board));
    			updatePieceDisplay();
			}
			
        }
    }
    public void loadPieceSprites(){
    	
    	try{
	    	String dir =  ".\\resources\\images\\pieces\\white\\";
	    	pieceSprites[0][1] = new ImageIcon(dir + "pawn.png");
	    	pieceSprites[0][2] = new ImageIcon(ImageIO.read(new File(dir + "knight.png")));
	    	pieceSprites[0][3] = new ImageIcon(ImageIO.read(new File(dir + "bishop.png")));
	    	pieceSprites[0][4] = new ImageIcon(ImageIO.read(new File(dir + "rook.png")));
	    	pieceSprites[0][5] = new ImageIcon(ImageIO.read(new File(dir + "queen.png")));
	    	pieceSprites[0][6] = new ImageIcon(ImageIO.read(new File(dir + "king.png")));
	    	dir =  ".\\resources\\images\\pieces\\black\\";
	    	pieceSprites[1][1] = new ImageIcon(ImageIO.read(new File(dir + "pawn.png")));
	    	pieceSprites[1][2] = new ImageIcon(ImageIO.read(new File(dir + "knight.png")));
	    	pieceSprites[1][3] = new ImageIcon(ImageIO.read(new File(dir + "bishop.png")));
	    	pieceSprites[1][4] = new ImageIcon(ImageIO.read(new File(dir + "rook.png")));
	    	pieceSprites[1][5] = new ImageIcon(ImageIO.read(new File(dir + "queen.png")));
	    	pieceSprites[1][6] = new ImageIcon(ImageIO.read(new File(dir + "king.png")));
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	
    	
    }
    public void updatePieceDisplay(){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		if(board[i][j]!=0){
        			try{
        				squaresPanels[i][j].setIcon(pieceSprites[board[i][j]/10-1][board[i][j]%10]);
        			} catch (NullPointerException e) {
    					e.printStackTrace();
    				}
        		} else {
        			squaresPanels[i][j].setIcon(null);
        		}
        	}
        }
    }
    public void highlightMoves(int[][] legals){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
    			if((i%2+j%2)%2==0){
    				if(legals[i][j]==1){
    					squaresPanels[i][j].setBackground(new Color(255,255,0));
    				} else {
    					squaresPanels[i][j].setBackground(new Color(255,255,255));
    				}
    			} else {
    				if(legals[i][j]==1){
    					squaresPanels[i][j].setBackground(new Color(128,128,0));
    				} else {
    					squaresPanels[i][j].setBackground(new Color(0,0,0));
    				}
    			}
        	}
        }
    }  
    public void clickedOn(int i, int j){
    	
    	if(!moving){
    		lastI=i;
    		lastJ=j;
    		moving = true;
    		if(board[i][j]!=0){
    			highlightMoves(legalMoves(i,j,board));
    		}
    	} else {
    		moving = false;
    		if(legalMoves(lastI,lastJ,board)[i][j]!=0){
    			System.out.println("Piece on ("+ lastI + "," + lastJ + ") moves to (" + i+","+j + ")");
    			System.out.println("    Move score was " + analyzeMove(i,j,lastI,lastJ,board[lastI][lastJ]/10,board));
    			board[i][j] = board[lastI][lastJ];
    			board[lastI][lastJ]=0;
    		}
    		highlightMoves(new int[8][8]);
    		updatePieceDisplay();
    		int[] coords = NoPlanningMove(2,board);
    		System.out.println("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
    		System.out.println("    Move score was " + analyzeMove(coords[2],coords[3],coords[0],coords[1],2,board));
			board[coords[2]][coords[3]] = board[coords[0]][coords[1]];
    		board[coords[0]][coords[1]]=0;
    		updatePieceDisplay();
    	}
    }
    //To move white piece, side=1, to move black piece, side=2
    public int[][] legalMoves(int r, int c, int[][] tempArr){
    	int[][] inArr = new int[tempArr.length][tempArr[0].length];
    	for(int i=0; i<tempArr.length; i++){
        	for (int j=0; j<tempArr[0].length; j++){
        		inArr[i][j]=tempArr[i][j];
        	}
        }
    	int[][] out = new int[8][8];
    	int piece = inArr[r][c]%10;
    	int side = inArr[r][c]/10;
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		boolean isLegal=false;
        		if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
        		
        			//Pawn
        			if (piece==1){ 
        				if(inArr[i][j]==0 && c==j && ((side==2&&r+1==i)||(side==1&&r-1==i))){ //pawn moves forward by one space
        					isLegal=true;
        				}
        				if(inArr[i][j]==0 && c==j && ((r==1&&side==2&&r+2==i&&inArr[i-1][j]==0)||(r==6&&side==1&&r-2==i&&inArr[i+1][j]==0))){ //pawn moves forward by two spaces
        					isLegal=true;
        				}
        				if(inArr[i][j]!=0 && r+side*2-3==i && (c==j+1||c==j-1)){ //pawn captures diagonally
        					isLegal=true;
        				}
        			}
        			
        			//Knight
        			if (piece==2){ 
        				if((i+1==r && j+2==c)||(i-1==r && j+2==c)||(i+1==r && j-2==c)||(i-1==r && j-2==c)||(i+2==r && j+1==c)||(i-2==r && j+1==c)||(i+2==r && j-1==c)||(i-2==r && j-1==c)){ //Knight's moves 
        					isLegal=true;
        				}
        			}
        			
        			//King
        			if (piece==6){ 
        				if((Math.abs(i-r)<=1&&Math.abs(j-c)<=1)&&(i!=r||j!=c)){
        					isLegal=true;
        				}
        			}
        		}
        		
        		if (isLegal){
        			out[i][j]=1;
        		}
        	}
    	}
    	//Bishop
    	if (piece==3){ 
			for(int i=r+1, j=c+1; i<8&&j<8; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j++;
			}
			for(int i=r-1, j=c+1; i>=0&&j<8; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j++;
			}
			for(int i=r+1, j=c-1; i<8&&j>=0; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j--;
			}
			for(int i=r-1, j=c-1; i>=0&&j>=0; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j--;
			}
		}
		
		//Rook
		if (piece==4){ 
			for(int i=r+1; i<8; i++){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
			}
			for(int i=r-1; i>=0; i--){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
			}
			for(int j=c+1; j<8; j++){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=9999999;
					}
				} else {
					j=9999999;
				}
			}
			for(int j=c-1; j>=0; j--){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=-9999999;
					}
				} else {
					j=-9999999;
				}
			}
		}
		
		//Queen
		if (piece==5){ 
			for(int i=r+1; i<8; i++){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
			}
			for(int i=r-1; i>=0; i--){
				if (side!=inArr[i][c]/10){ //Not occupied by your own pieces
					if(inArr[i][c]==0){
						out[i][c]=1;
					}else{
						out[i][c]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
			}
			for(int j=c+1; j<8; j++){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=9999999;
					}
				} else {
					j=9999999;
				}
			}
			for(int j=c-1; j>=0; j--){
				if (side!=inArr[r][j]/10){ //Not occupied by your own pieces
					if(inArr[r][j]==0){
						out[r][j]=1;
					}else{
						out[r][j]=1;
						j=-9999999;
					}
				} else {
					j=-9999999;
				}
			}
			for(int i=r+1, j=c+1; i<8&&j<8; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j++;
			}
			for(int i=r-1, j=c+1; i>=0&&j<8; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j++;
			}
			for(int i=r+1, j=c-1; i<8&&j>=0; i++){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=9999999;
					}
				} else {
					i=9999999;
				}
				j--;
			}
			for(int i=r-1, j=c-1; i>=0&&j>=0; i--){
				if (side!=inArr[i][j]/10){ //Not occupied by your own pieces
					if(inArr[i][j]==0){
						out[i][j]=1;
					}else{
						out[i][j]=1;
						i=-9999999;
					}
				} else {
					i=-9999999;
				}
				j--;
			}
		}
    	
    	return out;
    }
    public int[][] multiplyArrayElements(int[][] foo, int[][] bar){
    	int[][] out = new int[8][8];
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		out[i][j]=foo[i][j]*bar[i][j];
        	}
    	}
    	return out;
    }
    //Returns the fitness of a move. Fitness is defined as (your postmove score - enemy postmove score)-(your initial score - initial enemy score)
    public double analyzeMove(int startR, int startC, int endR, int endC, int side, int[][] tempArr){
    	int[][] inArr = new int[tempArr.length][tempArr[0].length];
    	for(int i=0; i<tempArr.length; i++){
        	for (int j=0; j<tempArr[0].length; j++){
        		inArr[i][j]=tempArr[i][j];
        	}
        }
        double fitnessStart=0;
        for(int[] foo: inArr){
        	for(int bar:foo){
        		if(bar/(side*10)==1){
        			int piece = bar%10;
        			if (piece==1)
        				fitnessStart+=1;
        			if (piece==2)
        				fitnessStart+=3.5;
        			if (piece==3)
        				fitnessStart+=3.5;
        			if (piece==4)
        				fitnessStart+=5.25;
        			if (piece==5)
        				fitnessStart+=10;
        			if (piece==6)
        				fitnessStart+=10000;
        		}else{
        			int piece = bar%10;
        			if (piece==1)
        				fitnessStart-=1;
        			if (piece==2)
        				fitnessStart-=3.5;
        			if (piece==3)
        				fitnessStart-=3.5;
        			if (piece==4)
        				fitnessStart-=5.25;
        			if (piece==5)
        				fitnessStart-=10;
        			if (piece==6)
        				fitnessStart-=10000;
        		}
        	}
        }
        inArr[startR][startC] = inArr[endR][endC];
    	inArr[endR][endC]=0;
    	double fitnessEnd=0;
        for(int[] foo: inArr){
        	for(int bar:foo){
        		if(bar/(side*10)==1){
        			int piece = bar%10;
        			if (piece==1)
        				fitnessEnd+=1;
        			if (piece==2)
        				fitnessEnd+=3.5;
        			if (piece==3)
        				fitnessEnd+=3.5;
        			if (piece==4)
        				fitnessEnd+=5.25;
        			if (piece==5)
        				fitnessEnd+=10;
        			if (piece==6)
        				fitnessEnd+=10000;
        		}else{
        			int piece = bar%10;
        			if (piece==1)
        				fitnessEnd-=1;
        			if (piece==2)
        				fitnessEnd-=3.5;
        			if (piece==3)
        				fitnessEnd-=3.5;
        			if (piece==4)
        				fitnessEnd-=5.25;
        			if (piece==5)
        				fitnessEnd-=10;
        			if (piece==6)
        				fitnessEnd-=10000;
        		}
        	}
        }
        return fitnessEnd-fitnessStart;
    }
    
    public int[] NoPlanningMove(int side, int[][] tempArr){
    	int[][] inBoard = new int[tempArr.length][tempArr[0].length];
    	for(int i=0; i<tempArr.length; i++){
        	for (int j=0; j<tempArr[0].length; j++){
        		inBoard[i][j]=tempArr[i][j];
        	}
        }
        int[][] myPieces = new int[tempArr.length][tempArr[0].length];
    	for(int i=0; i<tempArr.length; i++){
        	for (int j=0; j<tempArr[0].length; j++){
        		if(tempArr[i][j]/(side*10)==1)
        			myPieces[i][j]=1;
        	}
        }
        double maxFitness = -99999999;
        int fitStartR=-1;
        int fitStartC=-1;
        int fitEndR=-1;
        int fitEndC=-1;
        for(int pieceR=0; pieceR<8; pieceR++){
        	for(int pieceC=0; pieceC<8; pieceC++){
        		if(myPieces[pieceR][pieceC]==1){
        			int[][] legals = legalMoves(pieceR,pieceC,inBoard);
        			for(int legalR=0; legalR<8; legalR++){
        				for(int legalC=0; legalC<8; legalC++){
        					if(legals[legalR][legalC]==1){
        						double fit = analyzeMove(pieceR,pieceC,legalR,legalC,side,inBoard);
        						if (fit>maxFitness){
        							fitStartR=pieceR;
        							fitStartC=pieceC;
        							fitEndR=legalR;
        							fitEndC=legalC;
        						}
        					}
        				}
        			}
        		}
        	}
        }
        int[] out = {fitStartR,fitStartC,fitEndR,fitEndC};
        return out;
    }
}