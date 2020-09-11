import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    /** Adds three strings consequetively & prints out arraydeque. */
    public void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        assertTrue(ad1.isEmpty());

        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse(ad1.isEmpty());

        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that arraydeque is empty afterwards. */
    public void addRemoveTest() {

        System.out.println("Running add/remove test.");
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue(ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        assertFalse(ad1.isEmpty());

        ad1.removeFirst();
        // should be empty
        assertTrue(ad1.isEmpty());

        ad1.addFirst(20);
        ad1.addLast(30);
        ad1.addFirst(10);
        ad1.addLast(40);
        ad1.addLast(50);
        ad1.addLast(60);
        // should not be empty
        assertFalse(ad1.isEmpty());

        assertEquals(java.util.Optional.of(10), java.util.Optional.of(ad1.removeFirst()));
        // should not be empty
        assertFalse(ad1.isEmpty());

        assertEquals(java.util.Optional.of(60), java.util.Optional.of(ad1.removeLast()));
        // should not be empty
        assertFalse(ad1.isEmpty());

        assertEquals(java.util.Optional.of(50), java.util.Optional.of(ad1.removeLast()));
        // should not be empty
        assertFalse(ad1.isEmpty());

        assertEquals(java.util.Optional.of(40), java.util.Optional.of(ad1.removeLast()));
        // should not be empty
        assertFalse(ad1.isEmpty());

        assertEquals(java.util.Optional.of(30), java.util.Optional.of(ad1.removeLast()));
        // should not be empty
        assertFalse(ad1.isEmpty());

        assertEquals(java.util.Optional.of(20), java.util.Optional.of(ad1.removeLast()));
        // should be empty
        assertTrue(ad1.isEmpty());

        ad1.addLast(100);
        assertEquals(java.util.Optional.of(100), java.util.Optional.of(ad1.removeLast()));
    }

    @Test
    /** Adds many items to the front and then removes them from the front, checking for resizing capabilities. */
    public void resizeTest() {

        System.out.println("Running addFirst/removeFirst test.");
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue(ad1.isEmpty());

        // adding 1-100 starting w/ 100 and going to 1
        for (int i = 100; i > 0; i = i - 1) {
            ad1.addFirst(i);
        }
        //checking size
        assertEquals(100, ad1.size());

        //removing the first 75
        assertEquals(java.util.Optional.of(1), java.util.Optional.of(ad1.removeFirst()));
        assertEquals(java.util.Optional.of(100), java.util.Optional.of(ad1.removeLast()));
        for (int i = 0; i < 73; i = i + 1) {
            ad1.removeLast();
        }
        //checking size
        assertEquals(25, ad1.size());
    }

    @Test
    /** Adds many items to the back and then removes them from the back, checking for resizing capabilities. */
    public void resizeTest2() {

        System.out.println("Running addLast/removeLast test.");
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        // should be empty
        assertTrue(ad1.isEmpty());

        // adding 1-100 starting at 1 and going to 100
        for (int i = 1; i <= 100; i = i + 1) {
            ad1.addLast(i);
        }
        //checking size
        assertEquals(100, ad1.size());

        //removing the last 75
        for (int i = 0; i < 75; i = i + 1) {
            ad1.removeLast();
        }
        //checking size
        assertEquals(25, ad1.size());
    }
}
