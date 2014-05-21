
package gomoku;

public class Piece { //act as struct in c/c++
    //no need for private/getter/setter etc.
    int x; 
    int y;
    int side; //black = 0, white = 1
    public Piece(int x,int y,boolean tf){ //true for black, false for white
        this.x = x;
        this.y = y;
        if(tf)
            side = 0;
        else
            side = 1;
    }
     public Piece(int x,int y,int s){ //overload
        this.x = x;
        this.y = y;
	side = s;
    }   
}
