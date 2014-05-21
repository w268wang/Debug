
package gomoku;


import javax.swing.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.awt.event.*; 
import java.io.FileOutputStream;
import java.io.PrintStream;


public class Wuziqi extends JPanel{
    protected Color boardColour;//colour of the board
    protected Color colourWhite = Color.WHITE; //Color.WHITE
    protected Color colourBlack = Color.BLACK; //Color.BLACK
    private int difficulty; //1~3, the diffuculty
    private boolean vsAI;// is vsAI?
    private int totalTurns;//the total turns
    private boolean goFirst;//player goes first
    private boolean curTurn; //true for black
    private int pColour;//player's colour (white(1) or black(0))
    private int aiColour;//ai's colour
    private boolean nexTurn;//next turn flag, true for black false for white
    private int searchDepth; //the depth the AI will search (difficulty*2)
    protected int x_max = 15, x_min = 0; //search boundary, not seem to work well
    protected int y_max = 15, y_min = 0;
    private final static int N = 15;//the size of board, did not use much of this
    private java.util.List<Piece> list = new ArrayList<Piece>();//not awt list, to store the steps taken in order
    private java.util.List<Piece> repBuffer = new ArrayList<Piece>();//replay/save buffer
    private int board[][]; //the board
    protected int delay;//delay time frome setting
    protected boolean musicOn;//if play music, no use
    protected boolean wait = false;//is waiting for AI flag
    protected boolean replay = false;//is replaying game flag
    private boolean gameOver = false;//is game over flag;
    InformationPanel info;//the information panel
    private int leftXY[] = new int[2];//the coordinate of the left most piece of the winning row
    private int rightXY[] = new int[2];//right most
    protected boolean reDraw = false;// not really used variable
    protected int style = 0;//style of the board
    private boolean tie=false;//is it a tie
    public Wuziqi(String fileName){ //not used

    }
    
    public Wuziqi(int hVsAI,int dRate, int goF, InformationPanel info){
	
        setLayout(new BorderLayout());
        this.info = info;
	//set all infos from the parameter to the field
        if (hVsAI==1)
            vsAI = true;
        else
            vsAI=false;
        if (goF==1){
            goFirst = true;
	    pColour = 0;
	    aiColour = 1;
	}
	else{
            goFirst = false;
	    pColour = 1;
	    aiColour = 0;
	}
        difficulty = dRate;
        searchDepth = dRate*2; //double the difficulty
	//searchDepth = 4;
        board = new int [N][N];
        clearBoard(); //clear the board/initialize
        curTurn = true;//balck go first
        nexTurn = false;//next turn is white
        readSetting();//read the setting frome config.ini
	if (style == 1)
	    colourBlack = new Color(80,10,50);
        setSize(500,500);
        setVisible(true);
	if (!goFirst){ //place black piece in the middle if AI goes first
	    placePiece(7,7,curTurn);
	    totalTurns = 1;
	}
        //setBackground(Color.DARK_GRAY);
    }
    
    protected void readSetting(){
        try{
            Scanner s = new Scanner(new FileInputStream("config.ini"));//read the setting from file
            s.next(); //ignore first string
	    //read the ints
            int R = s.nextInt();
            int G = s.nextInt();
            int B = s.nextInt();
            boardColour = new Color(R,G,B);//set boardColour to this
            s.next();
            int music = s.nextInt();
            if (music==1)
                musicOn = true;
            else
                musicOn = false;
            s.next();
            delay = s.nextInt(); //delay time
	    s.next();
	    style = s.nextInt();//grey style
        }
        catch(IOException | NoSuchElementException e){ //catch 2 exceptions at once
            System.out.println("Resetting setting");
	    //reset all setting if an exception caught
            boardColour = new Color(250,235,215);
            musicOn = true;
            delay = 1000;
	    style = 0;
        }
	reDraw = true;
	//musicSetter(musicOn);
    }
    
    public void clearBoard(){
        for(int i=0;i<=14;i++)   
            for(int j=0;j<=14;j++){ 
                board[i][j]=-1; //set board to -1 for open spaces
            }
        list = new ArrayList<Piece>(); //clear ArrayList by instantiating a new one
	//boundary value set to 0
	x_max = 15;x_min = 0;
	y_max = 15;y_min = 0;
        curTurn = true;//balck go first
        nexTurn = false;//next turn is white
	totalTurns = 0;
    }
    
    void placePiece(int x,int y,boolean black){ //no idea why I passed the third para
        totalTurns++;//add 1 turn
        int side = black?0:1; //if it is black then 0 else 1
        board[x][y]=side;//assign it to the board
        list.add(new Piece(x,y,black)); //add the piece to the list
	//int type =findType(x,y,side);
	//System.out.println("type:" +type);
        curTurn = nexTurn; //switch turn when a piece is placed
        nexTurn = !nexTurn; //next turn swith as well
        info.updateInfo(); //update the info in informationPanel
	if (totalTurns==1&&goFirst){ //set the search border, not working well
	    if(x-1>=0)
		x_min = x-1;
	    if(x-1<=15)
		x_max = x+1;
	    if(y-1>=0)
		y_min = y-1;
	    if(y-1<=15)
		y_max = y+1;
	}
	else{
	    resetMaxMin(x,y);
	}
	//revalidate();
	//validate();
	if (!wait&&difficulty>1&&totalTurns>1) //if player placign the piece
	    this.paint(this.getGraphics());//update the board (force paint)
	else
	    repaint();
	isWin(x,y);
    }
    
    public void retract(){
        if (totalTurns<1||(!goFirst&&totalTurns==1)) //if turns lesser than 1
            return;
        totalTurns--; //decrease turn by 1
        curTurn = nexTurn; //swap turn
        nexTurn = !nexTurn;
        board[list.get(list.size()-1).x][list.get(list.size()-1).y] = -1; //set the last position to -1
        list.remove(list.size()-1);//remove it from the list
        info.retractInfo();//update info
        repaint(); //repaint
	if (vsAI){ //if vsAI retract once more
	    totalTurns--;
	    curTurn = nexTurn;
	    nexTurn = !nexTurn;
	    board[list.get(list.size()-1).x][list.get(list.size()-1).y] = -1;
	    list.remove(list.size()-1);
	    info.retractInfo();
	    repaint();
	}
    }
    
    //----------getter methods---------------//
    public int getBoard(int a,int b){
	if (a<0||a>14||b<0||b>14)
	    return 3; //out of boundary, -1 for space 0 for black 1 for white
        return board[a][b];
    }
    public boolean isVsAI(){
        //String aied = vsAI?"ai":"no ai";
        //System.out.println(aied);
        return vsAI;
    }
    public boolean getCurTurn(){
        return curTurn;
    }
    
    public void setOver(){ //set gameover
        gameOver = true;
    }

    public boolean isOver(){ //return gameover
        return gameOver;
    }
    /*
    public void musicSetter(boolean t){ //set music
	musicOn = t;
	backGroundMusic.stop();
	if (musicOn)
	    backGroundMusic.loop();
    }
    */
    public void replayWriter(String fileName){ //write replay (actually save game uses the same function)
        try{
            PrintStream out = new PrintStream(new FileOutputStream(fileName));
	    String a = vsAI?"1":"0"; 
	    String b = goFirst?"1":"0";
	    out.print(a+" "+difficulty+" "+b+" ");//print vsAI difficulty and goFirst
            for (int i=0;i<list.size();i++){ //print the information of all pieces in the list
                Piece sp = list.get(i);
                out.print(sp.x+" ");
                out.print(sp.y+" ");
                out.print(sp.side+" ");
            }
        }
        catch(IOException exce){
            System.out.println("Replay save fail");
        }
    }
    
    public void saveGame(String fileName){ //save game only needs to call replayWriter once
	replayWriter(fileName);
    }
    
    private void readFile(String fileName){ //read information from a file

        info.setVisible(true);
	//reset some values
        totalTurns = 0; 
        gameOver = false;
        curTurn = true;//balck go first
        nexTurn = false;//next turn is white
        readSetting();
        setSize(500,500);
        setVisible(true);
        //setBackground(Color.DARK_GRAY);
        board = new int [N][N];
        clearBoard();
        repaint();
        java.util.ArrayList<Integer> repData = new java.util.ArrayList<Integer>(); //data stored here
	repBuffer = new ArrayList<Piece>(); //clear repBuffer
        try{
            Scanner s = new Scanner(new FileInputStream(fileName));//read the setting from file
            while(s.hasNextInt()) //add until eof
                repData.add(s.nextInt());
        }
        catch(IOException | InputMismatchException ee){ //catch exception
            System.out.println("File Reading failure. Error 1");
            setVisible(false); //close the panel
	    clearBoard();
        }
	if (repData.size()<3){ //files should be at least 3 int long
	    System.out.println("File Reading failure. Error 2");
            setVisible(false);
	    clearBoard();
	    return;
	}
	//read the data
	vsAI=repData.get(0)==1?true:false;
	difficulty = repData.get(1);
	goFirst = repData.get(2)==1?true:false;
	info.clear(vsAI?1:0,difficulty,goFirst?1:0);//clear info
        if (repData.get(2)==1){
            goFirst = true;
	    pColour = 0;
	    aiColour = 1;
	}
	else{
            goFirst = false;
	    pColour = 1;
	    aiColour = 0;
	}
	
        int index = 3;
        if (repData.size()%3==0){ //should be divisible by 3 (length check)
            while(index<repData.size()){ //read all data to the buffer
                int a = repData.get(index);
                int b = repData.get(++index);
                int c = repData.get(++index);
                index++;
                repBuffer.add(new Piece(a,b,c==0?true:false)); //add the information of the piece to the buffer
            }
        }
        else{
            System.out.println("File Reading failure.Error 3");
            setVisible(false);
	    return;
        }
    }
    
    public void doReplay(String fileName){ //replay 
	replay = true; //set replay to true, so the player cannot place pieces
	readFile(fileName);
	drawRepBuffer();
    }
    public void loadGame(String fileName){
	readFile(fileName);//read file first
	clearBoard();
	for(int i=0;i<repBuffer.size();i++){//than read the repBuffer and place the pieces
	    Piece cp = this.repBuffer.get(i);
	    placePiece(cp.x,cp.y,cp.side==0?true:false);
	}
	repaint();
    }
    
    private void drawRepBuffer(){
	for(int i=0;i<repBuffer.size();i++){
	    this.paint(this.getGraphics());//java forced me to do this, calling paint() directly
	    //repaint();
	    //info.paint(this.getGraphics());
	    //info.updateInfo();//do not why this does not draw
	    //bottomPanel.paint(bottomPanel.getGraphics());
	    Piece cp = this.repBuffer.get(i); //get the information
	    placePiece(cp.x,cp.y,cp.side==0?true:false);   //place it on board
	    try{
		    Thread.sleep (delay); //delay for delay seconds
	    }
	    catch (InterruptedException eee){
	    }
	}
    }
    
    public void paint(Graphics g){
        super.paint(g);
       // System.out.println("do print");
        Graphics2D g2d = (Graphics2D) g;
	paintBoard(g2d);//print board
        paintPiece(g2d);//print piece
	//info.paint(info.getGraphics());
        if (gameOver&&!tie) //draw line when win
            drawWinLine(g2d);
    }
    private void paintBoard(Graphics2D g){
        g.setColor(boardColour); //set colour
	if (style==1)
	    g.setColor(new Color(37,37,37));
        g.fillRect(14,14,406-14,406-14);
        g.setColor(Color.BLACK);
        for(int i=14;i<=406;i+=28){   //draw the lines
            g.drawLine(i,14,i,406);   
        }   
        for(int i=14;i<=406;i+=28){   
            g.drawLine(14,i,406,i); 
        }  
    }
    private void paintPiece(Graphics2D g){
        for (int i=0;i<list.size();i++){ 
            Color pc = colourBlack;//set the colour
	    if (style==1) //stlye 1's piece colour
		pc = new Color(80,10,50);
	    else
		pc = Color.BLACK;
            if (list.get(i).side==1)
                pc = colourWhite; 
            g.setColor(pc);
            g.fillOval(14+list.get(i).x*28-10,14+list.get(i).y*28-10,20,20);//draw the piece
	    g.setColor(Color.BLACK);
	    g.drawOval(14+list.get(i).x*28-10,14+list.get(i).y*28-10,20,20);//outline for white stone
	    if (i==list.size()-1){//draw 2 short lines on the corners of the last piece
		int origX = 14+list.get(i).x*28-10; //get xy of last piece
		int origY = 14+list.get(i).y*28-10;
		g.setColor(Color.RED); //the small red corner for the last piece
		g.drawLine(origX-2,origY-2,origX+3-2,origY-2);
		g.drawLine(origX-2,origY-2,origX-2,origY+3-2);
		g.drawLine(origX+20+2,origY-2,origX+20-3+2,origY-2);
		g.drawLine(origX+20+2,origY-2,origX+20+2,origY+3-2);
		g.drawLine(origX-2,origY+20+2,origX+3-2,origY+20+2);
		g.drawLine(origX-2,origY+20+2,origX-2,origY+20-3+2);
		g.drawLine(origX+20+2,origY+20+2,origX+20-3+2,origY+20+2);
		g.drawLine(origX+20+2,origY+20+2,origX+20+2,origY+20-3+2);
	    }
        }
    }
    private void drawWinLine(Graphics2D g){
        System.out.println("draw win line called");
        int x1p = leftXY[0]*28+14; //get the data from global field
        int y1p = leftXY[1]*28+14;
        int x2p = rightXY[0]*28+14;
        int y2p = rightXY[1]*28+14;
        System.out.println("x1 "+x1p+" y1 "+y1p+" x2 "+x2p+" y2 "+y2p);
        g.setColor(Color.RED);
        g.drawLine(x1p,y1p,x2p,y2p);
    }

    public boolean isWin(int x,int y){
	if (totalTurns==225){
	    JOptionPane.showMessageDialog(null,"Game is tie. White wins, since disadvantage of going second.");
	    gameOver = true;
	    tie = true;
	    return true;
	}
        int directionX[]={1,1,0,-1}; //xy dir, vertical,"\"diag, horizontal, "/" diag 
        int directionY[]={0,1,1,1};
        int p=board[x][y]; //0 for black 1 for white -1 for none
        int stoneLen=1; 
        int leftLimit[] = new int[2];
        int rightLimit[] = new int [2];
        for(int dir=0; dir<4; dir++){//all four directions
            stoneLen=1;
            int X, Y;
            for(int i=1; i<=4; i++){//count for four pieces, positive dir
                X=x+i*directionX[dir];
                Y=y+i*directionY[dir];
                if(X<0 || X>14 || Y<0  || Y>14)   
                    break;   
                if(board[X][Y]==p)   
                    stoneLen++;   
                else{ //not same colour stone, break
                    leftLimit[0] = X-directionX[dir];
                    leftLimit[1] = Y-directionY[dir];
                    break;
                }
                if (i==4){
                    leftLimit[0] = X;
                    leftLimit[1] = Y;
                }
            }   
            for(int i=1; i<=4; i++){ //negative dir
                X=x-i*directionX[dir];
                Y=y-i*directionY[dir];  
                if(X<0 || X>14 || Y<0  || Y>14)   
                    break;   
                if(board[X][Y]==p)   
                    stoneLen++;   
                else{ //not same colour stone, break
                    rightLimit[0] = X+directionX[dir];
                    rightLimit[1] = Y+directionY[dir];
                    break;
                }
                if (i==4){
                    rightLimit[0] = X;
                    rightLimit[1] = Y;
                }
            }   
            if(stoneLen >= 5){ //if 5 in a row
                rightXY[0]=rightLimit[0]; //record the left right position
                rightXY[1]=rightLimit[1];
                leftXY[0] = leftLimit[0];
                leftXY[1]=leftLimit[1];
                int pw = board[x][y]==0?1:2;
                gameOver = true; //set game over
                repaint();
		JOptionPane.showMessageDialog(null,"Player "+pw+" Won");//display information
                return true; 
            }
        }   
        return false;
    }
    protected void placePieceAI(){
	wait = true;
	Piece a = new Piece (1,1,1);
	/*
	info.goWait();
	info.repaint();
	*/ 
	if (difficulty==1){
	    a = placeFinder();
	    placePiece(a.x,a.y,curTurn);
	}
	else { //find 5 for ai first. If found exit immediately
	    for (int x=0;x<14;x++)
		for (int y=0;y<14;y++){
		    if (board[x][y]==-1){
			int t = findType(x,y,aiColour);
			if (t==1){
			    //System.out.println("here "+x+" "+y);
			    placePiece(x,y,curTurn);
			    wait = false;
			    return;
			}
		    }
		}
	        
	    if (totalTurns==2&&aiColour==0){ //ai go first, third step determined here
		/*
		if (board[6][6]==pColour||board[8][6]==pColour||board[6][8]==pColour||board[8][8]==pColour){
		    //if 2nd piece is on the diagonals
		    int [][]diagPos = new int[][] {{-2,-2},{-2,-1},{-2,0},{-2,1},{-2,2},{-1,-1},{-1,0},
						    {0,-2},{1,-2},{1,-1},{1,0},{2,-2},{2,0}}; 
		    //the possible positions from (7,7) to place at
		    //for case 0, where the player placed at (6,8)
		    int t=-1;
		    if (board[6][6]==pColour) //determine which case
			t=1;
		    else if(board[6][8]==pColour)
			t=0;
		    else if (board[8][6]==pColour)
			t=2;
		    else if(board[8][8]==pColour)
			t=3;
		    int coeX=1;//coefficients (-1 or 1)
		    int coeY=1;
		    switch(t){
			case 0:break;
			case 1:coeY=-1;break; //reflect Y-coord
			case 2:coeX=-1;coeY=-1;break;//reflect both
			case 3:coeX=-1;break;//reflect the x-coord
		    }
		    //System.out.println("case is "+t);
		    int cs = randomInt(0,12);
		    a = new Piece(7+coeX*diagPos[cs][0],7+coeY*diagPos[cs][1],curTurn);//7 is the middle piece
		}
		else if(board[7][8]==pColour||board[7][6]==pColour||board[6][7]==pColour||board[8][7]==pColour){
		    //if player placed on a vertical/horizontal position
		    int[][]vertPos = new int[19][2]; //19 cases
		    int idx = 0;//index
		    for (int i=-2;i<3;i++)//almost all position around the middle are good
			for (int j=-2;j<3;j++){
			    if ((i==-1&&j==0)||(i==1&&j==0)||(i==-1&&j==2)||(i==1)&&(j==2)||(i==0)&&(j==0)||(i==0)&&(j==1))
				continue; //except for these
			    vertPos[idx][0]=i;
			    vertPos[idx++][1]=j;
			}
		    int t=-1;
		    if (board[7][8]==pColour) //determine the case
			t=0;
		    else if (board[7][6]==pColour)
			t=1;
		    else if (board[6][7]==pColour)
			t=2;
		    else if (board[8][7]==pColour)
			t=3;
		    int cs = randomInt(0,18);
		    switch(t){
			case 0: a =new Piece(7+vertPos[cs][0],7+vertPos[cs][1],curTurn);break;
			case 1: a =new Piece(7+(-1)*vertPos[cs][0],7+(-1)*vertPos[cs][1],curTurn);break; //reflect both xy
			case 2: a =new Piece(7+(-1)*vertPos[cs][1],7+vertPos[cs][0],curTurn);break;//inverse and reflect x
			case 3: a =new Piece(7+vertPos[cs][1],7+(-1)*vertPos[cs][0],curTurn);break;//inverse and reflect y
		    }
		}*/
		if (board[6][6]==pColour||board[8][6]==pColour||board[6][8]==pColour||board[8][8]==pColour){ 
		    //gaining great advantage at the start, when white placing on diagonals
		    if (board[6][6]==pColour) //determine which case
			a=new Piece(6,8,curTurn);
		    else if(board[6][8]==pColour)
			a=new Piece(6,6,curTurn);
		    else if (board[8][6]==pColour)
			a=new Piece(8,8,curTurn);
		    else if(board[8][8]==pColour)
			a=new Piece(8,6,curTurn);
		}
		else if(board[7][8]==pColour||board[7][6]==pColour||board[6][7]==pColour||board[8][7]==pColour){
		    //when white is on horizontal/vertical places
		    if (board[7][8]==pColour) //determine the case
			a=new Piece(6,8,curTurn);
		    else if (board[7][6]==pColour)
			a=new Piece(6,6,curTurn);
		    else if (board[6][7]==pColour)
			a=new Piece(6,8,curTurn);
		    else if (board[8][7]==pColour)
			a=new Piece(8,6,curTurn);
		}
		else
		    a = placeFinderX((curTurn?0:1));
	    }
	    else if (totalTurns==1&&aiColour==1&&(difficulty==2||difficulty==3)){ //place piece near first piece
		wait = false;
		int tx,ty;
		tx = list.get(0).x;
		ty = list.get(0).y;
		int dX[]={1,1,-1,0}; //xy dir, vertical,"\"diag, horizontal, "/" diag 
		int dY[]={1,0,1,1};
		for (int i=0;i<=3;i++){
		    if (getBoard(tx+dX[i],ty+dY[i])!=3&&randomInt(1,5)<=2){ //randomly place the piece
			placePiece(tx+dX[i],ty+dY[i],curTurn);
			return;
		    }
		    else if (getBoard(tx-dX[i],ty-dY[i])!=3&&randomInt(1,6-i)<=2){
			placePiece(tx-dX[i],ty-dY[i],curTurn);
			return;
		    }
		}
		for (int i=1;i<=4;i++){ //if still not returned
		    if (getBoard(tx+dX[i],ty+dY[i])!=3){
			placePiece(tx+dX[i],ty+dY[i],curTurn);
			return;
		    }
		    else if (getBoard(tx-dX[i],ty-dY[i])!=3){
			placePiece(tx-dX[i],ty-dY[i],curTurn);
			return;
		    }
		}
	    }
	    else
		a = placeFinderX((curTurn?0:1));
	    placePiece(a.x,a.y,curTurn);
	}
	wait = false;
	/*
	wait = false;
	info.reWait();
	info.repaint();
	*/ 
    }
    //old algorithm 
    private Piece placeFinder(){
	int xyValue[][] = new int [15][15];
	for (int x=0;x<15;x++) 
	    for (int y=0;y<15;y++)
		xyValue[x][y] = 0;
	for (int x=0;x<15;x++) //loop through all intersections
	    for (int y=0;y<15;y++)
		if (board[x][y]==-1){
		    int type = findType(x,y,aiColour);
		    xyValue[x][y] = typeScore(type)*2;
		    type = findType(x,y,pColour);
		    int s = typeScore(type);
		    xyValue[x][y] +=s; //add the score up
		    board[x][y] = -1;
		}
	int max = 2;
	Piece sameValues[] = new Piece [255];//15*15=225
	int upper = -1;
	for (int x=0;x<15;x++) //find the max value, and store its xy
	    for (int y=0;y<15;y++){
		if (xyValue[x][y]>max){
		    max = xyValue[x][y];
		}
	    }
	for (int x=0;x<15;x++)
	    for (int y=0;y<15;y++){
		if (xyValue[x][y]==max){ //add the xy of same score to array
		    sameValues[++upper] = new Piece(x,y,true); //only to store xy, does not matter to be true or false
		}
	    }
	/*
	for (int x=0;x<15;x++){
	    for (int y=0;y<15;y++)
		System.out.print(xyValue[y][x]+" ");
	    System.out.print("\n");
	}
	*/
	Piece theOne = sameValues[randomInt(0,upper)]; //random pick one
	return theOne;
    }
    
    //********************min-max search + alpha-beta prune******************************//
    //for difficulty > 1
    private int findTypeX(int a,int b,int s){
	int t;
	t = findType(a,b,s);
	return t;
    }
    
    public void resetMaxMin(int x,int y){
	if(x-1>=0)
	    x_min = (x_min<x-1 ? x_min:x-1);
	if(x+1<=15)
	    x_max = (x_max>x+1 ? x_max:x+1);
	if(y-1>=0)
	    y_min = (y_min<y-1 ? y_min:y-1);
	if(y+1<=15)
	    y_max = (y_max>y+1 ? y_max:y+1);
    }
    
    private Piece placeFinderX(int bwf){ //main call to start the search
	int x, y, mx = -100000000;
	x = y = -1;
	int[][] bests = getBests(bwf); //find the best nodes
	for (int k = 0; k < bests.length; k++) {//iterate the nodes
	    int i = bests[k][0];
	    int j = bests[k][1];
	    if (findTypeX(i, j,aiColour) == 1) { //if able to get 5 for ai return
		//old findTypeX(i, j,aiColour) == 1
		x = i;
		y = j;
		break;
	    }
	    if (findTypeX(i, j,pColour) == 1) { //then check for human
		//old findTypeX(i, j,pColour) == 1
		x = i;
		y = j;
		break;
	    }
	    int temp1=x_min,temp2=x_max,temp3=y_min,temp4=y_max;
	    board[i][j] = bwf;
	    resetMaxMin(i,j);
	    int t = findMin(-100000000, 100000000, searchDepth);
	    board[i][j]=-1;
	    x_min=temp1;
	    x_max=temp2;
	    y_min=temp3;
	    y_max=temp4;
	    if (t - mx > 500||Math.abs(t - mx)<500 &&randomTest(3)){ //if difference in value is lesser tham 500
		x = i;
		y = j;
		mx = t;	    
	    }
	}
	return new Piece(x,y,aiColour);
    }
    private int evaluate(){ //evaluation of the current board
	int v=0;
  	int mt_c = 1, mt_m = 1; //coefficient
  	if((curTurn?0:1) == pColour) //if player's turn
  		mt_m = 2; //set to double
  	else
  		mt_c = 2;	
	int i_min=(x_min==0 ? x_min:x_min-1);//border values
	int j_min=(y_min==0 ? y_min:y_min-1);
	int i_max=(x_max==15 ? x_max:x_max+1);
	int j_max=(y_max==15 ? y_max:y_max+1);
	for (int x = i_min; x < i_max; x++) //only examine the pieces in the border
	    for (int y = j_min; y < j_max; y++)
		if (board[x][y]==-1){ //blank piece
                  int type = findTypeX(x, y, aiColour); //get the type for ai
		  /*
                  if(type == 1)  
                  	v += 30 * mt_c * getMark(type);
                  else if(type == 2)					
                  	v += 10 * mt_c * getMark(type);
                  else if(type == 3)
                  	v += 3 * mt_c * getMark(type);
                  else
                  	v += mt_c * getMark(type);
		 */ 
		  v+=mt_c*typeScore(type); //add evaluation score by coefficient*score of type
		  //player
                  type = findTypeX(x, y,pColour);
		  /*
                  if(type == 1)
                  	v -= 30 * mt_m * getMark(type);
                  else if(type == 2)					
                  	v -= 10 * mt_m * getMark(type);
                  else if(type == 3)
                  	v -= 3 * mt_m * getMark(type);
                  else
                  	v -= mt_m * getMark(type);
		    */
		  v-=mt_m*typeScore(type); //subtract for human player
		}
	//System.out.println(v);
	return v;
    }
    protected int findMax(int alpha, int beta, int step) { //the findMax function
	//followed the algorithm minimax alpha beta prune search
	int mx = alpha;
        // if(true) return 0;
        if (step == 0) {
            return evaluate();
        }
	int[][] rt = getBests(aiColour); //find the best nodes to append
        for (int z = 0; z < rt.length; z++){
	    int i = rt[z][0];
	    int j = rt[z][1];
	    if (findTypeX(i, j,aiColour) == 1) //if type 1 (five in a row) is found
      		return 20*(typeScore(1) + step*100);//return max score
	    board[i][j] = aiColour;
	    int temp1=x_min,temp2=x_max,temp3=y_min,temp4=y_max;
	    resetMaxMin(i,j);
	    int t = findMin(mx, beta, step - 1); //recursively call Min(which calls Max)
	    board[i][j] = -1; //recover to blank
	    //set boundaries
	    x_min=temp1;
	    x_max=temp2;
	    y_min=temp3;
	    y_max=temp4;
	    //beta prune
	    if (t > mx)
		mx = t;
	    if (mx >= beta) 
		return mx;
	}
        return mx;
    }
    protected int findMin(int alpha, int beta, int step) {
	int mi = beta;
        // if(true) return 0;
        if (step == 0) {
            return evaluate();
        }
	int[][] rt = getBests(pColour);
	for (int z=0;z<rt.length;z++) {
	    int i = rt[z][0];
	    int j = rt[z][1];
	    if (findTypeX(i, j,pColour) == 1) 
      		return -20*(typeScore(1) + step*100);
	    int temp1=x_min,temp2=x_max,temp3=y_min,temp4=y_max;
	    board[i][j] = pColour;
	    // System.out.println("error....");
	    resetMaxMin(i,j);
	    int t = findMax(alpha, mi, step - 1);
	    board[i][j] = -1;
	    x_min=temp1;
	    x_max=temp2;
	    y_min=temp3;
	    y_max=temp4;
	    if (t < mi)
		mi = t;
	    //beta
	    if (mi <= alpha) {
		return mi;
	    }
	}
        return mi;
    }  
    
    private int[][] getBests(int bwf) { //get the best pieces
	int i_min=(x_min==0 ? x_min:x_min-1);
	int j_min=(y_min==0 ? y_min:y_min-1);
	int i_max=(x_max==15 ? x_max:x_max+1);
	int j_max=(y_max==15 ? y_max:y_max+1);
	//x y max min boundary same not to work properly
	int n = 0; //index
	int type_1,type_2;
	int[][] rt = new int[(i_max-i_min) * (j_max-j_min)][3];
	//System.out.println("im "+i_min+" jm "+j_min+" im "+i_max+" jm "+j_max);
	for ( int i = i_min; i < i_max; i++) 
	    for (int j = j_min; j < j_max; j++)
		if (board[i][j] == -1) {
		    type_1 = findTypeX(i,j,bwf); //get type
		    //System.out.println("type 1: "+type_1);
		    type_2 = findTypeX(i,j,1-bwf);//get type
		    //System.out.println("type 2: "+type_2);
		    rt[n][0] = i;
		    rt[n][1] = j;
		    rt[n][2] = typeScore(type_1) + typeScore(type_2); //return its mark
		    n++;
		}
      Arrays.sort(rt, new ArrComparator()); //sort the 2Darray
      int size = 7 > n? n:7;
      int[][] bests = new int[size][3];
      System.arraycopy(rt, 0, bests, 0, size);
      return bests;
  }
 
    
    //*******************Methods used in all difficulties******************************//
    int findType(int x,int y,int s){
    /*
     * 1: 5 in a row
     * 2: open 4 || double half-close 4 || half-close 4 + open 3
     * 3: double open 3
     * 4: half-open 3 + open 3
     * 5: open 3
     * 6: half-open 4 //fix return 5
     * 7: double open 2
     * 8: half-open 3
     * 9: half-open 2 + open 2
     * 10: open 2
     * 11: half open 2
     * 12: other
     */
	int fives=0,fours=0,threes=0,twos=0; //open 
	int cfours=0,cthrees=0,ctwos=0;//half-close
	int[][] types = new int [4][2];//type array
	for (int i = 1;i<=4;i++){
	    //types[i-1]=pieceCounterX(x,y,i);//count the pieces this xy can form in four directions
	    //System.out.println("p: "+types[i-1][0]+" c: "+types[i-1][1]);
	    types[i-1]=pieceCounterX(x,y,i,s); //s for side
	}
	for (int i=0;i<4;i++){ //iterate through and record the number of open/close number of pieces
	    if (types[i][0]>=5)
		fives++;
	    else if (types[i][0] == 4 && types[i][1] == 2)
		fours++;
	    else if (types[i][0] == 4 && types[i][1] != 2)
		cfours++;
	    else if (types[i][0] == 3 && types[i][1] == 2)
		threes++;
	    else if (types[i][0] == 3 && types[i][1] != 2)
		cthrees++;
	    else if (types[i][0] == 2 && types[i][1] == 2)
		twos++;
	    else if (types[i][0] == 2 && types[i][1] != 2)
		ctwos++;
	}
	//System.out.println("threes: "+threes+" cthrees: "+cthrees+" twos: "+twos+" ctwos: "+ctwos);
	if (fives>0)
	    return 1;
	if (fours!=0 || cfours>=2 || (cfours!=0&&threes!=0))
	    return 2;
	if (threes>=2)
	    return 3;
	if (threes!=0&&cthrees!=0)
	    return 4;
	if (threes!=0)
	    return 6;
	if (cfours!=0)
	    return 5;
	if (twos>=2)
	    return 7;
	if (cthrees>0)
	    return 8;
	if (ctwos!=0&&twos!=0)
	    return 9;
	if (twos!=0)
	    return 10;
	if (ctwos!=0)
	    return 11;
	return 12;
    }
    
    private int typeScore(int c){ //give score according to the type
	int typeMark[]={100000,25000,8000,3000,2000,800,200,110,80,40,10,1};
	return typeMark[c-1];
    }
    
    private int[] pieceCounter(int x,int y,int direction){ //same algorithm as last year
	//but code more simple and easier to understand
	//flaw: can't find space
	int side = board[x][y];
	int counterSide = side==0?1:0; //the counter side of side
	int dx=0,dy=0;
	switch(direction){
	    //x y below should swap place
		case 1:dx=0;dy=1;break; //horizontal
		case 2:dx=1;dy=0;break;//vertical
		case 3:dx=1;dy=1;break;// diagonally "\"
		case 4:dx=-1;dy=1;break;//diagonally "/"
	}
	int lp = 0; //left piece
	int rp = 0; //right piece
	int leftClose = 0; //1 for left being blocked
	int rightClose = 0;
	int i; //index
	boolean space = false;
	for (i=1;i<5;i++){ //go left-dir 5 pieces
	    int nx=x,ny=y;
	    nx = x+dx*i;//find new coordinate of x
	    ny = y+dy*i;//y
	    if (nx>=0&&nx<=14&&ny>=0&&ny<=14){ //make sure array not out of index
		if (getBoard(nx,ny)==side) //same piece
		    lp++; //counter + 1
		else
		    break;//break if not
	    }
	    else
		break;//out of boundary, break
	}
	int fx = x+dx*i,fy=y+dy*i;//final x and final y
	if (getBoard(fx,fy)==3||getBoard(fx,fy)==counterSide) //check if it is closed
	    leftClose = 1;
	
	//other direction
	for (i=1;i<5;i++){
	    int nx=x,ny=y;
	    nx = x-dx*i;
	    ny = y-dy*i;
	    if (nx>=0&&nx<=14&&ny>=0&&ny<=14){
		if (getBoard(nx,ny)==side)
		    rp++;
		else
		    break;
	    }
	    else
		break;
	}
	fx=x-dx*i;
	fy=y-dy*i;
	if (getBoard(fx,fy)==3||getBoard(fx,fy)==counterSide)
	    rightClose = 1;
	int situation[] = new int[2];
	situation[0] = lp+rp+1;
	situation[1] = rightClose+leftClose;
	return situation;
    }
    
    protected int[] pieceCounterX(int x,int y,int direction,int s){ //new counter, more powerful than the one last year
	int side = s; //side = black or white (1 or 2)
	int counterSide = side==0?1:0; //the counter side of side
	int dx=0,dy=0;

	switch(direction){
	    //x y below should swap place
		case 1:dx=0;dy=1;break; //horizontal
		case 2:dx=1;dy=0;break;//vertical
		case 3:dx=-1;dy=1;break;// diagonally "\"
		case 4:dx=1;dy=1;break;//diagonally "/"
	}
	if( !preCheck(x, y, dx, dy, side))
          return new int[] {0, 1};
	int lp = 1; //left piece
	int rp = 1; //right piece
	int leftClose = 0; //0 for being blocked
	int rightClose = 0;
	int leftSpace=1;
	int rightSpace=1;
	boolean isSpaceLeft=false,isSpaceRight=false; //to see if there is a space
	int i; //index
	//boolean space = false;
	
	for (i=1;x+i*dx<15&&x+i*dx>=0&&y+i*dy<15&&y+i*dy>=0;i++){ //go left till reach end
	    int nx=x,ny=y;
	    nx = x+dx*i;//find new coordinate of x
	    ny = y+dy*i;//y
	    if (getBoard(nx,ny)==side) //same piece
		lp++; //counter + 1
	    else if (getBoard(nx,ny)==-1){ //a space
		if (!isSpaceLeft){  //isn't a space at +ve dir
		    isSpaceLeft = true; //set a space
		    leftSpace = i; //record its position
		}
		else
		    break;//break in other cases
	    }
	    else
		break;//break if its counterSide	    
	}
	int fx = x+dx*i,fy=y+dy*i;//final x and final y
	if (getBoard(fx,fy)!=3){
	    if (getBoard(fx,fy)==-1){ //check if it is closed
		    leftClose ++;
		   //System.out.println("la1");
		if (lp==leftSpace)
		    isSpaceLeft = false;
		if (isSpaceLeft&&lp>3&&leftSpace<4)
		    leftClose --;
	    }
	    else if (getBoard(fx,fy)!=side&&i>=2){
		if (getBoard(x+(i-1)*dx,y+(i-1)*dy)==-1){ //check last step
		    leftClose ++;
		    //System.out.println("la2");
		    isSpaceLeft = false;
		}
	    }
	    
	}
	else if (i>=2&&getBoard(x+(i-1)*dx,y+(i-1)*dy)==-1){
	    leftClose ++;
	    //System.out.println("la3");
	    isSpaceLeft = false;
	}
	//other direction
	for (i=1;x-i*dx<15&&x-i*dx>=0&&y-i*dy<15&&y-i*dy>=0;i++){
	    int nx=x,ny=y;
	    nx = x-dx*i;
	    ny = y-dy*i;
	    if (getBoard(nx,ny)==side)
		rp++;
	    else if (getBoard(nx,ny)==-1){
		if (!isSpaceRight){
		    isSpaceRight = true;
		    rightSpace = i;
		}
		else
		    break;
	    }
	    else
		break;//break if not	
	}
	fx=x-dx*i; //get final xy
	fy=y-dy*i;
	//do space check and adjustions
	if (getBoard(fx,fy)!=3){ //in boundary
	    if (getBoard(fx,fy)==-1){ //check if it is closed
		    rightClose ++;
		    //System.out.println("ra1");
		if (rp==rightSpace) //if same place as rightSpace (such as 1)
		    isSpaceRight = false; //set to no space
		if (isSpaceRight&&rp>3&&rightSpace<4) //make no sense
		    rightClose --; //closed
	     }
	    else if (getBoard(fx,fy)!=side&&i>=2){ 
		if (getBoard(x-(i-1)*dx,y-(i-1)*dy)==-1){ //check last step , (--) = +
		    rightClose ++;
		    //System.out.println("ra2");
		    isSpaceRight = false;
		}
	    }
	}
	else if (i>=2&&getBoard(x-(i-1)*dx,y-(i-1)*dy)==-1){ //check last step for closeness
	    rightClose ++;
	    //System.out.println("ra3");
	    isSpaceRight = false;
	}
	
	//analyse type
	/*
	System.out.println("leftSpace: "+leftSpace+" rightSpace: "+rightSpace);
	System.out.println("leftClose: "+leftClose+" rightClose: "+rightClose);
	System.out.println("hasSpaceL: "+isSpaceLeft+" hasSpaceR: "+isSpaceRight);
	System.out.println("left p: "+lp+" right p: "+rp);
	*/
	if (!isSpaceRight&&!isSpaceLeft){ //no space
	    return new int[] {lp+rp-1, rightClose+leftClose};//add together
	}
	else if (isSpaceRight&&isSpaceLeft){ //there is a space at left and right
	    int p = leftSpace + rightSpace -1; //the pieces that are between the spaces, 
	    //it is by 1 short, but the left/right themselves are also counted so -1 
	    if (p>=5)//already going to form 5
		return new int[] {5,2}; //return this situation as 5 in a row and open
	    if (p==4)//four in a row?
		return new int[] {4,2}; //definetly open 4, since two sides are open
	    if (lp+rightSpace-1>=4||rp+leftSpace-1>=4)  //left pieces + 1 in the middle + right pieces to the space(-1)
		return new int []{4,1}; //it is closed
	    if (lp+rightSpace-1==3&&leftClose>0||rp+leftSpace-1==3&&rightClose>0)//find 3 open
		return new int[]{3,2};
	    return new int[]{3,1};
	    //rest are 3 close, since spaces are only counted when pieceInRow left/right > 1 
	} 
	else{ //only one side has space
	    if (lp+rp-1<5) //add together smaller than 5
		return new int[] {lp+rp-1,leftClose+rightClose};
	    else{ //actually >= 5
		if(isSpaceLeft && rp + leftSpace-1 >= 5) //return directly if there is a space
			return new int[] {rp + leftSpace-1, rightClose+1};
		if(isSpaceRight && lp + rightSpace-1 >= 5) 
			return new int[] {lp + rightSpace, leftClose+1};
		if(isSpaceLeft && (rp + leftSpace-1 == 4 && rightClose == 0 || leftSpace == 4) )
			return new int[] {4, 2}; //open 4
		if(isSpaceRight && (lp + rightSpace-1 == 4 && leftClose == 0 || rightSpace == 4) )
			return new int[] {4, 2};//open 4
		return new int[] {4, 1};//all other cases means closed 4
	    }	    
	}
	
    }
    
    //preCheck to see if placing the piece at xy can make anything happen
    private boolean preCheck(int x,int y, int ex,int ey, int bwf){ 
	
      int rt = 1; //total pieces
      for (int i=1; x+i*ex<15&&x+i*ex>=0&&y+i*ey<15&&y+i*ey>=0&&rt<5;i++)
          if (getBoard(x+i*ex,y+i*ey)!=1-bwf)
              rt++;
          else
              break;
      for (int i=1;x-i*ex>=0&&x-i*ex<15&&y-i*ey>=0&&y-i*ey<15&&rt<5;i++)
          if (getBoard(x-i*ex,y-i*ey)!= 1 - bwf)
              rt++;
          else
              break;
      return (rt >= 5); //true for possible 5
  }
     
    
    private int randomInt(int Min,int Max){ //the random function
	return Min + (int)(Math.random() * ((Max - Min) + 1));
    }
    private boolean randomTest(int kt) { //random test
	Random rm = new Random();
	return rm.nextInt() % kt == 0;
    }    

    
}
