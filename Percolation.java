import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // Private variables
    private final WeightedQuickUnionUF uf;
    private boolean[][] mat;
    private boolean[][] isConToBot;
    private boolean isRootConToBot;
    private final short n;
    private int openSites;
    //  Constructor
    public Percolation(int enn) {
        if (enn <= 0) throw new IllegalArgumentException("Out of Bounds!");
        n = (short) enn;
        uf = new WeightedQuickUnionUF((n*n) + 1);
        mat = new boolean[n][n];
        isConToBot = new boolean[n][n];
        openSites = 0;
    }
    // Private Methods
    private int get1d(short i, short j) {
        return ((int) i * (int) n) + j + 1;
    }
    private short[] get2d(int a) {
        short i = (short) ((a - 1) / n);
        short j = (short) ((a - 1) - (i * n));
        short[] ret = {i, j};
        return ret;
    }
    private short[][] getNeighbours(short i, short j) {
        byte count = 0, size = 4;
        if (((i == 0) || (i == (n - 1))) && ((j == 0) || (j == (n - 1)))) size = 2; // if (i, j) is at corners
        else if ((i == 0) || (i == (n - 1)) || (j == 0) || (j == (n - 1))) size = 3; // if (i, j) is at sides

        short[][] ret = new short[size][2];
        
        if (!(i == 0)) {
            ret[count][0] = (short) (i - 1);
            ret[count][1] = j;
            count++;
        }
        if (!(i == (n - 1))) {
            ret[count][0] = (short) (i + 1);
            ret[count][1] = j;
            count++;
        }
        if (!(j == 0)) {
            ret[count][0] = i;
            ret[count][1] = (short) (j - 1);
            count++;
        }
        if (!(j == (n - 1))) {
            ret[count][0] = i;
            ret[count][1] = (short) (j + 1);
        }
        return ret;
    }
    private boolean getConToBot(short i, short j) {
        int root = uf.find(get1d(i, j));
        if (root == 0) {
            return isRootConToBot;
        } else {
            short[] root2d = get2d(root);
            return isConToBot[root2d[0]][root2d[1]];
        }
    }
    private void setConToBot(short i, short j) {
        int root = uf.find(get1d(i, j));
        if (root == 0) {
            isRootConToBot = true;
        } else {
            short[] root2d = get2d(root);
            isConToBot[root2d[0]][root2d[1]] = true;
        }
    }
    private void unite(short i, short j) {
        short[][] neigh = getNeighbours(i, j);
        boolean changeIsConToBot = false;
        if (i == 0) {
            uf.union(get1d(i, j), 0); // if at top, union with first
        }
        if (i == (n - 1)) changeIsConToBot = true;

        for (byte a = 0; a < neigh.length; a++) {
            short x = neigh[a][0];
            short y = neigh[a][1];
            if (mat[x][y]) {
                if (!isRootConToBot && !changeIsConToBot) changeIsConToBot = getConToBot(x, y);

                uf.union(get1d(i, j), get1d(x, y));
            }
        }
        if (!isRootConToBot && changeIsConToBot) {
            setConToBot(i, j);
        }
    }
    private void openPriv(short i, short j) {
        if (!mat[i][j]) {
            ++openSites;
            mat[i][j] = true;
            unite(i, j);
        }
    }
    private boolean isFullPriv(short i, short j) {
        if (mat[i][j]) {
            return uf.connected(get1d(i, j), 0);
        } else return false;
    }
    private void excep(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n) throw new IllegalArgumentException("Out of Bounds!");
    }
    //  Public Methods
    public void open(int i, int j) {
        excep(i, j);
        openPriv((short) --i, (short) --j);
    }
    public boolean isOpen(int i, int j) {
        excep(i, j);
        return mat[--i][--j];
    }
    public boolean isFull(int i, int j) {
        excep(i, j);
        return isFullPriv((short) --i, (short) --j);
    }
    public int numberOfOpenSites() {
        return openSites;
    }
    public boolean percolates() {
        if (isRootConToBot) {
            return true;
        } else {
            int root = uf.find(0);
            if (root == 0) {
                return false;
            } else {
                short[] root2d = get2d(root);
                return isConToBot[root2d[0]][root2d[1]];    
            }  
        }
    }
}