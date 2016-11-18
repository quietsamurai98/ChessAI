/**
 * @(#)ChessAI.java
 *
 *
 * @author 
 * @version 1.00 2016/11/2
 */

import java.util.*;
public class ChessAI {
	private boolean ENPASSANT_ENABLED = false;
	private boolean CASTLING_ENABLED = false;
	private int[][] pawnArr={{0,  0,  0,  0,  0,  0,  0,  0},
					    	{50, 50, 50, 50, 50, 50, 50, 50},
					    	{10, 10, 20, 30, 30, 20, 10, 10},
					    	{ 5,  5, 10, 25, 25, 10,  5,  5},
					    	{ 0,  0,  0, 20, 20,  0,  0,  0},
					    	{ 5, -5,-10,  0,  0,-10, -5,  5},
					    	{ 5, 10, 10,-20,-20, 10, 10,  5},
					    	{ 0,  0,  0,  0,  0,  0,  0,  0}};
	private int[][] kngtArr={{-50,-40,-30,-30,-30,-30,-40,-50},
							 {-40,-20,  0,  0,  0,  0,-20,-40},
							 {-30,  0, 10, 15, 15, 10,  0,-30},
							 {-30,  5, 15, 20, 20, 15,  5,-30},
							 {-30,  0, 15, 20, 20, 15,  0,-30},
							 {-30,  5, 10, 15, 15, 10,  5,-30},
							 {-40,-20,  0,  5,  5,  0,-20,-40},
							 {-50,-40,-30,-30,-30,-30,-40,-50}};
	private int[][] bshpArr={{-20,-10,-10,-10,-10,-10,-10,-20},
							 {-10,  0,  0,  0,  0,  0,  0,-10},
							 {-10,  0,  5, 10, 10,  5,  0,-10},
							 {-10,  5,  5, 10, 10,  5,  5,-10},
							 {-10,  0, 10, 10, 10, 10,  0,-10},
							 {-10, 10, 10, 10, 10, 10, 10,-10},
						 	 {-10,  5,  0,  0,  0,  0,  5,-10},
							 {-20,-10,-10,-10,-10,-10,-10,-20}};
	private int[][] rookArr={{0,  0,  0,  0,  0,  0,  0,  0},
							{ 5, 10, 10, 10, 10, 10, 10,  5},
							{-5,  0,  0,  0,  0,  0,  0, -5},
							{-5,  0,  0,  0,  0,  0,  0, -5},
							{-5,  0,  0,  0,  0,  0,  0, -5},
							{-5,  0,  0,  0,  0,  0,  0, -5},
							{-5,  0,  0,  0,  0,  0,  0, -5},
							{ 0,  0,  0,  5,  5,  0,  0,  0}};
	private int[][] qwnArr={{-20,-10,-10, -5, -5,-10,-10,-20},
							{-10,  0,  0,  0,  0,  0,  0,-10},
							{-10,  0,  5,  5,  5,  5,  0,-10},
							{ -5,  0,  5,  5,  5,  5,  0, -5},
							{  0,  0,  5,  5,  5,  5,  0, -5},
							{-10,  5,  5,  5,  5,  5,  0,-10},
							{-10,  0,  5,  0,  0,  0,  0,-10},
							{-20,-10,-10, -5, -5,-10,-10,-20}};

    public ChessAI() {
    }
    public int[] aiMiniMax(int[][] PARAMETER_ARRAY, int trueSide, int side, int searchDepth){
    	int[][] arr=copyArr(PARAMETER_ARRAY);
    	if (testGameOver(arr)||searchDepth==0){
    		int[] out={-1,-1,-1,-1,getScore(arr,side)};
    		return out;
    	}
    	ArrayList<Integer> scores = new ArrayList<Integer>();
    	ArrayList<int[]> moves = new ArrayList<int[]>();
    	
    	int[][] myPieces = new int[arr.length][arr[0].length];
    	for(int i=0; i<arr.length; i++){
        	for (int j=0; j<arr[0].length; j++){
        		if(arr[i][j]/(side*10)==1){
        			int[][] legalMoveBoard=legalMoves(i,j,arr,"");
        			for(int a=0; a<arr.length; a++){
        				for(int b=0; b<arr[0].length; b++){
        					if(legalMoveBoard[a][b]==1){
        						int[] moveItem = {i,j,a,b};
        						moves.add(moveItem);
        						int[][] recurArr = copyArr(arr);
        						makeMove(moveItem,recurArr);
						    	scores.add(aiMiniMax(recurArr,trueSide,side%2+1,searchDepth-1)[4]);
			        		}
        				}
        			}
        		}
        	}
        }
        for(int[] possibleMove:moves){
        	scores.add(getMoveScore(possibleMove, arr, side));
        }
        
        ArrayList<Integer> order = new ArrayList<Integer>();
    	for(int i=0;i<moves.size();i++){
    		order.add(i);
    	}
    	Collections.shuffle(order);
    	if(side==trueSide){
			int index = 0;
			int maxScore=Integer.MIN_VALUE;
			for(int i:order){
				if(scores.get(i)>maxScore){
					maxScore=scores.get(i);
					index=i;
				}
			}
			if (moves.size()==0){
				if(kingChecked(arr, side)){
					int[] outArr = {-1,-1,-1,-1,Integer.MIN_VALUE};
					return outArr;
				} else {
					int[] outArr = {-2,-2,-2,-2,Integer.MIN_VALUE};
					return outArr;
				}
				
			} else {
				int[] outArr = {moves.get(index)[0],moves.get(index)[1],moves.get(index)[2],moves.get(index)[3],maxScore};
				return outArr;
			}
		} else {
			int index = 0;
			int minScore=Integer.MAX_VALUE;
			for(int i:order){
				if(scores.get(i)<minScore){
					minScore=scores.get(i);
					index=i;
				}
			}
			if (moves.size()==0){
				if(kingChecked(arr, side)){
					int[] outArr = {-1,-1,-1,-1,Integer.MAX_VALUE};
					return outArr;
				} else {
					int[] outArr = {-2,-2,-2,-2,Integer.MAX_VALUE};
					return outArr;
				}
			} else {
				int[] outArr = {moves.get(index)[0],moves.get(index)[1],moves.get(index)[2],moves.get(index)[3],minScore};
				return outArr;
			}
				
			
		}
    }
    private int[][] copyArr(int[][] arrIn){
    	int[][] arrOut= new int[arrIn.length][arrIn[0].length];
    	for(int i=0; i<arrIn.length; i++){
        	for (int j=0; j<arrIn[0].length; j++){
        		arrOut[i][j]=arrIn[i][j];
        	}
        }
    	return arrOut;
    }
    private int getMoveScore(int[] move, int[][] PARAMETER_ARRAY, int side){
    	int[][] arr = copyArr(PARAMETER_ARRAY);
    	makeMove(move, arr);
    	return getScore(arr,side);
    }
    private int getScore(int[][] PARAMETER_ARRAY, int side){
    	int [][] arr = copyArr(PARAMETER_ARRAY);
        int fitness=0;
        for(int r=0;r<7;r++){
        	for(int c=0;c<7;c++){
        		int piece = arr[r][c]%10;
        		if(arr[r][c]/10==side){
        			if (piece==1||piece==7){
        				fitness+=1000;
        				fitness+=pawnArr[r][c];
        			} else
        			if (piece==2){
        				fitness+=3500;
        				fitness+=kngtArr[r][c];
        			} else
        			if (piece==3){
        				fitness+=3500;
        				fitness+=bshpArr[r][c];
        			} else
        			if (piece==4||piece==8){
        				fitness+=5250;
        				fitness+=rookArr[r][c];
        			} else
        			if (piece==5){
        				fitness+=10000;
        				fitness+=qwnArr[r][c];
        			}
        			if (piece==6||piece==9){
        				fitness+=10000000;
        			}
        				
        		}else{
        			if (piece==1||piece==7){
        				fitness-=1000;
        				fitness-=pawnArr[7-r][c];
        			} else
        			if (piece==2){
        				fitness-=3500;
        				fitness-=kngtArr[7-r][c];
        			} else
        			if (piece==3){
        				fitness-=3500;
        				fitness-=bshpArr[7-r][c];
        			} else
        			if (piece==4||piece==8){
        				fitness-=5250;
        				fitness-=rookArr[7-r][c];
        			} else
        			if (piece==5){
        				fitness-=10000;
        				fitness-=qwnArr[7-r][c];
        			}
        			if (piece==6||piece==9){
        				fitness-=10000000;
        			}
        		}
        	}
        }
//        if (side==2){
//        	fitness*=1;
//        }
        return fitness;
    }
    private boolean kingChecked(int[][] PARAMETER_ARRAY, int side){
    	int[][] inBoard=copyArr(PARAMETER_ARRAY);
    	int[][] captureBoard = new int[8][8];
    	int otherSide = side%2+1;
    	int kingR=-1;
    	int kingC=-1;
    	for(int r=0; r<8; r++){
    		for(int c=0; c<8; c++){
    			if(inBoard[r][c]/10==otherSide){
    				addArrayElements(captureBoard,legalMoves(r,c,inBoard),"");
    			} else if(inBoard[r][c]%10==6||inBoard[r][c]%10==9){
    				kingR=r;
    				kingC=c;
    			}
    		}
    	}
    	try{
    		return multiplyArrayElements(inBoard, captureBoard, "")[kingR][kingC]!=0;
    	} catch (java.lang.ArrayIndexOutOfBoundsException e){
    		//e.printStackTrace();
    		for(int r=0; r<8; r++){
	    		for(int c=0; c<8; c++){
	    			if(inBoard[r][c]==0){
	    				System.out.print("0");
	    			}
	    			System.out.print(inBoard[r][c]+",");
	    		}
	    		System.out.println();
	    	}
	    	System.out.println();
	    	for(int r=0; r<8; r++){
	    		for(int c=0; c<8; c++){
	    			if(PARAMETER_ARRAY[r][c]==0){
	    				System.out.print("0");
	    			}
	    			System.out.print(PARAMETER_ARRAY[r][c]+",");
	    		}
	    		System.out.println();
	    	}
	    	int fail = inBoard[-1][-1];
	    	return true;
    	}
    	
    	
    }
    private int[][] legalMoves(int r, int c, int[][] tempArr){
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
        			if (piece==1||piece==7){ 
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
        			if (piece==6||piece==9){ 
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
		if (piece==4||piece==8){ 
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
		//EN PASSANT
		if(ENPASSANT_ENABLED&&piece==1){
			if(side==1&&r==3){
				if (c>0&&inArr[3][c-1]==27&&out[2][c-1]==0){
					out[2][c-1]=1;
				}
				if (c<7&&inArr[3][c+1]==27&&out[2][c+1]==0){
					out[2][c+1]=1;
				}
			}
			if(side==2&&r==4){
				if (c>0&&inArr[4][c-1]==17&&out[5][c-1]==0){
					out[5][c-1]=1;
				}
				if (c<7&&inArr[4][c+1]==17&&out[5][c+1]==0){
					out[5][c+1]=1;
				}
			}
		}
    	return out;
    }
    private int[][] legalMoves(int r, int c, int[][] tempArr, String CheckForCheck){
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
        			if (piece==1||piece==7){ 
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
        			if (piece==6||piece==9){ 
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
		if (piece==4||piece==8){ 
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
		if(CASTLING_ENABLED){
			//WHITE CASTLE KINGSIDE
			if(inArr[r][c]==19&&inArr[7][5]==0&&inArr[7][6]==0&&inArr[7][7]==18){ 
				int[][] tempArrMove = copyArr(inArr);
				if (!kingChecked(tempArrMove,1)){
					makeMove(7,4,7,5,tempArrMove);
					if (!kingChecked(tempArrMove,1)){
						int[][] tempArrMove2 = copyArr(inArr);
						makeMove(7,4,7,6,tempArrMove2);
						if (!kingChecked(tempArrMove2,1)){
							out[7][6]=2;
						}
					}
				}
			}
			
			//BLACK CASTLE KINGSIDE
			if(inArr[r][c]==29&&inArr[0][5]==0&&inArr[0][6]==0&&inArr[0][7]==28){
				int[][] tempArrMove = copyArr(inArr);
				if (!kingChecked(tempArrMove,2)){
					makeMove(0,4,0,5,tempArrMove);
					if (!kingChecked(tempArrMove,2)){
						int[][] tempArrMove2 = copyArr(inArr);
						makeMove(0,4,0,6,tempArrMove2);
						if (!kingChecked(tempArrMove2,2)){
							out[0][6]=3;
						}
					}
				}
			}
			
			//WHITE CASTLE QUEENSIDE
			if(inArr[r][c]==19&&inArr[7][3]==0&&inArr[7][2]==0&&inArr[7][1]==0&&inArr[7][0]==18){
				int[][] tempArrMove = copyArr(inArr);
				if (!kingChecked(tempArrMove,1)){
					makeMove(7,4,7,3,tempArrMove);
					if (!kingChecked(tempArrMove,1)){
						int[][] tempArrMove2 = copyArr(inArr);
						makeMove(7,4,7,2,tempArrMove2);
						if (!kingChecked(tempArrMove2,1)){
							int[][] tempArrMove3 = copyArr(inArr);
							makeMove(7,4,7,1,tempArrMove2);
							if (!kingChecked(tempArrMove2,1)){
								out[7][2]=4;
							}
						}
					}
				}
			}
			
			//BLACK CASTLE QUEENSIDE
			if(inArr[r][c]==29&&inArr[0][3]==0&&inArr[0][2]==0&&inArr[0][1]==0&&inArr[0][0]==28){
				int[][] tempArrMove = copyArr(inArr);
				if (!kingChecked(tempArrMove,2)){
					makeMove(0,4,0,3,tempArrMove);
					if (!kingChecked(tempArrMove,2)){
						int[][] tempArrMove2 = copyArr(inArr);
						makeMove(0,4,0,2,tempArrMove2);
						if (!kingChecked(tempArrMove2,2)){
							int[][] tempArrMove3 = copyArr(inArr);
							makeMove(0,4,0,1,tempArrMove2);
							if (!kingChecked(tempArrMove2,2)){
								out[0][2]=5;
							}
						}
					}
				}
			}
    	}
		//EN PASSANT
		if(ENPASSANT_ENABLED&&piece==1){
			if(side==1&&r==3){
				if (c>0&&inArr[3][c-1]==27&&out[2][c-1]==0){
					out[2][c-1]=1;
				}
				if (c<7&&inArr[3][c+1]==27&&out[2][c+1]==0){
					out[2][c+1]=1;
				}
			}
			if(side==2&&r==4){
				if (c>0&&inArr[4][c-1]==17&&out[5][c-1]==0){
					out[5][c-1]=1;
				}
				if (c<7&&inArr[4][c+1]==17&&out[5][c+1]==0){
					out[5][c+1]=1;
				}
			}
		}
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		if(out[i][j]==1){
        			int[][] tempArrMove = copyArr(inArr);
        			makeMove(r,c,i,j,tempArrMove);
        			if (kingChecked(tempArrMove,(inArr[r][c]/10))){
        				out[i][j]=0;
        			}
        		}
        	}
    	}
    	return out;
    }
    private void makeMove(int[] moveArr, int[][] boardArr){
    	boardArr[moveArr[2]][moveArr[3]] = boardArr[moveArr[0]][moveArr[1]];
    	boardArr[moveArr[0]][moveArr[1]]=0;
    	if(boardArr[moveArr[2]][moveArr[3]]==11&&moveArr[2]==0){
    		boardArr[moveArr[2]][moveArr[3]]=15;
    	}else if(boardArr[moveArr[2]][moveArr[3]]==21&&moveArr[2]==7){
    		boardArr[moveArr[2]][moveArr[3]]=25;
    	}
    	if(boardArr[moveArr[2]][moveArr[3]]==19){
    		boardArr[moveArr[2]][moveArr[3]]=16;
    		if(moveArr[3]==6){
    			boardArr[7][5]=14;
    			boardArr[7][7]=0;
    		} else if(moveArr[3]==2){
    			boardArr[7][3]=14;
    			boardArr[7][0]=0;
    		}
    	}else if(boardArr[moveArr[2]][moveArr[3]]==29){
    		boardArr[moveArr[2]][moveArr[3]]=26;
    		if(moveArr[3]==6){
    			boardArr[0][5]=24;
    			boardArr[0][7]=0;
    		} else if(moveArr[3]==2){
    			boardArr[0][3]=24;
    			boardArr[0][0]=0;
    		}
    	}
    	if(boardArr[moveArr[2]][moveArr[3]]==18){
    		boardArr[moveArr[2]][moveArr[3]]=14;
    	}else if(boardArr[moveArr[2]][moveArr[3]]==28){
    		boardArr[moveArr[2]][moveArr[3]]=24;
    	}
    	int side=boardArr[moveArr[2]][moveArr[3]]/10;
    	for(int a=0; a<8; a++){
    		for(int b=0; b<8; b++){
    			if(boardArr[a][b]/10==(side%2+1)&&boardArr[a][b]%10==7){
    				boardArr[a][b]=(side%2+1)*10+1;
    			}
    		}
    	}
    	
    	if(boardArr[moveArr[2]][moveArr[3]]==11&&moveArr[0]==6&&moveArr[2]==4){
    		boardArr[moveArr[2]][moveArr[3]]=17;
    	} else if(boardArr[moveArr[2]][moveArr[3]]==21&&moveArr[0]==1&&moveArr[2]==5){
    		boardArr[moveArr[2]][moveArr[3]]=17;
    	}
    	if(boardArr[moveArr[2]][moveArr[3]]==11&&moveArr[1]-moveArr[3]!=0&&boardArr[moveArr[2]+1][moveArr[3]]%10==7){
    		boardArr[moveArr[2]+1][moveArr[3]]=0;
    	}
    	if(boardArr[moveArr[2]][moveArr[3]]==21&&moveArr[1]-moveArr[3]!=0&&boardArr[moveArr[2]-1][moveArr[3]]%10==7){
    		boardArr[moveArr[2]-1][moveArr[3]]=0;
    	}
    }
    private void makeMove(int i1 ,int j1 ,int i2 ,int j2 , int[][] boardArr){
    	boardArr[i2][j2] = boardArr[i1][j1];
    	boardArr[i1][j1] = 0;
    	if(boardArr[i2][j2]==11&&i2==0){
    		boardArr[i2][j2]=15;
    	}else if(boardArr[i2][j2]==21&&i2==7){
    		boardArr[i2][j2]=25;
    	}
    	if(boardArr[i2][j2]==19){
    		boardArr[i2][j2]=16;
    		if(j2==6){
    			boardArr[7][5]=14;
    			boardArr[7][7]=0;
    		} else if(j2==2){
    			boardArr[7][3]=14;
    			boardArr[7][0]=0;
    		}
    	}else if(boardArr[i2][j2]==29){
    		boardArr[i2][j2]=26;
    		if(j2==6){
    			boardArr[0][5]=24;
    			boardArr[0][7]=0;
    		} else if(j2==2){
    			boardArr[0][3]=24;
    			boardArr[0][0]=0;
    		}
    	}
    	if(boardArr[i2][j2]==18){
    		boardArr[i2][j2]=14;
    	}else if(boardArr[i2][j2]==28){
    		boardArr[i2][j2]=24;
    	}
    	int side=boardArr[i2][j2]/10;
    	for(int a=0; a<8; a++){
    		for(int b=0; b<8; b++){
    			if(boardArr[a][b]/10==(side%2+1)&&boardArr[a][b]%10==7){
    				boardArr[a][b]=(side%2+1)*10+1;
    			}
    		}
    	}
    	
    	if(boardArr[i2][j2]==11&&i1==6&&i2==4){
    		boardArr[i2][j2]=17;
    	} else if(boardArr[i2][j2]==21&&i1==1&&i2==5){
    		boardArr[i2][j2]=17;
    	}
    	if(boardArr[i2][j2]==11&&j1-j2!=0&&boardArr[i2+1][j2]%10==1){
    		boardArr[i2+1][j2]=0;
    	}
    	if(boardArr[i2][j2]==21&&j1-j2!=0&&boardArr[i2-1][j2]%10==1){
    		boardArr[i2-1][j2]=0;
    	}
    }
    private int[][] multiplyArrayElements(int[][] foo, int[][] bar, String bat){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		foo[i][j]=foo[i][j]*bar[i][j];
        	}
    	}
    	return foo;
    }
    private int[][] addArrayElements(int[][] foo, int[][] bar, String bat){
    	for(int i=0; i<8; i++){
        	for(int j=0; j<8; j++){
        		foo[i][j]=foo[i][j]+bar[i][j];
        	}
    	}
    	return foo;
    }
    private boolean testGameOver(int[][] PARAMETER_ARRAY){
    	int kingCount=0;
    	for(int[] foo:PARAMETER_ARRAY){
    		for(int bar:foo){
    			if(bar%10==6||bar%10==9){
    				kingCount++;
    			}
    		}
    	}
    	return kingCount!=2;
    }
}