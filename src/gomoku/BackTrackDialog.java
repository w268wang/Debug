
package gomoku;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
//for debug purpose only, no comments written
public class BackTrackDialog extends JDialog implements ActionListener{
    int x; int y;
    boolean ok = false;
    JButton okButton;
    JButton cancelButton;
    IntegerTextField xv;
    IntegerTextField yv;
    public BackTrackDialog(Frame owner, String title){
	super(owner,title,true);
	setSize(80,150);
	setLocationByPlatform( true );
	getContentPane().setLayout(new FlowLayout());
	xv = new IntegerTextField(2);
	yv = new IntegerTextField(2);
	okButton = new JButton("ok");
	cancelButton = new JButton("cancel");
	okButton.addActionListener(this);
	cancelButton.addActionListener(this);
	okButton.setActionCommand("ok");
	cancelButton.setActionCommand("cancel");
	add(xv);
	add(yv);
	add(okButton);
	add(cancelButton);
    }
    public void actionPerformed(ActionEvent e) {
	String c =e.getActionCommand();
	if (c.equals("ok")){
	    //System.out.println(xv.getText()+" "+yv.getText());
	    x = Integer.parseInt(xv.getText());
	    y = Integer.parseInt(yv.getText());
	    setVisible(false);
	    ok = true;
	}
	if (c.equals("cancel")){
	    setVisible(false);
	}
    }
}
