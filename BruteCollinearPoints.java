import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private final LineSegment[] lines;
    private final int totalSegments;
    public BruteCollinearPoints(Point[] points) {

        if (points == null) throw new IllegalArgumentException();

        Point[] pointsData = new Point[points.length];
        for (int a = 0; a < points.length; a++) {
            if (points[a] == null) throw new IllegalArgumentException();
            pointsData[a] = points[a];
        }

        int total = 0;
        ArrayList<Integer> start = new ArrayList<Integer>();
        ArrayList<Integer> end = new ArrayList<Integer>();
        
        Arrays.sort(pointsData);
        
        for (int i = 0; i < pointsData.length; i++) {
            for (int j = i + 1; j < pointsData.length; j++) {

                if (pointsData[i].compareTo(pointsData[j]) == 0) throw new IllegalArgumentException();

                for (int k = j + 1; k < pointsData.length; k++) {
                    if (doubleEq(pointsData[i].slopeTo(pointsData[j]), pointsData[i].slopeTo(pointsData[k]))) {
                        
                        for (int a = k + 1; a < pointsData.length; a++) {
                            if (doubleEq(pointsData[i].slopeTo(pointsData[k]), pointsData[i].slopeTo(pointsData[a]))) {
                                total++;
                                start.add(total - 1, i);
                                end.add(total - 1, a);
                                break;
                            }
                        }
                    }
                }
            }
        }
        totalSegments = total;
        lines = new LineSegment[totalSegments];
        for (int m = 0; m < totalSegments; m++) {
            lines[m] = new LineSegment(pointsData[start.get(m)], pointsData[end.get(m)]);
        }
    }
    private boolean doubleEq(double a, double b) {
        if (a < b) return false;
        else if (a > b) return false;
        else return true;
    }
    public int numberOfSegments() {
        int ret = totalSegments;
        return ret;
    }
    public LineSegment[] segments() {
        LineSegment[] ret = new LineSegment[lines.length];
        for (int i = 0; i < lines.length; i++) ret[i] = lines[i];
        return ret;
    }
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] pointsData = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            pointsData[i] = new Point(x, y);
        }

        // draw the pointsData
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : pointsData) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(pointsData);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}