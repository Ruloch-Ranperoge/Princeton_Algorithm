import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] lines;
    private int linecnt;
    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        Point [] pointscopy = Arrays.copyOf(points, points.length);
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
        linecnt = 0;
        lines = new LineSegment[pointscopy.length];
        for (int i = 0; i < pointscopy.length; i++)
        {
            // if (pointscopy[i] == null) throw new java.lang.IllegalArgumentException("null point");
            for (int j = i + 1; j < pointscopy.length; j++)
            {
                // if (pointscopy[j] == null) throw new java.lang.IllegalArgumentException("null point");
                if (pointscopy[j].compareTo(pointscopy[i]) == 0)
                {
                    throw new java.lang.IllegalArgumentException("repeat point");
                }
                for (int k = j + 1; k < pointscopy.length; k++)
                {
                    // if (pointscopy[k] == null) throw new java.lang.IllegalArgumentException("null point");
                    if (pointscopy[k].compareTo(pointscopy[j]) * pointscopy[k].compareTo(pointscopy[i]) == 0)
                    {
                        throw new java.lang.IllegalArgumentException("repeat point");
                    }
                    for (int l = k + 1; l <pointscopy.length; l++)
                    {
                        // if (pointscopy[l] == null) throw new java.lang.IllegalArgumentException("null point");
                        if (pointscopy[l].compareTo(pointscopy[k]) * pointscopy[l].compareTo(pointscopy[j]) * pointscopy[l].compareTo(pointscopy[i]) == 0)
                        {
                            throw new java.lang.IllegalArgumentException("repeat point");
                        }
                        double slope1 = pointscopy[i].slopeTo(pointscopy[j]);
                        double slope2 = pointscopy[j].slopeTo(pointscopy[k]);
                        double slope3 = pointscopy[k].slopeTo(pointscopy[l]);
                        if (slope1 == slope2 && slope2 == slope3)
                        {
                            Point [] tmp = new Point[4];
                            tmp[0] = pointscopy[i];
                            tmp[1] = pointscopy[j];
                            tmp[2] = pointscopy[k];
                            tmp[3] = pointscopy[l];
                            Arrays.sort(tmp);

                            lines[linecnt++] = new LineSegment(tmp[0], tmp[3]);
                        }
                    }
                }
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
}
