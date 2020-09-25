package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        arb.enqueue(69);
        assertEquals(10, arb.capacity());
        assertEquals(1, arb.fillCount());
        assertEquals(69, arb.peek());
        assertEquals(69, arb.dequeue());
    }

    @Test
    public void equalsTest() {
        ArrayRingBuffer arb1 = new ArrayRingBuffer(4);
        arb1.enqueue(1);
        arb1.enqueue(2);
        arb1.enqueue(3);
        arb1.enqueue(4);

        ArrayRingBuffer arb2 = new ArrayRingBuffer(4);
        arb2.enqueue(1);
        arb2.enqueue(2);
        arb2.enqueue(3);
        arb2.enqueue(4);
        assertTrue(arb1.equals(arb2));
        assertTrue(arb2.equals(arb1));

        ArrayRingBuffer arb3 = new ArrayRingBuffer(4);
        arb3.enqueue(4);
        arb3.enqueue(3);
        arb3.enqueue(2);
        arb3.enqueue(1);
        assertFalse(arb1.equals(arb3));
        assertFalse(arb3.equals(arb1));
    }

    @Test
    public void iteratorTest() {
        ArrayRingBuffer arb = new ArrayRingBuffer(4);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);

        Iterator<Integer> arbIterator = arb.iterator();
        int[] expected = new int[] {1, 2, 3, 4};
        for (int num : expected) {
            assertEquals(num, (int) arbIterator.next());
        }
    }
}
