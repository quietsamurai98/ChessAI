/**
 * @(#)ChessAI.java
 *
 *
 * @author 
 * @version 1.00 2016/11/2
 */

import java.util.*;
public class ChessAI {

    public ChessAI() {
    }
    public int[] aiMiniMax(int[][] PARAMETER_ARRAY, int side, int searchDepth){
    	int[][] arr=copyArr(PARAMETER_ARRAY);
    	if (testGameOver(arr)||searchDepth==0){
    		int[] out={-1,-1,-1,-1,getScore(arr)};
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
        						if(kingChecked(recurArr, side)){
        							if(moves.size()==0){
        								moves.clear();
        							} else{
        								moves.remove(moves.size()-1);
        							}
        						} else if(side==1){
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
    	if(side==1){
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
    private int getMoveScore(int[] move, int[][] PARAMETER_ARRAY){
    	int[][] arr = copyArr(PARAMETER_ARRAY);
    	makeMove(move, arr);
    	return getScore(arr);
    }
    private int getScore(int[][] PARAMETER_ARRAY){
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
    			} else if(inBoard[r][c]%10==6){
    				kingR=r;
    				kingC=c;
    			}
    		}
    	}
    	return multiplyArrayElements(inBoard, captureBoard, "")[kingR][kingC]!=0;
    	
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
    private void makeMove(int[] moveArr, int[][] boardArr){
    	boardArr[moveArr[2]][moveArr[3]] = boardArr[moveArr[0]][moveArr[1]];
    	boardArr[moveArr[0]][moveArr[1]]=0;
    	if(boardArr[moveArr[2]][moveArr[3]]==11&&moveArr[2]==0){
    		boardArr[moveArr[2]][moveArr[3]]=15;
    	}else if(boardArr[moveArr[2]][moveArr[3]]==21&&moveArr[2]==7){
    		boardArr[moveArr[2]][moveArr[3]]=25;
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
    			if(bar%10==6){
    				kingCount++;
    			}
    		}
    	}
    	return kingCount!=2;
    }
}