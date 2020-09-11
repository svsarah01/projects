/**
 * @author Vivant Sakore on 1/29/2020
 */
public class BuggyIntDList extends IntDList {

    /**
     * @param values creates a BuggyIntDList with ints values.
     */
    public BuggyIntDList(Integer... values) {
        super(values);
    }


    public void mergeIntDList(IntDList l) {
        front = sortedMerge(this.front, l.front);
    }


    private DNode sortedMerge(DNode d1, DNode d2) {

        if (d1 == null) {
            return d2;
        } else if (d2 == null) {
            return d1;
        } else if (d1.val <= d2.val) {
            d1.next = sortedMerge(d1.next, d2);
            d1.next.prev = d1;
            d1.prev = null;
            return d1;
        } else {
            d2.next = sortedMerge(d1, d2.next);
            d2.next.prev = d2;
            d2.prev = null;
            return d2;
        }
    }

    public void reverse() {

        DNode temp = null;
        DNode p = front;


        if (p != null) {
            back = front;
        }
        while (p != null) {
            temp = p.prev;
            p.prev = p.next;
            p.next = temp;
            p = p.prev;
        }

        if (temp != null) {
            front = temp.prev;
        }
    }
}
