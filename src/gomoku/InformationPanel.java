
package gomoku;

import javax.swing.*;
import java.awt.*;


public class InformationPanel extends JPanel{
    JButton retractButton; 
    JLabel turnDisplay;
    JLabel totalTurn;//totalTurns
    JLabel modeDisplay; //the mode to display
    JLabel difficultyDisplay;//the difficulty that is displaying
    JPanel panelNO0;//first panel
    JPanel panelNO1; //2nd
    JPanel panelNO2;
    //JPanel panelNO3;
    int turns=0;
    boolean turnIndicator=true; //flag for turn
    public InformationPanel(int mode,int difficulty,int goFirst){
        turns=0; //set to 0 turns
        turnIndicator=true;
        setLayout(new GridLayout(1,3,1,1));
        retractButton = new JButton("Retract");
        panelNO0= new JPanel();
        panelNO0.add(retractButton);
        panelNO0.add(new JLabel(""));
        panelNO1 = new JPanel();
        turnDisplay = new JLabel("Turn: Player 1"); //initial displaying
        totalTurn = new JLabel("Total Turns: 0");
        panelNO1.add(turnDisplay);
        panelNO1.add(totalTurn);
        panelNO2= new JPanel();
        String modeDis;
        if (mode == 1){ //find which label to display
            modeDis = "HumanVsAI";
	    if(goFirst==1)
		modeDis+=" Human First";
	    else
		modeDis+=" AI First";
	}
        else
            modeDis = "HumanVsHuman";
        modeDisplay = new JLabel(modeDis);
        String difStr; //find the difficulty string
        if (difficulty==1)
            difStr = "Beginner";
        else if (difficulty==2)
            difStr = "Intermediate";
        else
            difStr = "Advanced";
        if (mode==0) //no need to display the difficulty if vs Human
            difStr = "N/A";
        difficultyDisplay = new JLabel("Difficulty: "+difStr);
        panelNO2.add(modeDisplay);
        panelNO2.add(difficultyDisplay);
        this.add(panelNO0);
        this.add(panelNO1);
        this.add(panelNO2);
        setPreferredSize(new Dimension(420,50));
    }
    public void clear(int mode,int difficulty,int goFirst){
        this.removeAll(); //remove all components
	//same as the constructor
        turns=0;
        turnIndicator=true;
	if (goFirst==0){
	    turns++;
	 turnIndicator = false;   
	}
        retractButton = new JButton("Retract");
        panelNO0= new JPanel();
        panelNO0.add(retractButton);
        panelNO0.add(new JLabel(""));
        panelNO1 = new JPanel();
        turnDisplay = new JLabel("Turn: Player 1");
        totalTurn = new JLabel("Total Turns: 0");
        panelNO1.add(turnDisplay);
        panelNO1.add(totalTurn);
        panelNO2= new JPanel();
        String modeDis;
        if (mode == 1){
            modeDis = "HumanVsAI";
	    if(goFirst==1)
		modeDis+=" Human First";
	    else
		modeDis+=" AI First";
	}
        else
            modeDis = "HumanVsHuman";
        modeDisplay = new JLabel(modeDis);
        String difStr;
        if (difficulty==1)
            difStr = "Beginner";
        else if (difficulty==2)
            difStr = "Intermediate";
        else
            difStr = "Advanced";
        if (mode==0)
            difStr = "N/A";
        difficultyDisplay = new JLabel("Difficulty: "+difStr);
        panelNO2.add(modeDisplay);
        panelNO2.add(difficultyDisplay);
        this.add(panelNO0);
        this.add(panelNO1);
        this.add(panelNO2);
        setPreferredSize(new Dimension(420,50));        
    }
    
    public void updateInfo(){ //update info if a new piece is placed
        turns++;//add 1 turn
        turnIndicator = !turnIndicator; //other player's turn now
        int p = turnIndicator?1:2;
	//change the text labels
        turnDisplay.setText("Turn: Player "+p);
        totalTurn.setText("Total Turns: "+turns);
        repaint();
    }
    public void retractInfo(){
        turns--; //decrease by 1 when retracting
        turnIndicator = !turnIndicator; //change turn
        int p = turnIndicator?1:2;
        turnDisplay.setText("Turn: Player "+p);
        totalTurn.setText("Total Turns: "+turns);
        repaint();
    }
    /*
    public void goWait(){
	removeAll(); 
	this.add(new JLabel("Computer thinking, be patient.")); //not working, cannot call repaint() too fast!
	updateUI();
	//this.paint(this.getGraphics());
    }
    public void reWait(){
	removeAll();
        this.add(panelNO0);
        this.add(panelNO1);
        this.add(panelNO2);	
    }
    */
}
