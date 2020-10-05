package hw2;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int experiments;
    private int size;
    private Percolation grid;
    private double [] xArray;

    public PercolationStats(int N, int T, PercolationFactory pf) {
        experiments = T;
        size = N;
        if (size <= 0 || experiments <= 0) {
            throw new IllegalArgumentException();
        }
        xArray = new double [experiments];
        for (int i = 0; i < T; i++) {
            grid = pf.make(size);
            while (!(grid.percolates())) {
                int row = StdRandom.uniform(0, size);
                int col = StdRandom.uniform(0, size);
                if (!(grid.isOpen(row, col))) {
                    grid.open(row, col);
                }
            } xArray[i] = grid.numberOfOpenSites() / (size * size) * 1.0;
        }
    }
    public double mean() {
        return StdStats.mean(xArray);
    }
    public double stddev() {
        return StdStats.stddev(xArray);
    }
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(experiments));
    }
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(experiments));
    }
}
