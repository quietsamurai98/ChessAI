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
    ImageIcon pieceSprites[][] = new ImageIcon[2][6];
    static final int reset[][] = {{23,21,22,24,25,22,21,23},{20,20,20,20,20,20,20,20},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{ 0, 0, 0, 0, 0, 0, 0, 0},{10,10,10,10,10,10,10,10},{13,11,12,14,15,12,11,13}};
    int board[][];
    boolean moving;
    int lastI;
    int lastJ;
    int loop=10000;
        
    public Chess() {
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
    public void loadPieceSprites(){
    	
    	try{
	    	String dir =  ".\\resources\\images\\pieces\\white\\";
	    	pieceSprites[0][0] = new ImageIcon(dir + "pawn.png");
	    	pieceSprites[0][1] = new ImageIcon(ImageIO.read(new File(dir + "knight.png")));
	    	pieceSprites[0][2] = new ImageIcon(ImageIO.read(new File(dir + "bishop.png")));
	    	pieceSprites[0][3] = new ImageIcon(ImageIO.read(new File(dir + "rook.png")));
	    	pieceSprites[0][4] = new ImageIcon(ImageIO.read(new File(dir + "queen.png")));
	    	pieceSprites[0][5] = new ImageIcon(ImageIO.read(new File(dir + "king.png")));
	    	dir =  ".\\resources\\images\\pieces\\black\\";
	    	pieceSprites[1][0] = new ImageIcon(ImageIO.read(new File(dir + "pawn.png")));
	    	pieceSprites[1][1] = new ImageIcon(ImageIO.read(new File(dir + "knight.png")));
	    	pieceSprites[1][2] = new ImageIcon(ImageIO.read(new File(dir + "bishop.png")));
	    	pieceSprites[1][3] = new ImageIcon(ImageIO.read(new File(dir + "rook.png")));
	    	pieceSprites[1][4] = new ImageIcon(ImageIO.read(new File(dir + "queen.png")));
	    	pieceSprites[1][5] = new ImageIcon(ImageIO.read(new File(dir + "king.png")));
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
        while(true&&kingsAreAlive){
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
				aiMoveCapture(1);
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
        		aiMoveCapture(2);
			}
			
        }
    }
    
    public void clickedOn(int i, int j){
    	System.out.println(i+","+j);
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
    			board[i][j] = board[lastI][lastJ];
    			board[lastI][lastJ]=0;
    		}
    		highlightMoves(new int[8][8]);
    		updatePieceDisplay();
    		
    		aiMoveCapture(2);
    	}
    }
    
    //To move white piece, side=1, to move black piece, side=2
    public void aiMoveRandom(int side){
    	boolean hasLegalMoves = false;
    	int initialr=0;
	    int initialc=0;
    	while(!hasLegalMoves){
	    	ArrayList<Integer> rList = new ArrayList<Integer>();
	    	ArrayList<Integer> cList = new ArrayList<Integer>();
	    	for(int i=0; i<8; i++){
	        	for(int j=0; j<8; j++){
	        		if(board[i][j]/10==side){
	        			rList.add(i);
	        			cList.add(j);
	        		}
	        	}
	    	}
	    	int index = (int)(Math.random()*rList.size());
	    	initialr=Integer.valueOf(rList.get(index));
	    	initialc=Integer.valueOf(cList.get(index));
	    	int[][] arr = legalMoves(initialr,initialc,board);
	    	for(int[] foo: arr){
	    		for(int bar:foo){
	    			if(!hasLegalMoves&&bar==1){
	    				hasLegalMoves=true;
	    			}
	    		}
	    	}
    	}
    	int[][] arr = legalMoves(initialr,initialc,board);
    	ArrayList<Integer> r = new ArrayList<Integer>();
    	ArrayList<Integer> c = new ArrayList<Integer>();
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		if(arr[i][j]!=0){
        			r.add(i);
        			c.add(j);
        		}
        	}
    	}
    	int index = (int)(Math.random()*r.size());
	    int finalr=Integer.valueOf(r.get(index));
	    int finalc=Integer.valueOf(c.get(index));
    	if(legalMoves(initialr,initialc,board)[finalr][finalc]!=0){
    		board[finalr][finalc] = board[initialr][initialc];
    		board[initialr][initialc] = 0;
    		updatePieceDisplay();
    	}
    	
    }
    public void aiMoveCapture(int side){
    	int maxInitialR=0;
    	int maxInitialC=0;
    	int maxFinalR=0;
    	int maxFinalC=0;
    	int maxCapture=-1;
    	for(int loopCounter = 0; loopCounter<loop; loopCounter++){
	    	boolean hasLegalMoves = false;
	    	int initialr=0;
		    int initialc=0;
	    	while(!hasLegalMoves){
		    	ArrayList<Integer> rList = new ArrayList<Integer>();
		    	ArrayList<Integer> cList = new ArrayList<Integer>();
		    	for(int i=0; i<8; i++){
		        	for(int j=0; j<8; j++){
		        		if(board[i][j]/10==side){
		        			rList.add(i);
		        			cList.add(j);
		        		}
		        	}
		    	}
		    	int index = (int)(Math.random()*rList.size());
		    	initialr=Integer.valueOf(rList.get(index));
		    	initialc=Integer.valueOf(cList.get(index));
		    	int[][] arr = legalMoves(initialr,initialc,board);
		    	for(int[] foo: arr){
		    		for(int bar:foo){
		    			if(!hasLegalMoves&&bar==1){
		    				hasLegalMoves=true;
		    			}
		    		}
		    	}
	    	}
	    	int[][] arr = legalMoves(initialr,initialc,board);
	    	ArrayList<Integer> r = new ArrayList<Integer>();
	    	ArrayList<Integer> c = new ArrayList<Integer>();
	    	for(int i=0; i<8; i++){
	        	for(int j=0; j<8; j++){
	        		if(arr[i][j]!=0&&board[i][j]!=0){
	        			r.add(i);
	        			c.add(j);
	        		}
	        	}
	    	}
	    	if(r.size()==0){
	    		for(int i=0; i<8; i++){
		        	for(int j=0; j<8; j++){
		        		if(arr[i][j]!=0){
		        			r.add(i);
		        			c.add(j);
		        		}
		        	}
		    	}
	    	}
	    	int index = (int)(Math.random()*r.size());
		    int finalr=Integer.valueOf(r.get(index));
		    int finalc=Integer.valueOf(c.get(index));
	    	if(legalMoves(initialr,initialc,board)[finalr][finalc]!=0){
	    		if(board[finalr][finalc]%10-aiMoveCapture(side, board)>maxCapture){
	    			maxFinalR=finalr;
	    			maxFinalC=finalc;
	    			maxInitialR=initialr;
	    			maxInitialC=initialc;
	    			maxCapture=board[finalr][finalc]%10-aiMoveCapture(side%2+1, board);
	    		}
	    	}	
    	}
    	board[maxFinalR][maxFinalC] = board[maxInitialR][maxInitialC];
	    board[maxInitialR][maxInitialC] = 0;
	    System.out.println("MOVE SCORE:" + maxCapture);
	    
	    updatePieceDisplay();
    }
    public int aiMoveCapture(int side, int[][] boardTemp){
    	int[][] boardIn = new int[boardTemp.length][boardTemp[0].length];
    	for(int i=0;i<8;i++){
    		for(int j=0;j<8;j++){
    			boardIn[i][j]=boardTemp[i][j];
    		}
    	}
    	int maxInitialR=0;
    	int maxInitialC=0;
    	int maxFinalR=0;
    	int maxFinalC=0;
    	int maxCapture=-9999;
    	for(int loopCounter = 0; loopCounter<loop; loopCounter++){
	    	boolean hasLegalMoves = false;
	    	int initialr=0;
		    int initialc=0;
	    	while(!hasLegalMoves){
		    	ArrayList<Integer> rList = new ArrayList<Integer>();
		    	ArrayList<Integer> cList = new ArrayList<Integer>();
		    	for(int i=0; i<8; i++){
		        	for(int j=0; j<8; j++){
		        		if(boardIn[i][j]/10==side){
		        			rList.add(i);
		        			cList.add(j);
		        		}
		        	}
		    	}
		    	int index = (int)(Math.random()*rList.size());
		    	initialr=Integer.valueOf(rList.get(index));
		    	initialc=Integer.valueOf(cList.get(index));
		    	int[][] arr = legalMoves(initialr,initialc,boardIn);
		    	for(int[] foo: arr){
		    		for(int bar:foo){
		    			if(!hasLegalMoves&&bar==1){
		    				hasLegalMoves=true;
		    			}
		    		}
		    	}
	    	}
	    	int[][] arr = legalMoves(initialr,initialc,boardIn);
	    	ArrayList<Integer> r = new ArrayList<Integer>();
	    	ArrayList<Integer> c = new ArrayList<Integer>();
	    	for(int i=0; i<8; i++){
	        	for(int j=0; j<8; j++){
	        		if(arr[i][j]!=0&&boardIn[i][j]!=0){
	        			r.add(i);
	        			c.add(j);
	        		}
	        	}
	    	}
	    	if(r.size()==0){
	    		for(int i=0; i<8; i++){
		        	for(int j=0; j<8; j++){
		        		if(arr[i][j]!=0){
		        			r.add(i);
		        			c.add(j);
		        		}
		        	}
		    	}
	    	}
	    	int index = (int)(Math.random()*r.size());
		    int finalr=Integer.valueOf(r.get(index));
		    int finalc=Integer.valueOf(c.get(index));
	    	if(legalMoves(initialr,initialc,boardIn)[finalr][finalc]!=0){
	    		if(boardIn[finalr][finalc]%10>maxCapture){
	    			maxFinalR=finalr;
	    			maxFinalC=finalc;
	    			maxInitialR=initialr;
	    			maxInitialC=initialc;
	    			maxCapture=boardIn[finalr][finalc]%10;
	    		}
	    	}	
    	}
    	boardIn[maxFinalR][maxFinalC] = boardIn[maxInitialR][maxInitialC];
	    boardIn[maxInitialR][maxInitialC] = 0;
	    //System.out.println("MOVE SCORE:" + maxCapture);
	    return maxCapture;
    }
    public int[][] legalMoves(int r, int c, int[][] inBoard){
    	int[][] out = new int[8][8];
    	int piece = inBoard[r][c]%10;
    	int sideLegal = inBoard[r][c]/10;
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		boolean isLegal=false;
        		if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
        		
        			//Pawn
        			if (piece==0){ 
        				if(inBoard[i][j]==0 && c==j && ((sideLegal==2&&r+1==i)||(sideLegal==1&&r-1==i))){ //pawn moves forward by one space
        					isLegal=true;
        				}
        				if(inBoard[i][j]==0 && c==j && ((r==1&&sideLegal==2&&r+2==i)||(r==6&&sideLegal==1&&r-2==i))){ //pawn moves forward by two spaces
        					isLegal=true;
        				}
        				if(inBoard[i][j]!=0 && r+sideLegal*2-3==i && (c==j+1||c==j-1)){ //pawn captures diagonally
        					isLegal=true;
        				}
        			}
        			
        			//Knight
        			if (piece==1){ 
        				if((i+1==r && j+2==c)||(i-1==r && j+2==c)||(i+1==r && j-2==c)||(i-1==r && j-2==c)||(i+2==r && j+1==c)||(i-2==r && j+1==c)||(i+2==r && j-1==c)||(i-2==r && j-1==c)){ //Knight's moves 
        					isLegal=true;
        				}
        			}
        			
        			
        			//King
        			if (piece==5){ 
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
    	if (piece==2){ 
			for(int i=r+1, j=c+1; i<8&&j<8; i++){
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
		if (piece==3){ 
			for(int i=r+1; i<8; i++){
				if (sideLegal!=inBoard[i][c]/10){ //Not occupied by your own pieces
					if(inBoard[i][c]==0){
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
				if (sideLegal!=inBoard[i][c]/10){ //Not occupied by your own pieces
					if(inBoard[i][c]==0){
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
				if (sideLegal!=inBoard[r][j]/10){ //Not occupied by your own pieces
					if(inBoard[r][j]==0){
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
				if (sideLegal!=inBoard[r][j]/10){ //Not occupied by your own pieces
					if(inBoard[r][j]==0){
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
		if (piece==4){ 
			for(int i=r+1; i<8; i++){
				if (sideLegal!=inBoard[i][c]/10){ //Not occupied by your own pieces
					if(inBoard[i][c]==0){
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
				if (sideLegal!=inBoard[i][c]/10){ //Not occupied by your own pieces
					if(inBoard[i][c]==0){
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
				if (sideLegal!=inBoard[r][j]/10){ //Not occupied by your own pieces
					if(inBoard[r][j]==0){
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
				if (sideLegal!=inBoard[r][j]/10){ //Not occupied by your own pieces
					if(inBoard[r][j]==0){
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
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
				if (sideLegal!=inBoard[i][j]/10){ //Not occupied by your own pieces
					if(inBoard[i][j]==0){
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
    public static void main(String[] args) {
        // TODO code application logic here
        Chess chess = new Chess();
        chess.pseudoMain();
        
    }
}
