/*
 ************************************************************************
 *                          Program Header                              *
 ************************************************************************
 * PROGRAMMER'S NAME:    Felix Tian                                     *
 * DATE:                 Tuesday June 12, 2012                          *
 * PROGRAM NAME:         Gomoku++                                       *
 * CLASS:                ICS4U1                                         *
 * TEACHER:              Mrs.Barsan                                     *
 * DUE DATE:             Monday, June 18, 2012                          *
 ************************************************************************
 * WHAT THE PROGRAM DOES                                                *
 * The program is a simulation of the game Gomoku. Gomoku is an abstract*
 * strategy board game.Players alternate in placing a stone of their	*
 * color on an empty point. the first player to get an unbroken row of	*
 * five stones horizontally, vertically, or diagonally wins the game.	*
 * This program extends on last semster's project the "Gomoku". Many	*
 * features are added in this program, and mainly the AI is much	    *
 * stronger than the last "Gomoku".					                    *
 ************************************************************************
 * CLASSES								                                *
 * ArrComparator: Comparator that compares an array's 2nd element	    *
 *	Fields:								                                *
 *	    int column;							                            *
 *	    int sortOrder;						                            *
 *	Methods:							                                *
 *	    public int compare(Object a, Object b)			                *
 *									                                    *
 * BackTrackDialog: used for debugging					                *
 *	Fields:								                                *
 *	    int x; int y;													*
 *	    boolean ok = false;												*
 *	    JButton okButton;												*
 *	    JButton cancelButton;											*
 *	    IntegerTextField xv;											*
 *	    IntegerTextField yv;											*
 *	Methods:															*
 *	    public BackTrackDialog(Frame owner, String title)		*
 *	    public void actionPerformed(ActionEvent e)			*
 *									*
 * IntegerTextField: the text field that only accepts +ve integers	*
 *	Fields:								*
 *	    static String unAcceptableChar				*
 *	Methods:							*
 *	    public IntegerTextField(int a)				*
 *	    public void processKeyEvent(KeyEvent e)			*
 *									*
 * InformationPanel: the panel that display the all the information	*
 *	Fields:								*
 *	    JButton retractButton;					*
 *	    JLabel turnDisplay;						*
 *	    JLabel totalTurn;						*
 *	    JLabel modeDisplay;						*
 *	    JLabel difficultyDisplay;					*
 *	    JPanel panelNO0;						*
 *	    JPanel panelNO1;						*
 *	    JPanel panelNO2;						*
 *	Methods:							*
 *	    public InformationPanel(int mode,int difficulty,int goFirst)*
 *	    public void clear(int mode,int difficulty,int goFirst)	*
 *	    public void updateInfo()					*
 *	    public void retractInfo()					*
 *									*
 * WaitPanel: The panel displaying the wait information			*
 *	Methods:							*
 *	    public WaitPanel()						*	
 *	    public void paint(Graphics g)				*
 *									*
 * NewGameFrame: A dialog to start new game				*
 *	Fields:								*
 *	    int hVsAi=1;						*
 *	    int difficulty=2;						*
 *	    int first=1;						*
 *	    boolean ok = false;						*
 *	    JButton okButton = new JButton("Start");			*
 *	Methods:							*
 *	    public NewGameFrame(Frame owner, String title)		*
 *	    public void actionPerformed(ActionEvent e)			*
 *									*
 * Piece: Stores the position and colour of a piece			*
 *	Fields:								*
 *	    int x;							*
 *	    int y;							*
 *	    int side;							*
 *	Methods:							*
 *	    public Piece(int x,int y,boolean tf)			*
 *	    public Piece(int x,int y,int s)				*
 *									*
 * SettingFrame: A setting dialog that read setting from user		*
 *	Fields:								*
 *	    int ok = 0;							*
 *	    int R = 250,G =235, B=215;					*
 *	    int music = 1;						*
 *	    int style = 0;						*
 *	    int dT=1000;						*
 *	    JTextField rv;						*
 *	    JTextField gv;						*
 *	    JTextField bv;						*
 *	    JTextField delayTime;					*
 *	Methods:							*
 *	    public SettingFrame(Frame owner,String title)		*
 *	    public void actionPerformed(ActionEvent e)			*
 *									*
 * Wuziqi: The class that has all the logic of the game			*
 *	Fields:								*
 *	    protected Color boardColour;				*
 *	    protected Color colourWhite = Color.WHITE;			*
 *	    protected Color colourBlack = Color.BLACK;			*
 *	    private int difficulty;					*
 *	    private boolean vsAI;					*
 *	    private int totalTurns;					*
 *	    private boolean goFirst;					*
 *	    private boolean curTurn;					*
 *	    private int pColour;					*
 *	    private int aiColour;					*
 *	    private boolean nexTurn;					*
 *	    private int searchDepth;					*
 *	    protected int x_max = 15, x_min = 0;			*
 *	    protected int y_max = 15, y_min = 0;			*
 *	    private final static int N = 15;				*
 *	    private java.util.List<Piece> list = new ArrayList<Piece>();*
 *	    private java.util.List<Piece> repBuffer;			*
 *	    private int board[][];					*
 *	    protected int delay;					*
 *	    protected boolean musicOn;					*
 *	    protected boolean wait = false;				*
 *	    protected boolean replay = false;				*   
 *	    private boolean gameOver = false;				*
 *	    InformationPanel info;					*
 *	    private int leftXY[] = new int[2];				*
 *	    private int rightXY[] = new int[2];				*
 *	    protected boolean reDraw = false;				*
 *	    protected int style = 0;					*
 *	Methods:							*
 *	    public Wuziqi(int hVsAI,int dRate, int goF,			*
 *				    InformationPanel info)		*
 *	    protected void readSetting()				*
 *	    public void clearBoard()					*
 *	    void placePiece(int x,int y,boolean black)			*
 *	    public void retract()					*
 *	    public int getBoard(int a,int b)				*
 *	    public boolean isVsAI()					*
 *	    public boolean getCurTurn()					*
 *	    public void setOver()					*
 *	    public boolean isOver()					*
 *	    public void replayWriter(String fileName)			*
 *	    public void saveGame(String fileName)			*
 *	    private void readFile(String fileName)			*
 *	    public void doReplay(String fileName)			*
 *	    public void loadGame(String fileName)			*
 *	    private void drawRepBuffer()				*
 *	    public void paint(Graphics g)				*
 *	    private void paintBoard(Graphics2D g)			*
 *	    private void paintPiece(Graphics2D g)			*
 *	    private void drawWinLine(Graphics2D g)			*
 *	    public boolean isWin(int x,int y)				*
 *	    protected void placePieceAI()				*
 *	    private Piece placeFinder()					*
 *	    private int findTypeX(int a,int b,int s)			*
 *	    public void resetMaxMin(int x,int y)			*
 *	    private Piece placeFinderX(int bwf)				*
 *	    private int evaluate()					*
 *	    protected int findMax(int alpha, int beta, int step)	*
 *	    protected int findMin(int alpha, int beta, int step)	*
 *	    private int[][] getBests(int bwf)				*
 *	    int findType(int x,int y,int s)				*
 *	    int typeScore(int c)					*
 *	    private int getMark(int k)					*
 *	    private int[] pieceCounter(int x,int y,int direction)	*
 *	    protected int[] pieceCounterX(int x,int y,			*
 *					    int direction,int s)	*
 *	    private boolean preCheck(int x,int y, int ex,		*
 *						int ey, int bwf)	*
 *	    private int randomInt(int Min,int Max)			*
 *	    private boolean randomTest(int kt)				*
 *									*
 * Gomoku: the Main methods that manages all panels			*
 *	Fields:								*
 *	    private JMenuBar menubar;					*
 *	    private JMenu[] mainMenu;					*
 *	    private JMenuItem[] subMenu1;				*
 *	    private JMenuItem[] subMenu2				*
 *	    private static final int WIDTH = 430;			*
 *	    private static final int HEIGHT = 520;			*
 *	    private Wuziqi gamePanel;					*
 *	    private JPanel botPanelPanel;				*
 *	    private InformationPanel bottomPanel;			*
 *	    private int humanVsAi=1;					*
 *	    private int difficulty=2;					*
 *	    private int goFirst=1;					*
 *	    private Gomoku me = this;					*
 *	    boolean playMusic;						*
 *	Methods:							*
 *	    public void instNewGame()					*
 *	    public String myFileChooser(String title,String fileTypeName*
 *				,String fileType,boolean openSave)	*
 *	    public void actionPerformed(ActionEvent e)			*
 *	    public static void main (String[] args)			*
 *	class Mouseclicked extends MouseAdapter				*
 *		public void mouseClicked(MouseEvent evt)		*
 *									*
 ************************************************************************
 * ERROR HANDLING							*
 * This program is mainly controlled by mouse. Almost all the input	*
 * field that reads from the keyboard has error checks. Every intput	*
 * text field is limited to be positive integers only, where prevents	*
 * the user to input string/characters and crash the program. The	*
 * program also has a lot of interaction with disk files. In case the	*
 * user change the data in the files, error checks are also done when	*
 * reading the files. The length of the file is examined and if the data*
 * does not match the required length or is not the required type the	*
 * program will refuse to read the file. The rgb values are limited to  *
 * be 255 max.								*
 ************************************************************************
 * PROGRAM LIMITATIONS							*
 * The graphic is simpler than last semster's project.Animations would	*
 * require the program to use thread. Human vs human mode can only be	*
 * played on the same machine. The operation to place piece on the board*
 * is limited to be mouse clicks only					*
 ************************************************************************
 * EXTENSIONS AND IMPROVEMENTS						*
 * 1.Able to play pvp through the internet				*
 * 2.More smooth animation						*
 * 3.Can use the keyboard to land pieces				*
 * 4.Use external pictures for piece and board				*
 * 5.Add ranking for lowest steps and time to win a game		*
 ************************************************************************
*/  


package gomoku;


import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.io.*;
import java.util.*;

public class Gomoku extends JFrame implements ActionListener{

    private JMenuBar menubar;	//the menu bar at the top
    private JMenu[] mainMenu={new JMenu("Game"),new JMenu("Help")};
    private JMenuItem[] subMenu1={new JMenuItem("New Game"),new JMenuItem("Save Game"),new JMenuItem("Load Game"),new JMenuItem("Save Replay"),
                                   new JMenuItem("Replay Game"),new JMenuItem("Setting"),new JMenuItem("Exit")};
    private JMenuItem[] subMenu2={new JMenuItem("How to play"),new JMenuItem("About"),new JMenuItem("Backtrack")};
    private static final int WIDTH = 430;
    private static final int HEIGHT = 520;
    private Wuziqi gamePanel; //panel for the actual game
    private JPanel botPanelPanel; //bottom panel to display information or waiting message
    private InformationPanel bottomPanel;//the information
    private int humanVsAi=1;//is human vs Ai? 1 : 0
    private int difficulty=2;//difficulty (1~3)
    private int goFirst=1;//goFirst?1:0
//    private Gomoku me = this;
    boolean playMusic; //flag for playing music or not
    URL musicFile = getClass().getResource("music.wav"); //the URL to music
    AudioClip backGroundMusic = java.applet.Applet.newAudioClip(musicFile); //find the music
    //Menuitemclicked menuAction=new Menuitemclicked();
    Mouseclicked mouseclicked=new Mouseclicked();
    public Gomoku(){
            setTitle("Gomoku++");
            setSize(WIDTH,HEIGHT);
            setResizable(false);
            setLayout(new BorderLayout()); 
	    //to add the menu and add listener to all of them
            menubar=new JMenuBar();
            subMenu1[0].setActionCommand("new");
            subMenu1[1].setActionCommand("save");
            subMenu1[2].setActionCommand("load");
            subMenu1[3].setActionCommand("saveRep");
            subMenu1[3+1].setActionCommand("replay");
            subMenu1[4+1].setActionCommand("setting");
            subMenu1[5+1].setActionCommand("exit");
            subMenu2[0].setActionCommand("how");
            subMenu2[1].setActionCommand("about");
            subMenu2[2].setActionCommand("backtrack");
            for(int i=0;i<7;i++){ //add subMenu to mainMenu
                mainMenu[0].add(subMenu1[i]);
                subMenu1[i].addActionListener(this);
                /*
                 * ==========================================
                 * ==========================================
                 */
            }
            for(int i=0;i<2;i++){ //change to i < 3 to add backtrack function
                mainMenu[1].add(subMenu2[i]);
                subMenu2[i].addActionListener(this);
            }
            for(int i=0;i<2;i++)
                    menubar.add(mainMenu[i]);
            setJMenuBar(menubar); //set menu bar to the frame
            
            setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2
                            - WIDTH / 2, Toolkit.getDefaultToolkit()
                            .getScreenSize().height
                            / 2 - HEIGHT / 2); //set the frame in the middle of the secreen
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exit on close
            /*
            try{
                Scanner s = new Scanner(new FileInputStream("game.ini"));//read the setting from file
                humanVsAi = s.nextInt();
                difficulty = s.nextInt();
                goFirst = s.nextInt();
            }
            catch (IOException ex){
                System.out.println("Please don't fool around with game.ini file");
            }
            
            Container p = getContentPane();
            bottomPanel = new InformationPanel(humanVsAi,difficulty,goFirst);
            p.add(bottomPanel,BorderLayout.SOUTH);
            gamePanel = new Wuziqi(humanVsAi,difficulty,goFirst,bottomPanel);
            p.add(gamePanel,BorderLayout.CENTER);
            gamePanel.addMouseListener(mouseclicked);
	    bottomPanel.retractButton.addActionListener(this);
            bottomPanel.retractButton.setActionCommand("retract");
	    */
	    try{
		Scanner s = new Scanner(new FileInputStream("config.ini"));//read the setting from file, only read info about music
		s.next(); //read until music
		s.nextInt();
		s.nextInt();
		s.nextInt();
		s.next();
		int music = s.nextInt();
		if (music==1)
		    playMusic = true;
		else
		    playMusic = false;
	    }
	    catch(IOException | NoSuchElementException ee){ //catch 2 exceptions at once
		playMusic = true; //default true
	    }
	    if (playMusic) //loop if true
		backGroundMusic.loop();
	    /*
	     * =================================================
	     * ================panel============================
	     * =================================================
	     */
	    //create information panel
	    botPanelPanel = new JPanel();
	    bottomPanel = new InformationPanel(humanVsAi,difficulty,goFirst); //information panel
	    gamePanel = new Wuziqi(humanVsAi,difficulty,goFirst,bottomPanel); //the actual game
	    botPanelPanel.add(bottomPanel);
	    Container p = getContentPane();
	    //reset the actionlistener for retract button
	    bottomPanel.retractButton.addActionListener(this);
	    bottomPanel.retractButton.setActionCommand("retract");
	    p.add(gamePanel, "Center");
	    p.add(botPanelPanel ,"South");
	    //instNewGame();
	    //gamePanel = new Wuziqi(humanVsAi,difficulty,goFirst,bottomPanel);
	    gamePanel.addMouseListener(mouseclicked); //reAdd mouse listener
            setVisible(true);
            //repaint();
    }
    
    /*public void paint(Graphics g){
	gamePanel.paint(g);
	bottomPanel.paint(g);
    }*/

    public void instNewGame(){//start new game
			NewGameFrame newWindow = new NewGameFrame(null, "New Game");//create NewGameFrame to setup the settings
			newWindow.setSize(430,125);   //size
			newWindow.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-410/2,Toolkit.getDefaultToolkit().getScreenSize().height/2-125/2);
			newWindow.setVisible(true);
			if (newWindow.ok){ //if user clicked button
				humanVsAi= newWindow.hVsAi; //read all the information
				difficulty =newWindow.difficulty;
				goFirst=newWindow.first;
				newWindow.dispose(); //dispose dialog
			}
	System.out.println(humanVsAi+" "+difficulty+" "+goFirst);
	Container p = getContentPane();
	gamePanel.setVisible(false);//close the old one
	bottomPanel = new InformationPanel(humanVsAi,difficulty,goFirst); //add these panels to the frame
	gamePanel = new Wuziqi(humanVsAi,difficulty,goFirst,bottomPanel);
	//bottomPanel = new InformationPanel(humanVsAi,difficulty);
	bottomPanel.clear(humanVsAi,difficulty,goFirst);//clear the old information
	//readd listener
	bottomPanel.retractButton.addActionListener(this);
	bottomPanel.retractButton.setActionCommand("retract");
	botPanelPanel.removeAll();
	botPanelPanel.add(bottomPanel);
	//botPanelPanel.add(new WaitPanel());
	//update the graphics
	botPanelPanel.validate();
	botPanelPanel.revalidate();
	botPanelPanel.repaint();
	bottomPanel.repaint();
	p.add(gamePanel, "Center");
	p.add(botPanelPanel ,"South");
	gamePanel.addMouseListener(mouseclicked); 
	repaint();	
    }
    
    public String myFileChooser(String title,String fileTypeName,String fileType,boolean openSave){ //steps needed to choose a file
        JFileChooser chooser = new JFileChooser(); //create file chooser object
        chooser.setCurrentDirectory(new java.io.File("."));//set to current directory
        chooser.setDialogTitle(title);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(fileTypeName, fileType);
        chooser.setFileFilter(filter);
        int returnVal;
        if (openSave) //if true, open; otherwise, save
             returnVal = chooser.showOpenDialog(Gomoku.this);//show dialog to open
        else
            returnVal = chooser.showSaveDialog(Gomoku.this);//show dialog to save
        String re;
        if(returnVal == JFileChooser.APPROVE_OPTION) {//if a file has choosen
                re = chooser.getSelectedFile().getPath(); //get its path name (do not know how to use the file directly)
        }
        else
            return null;
        if (!openSave) //save, add the extension
            re+=("."+fileType);
        return re;
    }
    
    
    
    class Mouseclicked extends MouseAdapter{  //read information about mouse
        public void mouseClicked(MouseEvent evt){   
            if(gamePanel.wait||gamePanel.replay||gamePanel.isOver())//computer thinking
                return; 
            int mouseX=evt.getX();  //mouse's coordinates
            int mouseY=evt.getY();
            //System.out.println(mouseX+" "+mouseY);
            for(int i=0;i<=14;i++)   
                for(int j=0;j<=14;j++){   
                    if(mouseX>=14+i*28-10&&mouseX<=14+i*28+10&&mouseY>=14+j*28-10&&mouseY<=14+j*28+10)    
                        if(gamePanel.getBoard(i,j)==-1){//no one placed any stone on this pos yet 
			    System.out.println("x: "+i+" y: "+j);
                            gamePanel.placePiece(i,j,gamePanel.getCurTurn());//place the stone
                            if (gamePanel.isVsAI()&&!gamePanel.isOver()){//if vs ai and game not end
				botPanelPanel.removeAll(); //clear the panel
				botPanelPanel.paint(botPanelPanel.getGraphics()); //display nothing.....
				WaitPanel x = new WaitPanel();
				botPanelPanel.add(x);
				x.paint(botPanelPanel.getGraphics()); //force paint waiting message
				//botPanelPanel.setSize(420,50);
				//botPanelPanel.repaint();
				//repaint();
                                gamePanel.placePieceAI();//ai place piece
				botPanelPanel.removeAll(); //clear panel again
				botPanelPanel.add(bottomPanel); //display the information
				botPanelPanel.revalidate();
				//repaint();
			    }
                            return;   
                        }
                }
        }   
    }
    
    public void actionPerformed(ActionEvent e) { //where actions happen
	String cmdx = e.getActionCommand();
	if (cmdx.equals("retract")){
	    System.out.println("retract called");
	    if (!gamePanel.isOver()&&!gamePanel.replay) //can only when game is not over and not replay
		gamePanel.retract();
	    return;
	}
	JMenuItem target = (JMenuItem)e.getSource(); //get targeted menu item
	String cmd = target.getActionCommand(); //store the commad in cmd
	if(cmd.equals("new")){ //instantiate new game
	    System.out.println("New game");
	    instNewGame();
	}
	if (cmd.equals("save")){ //save game
	    String fileChose = myFileChooser("Save Game","Gomoku Save File","gsav",false);
	    if(fileChose!=null) {//if a file has choosen
		    System.out.println("You chose to open this file: " +fileChose);
		    gamePanel.replayWriter(fileChose); 
	    }

	}
	if (cmd.equals("saveRep")){ //save replay
	    String fileChose = myFileChooser("Save Replay","Gomoku Replay File","grep",false);
	    if(fileChose!=null) {//if a file has choosen
		System.out.println("You chose to open this file: " +fileChose);
		gamePanel.replayWriter(fileChose); //actually same as save game
	    }                

	}
	if (cmd.equals("load")){ //load game
	    System.out.println("load");
	    String fileChose = myFileChooser("Load Game","Gomoku Save File","gsav",true);
	    if(fileChose!=null) {//if a file has choosen
		System.out.println("You chose to open this file: " +fileChose);
		gamePanel.loadGame(fileChose);
		//has to readd listener everytime new game instantiates
		bottomPanel.retractButton.setActionCommand("retract");
		bottomPanel.retractButton.addActionListener(this);
	    }
	}
	if (cmd.equals("replay")){
	    System.out.println("replay");
	    String fileChose = myFileChooser("Open Replay","Gomoku Replay File","grep",true);
	    if(fileChose!=null) {//if a file has choosen
		System.out.println("You chose to open this file: " +fileChose);
		gamePanel.doReplay(fileChose); //replay file
		System.out.println("replay finished");
		JOptionPane.showMessageDialog(null,"Replay has finished");
	    }
	    //bottomPanel = new InformationPanel(humanVsAi,difficulty);
	    //getContentPane().add(bottomPanel,BorderLayout.SOUTH);
	}
	if (cmd.equals("setting")){ //read the setting
	    System.out.println("setting");
	    SettingFrame tempFrame = new SettingFrame(null, "Settings"); //tempFrame for setting
	    if (tempFrame.ok==1){ //if tempFrame oked
		gamePanel.readSetting();
		backGroundMusic.stop(); //stop the music every time setting is called
		if (tempFrame.music==1) //restart music if required
		    backGroundMusic.loop();
		//read other settings
		gamePanel.boardColour = new Color(tempFrame.R,tempFrame.G,tempFrame.B);
		gamePanel.style =tempFrame.style;
		tempFrame.dispose();
	    }
	    gamePanel.repaint();
	    //call setting frame
	}
	if (cmd.equals("exit")){
	    System.out.println("exit");
	    System.exit(1); //exit
	}
	if (cmd.equals("backtrack")){ //debuging only
	    BackTrackDialog d = new BackTrackDialog(null,"debug");
	    d.setVisible(true);
	    int x,y;
	    if (d.ok){
		x = d.x;y=d.y;
		int ty1 = gamePanel.findType(x,y,0);
		int ty2 = gamePanel.findType(x,y,1);
		System.out.println(x+" "+y);
		System.out.println("Piece colour: "+gamePanel.getBoard(x,y)+" pt1: "+ty1+" pt2"+ty2);
		for (int i=1;i<=4;i++){
		    int s[]=gamePanel.pieceCounterX(x,y,i,0);
		    System.out.println("Length0: "+ s[0]+" Close0: "+s[1]);
		    int ss[]=gamePanel.pieceCounterX(x,y,i,1);
		    System.out.println("Length1: "+ ss[0]+" Close1: "+ss[1]);
		}
	    }
	    d.dispose();
	}
	if (cmd.equals("how")){ //how to play part
	    JOptionPane.showMessageDialog(null,"\t                                                      "
		    + "How to Play\n"
		    + "Black plays first, and players alternate in placing a stone of their color on an \n"
		    + "empty intersection. The winner is the first player to get an unbroken row of five \n"
		    + "stone horizontally vertically, or diagonally.\n");
	}
	    if (cmd.equals("about")){ //about message
		JOptionPane.showMessageDialog(null,"Gomoku++ V1.2\nImplmented by Felix Tian\n"
				+ "Updated and debugged by David Wang\n"
				+ "Contact me: w268wang@uwaterloo.ca");
	}           

    }      
    public static void main (String[] args){ //The main
        Gomoku theGame =  new Gomoku(); 
        //JFrame newWindow = new NewGameFrame(); //NewGameFrame to setup game as well
    }

}
