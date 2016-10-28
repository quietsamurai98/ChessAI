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
import java.util.*;
public class Chess {
        
    /**
     * Creates a new instance of <code>Chess</code>.
     */
    JFrame chessInterface;
    JPanel boardPanel;
    JLabel squaresPanels[][];
    String pieceSprites[][] = new String[2][7];
    static final int reset[][] = {{24,22,23,25,26,23,22,24},{21,21,21,21,21,21,21,21},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{11,11,11,11,11,11,11,11},{14,12,13,15,16,13,12,14}};
    int board[][];
    boolean moving;
    int lastI;
    int lastJ;
    boolean AIvsAI=false;
    int depth = 4;
    int currentSide=1;
    
    public Chess(int ARGUMENT_DEPTH, boolean ARGUMENT_AIvsAI) {
    	depth = ARGUMENT_DEPTH;
    	AIvsAI= ARGUMENT_AIvsAI;
    }
    public static void main(String[] args) {
        if (args.length==1){
        	Chess chess = new Chess(Integer.valueOf(args[0]),false);
        	chess.pseudoMain();
        } else if (args.length>=2){
        	Chess chess = new Chess(Integer.valueOf(args[0]),Boolean.valueOf(args[1]));
        	chess.pseudoMain();
        } else {
        	Chess chess = new Chess(3, false);
        	chess.pseudoMain();
        }
        
        
    }
    public void createBoardWindow(){
    	int boardSize=600;
    	
    	JFrame chessInterface = new JFrame();
        chessInterface.setTitle("Chess Interface");
        
        chessInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chessInterface.setFocusable(true);
        chessInterface.requestFocus();

        boardPanel = new JPanel();
        boardPanel.setMinimumSize(new Dimension(boardSize,boardSize));
        boardPanel.setMaximumSize(new Dimension(boardSize,boardSize));
        boardPanel.setPreferredSize(new Dimension(boardSize,boardSize));
        boardPanel.setSize(boardSize,boardSize);
        boardPanel.setLayout(new GridLayout(8,8,0,0));
        int squareSize=boardSize/8;
    	squaresPanels = new JLabel[8][8];
    	boolean squareColor = true;
        for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		squaresPanels[i][j] = new JLabel();
        		squaresPanels[i][j].setOpaque(true);
        		squaresPanels[i][j].setSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setMinimumSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setMaximumSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setPreferredSize(new Dimension(squareSize,squareSize));
        		squaresPanels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				squaresPanels[i][j].setVerticalAlignment(SwingConstants.CENTER);
				squaresPanels[i][j].setFont(new Font("Segoe UI Symbol", squaresPanels[i][j].getFont().getStyle(), 70));
				squaresPanels[i][j].setForeground(Color.BLACK);
        		if(squareColor){
        			squaresPanels[i][j].setBackground(Color.gray);
        		} else {
        			squaresPanels[i][j].setBackground(Color.darkGray);
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
					if(bar%10==6){
						kingCount++;
					}
				}
			}
			kingsAreAlive=kingCount==2;
			if(kingsAreAlive){
				int[] coords = PlanAheadMove(1,board,depth);
    			System.out.println("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
    			System.out.println("    Move score was " + analyzeMove(coords[2],coords[3],coords[0],coords[1],1,board));
    			board[coords[2]][coords[3]] = board[coords[0]][coords[1]];
    			board[coords[0]][coords[1]]=0;
    			updatePieceDisplay();
			}
			waitms(0);
			kingCount=0;
			for(int[] foo:board){
				for(int bar:foo){
					if(bar%10==6){
						kingCount++;
					}
				}
			}
			kingsAreAlive=kingCount==2;
			if(kingsAreAlive){
				int[] coords = PlanAheadMove(2,board,depth);
    			System.out.println("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
    			System.out.println("    Move score was " + analyzeMove(coords[2],coords[3],coords[0],coords[1],2,board));
    			board[coords[2]][coords[3]] = board[coords[0]][coords[1]];
    			board[coords[0]][coords[1]]=0;
    			updatePieceDisplay();
			}
			waitms(0);
        }
    }
    public void waitms(long ms){
    	try{
    		Thread.sleep(ms);
    	} catch (InterruptedException  e) {
    		
    	}
    }
    public void makeMove(int[] moveArr, int[][] boardArr){
    	boardArr[moveArr[2]][moveArr[3]] = boardArr[moveArr[0]][moveArr[1]];
    	boardArr[moveArr[0]][moveArr[1]]=0;
    }
    public void makeMove(int i1 ,int j1 ,int i2 ,int j2 , int[][] boardArr){
    	boardArr[i2][j2] = boardArr[i1][j1];
    	boardArr[i1][j1] = 0;
    }
    public int[][] copyArr(int[][] arrIn){
    	int[][] arrOut= new int[arrIn.length][arrIn[0].length];
    	for(int i=0; i<arrIn.length; i++){
        	for (int j=0; j<arrIn[0].length; j++){
        		arrOut[i][j]=arrIn[i][j];
        	}
        }
    	return arrOut;
    }
    public void loadPieceSprites(){
	    	pieceSprites[0][1] = "\u2659";//WHITE PAWN
	    	pieceSprites[0][2] = "\u2658";//WHITE KNIGHT
	    	pieceSprites[0][3] = "\u2657";//WHITE BISHOP
	    	pieceSprites[0][4] = "\u2656";//WHITE ROOK
	    	pieceSprites[0][5] = "\u2655";//WHITE QUEEN
	    	pieceSprites[0][6] = "\u2654";//WHITE KING
	    	
	    	pieceSprites[1][1] = "\u265F";//BLACK PAWN
	    	pieceSprites[1][2] = "\u265E";//BLACK KNIGHT
	    	pieceSprites[1][3] = "\u265D";//BLACK BISHOP
	    	pieceSprites[1][4] = "\u265C";//BLACK ROOK
	    	pieceSprites[1][5] = "\u265B";//BLACK QUEEN
	    	pieceSprites[1][6] = "\u265A";//BLACK KING
    }
    public void updatePieceDisplay(){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		
        		if(board[i][j]/10==1){
        			squaresPanels[i][j].setText(pieceSprites[0][board[i][j]%10]);
        			squaresPanels[i][j].setForeground(Color.white);
        		} else if(board[i][j]/10==2){
        			squaresPanels[i][j].setText(pieceSprites[0][board[i][j]%10]);
        			squaresPanels[i][j].setForeground(Color.black);
        		} else {
        			squaresPanels[i][j].setText(" ");
        		}
        	}
        }
    }
    public void highlightMoves(int[][] legals){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
    			if((i%2+j%2)%2==0){
    				if(legals[i][j]==1){
    					squaresPanels[i][j].setBackground(new Color(190,190,0));
    				} else {
    					squaresPanels[i][j].setBackground(Color.gray);
    				}
    			} else {
    				if(legals[i][j]==1){
    					squaresPanels[i][j].setBackground(new Color(100,100,0));
    				} else {
    					squaresPanels[i][j].setBackground(Color.darkGray);
    				}
    			}
        	}
        }
    }  
    public void clickedOn(int i, int j){
    	if(board[i][j]/10!=2||moving){
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
		    		highlightMoves(new int[8][8]);
		    		updatePieceDisplay();
		    		int[] coords = aiMiniMax(board,2,depth);
	    			System.out.println("Piece on ("+ coords[0] + "," + coords[1] + ") moves to (" + coords[2]+","+coords[3] + ")");
	    			System.out.println("    Move score was " + analyzeMove(coords[2],coords[3],coords[0],coords[1],2,board));
	    			board[coords[2]][coords[3]] = board[coords[0]][coords[1]];
	    			board[coords[0]][coords[1]]=0;
		    		updatePieceDisplay();
	    		} else {
	    			highlightMoves(new int[8][8]);
	    		}
	    	}
    	}
    	
    }
    public boolean testGameOver(int[][] PARAMETER_ARRAY){
    	int kingCount=0;
    	for(int[] foo:PARAMETER_ARRAY){
    		for(int bar:foo){
    			if(bar%10==6){
    				kingCount++;
    			}
    		}
    	}
    	return kingCount!=2;
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
    
    public int getScore(int[][] PARAMETER_ARRAY){
    	int [][] arr = copyArr(PARAMETER_ARRAY);
        double fitness=0;
        for(int[] foo: arr){
        	for(int bar:foo){
        		int piece = bar%10;
        		if(bar/10==1){
        			if (piece==1)
        				fitness+=1;
        			if (piece==2)
        				fitness+=3.5;
        			if (piece==3)
        				fitness+=3.5;
        			if (piece==4)
        				fitness+=5.25;
        			if (piece==5)
        				fitness+=10;
        			if (piece==6)
        				fitness+=1000;
        		}else{
        			if (piece==1)
        				fitness-=1;
        			if (piece==2)
        				fitness-=3.5;
        			if (piece==3)
        				fitness-=3.5;
        			if (piece==4)
        				fitness-=5.25;
        			if (piece==5)
        				fitness-=10;
        			if (piece==6)
        				fitness-=1000;
        		}
        	}
        }
        return (int)(fitness*100);
    }
    
    public int getMoveScore(int[] move, int[][] PARAMETER_ARRAY){
    	int[][] arr = copyArr(PARAMETER_ARRAY);
    	makeMove(move, arr);
    	return getScore(arr);
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
        ArrayList<Integer> randomOrder = new ArrayList<Integer>();
		for (int i = 0; i <= 63; i++)
		{
		    randomOrder.add(i);
		}
		Collections.shuffle(randomOrder);
        for(int foo:randomOrder){
    		int pieceR=foo/8;
    		int pieceC=foo%8;
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
        int[] out = {fitStartR,fitStartC,fitEndR,fitEndC};
        return out;
    }
    
    public int[] PlanAheadMove(int side, int[][] tempArr, int searchDepth){
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
        ArrayList<Integer> randomOrder = new ArrayList<Integer>();
		for (int i = 0; i < 64; i++)
		{
		    randomOrder.add(i);
		}
		Collections.shuffle(randomOrder);
        for(int foo:randomOrder){
    		int pieceR=foo/8;
    		int pieceC=foo%8;
    		if(myPieces[pieceR][pieceC]==1){
    			int[][] legals = legalMoves(pieceR,pieceC,inBoard);
    			for(int legalR=0; legalR<8; legalR++){
    				for(int legalC=0; legalC<8; legalC++){
    					if(legals[legalR][legalC]==1){
				    		double fit = analyzeMove(pieceR,pieceC,legalR,legalC,side,inBoard);
    						if (searchDepth>0){
    							int[][] outBoard = new int[inBoard.length][inBoard[0].length];
						    	for(int i=0; i<inBoard.length; i++){
						        	for (int j=0; j<inBoard[0].length; j++){
						        		outBoard[i][j]=inBoard[i][j];
						        	}
						        }
								outBoard[legalR][legalC] = outBoard[pieceR][pieceC];
					    		outBoard[pieceR][pieceC]=0;
					    		int[] coords = PlanAheadMove((side%2)+1, outBoard, searchDepth-1);
					    		fit += analyzeMove(coords[2],coords[3],coords[0],coords[1],(side%2)+1,outBoard);
					    	}
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
    	int[] out = {fitStartR,fitStartC,fitEndR,fitEndC};
        return out;
    }
    
    public int[] aiMiniMax(int[][] PARAMETER_ARRAY, int side, int searchDepth){
    	int[][] arr=copyArr(PARAMETER_ARRAY);
    	if (testGameOver(arr)||searchDepth==0){
    		int[] out={-1,-1,-1,-1,getScore(arr)*currentSide};
    		return out;
    	}
    	
    	
    	ArrayList<Integer> scores = new ArrayList<Integer>();
    	ArrayList<int[]> moves = new ArrayList<int[]>();
    	
    	int[][] myPieces = new int[arr.length][arr[0].length];
    	for(int i=0; i<arr.length; i++){
        	for (int j=0; j<arr[0].length; j++){
        		if(arr[i][j]/(side*10)==1){
        			int[][] legalMoveBoard=legalMoves(i,j,arr);
        			for(int a=0; a<arr.length; a++){
        				for(int b=0; b<arr[0].length; b++){
        					if(legalMoveBoard[a][b]==1){
        						int[] moveItem = {i,j,a,b};
        						moves.add(moveItem);
        						int[][] recurArr = copyArr(arr);
        						makeMove(moveItem,recurArr);
        						if(side==1){
						    		scores.add(aiMiniMax(recurArr,2,searchDepth-1)[4]);
						    	} else if(side==2){
						    		scores.add(aiMiniMax(recurArr,1,searchDepth-1)[4]);
						    	}
			        		}
        				}
        			}
        		}
        	}
        }
        for(int[] possibleMove:moves){
        	scores.add(getMoveScore(possibleMove, arr));
        }
        
        ArrayList<Integer> order = new ArrayList<Integer>();
    	for(int i=0;i<moves.size();i++){
    		order.add(i);
    	}
    	Collections.shuffle(order);
    	if(side==currentSide){
			int index = 0;
			int maxScore=Integer.MIN_VALUE;
			for(int i:order){
				if(scores.get(i)>maxScore){
					maxScore=scores.get(i);
					index=i;
				}
			}
			int[] outArr = {moves.get(index)[0],moves.get(index)[1],moves.get(index)[2],moves.get(index)[3],maxScore};
			return outArr;
		} else {
			int index = 0;
			int minScore=Integer.MAX_VALUE;
			for(int i:order){
				if(scores.get(i)<minScore){
					minScore=scores.get(i);
					index=i;
				}
			}
			int[] outArr = {moves.get(index)[0],moves.get(index)[1],moves.get(index)[2],moves.get(index)[3],minScore};
			return outArr;
		}
    }
}
