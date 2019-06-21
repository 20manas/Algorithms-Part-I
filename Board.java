import java.util.LinkedList;

public class Board {
    private final short dim;
    private final char[] bloks;
    private final int ham;
    private final int man;
    private final short iZero;
    private final short jZero;
    public Board(int[][] blocks) {
        dim = (short) blocks.length;
        bloks = new char[dim * dim];
        int hamTemp = 0;
        int manTemp = 0;
        short iZeroTemp = 0;
        short jZeroTemp = 0;
        for (short i = 0; i < dim; i++) {
            for (short j = 0; j < dim; j++) {
                int k = (i * dim) + j;
                int val = blocks[i][j];
                bloks[k] = (char) val;
                if (val != 0) {
                    if (val != k + 1) {
                        hamTemp += 1;
                        int iCorrect = (val - 1) / dim;
                        int jCorrect = (val - 1) - (iCorrect * dim);
                        manTemp += (Math.abs(iCorrect - i) + Math.abs(jCorrect - j));
                    }
                } else {
                    iZeroTemp = i;
                    jZeroTemp = j;
                }
            }
        }
        ham = hamTemp;
        man = manTemp;
        iZero = iZeroTemp;
        jZero = jZeroTemp;
    }
    public int dimension() {
        return dim;
    }
    public int hamming() {
        return ham;
    }
    public int manhattan() {
        return man;
    }
    public boolean isGoal() {
        return ham == 0;
    }
    public Board twin() {
        int[][] blocks = new int[dim][dim];
        boolean found = false;
        short iFound = 0;
        short jFound = 0;
        for (short i = 0; i < dim; i++) {
            for (short j = 0; j < dim; j++) {
                blocks[i][j] = (int) bloks[(i * dim) + j];
                if (!found && j != dim - 1 && bloks[(i * dim) + j] != 0 && bloks[(i * dim) + j + 1] != 0) {
                    found = true;
                    iFound = i;
                    jFound = j;
                }
            }
        }
        int temp = blocks[iFound][jFound];
        blocks[iFound][jFound] = blocks[iFound][jFound + 1];
        blocks[iFound][jFound + 1] = temp;
        return new Board(blocks);
    }
    public boolean equals(Object a) {
        if (a == this) return true;
        if (a == null) return false;
        if (a.getClass() != this.getClass()) return false;
        Board that = (Board) a;
        if (that.dim != this.dim || that.ham != this.ham || that.man != this.man) {
            return false;
        }
        boolean ret = true;
        for (int i = 0; i < (dim * dim); i++) {
            if (this.bloks[i] != that.bloks[i]) ret = false;
            if (!ret) break;
        }
        return ret;
    }
    public Iterable<Board> neighbors() {
        Board board;
        int[][] blocks = new int[dim][dim];
        int temp;
        short i = 0;
        short j = 0;
        for (i = 0; i < dim; i++) {
            for (j = 0; j < dim; j++) {
                blocks[i][j] = (int) bloks[(i * dim) + j];
            }
        }
        LinkedList<Board> ret = new LinkedList<Board>();
        i = iZero;
        j = jZero;
        if (j < dim - 1) {
            temp = blocks[i][j];
            blocks[i][j] = blocks[i][j + 1];
            blocks[i][j + 1] = temp;
            board = new Board(blocks);
            ret.add(board);
            temp = blocks[i][j];
            blocks[i][j] = blocks[i][j + 1];
            blocks[i][j + 1] = temp;
        }
        if (j > 0) {
            temp = blocks[i][j];
            blocks[i][j] = blocks[i][j - 1];
            blocks[i][j - 1] = temp;
            board = new Board(blocks);
            ret.add(board);
            temp = blocks[i][j];
            blocks[i][j] = blocks[i][j - 1];
            blocks[i][j - 1] = temp;
        }
        if (i < dim - 1) {
            temp = blocks[i][j];
            blocks[i][j] = blocks[i + 1][j];
            blocks[i + 1][j] = temp;
            board = new Board(blocks);
            ret.add(board);
            temp = blocks[i][j];
            blocks[i][j] = blocks[i + 1][j];
            blocks[i + 1][j] = temp;
        }
        if (i > 0) {
            temp = blocks[i][j];
            blocks[i][j] = blocks[i - 1][j];
            blocks[i - 1][j] = temp;
            board = new Board(blocks);
            ret.add(board);
            temp = blocks[i][j];
            blocks[i][j] = blocks[i - 1][j];
            blocks[i - 1][j] = temp;
        }
        return ret;
    }
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(dim);
        ret.append("\n");
        for (int i = 0; i < (dim * dim); i++) {
            ret.append((int) bloks[i]);
            ret.append(" ");
            if ((i + 1) % dim == 0) ret.append("\n");
        }
        return ret.toString();
    }
    public static void main(String[] args) {
        int[][] blocks = new int[][] {
            {1, 2, 3, 4, 5, 7, 14},
            {8, 9, 10, 11, 12, 13, 6},
            {15, 16, 17, 18, 19, 20, 21},
            {22, 23, 24, 25, 26, 27, 28},
            {29, 30, 31, 32, 33, 0, 34},
            {36, 37, 38, 39, 40, 41, 35},
            {43, 44, 45, 46, 47, 48, 42}
        };
        Board b = new Board(blocks);
        for (Board i : b.neighbors()) System.out.println(i);
    }
}