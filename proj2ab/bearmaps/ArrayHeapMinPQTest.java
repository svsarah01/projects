package bearmaps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import static edu.princeton.cs.algs4.StdRandom.uniform;
import static org.junit.Assert.*;

public class ArrayHeapMinPQTest{

    @Test
    public void sanityCheck() {
        ArrayHeapMinPQ arrayPQ = new ArrayHeapMinPQ();
        NaiveMinPQ naivePQ = new NaiveMinPQ();
        arrayPQ.add("item 1", 10);
        naivePQ.add("item 1", 10);
        assertEquals(arrayPQ.size(), naivePQ.size());
        assertEquals(arrayPQ.getSmallest(), naivePQ.getSmallest());
        arrayPQ.add("item 2", 2);
        naivePQ.add("item 2", 2);
        assertEquals(arrayPQ.size(), naivePQ.size());
        assertEquals(arrayPQ.getSmallest(), naivePQ.getSmallest());
        arrayPQ.changePriority("item 1", 1);
        naivePQ.changePriority("item 1", 1);
        assertEquals(arrayPQ.getSmallest(), naivePQ.getSmallest());
        arrayPQ.removeSmallest();
        naivePQ.removeSmallest();
        assertEquals(arrayPQ.size(), naivePQ.size());
        assertTrue(arrayPQ.contains("item 2"));
        assertFalse(arrayPQ.contains("item 1"));
    }

    @Test
    public void sanitySwim() {
        ArrayHeapMinPQ arrayPQ = new ArrayHeapMinPQ();
        NaiveMinPQ naivePQ = new NaiveMinPQ();
        arrayPQ.add("item 5", 10);
        naivePQ.add("item 5", 10);
        arrayPQ.add("item 4", 9);
        naivePQ.add("item 4", 9);
        arrayPQ.add("item 3", 8);
        naivePQ.add("item 3", 8);
        arrayPQ.add("item 6", 12);
        naivePQ.add("item 6", 12);
        arrayPQ.add("item 7", 15);
        naivePQ.add("item 7", 15);
        arrayPQ.add("item 1", 3);
        naivePQ.add("item 1", 3);
        arrayPQ.add("item 2", 5);
        naivePQ.add("item 2", 5);
        assertEquals(arrayPQ.getSmallest(), naivePQ.getSmallest());
    }

    @Test
    public void sanitySink() {
        ArrayHeapMinPQ arrayPQ = new ArrayHeapMinPQ();
        NaiveMinPQ naivePQ = new NaiveMinPQ();
        arrayPQ.add("item 5", 10);
        naivePQ.add("item 5", 10);
        arrayPQ.add("item 4", 9);
        naivePQ.add("item 4", 9);
        arrayPQ.add("item 3", 8);
        naivePQ.add("item 3", 8);
        arrayPQ.add("item 6", 12);
        naivePQ.add("item 6", 12);
        arrayPQ.add("item 7", 15);
        naivePQ.add("item 7", 15);
        arrayPQ.add("item 1", 3);
        naivePQ.add("item 1", 3);
        arrayPQ.add("item 2", 5);
        naivePQ.add("item 2", 5);
        assertEquals(arrayPQ.removeSmallest(), naivePQ.removeSmallest());
        assertEquals(arrayPQ.removeSmallest(), naivePQ.removeSmallest());
        assertEquals(arrayPQ.getSmallest(), naivePQ.getSmallest());
    }

    @Test
    public void removeSmallestRandom() {
        ArrayHeapMinPQ arrayPQ = new ArrayHeapMinPQ();
        NaiveMinPQ naivePQ = new NaiveMinPQ();

        /* random addition of elements to both pqs */
        for (int i = 0; i < 10000; i++) {
            double temp = uniform(-500.0, 500.0);
            arrayPQ.add(i, temp);
            naivePQ.add(i, temp);
        }

        /* random removeSmallest calls to verify arrayPQ == naivePQ */
        for (int i = 0; i < 200; i++) {
            assertEquals(arrayPQ.removeSmallest(), naivePQ.removeSmallest());
        }

        /* changing priority of smallest & verifying */
        for (int i = 0; i < 25; i++) {
            double temp = uniform(-500.0, 500);
            arrayPQ.changePriority(arrayPQ.getSmallest(), temp);
            naivePQ.changePriority(naivePQ.getSmallest(), temp);
            assertEquals(arrayPQ.getSmallest(), naivePQ.getSmallest());
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
        List<Double> removeSmallestTimes = new ArrayList<>();
        List<Integer> removeSmallestOps = List.of(1000, 1000, 1000, 1000, 1000, 1000);

        for (int N : listofNs) {
            /* testing ArrayHeapMinPQ construction runtime */
            Stopwatch constructSW = new Stopwatch();
            ArrayHeapMinPQ arrayPQ = new ArrayHeapMinPQ();
            for (int i = 0; i < N; i++) {
                double temp = uniform(-500.0, 500.0);
                arrayPQ.add(i, temp);
            }
            constructionTimes.add(constructSW.elapsedTime());

            /* testing ArrayHeapMinPQ removeSmallest runtime */
            Stopwatch removeSW = new Stopwatch();
            for (int i = 0; i < 1000; i++) {
                arrayPQ.removeSmallest();
            }
            removeSmallestTimes.add(removeSW.elapsedTime());
        }

        List<Double> naiveConstructionTimes = new ArrayList<>();
        List<Double> naiveRemoveSmallestTimes = new ArrayList<>();
        for (int N : listofNs) {
            /* testing ArrayHeapMinPQ construction runtime */
            Stopwatch naiveConstructSW = new Stopwatch();
            NaiveMinPQ naivePQ = new NaiveMinPQ();
            for (int i = 0; i < N; i++) {
                double temp = uniform(-500.0, 500.0);
                naivePQ.add(i, temp);
            }
            naiveConstructionTimes.add(naiveConstructSW.elapsedTime());

            /* testing ArrayHeapMinPQ removeSmallest runtime */
            Stopwatch naiveRemoveSW = new Stopwatch();
            for (int i = 0; i < 1000; i++) {
                naivePQ.removeSmallest();
            }
            naiveRemoveSmallestTimes.add(naiveRemoveSW.elapsedTime());
        }

        /* printing timing tables */
        printTimingTable(listofNs, constructionTimes, listofNs,
                "Timing table for ArrayHeapMinPQ Construction");
        System.out.println();
        printTimingTable(listofNs, removeSmallestTimes, removeSmallestOps,
                "Timing table for ArrayHeapMinPQ removeSmallest");
        System.out.println();
        printTimingTable(listofNs, naiveConstructionTimes, listofNs,
                "Timing table for NaiveMinPQ Construction");
        System.out.println();
        printTimingTable(listofNs, naiveRemoveSmallestTimes, removeSmallestOps,
                "Timing table for NaiveMinPQ removeSmallest");
    }
}
