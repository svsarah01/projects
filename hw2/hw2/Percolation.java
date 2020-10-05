package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean [][] grid;
    private int size;
    private WeightedQuickUnionUF topSite;
    private WeightedQuickUnionUF bottomSite;
    private int totalOpen;
    private int topNode;
    private int bottomNode;

    public Percolation(int N) { // create N-by-N grid, with all sites initially blocked
        size = N;
        totalOpen = 0;
        grid = new boolean[N][N];
        topSite = new WeightedQuickUnionUF(N * N + 1);
        bottomSite = new WeightedQuickUnionUF(N * N + 2);
        topNode = N * N;
        bottomNode = (N * N) + 1;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
            }
        }
    }

    private int xyTo1D(int row, int col) {
        return (size * row) + col;
    }

    private void isValid (int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Not a valid row or column index!");
        }
    }

    public void open(int row, int col) { // open the site (row, col) if it is not open already
        isValid(row);
        isValid(col);
        if (!(grid[row][col])) {
            grid[row][col] = true;
            totalOpen += 1;
        }
        if (row == 0) {
            bottomSite.union(topNode, xyTo1D(row, col));
            topSite.union(topNode, xyTo1D(row, col));
        }
        if (row == size - 1) {
            bottomSite.union(bottomNode, xyTo1D(row, col));
        }
        if (row != size - 1 && grid[row + 1][col]) {
            bottomSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));
            topSite.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        if (row != 0 && grid[row - 1][col]) {
            bottomSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));
            topSite.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        if (col != size - 1 && grid[row][col + 1]) {
            bottomSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
            topSite.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
        if (col != 0 && grid[row][col - 1]) {
            bottomSite.union(xyTo1D(row, col), xyTo1D(row, col - 1));
            topSite.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }
    }
    public boolean isOpen(int row, int col) { // is the site (row, col) open?
        isValid(row);
        isValid(col);
        return grid[row][col];
    }
    public boolean isFull(int row, int col) { // is the site (row, col) full?
        isValid(row);
        isValid(col);
        return topSite.connected(topNode, xyTo1D(row, col));
    }
    public int numberOfOpenSites() {  // number of open sites
        return totalOpen;
    }
    public boolean percolates() { // does the system percolate?
        return bottomSite.connected(topNode, bottomNode);
    }
    public static void main(String[] args) { // use for unit testing (not required, but keep this here for the autograder)

    }
}
