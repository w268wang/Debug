
package gomoku;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class SettingFrame extends JDialog
                             implements ActionListener {
    int ok = 0;
    int R = 250,G =235, B=215;
    int music = 1;
    int style = 0;
    int dT=1000;
    JTextField rv;
    JTextField gv;
    JTextField bv;
    JTextField delayTime;
    
    public SettingFrame(Frame owner,String title){
	super(owner,title,true);
        int h = 195;
        int w = 280;
        setSize(w,h);
        setTitle("Game Setting");
        getContentPane().setLayout(new GridLayout(5,1));
        setResizable(false);
        
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2
            - 200, Toolkit.getDefaultToolkit()
            .getScreenSize().height
            / 2 - 80); //set dialog to the middle of the screen
	//initial settings
        try{
            Scanner s = new Scanner(new FileInputStream("config.ini"));//read the setting from file
            s.next();
            R = s.nextInt();
            G = s.nextInt();
            B = s.nextInt();
            s.next();
            music = s.nextInt();
            s.next();
            dT = s.nextInt(); //delay time
	    s.next();
	    style = s.nextInt();
        }
        catch(IOException | NoSuchElementException ee){ //catch 2 exceptions at once
            System.out.println("Resetting setting");
	    //reset all
            R = 255;
            G = 235;
            B = 215;
            music = 1;
            dT = 1000;
        }
        //add the text fields and so on....
        JPanel linePanel1 = new JPanel(new FlowLayout());
        linePanel1.add(new JLabel("Board Colour"));
        linePanel1.add(new JLabel("R"));
        rv  = new IntegerTextField(3);
        rv.setText(Integer.toString(R));
        linePanel1.add(rv);
        linePanel1.add(new JLabel("G"));
        gv  = new IntegerTextField(3);
        gv.setText(Integer.toString(G));
        linePanel1.add(gv);
        linePanel1.add(new JLabel("B"));
        bv  = new IntegerTextField(3);
        bv.setText(Integer.toString(B));
        linePanel1.add(bv);
        JPanel linePanel2 = new JPanel(new FlowLayout());
        linePanel2.add(new JLabel("Music"));
        JRadioButton musicOn = new JRadioButton("On");
        JRadioButton musicOff = new JRadioButton("Off");
        ButtonGroup group1 = new ButtonGroup();
        group1.add(musicOn);
        group1.add(musicOff);
        musicOn.setActionCommand("On");
        musicOff.setActionCommand("Off");
        if (music==0)
            musicOff.setSelected(true);
        else
            musicOn.setSelected(true);
        musicOff.addActionListener(this);
        musicOn.addActionListener(this);
        linePanel2.add(musicOn);
        linePanel2.add(musicOff);
        JPanel linePanelX = new JPanel(new FlowLayout());
        linePanelX.add(new JLabel("Use GreyStyle"));
	JRadioButton styleOn = new JRadioButton("Yes");
        JRadioButton styleOff = new JRadioButton("No");
        ButtonGroup groupX = new ButtonGroup();
        groupX.add(styleOn);
        groupX.add(styleOff);
	linePanelX.add(styleOn);
	styleOn.addActionListener(this);
        styleOn.setActionCommand("st");
	linePanelX.add(styleOff);
	styleOff.addActionListener(this);
        styleOff.setActionCommand("sf");
        if (style==0)
            styleOff.setSelected(true);
        else
            styleOn.setSelected(true);
        JPanel linePanel3 = new JPanel (new FlowLayout());
        linePanel3.add(new JLabel("Replay Delay(ms)"));
        delayTime = new IntegerTextField(4);
        delayTime.setText(Integer.toString(dT));
        linePanel3.add(delayTime);
        JPanel linePanel4 = new JPanel (new FlowLayout());
        linePanel4.add(new JLabel(""));
        JButton okButton = new JButton(" OK ");
        okButton.addActionListener(this);
        okButton.setActionCommand("ok");
        linePanel4.add(okButton);
        JButton cButton = new JButton("Cancel");
        cButton.addActionListener(this);
        cButton.setActionCommand("cancel");        
        linePanel4.add(cButton);
        linePanel4.add(new JLabel(""));
        add(linePanel1);
	add(linePanelX);
        add(linePanel2);
        add(linePanel3);
        add(linePanel4);
        setVisible(true);
    }
    private int limit(int x){
	return x>255?255:x;
    }
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("ok")){ //ok buttone pressed
            System.out.println("ok setting");
	    //read rgb values
            R = Integer.parseInt(rv.getText()); //no need to check since they are read from IntegerTextField
            G =  Integer.parseInt(gv.getText());
            B = Integer.parseInt(bv.getText());
            dT = Integer.parseInt(delayTime.getText());
	    R = limit(R); //limit value to be 255 max
	    G = limit(G);
	    B = limit(B);
	    ok = 1;
            //music does not need to assign anything
            try{ //write everthing into the config.ini file
                 PrintStream out = new PrintStream(new FileOutputStream("config.ini"));
                 out.println("BoardColour "+R+" "+G+" "+B);
                 out.println("Music "+music);
                 out.println("DelayTime "+dT);
		 out.println("stlye "+style);
            }
            catch(IOException exc){
                System.out.println("Unknown error caused by Printing Stream into file config.ini");//may happen if on CD
            }
	    setVisible(false);
            //close frame
        }
        if (cmd.equals("cancel"))
	    this.dispose();
        if (cmd.equals("On"))
            music = 1;
        if (cmd.equals("Off"))
            music = 0;
	if (cmd.equals("st")) //style true
	    style = 1;
	if (cmd.equals("sf")) //false
	    style = 0;
    }
}
