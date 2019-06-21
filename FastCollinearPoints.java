import java.util.ArrayList;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private final int totalSegments;
    private final LineSegment[] lines;
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        Point[] pointsData = new Point[points.length];
        for (int a = 0; a < points.length; a++) {
            if (points[a] == null) throw new IllegalArgumentException();
            pointsData[a] = points[a];
        }

        int total = 0;
        ArrayList<Point> start = new ArrayList<Point>();
        ArrayList<Point> end = new ArrayList<Point>();
        Point[] temp = new Point[pointsData.length];
        for (int x = 0; x < pointsData.length; x++) temp[x] = pointsData[x];
        Arrays.sort(pointsData);

        for (int i = 0; i < pointsData.length; i++) {
            for (int y = 0; y < pointsData.length; y++) temp[y] = pointsData[y];
            Arrays.sort(temp, 0, temp.length, pointsData[i].slopeOrder());
            int sameSlopeCount = 1;
            double sameSlope = Double.NEGATIVE_INFINITY;
            Point sameSlopeMin = pointsData[i];
            Point sameSlopeMax = pointsData[i];
            int sameElement = 0;
            for (int j = 0; j < temp.length; j++) {
                if (pointsData[i].compareTo(temp[j]) == 0) {
                    sameElement++;
                    if (sameElement > 1) throw new IllegalArgumentException();
                } else if (doubleEq(sameSlope, pointsData[i].slopeTo(temp[j]))) {
                    sameSlopeCount++;
                    sameSlopeMin = min(sameSlopeMin, temp[j]);
                    sameSlopeMax = max(sameSlopeMax, temp[j]);
                    if (sameSlopeMin == pointsData[i]) {
                        if (sameSlopeCount == 4) {
                            total++;
                            start.add(total - 1, sameSlopeMin);
                            end.add(total - 1, sameSlopeMax);
                        } else if (sameSlopeCount > 4) {
                            start.set(total - 1, sameSlopeMin);
                            end.set(total - 1, sameSlopeMax);
                        }
                    }
                } else {
                    sameSlope = pointsData[i].slopeTo(temp[j]);
                    sameSlopeCount = 2;
                    sameSlopeMin = min(pointsData[i], temp[j]);
                    sameSlopeMax = max(pointsData[i], temp[j]);
                }
            }
            sameSlope = Double.NEGATIVE_INFINITY;
        }
        totalSegments = total;
        lines = new LineSegment[totalSegments];
        for (int k = 0; k < totalSegments; k++) {
            lines[k] = new LineSegment(start.get(k), end.get(k));
        }
    }
    private boolean doubleEq(double a, double b) {
        if (a < b) return false;
        else if (a > b) return false;
        else return true;
    }
    private Point min(Point a, Point b) {
        Point c;
        if (a.compareTo(b) > 0) c = b;
        else c = a;
        return c;
    }
    private Point max(Point a, Point b) {
        if (a.compareTo(b) < 0) return b;
        else return a;
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
        StdDraw.setPenRadius(0.005);
        for (Point p : pointsData) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        StdDraw.setPenRadius(0.001);
        FastCollinearPoints collinear = new FastCollinearPoints(pointsData);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}