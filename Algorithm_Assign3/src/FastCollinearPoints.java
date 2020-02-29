import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment [] lines;
    private double [] slopes;
    private Point [] mypoints;
    private int linecnt;
    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        Point [] pointscopy = Arrays.copyOf(points,points.length);
        if (pointscopy == null)
        {
            throw new java.lang.IllegalArgumentException("null Point[]");
        }
        for (int i = 0; i < pointscopy.length; i++)
        {
            if (pointscopy[i] == null)
            {
                throw new java.lang.IllegalArgumentException("null Point");
            }
        }
        for (int i = 0; i < pointscopy.length - 1; i++)
        {
            for (int j = i + 1; j < pointscopy.length; j++)
            {
                if (pointscopy[j].compareTo(pointscopy[i]) == 0)
                {
                    throw new java.lang.IllegalArgumentException("repeat point");
                }
            }
        }
        linecnt = 0;
        mypoints = new Point[pointscopy.length];
        lines = new LineSegment[pointscopy.length];
        slopes = new double[pointscopy.length];

        for (int i = 0; i < pointscopy.length - 1; i++)
        {
            Arrays.sort(pointscopy,i + 1, pointscopy.length);
            Arrays.sort(pointscopy,i + 1, pointscopy.length, pointscopy[i].slopeOrder());
            int start = i + 1;
            int end = i + 2;
            int cnt = 2; // p and start
            double prevslope = Double.NEGATIVE_INFINITY;
            while (start < pointscopy.length)
            {
                /*
                if (pointscopy[start].compareTo(pointscopy[i]) == 0)
                {
                    throw new java.lang.IllegalArgumentException("repeat Point");
                }
                */
                //find colline pointscopy
                double startToI = pointscopy[start].slopeTo(pointscopy[i]);
                while (end < pointscopy.length)
                {
                    /*
                    if (pointscopy[end].compareTo(pointscopy[i]) == 0)
                    {
                        throw new java.lang.IllegalArgumentException("repeat Point");
                    }
                    */
                    if (pointscopy[end].slopeTo(pointscopy[i]) == startToI)
                    {
                        end++;
                        cnt++;
                    }
                    else
                    {
                        break;
                    }
                }
                if (cnt >= 4) // found
                {
                    // check repeat

                    boolean repeated = false;
                    for (int j = 0; j < linecnt; j++)
                    {
                        double tmp = pointscopy[start].slopeTo(mypoints[j]);
                        if (startToI == slopes[j] &&
                                (tmp == slopes[j] || tmp == Double.NEGATIVE_INFINITY))
                        {
                            repeated = true;
                            break;
                        }
                    }

                    // add line segment
                    if (!repeated)
                    {
                        if (linecnt >= slopes.length)
                        {
                            double [] tmpslopes = new double[slopes.length * 2];
                            Point [] tmpmypoints = new Point[slopes.length * 2];
                            LineSegment [] tmplines = new LineSegment[slopes.length * 2];
                            for (int m = 0; m < slopes.length; m++)
                            {
                                tmpslopes[m] = slopes[m];
                                tmpmypoints[m] = mypoints[m];
                                tmplines[m] = lines[m];
                            }
                            slopes = tmpslopes;
                            mypoints = tmpmypoints;
                            lines = tmplines;
                        }
                        slopes[linecnt] = startToI;
                        mypoints[linecnt] = pointscopy[start];
                        if (pointscopy[i].compareTo(pointscopy[start]) < 0)
                        {
                            lines[linecnt++] = new LineSegment(pointscopy[i], pointscopy[end - 1]);
                        }
                        else if (pointscopy[i].compareTo(pointscopy[end - 1]) > 0)
                        {
                            lines[linecnt++] = new LineSegment(pointscopy[start], pointscopy[i]);
                        }
                        else
                        {
                            lines[linecnt++] = new LineSegment(pointscopy[start], pointscopy[end - 1]);
                        }
                    }
                }
                start = end;
                end++;
                cnt = 2;
            }
        }
        LineSegment [] tmp = new LineSegment[linecnt];
        for (int i = 0; i < linecnt; i++)
        {
            tmp[i] = lines[i];
        }
        lines = tmp;
    }
    public int numberOfSegments()        // the number of line segments
    {
        return linecnt;
    }
    public LineSegment[] segments()                // the line segments
    {
        return lines;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}