import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private int pointNumber;
    private class Node {
        public Point2D point;
        public Node left;
        public Node right;
        public boolean horVer;
        public Node(Point2D p,  boolean horver) {
            point = p;
            horVer = horver;
            left = null;
            right = null;
        }

        public Node(Point2D p, boolean horver, Node leftChild, Node rightChild) {
            point = p;
            horVer = horver;
            left = leftChild;
            right = rightChild;
        }
    }

    private Node root;
    // construct an empty set of points
    public KdTree() {
        pointNumber = 0;
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointNumber == 0;
    }

    // number of points in the set
    public int size() {
        return pointNumber;
    }

    // add the point to the set (if it is not already in the set)
    private Node insert(Node node, Point2D p, boolean horver) {
        if (node == null) {
            pointNumber += 1;
            return new Node(p, !horver);
        }
        if (node.point.equals(p)) return node;
        if (node.horVer) {
            if (p.y() <= node.point.y()) {
                node.left = insert(node.left, p, !horver);
            }
            else {
                node.right = insert(node.right, p, !horver);
            }
        }
        else {
            if (p.x() <= node.point.x()) {
                node.left = insert(node.left, p, !horver);
            }
            else {
                node.right = insert(node.right, p, !horver);
            }
        }
        return node;
    }
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point2D is NULL!");
        }
        root = insert(root, p, true);
    }

    // does the set contain point p?
    private boolean contains(Node node, Point2D p) {
        if (node == null) {
            return false;
        }
        if (node.point.equals(p)) return true;
        if (node.horVer) {
            if (p.y() <= node.point.y()) return contains(node.left, p);
            else return contains(node.right, p);
        }
        else {
            if (p.x() <= node.point.x()) return contains(node.right, p);
            else return contains(node.right, p);
        }
    }
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point2D is NULL!");
        }
        return contains(root, p);
    }

    // draw all points to standard draw
    private void draw(Node node) {
        if (node != null) {
            node.point.draw();
            draw(node.left);
            draw(node.right);
        }
    }
    public void draw() {
        draw(root);
    }

    // all points that are inside the rectangle (or on the boundary)
    private Iterable<Point2D> range(RectHV rect, Node node, double xMin, double xMax, double yMin, double yMax) {
        List<Point2D> pointInRect = new ArrayList<>();
        if (node == null) return pointInRect;
        if (rect.contains(node.point)) pointInRect.add(node.point);

        if (node.horVer) {
            // Under rect
            if (rect.intersects(new RectHV(xMin, yMin, xMax, node.point.y()))) {
                pointInRect.addAll((List<Point2D>) range(rect, node.left, xMin, xMax, yMin, node.point.y()));
            }

            // Upper rect
            if (rect.intersects(new RectHV(xMin, node.point.y(), xMax, yMax))) {
                pointInRect.addAll((List<Point2D>) range(rect, node.right, xMin, xMax, node.point.y(), yMax));
            }
        }
        else {
            // Left rect
            if (rect.intersects(new RectHV(xMin, yMin, node.point.x(), yMax))) {
                pointInRect.addAll((List<Point2D>) range(rect, node.left, xMin, node.point.x(), yMin, yMax));
            }

            // right rect
            if (rect.intersects(new RectHV(node.point.x(), yMin, xMax, yMax))) {
                pointInRect.addAll((List<Point2D>) range(rect, node.right, node.point.x(), xMax, yMin, yMax));
            }
        }

        return pointInRect;
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("RectHV is NULL");
        }
        return range(rect, root, 0, 1, 0, 1);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    private Point2D nearest (Node node, Point2D p, RectHV rect) {
        if (node == null) return null;
        Point2D pointMaj, pointMin = null;
        RectHV tmpRectMaj, tmpRectMin;
        Node nodeMaj, nodeMin;
        if (node.horVer) {
            if (p.y() <= node.point.y()) {
                // search lower rect first
                tmpRectMaj = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
                tmpRectMin = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
                nodeMaj = node.left;
                nodeMin = node.right;
            }
            else {
                // search upper rect first
                tmpRectMin = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), node.point.y());
                tmpRectMaj = new RectHV(rect.xmin(), node.point.y(), rect.xmax(), rect.ymax());
                nodeMin = node.left;
                nodeMaj = node.right;
            }
        }
        else {
            if (p.x() <= node.point.x()) {
                // search left rect first
                tmpRectMaj = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                tmpRectMin = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                nodeMaj = node.left;
                nodeMin = node.right;
            }
            else {
                // search right rect first
                tmpRectMin = new RectHV(rect.xmin(), rect.ymin(), node.point.x(), rect.ymax());
                tmpRectMaj = new RectHV(node.point.x(), rect.ymin(), rect.xmax(), rect.ymax());
                nodeMin = node.left;
                nodeMaj = node.right;
            }

        }
        pointMaj = nearest(nodeMaj, p, tmpRectMaj);
        if (pointMaj == null || p.distanceTo(pointMaj) > tmpRectMaj.distanceTo(p)) {
            pointMin = nearest(nodeMin, p, tmpRectMin);
        }
        if (pointMaj == null) {
            if (pointMin == null || p.distanceTo(node.point) < p.distanceTo(pointMin)) {
                return node.point;
            }
            else {
                return pointMin;
            }
        }
        else {
            Point2D tmp;
            if (p.distanceTo(node.point) < p.distanceTo(pointMaj)) {
                tmp = node.point;
            }
            else {
                tmp = pointMaj;
            }
            if (pointMin == null || p.distanceTo(tmp) < p.distanceTo(pointMin)) {
                return tmp;
            }
            else {
                return pointMin;
            }
        }
    }
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Point2D is NULL!");
        }
        return nearest(root, p, new RectHV(0, 0, 1, 1));
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        System.out.println("Unit test for point SET.");
    }

}