package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static edu.princeton.cs.algs4.StdRandom.uniform;
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
            Point p = new Point(uniform(-500.0, 500.0), uniform(-500.0, 500.0));
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
            Point pt = new Point(uniform(-500.0, 500.0), uniform(-500.0, 500.0));
            Point actual = kd.nearest(pt.getX(), pt.getY());
            Point expected = nps.nearest(pt.getX(), pt.getY());
            assertEquals(expected, actual);
        }
    }

    private static void printTimingTable(List<Integer> listofNs, List<Double> times,
                                         List<Integer> opCounts, String message) {
        System.out.println(message);
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < listofNs.size(); i += 1) {
            int N = listofNs.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    @Test
    public void timing() {
        List<Integer> listofNs = List.of(31250, 62500, 125000, 250000, 500000, 1000000);
        List<Double> constructionTimes = new ArrayList<>();
        List<Double> nearestTimes = new ArrayList<>();
        List<Integer> nearestOps = List.of(1000000, 1000000, 1000000, 1000000, 1000000, 1000000);
        for (int N : listofNs) {
            /* testing construction times for KDTree */
            List<Point> points = listOfPoints(N);
            Stopwatch sw = new Stopwatch();
            KDTree kd = new KDTree(points);
            double constructionTime = sw.elapsedTime();
            assertTrue(constructionTime < 20);
            constructionTimes.add(constructionTime);

            /* testing nearest times for KDTree */
            Stopwatch sw2 = new Stopwatch();
            for (int i = 0; i < 1000000; i++) {
                Point randPoint = new Point(uniform(-500.0, 500.0), uniform(-500.0, 500.0));
                kd.nearest(randPoint.getX(), randPoint.getY());
            }
            double nearestTime = sw2.elapsedTime();
            assertTrue(nearestTime < 15);
            nearestTimes.add(nearestTime);
        }

        /* testing nearest times for NaivePointSet for comparison */
        List<Integer> npsNs = List.of(125, 250, 500, 1000);
        List<Double> npsTimes = new ArrayList<>();
        List<Integer> npsOps = List.of(1000000, 1000000, 1000000, 1000000);
        for (int N : npsNs) {
            List<Point> points = listOfPoints(N);
            NaivePointSet nps = new NaivePointSet(points);
            Stopwatch sw3 = new Stopwatch();
            for (int i = 0; i < 1000000; i++) {
                Point randPoint = new Point(uniform(-500.0, 500.0), uniform(-500.0, 500.0));
                nps.nearest(randPoint.getX(), randPoint.getY());
            }
            double npsNearestT = sw3.elapsedTime();
            npsTimes.add(npsNearestT);
        }

        /* printing timing tables */
        printTimingTable(listofNs, constructionTimes, listofNs,
                "Timing table for Kd-Tree Construction");
        printTimingTable(listofNs, nearestTimes, nearestOps,
                "Timing table for Kd-Tree Nearest");
        printTimingTable(npsNs, npsTimes, npsOps,
                "Timing table for NaivePointSet Nearest");
    }
}
