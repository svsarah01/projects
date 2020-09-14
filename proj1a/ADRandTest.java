import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class ADRandTest {

    @Test
    public void Test() {
        System.out.println("Running randomized test.");
        ArrayDeque<Integer> ad = new ArrayDeque<Integer>();
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        Random rand = new Random(5);
        //the value 5 is the seed. This means that if I run the code over and over, I get a deterministic result
        //That makes it much easier to debug, since you can recreate the issue and set a conditional breakpoint accordingly
        for (int i = 0; i < 200; i++) { //the i == 5 is arbitrary, just wanted it to be readable
            int randomnum = rand.nextInt(5); //gives a random value between 0 and 1 inclusive
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

