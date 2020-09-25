package es.datastructur.synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

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
}
