package bearmaps;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KDTreeTest {

    @Test
    public void nearestTest() {
        Point A = new Point(2, 3); // constructs a Point with x = 1.1, y = 2.2
        Point B = new Point(4, 2);
        Point C = new Point(4, 5);
        Point D = new Point(3, 3);
        Point E = new Point(1, 5);
        Point F = new Point(4, 4);

        KDTree nn = new KDTree(List.of(A, B, C, D, E, F));
        Point ret = nn.nearest(0, 7); // should return E
        assertEquals(E, ret);
    }

    private List<Point> listOfPoints(int N) {
        List<Point> points = new ArrayList();
        for (int i = 0; i < N; i++) {
            Point p = new Point(StdRandom.uniform(-500.0, 500.0), StdRandom.uniform(-500.0, 500.0));
            points.add(p);
        }
        return points;
    }

    @Test
    public void randNearest() {
        List<Point> points = listOfPoints(200);
        KDTree kd = new KDTree(points);
        NaivePointSet nps = new NaivePointSet(points);

        for (int i = 0; i < 100; i++) {
            Point pt = new Point(StdRandom.uniform(-500.0, 500.0), StdRandom.uniform(-500.0, 500.0));
            Point actual = kd.nearest(pt.getX(), pt.getY());
            Point expected = nps.nearest(pt.getX(), pt.getY());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void timing() {
        List<Point> points = listOfPoints(1000000);
        Stopwatch sw = new Stopwatch();
        KDTree kd = new KDTree(points);
        double constructionTime = sw.elapsedTime();

//        Stopwatch sw2 = new Stopwatch();
//        for (int i = 0; i < 1000000; i++) {
//            Point randPoint = new Point(StdRandom.uniform(-500.0, 500.0), StdRandom.uniform(-500.0, 500.0));
//            kd.nearest(randPoint.getX(), randPoint.getY());
//        }
//        double nearestTime = sw2.elapsedTime();

        assertTrue(constructionTime < 20);
//        assertTrue(nearestTime < 15);
    }
}
