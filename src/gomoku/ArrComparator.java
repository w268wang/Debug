
package gomoku;

import java.util.Comparator;

class ArrComparator implements Comparator<Object> {
    int column = 2;

    int sortOrder = -1; //descending order

    public ArrComparator() {
    }
    public int compare(Object a, Object b) {
        if (a instanceof int[]) { //make sure its an array
            return sortOrder * (((int[]) a)[column] - ((int[]) b)[column]);
        }
        throw new IllegalArgumentException("param a,b must int[].");
    }
}
