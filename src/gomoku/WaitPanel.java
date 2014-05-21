
package gomoku;

import javax.swing.*;
import java.awt.*;

public class WaitPanel extends JPanel{ //draw wait
    public WaitPanel(){
	//add(new JLabel("Computer Thinking...Please be patient!"));
	//setPreferredSize(new Dimension(430,500));
	setVisible(true);
    }
    public void paint(Graphics g){
	Font f = new Font("Dialog", Font.PLAIN, 18);
	g.setFont(f); //18 size font
	g.drawString("Computer Thinking...Please be patient!", 70, 20); //print waiting message
    }
}
