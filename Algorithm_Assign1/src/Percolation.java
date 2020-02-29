

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int size;
    private boolean[][]matrix;
    private boolean[][]marktop;
    private boolean[][]markbottom;
    private boolean flag;
    private WeightedQuickUnionUF wquf;
    private int numOfOpenSites;
    // Construction
    // create n-by-n grid, with all sites blocked,0 for blocked, 1 for opened
    public Percolation(int n)
    {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        numOfOpenSites = 0;
        size = n;
        matrix = new boolean[n][n];
        marktop = new boolean[n][n];
        markbottom = new boolean[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (i == 0)
                {
                    marktop[i][j] = true; // connected to top
                }
                else if (i == n-1)
                {
                    markbottom[i][j] = true; // connected to bottom
                }
                else
                {
                    marktop[i][j] = false; // initialize all other mark to 0
                    markbottom[i][j] = false;
                }
                matrix[i][j] = false; // initialize all to 0
            }
        }
        // (row x, col y)->((x-1)*n+(y-1))
        wquf = new WeightedQuickUnionUF(n * n);
    }

    // Method:open(int row,int col)
    // open site (row, col) if it is not open already
    // Union if around is open
    public void open(int row, int col)
    {
        if (row < 1 || row > size || col < 1 || col > size)
        {
            throw new IllegalArgumentException("row and col should be in [1,n]");
        }
        if (matrix[row-1][col-1] == true)// has been open
        {
            return;
        }
        else
        {
            matrix[row-1][col-1] = true;
            numOfOpenSites += 1;
            boolean connectedtotop = false;
            boolean connectedtobottom = false;
            if (row == 1) connectedtotop = true;
            if (row == size) connectedtobottom = true;
            if (row > 1 && matrix[row-2][col-1] == true)// if not at top edge and up is open
            {
                int tmp = wquf.find((row-2) * size + col - 1);
                int x = tmp / size;
                int y = tmp % size;
                if (marktop[x][y] == true) connectedtotop = true;
                if (markbottom[x][y] == true) connectedtobottom = true;
                wquf.union((row - 1) * size + col - 1, (row - 2) * size + col - 1); // union up
            }
            if (row < size && matrix[row][col-1] == true)// if not at bottom edge and down is open
            {
                int tmp = wquf.find((row) * size + col - 1);
                int x = tmp / size;
                int y = tmp % size;
                if (marktop[x][y] == true) connectedtotop = true;
                if (markbottom[x][y] == true) connectedtobottom = true;
                wquf.union((row - 1) * size + col - 1, (row) * size + col - 1); // union down
            }
            if (col > 1 && matrix[row-1][col-2] == true)// if not at left edge and left is open
            {
                int tmp = wquf.find((row-1) * size + col - 2);
                int x = tmp / size;
                int y = tmp % size;
                if (marktop[x][y] == true) connectedtotop = true;
                if (markbottom[x][y] == true) connectedtobottom = true;
                wquf.union((row - 1) * size + col - 1, (row - 1) * size + col - 2); // union left
            }
            if (col < size && matrix[row-1][col] == true)// if not at right edge and left is open
            {
                int tmp = wquf.find((row-1) * size + col);
                int x = tmp / size;
                int y = tmp % size;
                if (marktop[x][y] == true) connectedtotop = true;
                if (markbottom[x][y] == true) connectedtobottom = true;
                wquf.union((row - 1) * size + col - 1, (row - 1) * size + col); // union right
            }

            int tmp = wquf.find((row-1) * size + col - 1);
            int x = tmp / size;
            int y = tmp % size;
            if (connectedtotop) marktop[x][y] = true;
            if (connectedtobottom) markbottom[x][y] = true;

            if (connectedtotop && connectedtobottom) flag = true;

        }
    }

    // Method:isopen(int row, int col)
    // is site (row, col) open?
    public boolean isOpen(int row, int col)
    {
        if (row < 1 || row > size || col < 1 || col > size)
        {
            throw new IllegalArgumentException("row and col should be in [1,n]");
        }
        return matrix[row-1][col-1] == true;
    }

    // Method:isFull(int row, int col)
    // is site (row, col) full?
    public boolean isFull(int row, int col)
    {
        if (row < 1 || row > size || col < 1 || col > size)
        {
            throw new IllegalArgumentException("row and col should be in [1,n]");
        }
        int tmp = wquf.find((row-1) * size + col - 1);
        int x = tmp / size;
        int y = tmp % size;
        if (marktop[x][y] == true && matrix[row-1][col-1] == true)
        {
            return true;
        }
        else return false;
    }

    // Method:numberOfOpenSites()
    // Return number of open sites
    public int numberOfOpenSites()
    {
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates()
    {
        return flag;
    }

    // test client (optional)
    public static void main(String[] args)
    {
        //empty main
    }
}
