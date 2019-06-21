import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private final int t;
    private final int n;
    private double meanVal;
    private double stddevVal;
    private double[] testResults;
    public PercolationStats(int enn, int trials) {
        if (enn <= 0 || trials <= 0)throw new IllegalArgumentException("Out of Bounds!");
        n = enn;
        t = trials;
        meanVal = 0.0d;
        stddevVal = 0.0d;
        testResults = new double[t];
    }
    private double test() {
        Percolation p = new Percolation(n);
        int i, j;
        while (!p.percolates()) {
            i = StdRandom.uniform(n) + 1;
            j = StdRandom.uniform(n) + 1;
            if (!p.isOpen(i, j)) p.open(i, j);
        }
        double ret = (p.numberOfOpenSites() / (double) (n * n));
        return ret;
    }
    private void performTests() {
        int i;
        for (i = 0; i < t; i++) {
            testResults[i] = test();
        }
    }
    private double confidence(double m) {
        if (meanVal == 0.0d) mean();
        if (stddevVal == 0.0d) stddev();
        return (meanVal + ((m * 1.96 * stddevVal) / Math.sqrt(t)));
    }
    // Public methods
    public double mean() {
        if (testResults[0] == 0.0d) performTests();
        meanVal = StdStats.mean(testResults);
        return meanVal;
    }
    public double stddev() {
        if (testResults[0] == 0.0d) performTests();
        stddevVal = StdStats.stddev(testResults);
        return stddevVal;
    }
    public double confidenceLo() {
        return confidence(-1);
    }
    public double confidenceHi() {
        return confidence(1);
    }
    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}