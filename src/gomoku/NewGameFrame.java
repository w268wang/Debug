
package gomoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class NewGameFrame extends JDialog
                             implements ActionListener {
    int hVsAi=1;//true for human vs ai
    int difficulty=2;//1,2,3
    int first=1;//player first or not
    boolean ok = false;
    JButton okButton = new JButton("Start");
    public NewGameFrame(Frame owner, String title){
	super(owner,title,true);
        //setAlwaysOnTop( true );
        setLocationByPlatform( true );
        //super(new BorderLayout());
        //setTitle("New Game");
        getContentPane().setLayout(new BorderLayout());

	/*
	setSize(410,125);
        setResizable(false);
        setVisible(true);
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2
                - 200, Toolkit.getDefaultToolkit()
                .getScreenSize().height
                / 2 - 80);
	*/
		
        //Container p = getContentPane();
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //add all radio buttons
        JRadioButton ha = new JRadioButton("Human Vs Ai");
        ha.setActionCommand("Human Vs Ai");
        ha.setSelected(true);
        JRadioButton hh = new JRadioButton("Human Vs Human");
        hh.setActionCommand("Human Vs Human");
 
        JRadioButton d1 = new JRadioButton("Beginner");
        d1.setActionCommand("Beginner"); 
        JRadioButton d2 = new JRadioButton("Intermediate");
        d2.setActionCommand("Intermediate");
        d2.setSelected(true);
        JRadioButton d3 = new JRadioButton("Advanced");
        d3.setActionCommand("Advanced");
        
        JRadioButton g1 = new JRadioButton("Go First");
        g1.setActionCommand("Go First");
        g1.setSelected(true);
        JRadioButton g2 = new JRadioButton("Go Second");
        g2.setActionCommand("Go Second");       
        
        ButtonGroup group1 = new ButtonGroup(); //group the radio buttons
        group1.add(ha);
        group1.add(hh);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(d1);
        group2.add(d2);
        group2.add(d3);
        ButtonGroup group3 = new ButtonGroup();
        group3.add(g1);
        group3.add(g2);
        //add listener for all
        ha.addActionListener(this);
        hh.addActionListener(this);
        d1.addActionListener(this);
        d2.addActionListener(this);
        d3.addActionListener(this);
        g1.addActionListener(this);
        g2.addActionListener(this);
        
        JPanel radioPanel = new JPanel(new GridLayout(4, 3,10,0));
        //add everything to main panel
        radioPanel.add(new JLabel(" Mode"));
        radioPanel.add(new JLabel(" Difficulty"));
        radioPanel.add(new JLabel(" Forehand"));
	//goes from left to right
        radioPanel.add(ha); //human vs AI
        radioPanel.add(d1); //difficulty 1
        radioPanel.add(g1);//go first 
        
        radioPanel.add(hh);//human vs human
        radioPanel.add(d2);//difficulty 2
        radioPanel.add(g2);//go second
        
        radioPanel.add(new JLabel(""));//space
        radioPanel.add(d3);//difficulty 3
        radioPanel.add(new JLabel(""));
        
        add(radioPanel, BorderLayout.CENTER);
        okButton = new JButton( "Start ");
        okButton.setActionCommand("startGame");
        okButton.addActionListener(this);
        add(okButton, BorderLayout.SOUTH);
        //this.setBorder(BorderFactory.createEmptyBorder(5,20,5,20));
        
    }
     public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Human Vs Ai"))
            hVsAi=1;//true
        else if (cmd.equals("Human Vs Human"))
            hVsAi=0;//false
        if (cmd.equals("Beginner")) //read the difficulty
            difficulty = 1;
        else if(cmd.equals("Intermediate"))
            difficulty = 2;
        else if(cmd.equals("Advanced"))
            difficulty = 3;
        if (cmd.equals("Go First"))
            first = 1;//true
        else if (cmd.equals("Go Second"))
            first = 0;//false
        if (cmd.equals("startGame")){
	    ok = true; //can access to the data now
	    setVisible(false);
            //this.dispose();//close frame
        }
     }
}
