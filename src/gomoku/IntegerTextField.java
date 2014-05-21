
package gomoku;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

public class IntegerTextField extends JTextField {
    static String unAcceptableChar = "";
    public IntegerTextField(int a){ //constructor, do not know if it works
        super(a);
    }
    public void processKeyEvent(KeyEvent e) { //override this part
        for (int i = 58;i<128;i++) //add all chars after (char)57 (int 9) to the string
            unAcceptableChar += String.valueOf((char)i); 
        for (int i = 0;i<48;i++){ //add chars before char(49) (int 0)
            if (i==8) //backspace is allowed
                continue;
            unAcceptableChar += String.valueOf((char)i);
        }
        char c = e.getKeyChar(); //get char
        if(unAcceptableChar.indexOf(c) > -1) {//to see if key pressed is an integer
            e.consume();//ignore it
            return;
        } 
        super.processKeyEvent(e);
    }
}