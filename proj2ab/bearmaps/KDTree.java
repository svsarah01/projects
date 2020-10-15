package bearmaps;

import java.util.List;

public class KDTree implements PointSet {
    private class Node {
        Node left;
        Node right;
        Point p;
        boolean xOrY;

        private Node(Point point, boolean xY) {
            p = point;
            left = null;
            right = null;
            xOrY = xY;
        }
    }

    private Node put(Point p, Node n, boolean xOrY) {
        if (n == null) {
            n = new Node(p, xOrY);
        } else if (p == n.p) {
            return n;
        } else {
            boolean cmp;
            if (n.xOrY) {
                cmp = p.getX() < n.p.getX();
            } else {
                cmp = p.getY() < n.p.getY();
            }
            if (cmp) {
                n.left = put(p, n.left, xOrY);
            } else {
                n.right = put(p, n.right, xOrY);
            }
        }
        return n;
    }

    Node root;

    public KDTree(List<Point> points) {
        root = null;
        for (int i = 0; i < points.size(); i++) {
            if (i % 2 == 0) {
                root = put(points.get(i), root, true);
            } else {
                root = put(points.get(i), root, false);
            }
        }
    }

    private Node nearestHelper(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Point.distance(n.p, goal) < Point.distance(best.p, goal)) {
            best = n;
        }
        boolean cmp;
        Point bestPossible;
        if (n.xOrY) {
            cmp = goal.getX() < n.p.getX();
            bestPossible = new Point(n.p.getX(), goal.getY());
        } else {
            cmp =  goal.getY() < n.p.getY();
            bestPossible = new Point(goal.getX(), n.p.getY());
        }
        Node goodSide;
        Node badSide;
        if (cmp) {
            goodSide = n.left;
            badSide = n.right;
        } else {
            goodSide = n.right;
            badSide = n.left;
        }
        best = nearestHelper(goodSide, goal, best);
        /* pruning */
        if (Point.distance(bestPossible, goal) < Point.distance(best.p, goal)) {
            best = nearestHelper(badSide, goal, best);
        }
        return best;
    }

    public Point nearest(double x, double y) {
        Node nearestNode =  nearestHelper(root, new Point(x, y), root);
        return nearestNode.p;
    }
}

