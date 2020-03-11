import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;
    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p:pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> pointInRect = new ArrayList<>();
        double xMin = rect.xmin(), yMin = rect.ymin(), xMax = rect.xmax(), yMax = rect.ymax();
        for (Point2D p:pointSet) {
            double tmpX = p.x();
            double tmpY = p.y();
            if (tmpX <= xMax && tmpX >= xMin && tmpY <= yMax && tmpY >= yMin) {
                pointInRect.add(p);
            }
        }
        return pointInRect;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        double minDis = Double.MAX_VALUE;
        Point2D nearestPoint = null;
        for (Point2D pp:pointSet) {
            if (pp.distanceTo(p) < minDis) {
                minDis = pp.distanceTo(p);
                nearestPoint = pp;
            }
        }
        return nearestPoint;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        System.out.println("Unit test for point SET.");
    }

}