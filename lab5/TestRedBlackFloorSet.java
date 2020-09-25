import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Created by hug.
 */
public class TestRedBlackFloorSet {
    @Test
    public void randomizedTest() {
        AListFloorSet alfs = new AListFloorSet();
        RedBlackFloorSet rbfs = new RedBlackFloorSet();
        for (int i = 0; i < 1000000; i++) {
            double rand = StdRandom.uniform(-5000, 5000);
            alfs.add(rand);
            rbfs.add(rand);
        }
        for (int i = 0; i < 100000; i++) {
            double rand = StdRandom.uniform(-5000, 5000);
            assertEquals(alfs.floor(rand), rbfs.floor(rand), 0.000001);
        }
    }
}
