import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that collects timing information about AList construction.
 */
public class TimeAList {
    private static void printTimingTable(List<Integer> Ns, List<Double> times, List<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        List<Integer> Ns = List.of(1000, 2000, 4000, 8000, 16000, 32000, 64000, 128000);
        List<Double> times = new LinkedList<>();
        AList<Integer> al = new AList<>();
        for (int n : Ns) {
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < n; i++) {
                al.addLast(100);
            }
            times.add(sw.elapsedTime());
        }
        printTimingTable(Ns, times, Ns);
    }


}
