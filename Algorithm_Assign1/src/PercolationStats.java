import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
    private double [] threscount;
    private double mm;
    private double dev;
    public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
    {

        if (n <= 0 || trials <= 0)
        {
            throw new IllegalArgumentException("n and trials should be positive");
        }

        threscount = new double[trials];
        int x, y;
        for (int i = 0; i < trials; i++)
        {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) // do until percolation
            {
                do{
                    x = StdRandom.uniform(1, n+1); // generate x in [1,n]
                    y = StdRandom.uniform(1, n+1); // generate y in [1,n]
                } while (perc.isOpen(x, y)); // open a block site
                perc.open(x, y);
            }
            threscount[i] = perc.numberOfOpenSites() / ((double)(n * n));
        }
        mm = StdStats.mean(threscount);
        dev = StdStats.stddev(threscount);
    }
    public double mean()                          // sample mean of percolation threshold
    {
        return mm;
    }
    public double stddev()                        // sample standard deviation of percolation threshold
    {
        return dev;
    }
    public double confidenceLo()                  // low  endpoint of 95% confidence interval
    {
        return mm - 1.96 * dev / Math.sqrt(threscount.length);
    }
    public double confidenceHi()                  // high endpoint of 95% confidence interval
    {
        return mm + 1.96 * dev / Math.sqrt(threscount.length);
    }

    public static void main(String[] args)        // test client (described below)
    {
        /*
        String in = args[0];
        int n = Integer.valueOf(in);
        in = args[1];
        int trials = Integer.valueOf(in);
        PercolationStats percstats = new PercolationStats(n,trials);
        System.out.println("mean = "+percstats.mean());
        System.out.println("stddev = "+percstats.stddev());
        System.out.println("95% confidence interval = ["+percstats.confidenceLo()+","+percstats.confidenceHi()+"]");
        */
    }
}
