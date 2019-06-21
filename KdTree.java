import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class KdTree {
    private final Node root;
    private int sz;
    private class Node {
        public Node leftChild;
        public Node rightChild;
        public boolean xCompare;
        private Point2D point;
        public Node(Point2D p) {
            point = p;
        }
        public Node() {
            this(null);
        }
        public boolean isEmpty() {
            return point == null;
        }
        public byte compareTo(Node that) {
            if (xCompare) {
                double xThis = this.point.x();
                double xThat = that.point.x();
                if (xThis > xThat) return -1;
                else if (xThis < xThat) return 1;
                else return 0;
            } else {
                double yThis = this.point.y();
                double yThat = that.point.y();
                if (yThis > yThat) return -1;
                else if (yThis < yThat) return 1;
                else return 0;
            }
        }
        public double coord() {
            if (xCompare) {
                return point.x();
            } else {
                return point.y();
            }
        }
    };
    private class Recur {
        private Node node;
        public boolean processedOnce;
        public boolean isRight;
        public double gap;
        public Recur(Node n) {
            node = n;
        }
    }
    public KdTree() {
        root = new Node();
        sz = 0;
    }
    public boolean isEmpty() {
        return root.isEmpty();
    }
    public int size() {
        return sz;
    }
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        } else {
            sz += 1;
            if (root.isEmpty()) {
                root.point = p;
                root.xCompare = true;
            } else {
                Node temp = root;
                Node node = new Node(p);
                while (true) {
                    byte com = temp.compareTo(node);
                    if (com == 0 && temp.point.equals(node.point)) {
                        sz -= 1;
                        return;
                    }
                    if (com >= 0) {
                        if (temp.rightChild != null) {
                            temp = temp.rightChild;
                        } else {
                            node.xCompare = !temp.xCompare;
                            temp.rightChild = node;
                            break;
                        }
                    } else { // if (com < 0)
                        if (temp.leftChild != null) {
                            temp = temp.leftChild;
                        } else {
                            node.xCompare = !temp.xCompare;
                            temp.leftChild = node;
                            break;
                        }
                    }
                }
            }
        }
    }
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        } else {
            if (root.isEmpty()) {
                return false;
            } else {
                Node temp = root;
                Node node = new Node(p);
                while (true) {
                    byte com = temp.compareTo(node);
                    if (com == 0 && temp.point.equals(node.point)) return true;
                    if (com >= 0) {
                        if (temp.rightChild != null) {
                            temp = temp.rightChild;
                        } else {
                            return false;
                        }
                    } else {
                        if (temp.leftChild != null) {
                            temp = temp.leftChild;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
    }
    public void draw() {
        if (root == null || root.point == null) return;
        Queue<Node> nodes = new LinkedList<Node>();
        nodes.add(root);

        while (!nodes.isEmpty()) {
            Node node = nodes.poll();
            node.point.draw();
            if (node.leftChild != null && node.leftChild.point != null) nodes.add(node.leftChild);
            if (node.rightChild != null && node.rightChild.point != null) nodes.add(node.rightChild);
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        else {
            if (root == null || root.point == null) return null;

            double xMin = rect.xmin(), xMax = rect.xmax(), yMin = rect.ymin(), yMax = rect.ymax();

            Queue<Node> nodes = new LinkedList<Node>();
            Queue<Point2D> ret = new LinkedList<Point2D>();
            nodes.add(root);

            while (!nodes.isEmpty()) {
                Node node = nodes.poll();
                if (rect.contains(node.point)) ret.add(node.point);
                if (node.xCompare) {
                    double x = node.point.x();
                    if (xMin <= x && node.leftChild != null && node.leftChild.point != null) nodes.add(node.leftChild);
                    if (xMax >= x && node.rightChild != null && node.rightChild.point != null) nodes.add(node.rightChild);
                } else {
                    double y = node.point.y();
                    if (yMin <= y && node.leftChild != null && node.leftChild.point != null) nodes.add(node.leftChild);
                    if (yMax >= y && node.rightChild != null && node.rightChild.point != null) nodes.add(node.rightChild);
                }
            }
            return ret;
        }
    }
    public Point2D nearest(Point2D query) {
        if (query == null) {
            throw new IllegalArgumentException();
        } else {
            if (root == null || root.point == null) return null;

            Point2D ret = null;
            double min = Double.POSITIVE_INFINITY;
            
            double temp1 = 0, xQuery = query.x(), yQuery = query.y();

            Stack<Recur> stak = new Stack<Recur>();
            stak.push(new Recur(root));
            while(!stak.isEmpty()) {
                Recur elem = stak.pop();
                if (!elem.processedOnce) {
                    temp1 = query.distanceSquaredTo(elem.node.point);
                    if (temp1 < min) {
                        min = temp1;
                        ret = elem.node.point;
                    }
                    stak.push(elem);
                    elem.gap = (elem.node.xCompare ? xQuery : yQuery) - elem.node.coord();
                    if (elem.gap < 0) {
                        if (elem.node.leftChild != null && elem.node.leftChild.point != null) stak.push(new Recur(elem.node.leftChild));
                        elem.processedOnce = true;
                    } else {
                        if (elem.node.rightChild != null && elem.node.rightChild.point != null) stak.push(new Recur(elem.node.rightChild));
                        elem.processedOnce = elem.isRight = true;
                    }
                } else {
                    if (elem.isRight) {
                        if (min > (elem.gap * elem.gap) && elem.node.leftChild != null && elem.node.leftChild.point != null) {
                            stak.push(new Recur(elem.node.leftChild));
                        }
                    } else {
                        if (min > (elem.gap * elem.gap) && elem.node.rightChild != null && elem.node.rightChild.point != null) {
                            stak.push(new Recur(elem.node.rightChild));
                        }
                    }
                }
            }
            return ret;
        }
    }
}