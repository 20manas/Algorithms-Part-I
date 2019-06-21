import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> sets;
    public PointSET() {
        sets = new TreeSet<Point2D>();
    }
    public boolean isEmpty() {
        return sets.isEmpty();
    }
    public int size() {
        return sets.size();
    }
    public void insert(Point2D node) {
        if (node == null) {
            throw new IllegalArgumentException();
        } else {
            sets.add(node);
        }
    }
    public boolean contains(Point2D node) {
        if (node == null) {
            throw new IllegalArgumentException();
        } else {
            return sets.contains(node);            
        }
    }
    public void draw() {
        for (Point2D i : sets) {
            i.draw();
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        } else {
            TreeSet<Point2D> ret = new TreeSet<Point2D>();
            for (Point2D i : sets) {
                if (rect.contains(i)) ret.add(i);
            }
            return ret;
        }
    }
    public Point2D nearest(Point2D node) {
        if (node == null) {
            throw new IllegalArgumentException();
        } else {
            Point2D ret = null;
            double dist = 0;
            double temp = 0;
            for (Point2D i : sets) {
                if (ret == null) {
                    ret = i;
                    dist = node.distanceSquaredTo(i);
                } else {
                    temp = node.distanceSquaredTo(i);
                    if (temp < dist) {
                        ret = i;
                        dist = temp;
                    }
                }
            }
            return ret;
        }
    }
}