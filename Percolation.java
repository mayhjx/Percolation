import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] sites;
    private int openedSiteCound;
    private final int n;
    private final WeightedQuickUnionUF weightedQuickUnionUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n);

        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        this.n = n;

        sites = new boolean[n][n];
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                sites[row][col] = false;
            }
        }

        // connect top row to visual top site
        for (int col = 1; col < n; col++) {
            weightedQuickUnionUF.union(col, 0);
        }

        // connect botton row to visual bottom site
        for (int col = n * n - n; col < n * n; col++) {
            weightedQuickUnionUF.union(col, n * n - 1);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isOpen(row, col)) {
            sites[row - 1][col - 1] = true;
            openedSiteCound++;

            // connect it to all of its adjacent open sites
            int current = (row - 1) * n + col - 1;
            int left = (row - 1) * n + col - 2;
            int right = (row - 1) * n + col;
            int top = (row - 2) * n + col - 1;
            int bottom = row * n + col - 1;

            // left site
            if (col > 1 && isOpen(row, col - 1)) {
                weightedQuickUnionUF.union(left, current);
            }

            // right site
            if (col < n && isOpen(row, col + 1)) {
                weightedQuickUnionUF.union(right, current);
            }

            // top site
            if (row > 1 && isOpen(row - 1, col)) {
                weightedQuickUnionUF.union(top, current);
            }

            // bottom site
            if (row < n && isOpen(row + 1, col)) {
                weightedQuickUnionUF.union(bottom, current);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || col <= 0 || row > n || col > n) {
            throw new IllegalArgumentException();
        }
        return sites[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // A full site is an open site that can be
        // connected to an open site in the top row
        // via a chain of neighboring (left, right, up, down) open sites.
        if (!isOpen(row, col)) {
            return false;
        }
        return weightedQuickUnionUF.find(0) == weightedQuickUnionUF.find((row - 1) * n + col - 1);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openedSiteCound;
    }

    // does the system percolate?
    public boolean percolates() {
        // if there is a full site in the bottom row
        for (int col = 1; col <= n; col++) {
            // System.out.println("is full? " + isFull(n, col));
            if (isFull(n, col)) {
                return true;
            }
        }
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        var test = new Percolation(5);
        test.open(1, 1);
        test.open(2, 1);
        test.open(2, 2);
        test.open(3, 2);
        test.open(3, 3);
        test.open(3, 4);
        test.open(4, 4);
        test.open(4, 5);
        test.open(5, 4);

        for (int row = 0; row < test.sites.length; row++) {
            for (int col = 0; col < test.sites[row].length; col++) {
                System.out.print(test.sites[row][col] + " ");
            }
            System.out.println();
        }

        for (int i = 0; i < 25; i++) {
            System.out.println(i + " " + test.weightedQuickUnionUF.find(i));
        }

        System.out.println(test.percolates());
    }
}