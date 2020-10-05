package hw2;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int experiments;
    private int size;
    private Percolation grid;
    private double [] x_array;

    public PercolationStats(int N, int T, PercolationFactory pf) { // perform T independent experiments on an N-by-N grid
        experiments = T;
        size = N;
        if (N <= 0 || T < 0) {
            throw new IllegalArgumentException();
        }
        x_array = new double [T];
        for (int i = 0; i < T; i ++) {
            grid = pf.make(size);
            int xi = 0;
            while (!(grid.percolates())) {
                int row = StdRandom.uniform(0, size);
                int col = StdRandom.uniform(0, size);
                if (grid.isOpen(row, col)) {
                    continue;
                }
                else {
                    grid.open(row, col);
                    xi += 1;
                }
            }
            x_array[i] = xi/N;
        }
    }
    public double mean() { // sample mean of percolation threshold
        return StdStats.mean(x_array);
    }
    public double stddev() { // sample standard deviation of percolation threshold
        return StdStats.stddev(x_array);
    }
    public double confidenceLow() { // low endpoint of 95% confidence interval
        return mean() - (1.96 * stddev() / Math.sqrt(experiments));
    }
    public double confidenceHigh() { // high endpoint of 95% confidence interval
        return mean() + (1.96 * stddev() / Math.sqrt(experiments));
    }
}
