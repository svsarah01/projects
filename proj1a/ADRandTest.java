import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class ADRandTest {

    @Test
    public void test() {
        System.out.println("Running randomized test.");
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        Random rand = new Random(5);
        for (int i = 0; i < 200; i++) {
            int randomnum = rand.nextInt(5);
            if (randomnum == 0) {
                ad.addFirst(i);
                lld.addFirst(i);
            }
            if (randomnum == 1) {
                ad.addLast(i);
                lld.addLast(i);
            }
            if (randomnum == 2) {
                Integer x = lld.get(i);
                Integer y = ad.get(i);
                assertEquals(x, y);
            }
            if (randomnum == 3) {
                Integer x = lld.removeFirst();
                Integer y = ad.removeFirst();
                assertEquals(x, y);
            }
            if (randomnum == 4) {
                Integer x = lld.removeLast();
                Integer y = ad.removeLast();
                assertEquals(x, y);
            }
        }
    }
}

