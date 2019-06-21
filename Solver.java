import java.util.Comparator;
import java.util.LinkedList;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class Solver {
    private final int totalMoves;
    private final Node sol;
    private final boolean canBeSolved;
    private class Node implements Comparable<Node> {
        public final Board board;
        public final Node prev;
        public final int moves;
        private final int man;
        private final Node root;
        public Node(Board b) {
            board = b;
            prev = null;
            moves = 0;
            man = board.manhattan();
            this.root = null;
        }
        public Node(Board b, Node p) {
            board = b;
            prev = p;
            moves = prev.moves + 1;
            man = board.manhattan();
            if (p.root == null) {
                this.root = p;
            } else {
                this.root = p.root;
            }

        }
        public Node getRoot() {
            return this.root;
        }
        public int compareTo(Node that) {
            int sum1 = this.man + this.moves;
            int sum2 = that.man + that.moves;
            if (sum1 == sum2) {
                return this.man - that.man;
            } else {
                return sum1 - sum2;
            }
        }
    };
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        Node prime = new Node(initial);
        Node twin = new Node(prime.board.twin());
        Node node = prime;

        MinPQ<Node> pq = new MinPQ<Node>();
        pq.insert(prime);
        pq.insert(twin);
        while (!node.board.isGoal()) {
            node = pq.delMin();
            for (Board i : node.board.neighbors()) {
                if (node.prev == null || !node.prev.board.equals(i)) pq.insert(new Node(i, node));
            }
        }
        if (node.getRoot() == prime || prime.board.isGoal()) {
            canBeSolved = true;
            sol = node;
            totalMoves = sol.moves;
        } else {
            canBeSolved = false;
            sol = null;
            totalMoves = -1;
        }
    }
    public boolean isSolvable() {
        return canBeSolved;
    }
    public int moves() {
        return totalMoves;
    }
    public Iterable<Board> solution() {
        if (canBeSolved) {
            LinkedList<Board> ret = new LinkedList<Board>();
            Node a = sol;
            while (a != null) {
                // addFirst effectively reverses order. So order: topmost Node ... to ... sol (at the end)
                ret.addFirst(a.board);
                a = a.prev;
            }
            return ret;
        } else {
            return null;
        }
    }
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}