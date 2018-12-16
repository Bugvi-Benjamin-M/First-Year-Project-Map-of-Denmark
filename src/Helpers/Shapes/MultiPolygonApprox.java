package Helpers.Shapes;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A polygon class that save the points as float values in a float[], and make use
 * of path generalisation depending on the zoom factor in the Affine Transform, that
 * is in use.
 *
 * @author Troels Lund Bjerre
 */
public class MultiPolygonApprox extends PolygonApprox {
    private static final long serialVersionUID = 1L;
    byte[] pointtypes;

    /**
     *
     * @param rel - a List that contains a List of elements that extends Point2D, to ensure that the elements
     *               have x and y coordinates. (Fx: OSMRelation extends ArrayList<OSMWay>)
     */
    public MultiPolygonApprox(List<? extends List<? extends Point2D> > rel) {
        //Convert the list of lists into a single array
        int npoints = 0;
        for (List<?> l : rel) {
            if (l != null)
                npoints += l.size();
        }
        coords = new float[npoints << 1];
        pointtypes = new byte[npoints];
        Arrays.fill(pointtypes, (byte)PathIterator.SEG_LINETO);
        int coord = 0;
        int point = 0;
        for (List<? extends Point2D> l : rel) {
            if (l != null) {
                pointtypes[point] = (byte)PathIterator.SEG_MOVETO;
                point += l.size();
                for (Point2D p : l) {
                    coords[coord++] = (float)p.getX();
                    coords[coord++] = (float)p.getY();
                }
            }
        }
        init();
    }

    /**
     * Calculates the shortest distance to the polygon from a given point.
     *
     * @param p - Point2D
     * @return double - the shortest distance from p to the polygon
     */
    public double distTo(Point2D p) {
        double dist = Double.MAX_VALUE;
        double px = p.getX();
        double py = p.getY();
        for (int i = 2; i < coords.length; i += 2) {
            if (i >> 1 < pointtypes.length && pointtypes[i >> 1] != PathIterator.SEG_MOVETO)
                dist = Math.min(dist, Line2D.ptSegDist(coords[i - 2], coords[i - 1],
                                          coords[i], coords[i + 1], px, py));
        }
        return dist;
    }

    /**
     * return an iterator over the polygon. Only contains the points in the polygon to be drawn
     * after path generalisation have been determined.
     *
     * @param at  - The current Affine Transform.
     * @param pixelsq - The path generalisation value (when to skip points to make the polygon more edgy)
     * @return an iterator over the points to make the polygon.
     */
    public PathIterator getPathIterator(AffineTransform at, float pixelsq) {
        return new MultiPolygonApproxIterator(at, pixelsq);
    }

    /**
     * return an iterator over the polygon. Only contains the points in the polygon to be drawn
     * after path generalisation have been determined.
     * Path generalisation is set on behalf of the flatness given as a parameter.
     *
     * @param at - the current affine transform
     * @param flatness - for path generalisation
     * @return an iterator over the points to make the polygon.
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new MultiPolygonApproxIterator(at, (float)(flatness * flatness));
    }

    //The Iterator over the polygon
    class MultiPolygonApproxIterator extends PolygonApproxIterator {
        public MultiPolygonApproxIterator(AffineTransform _at, float _pixelsq)
        {
            super(_at, _pixelsq);
        }

        //Give the next set of coordinates
        public void next() {
            float fx = coords[index];
            float fy = coords[index + 1];
            index += 2;
            while (index < coords.length - 2 && pointtypes[(index >> 1) + 1] == PathIterator.SEG_LINETO && distSq(fx, fy, coords[index], coords[index + 1]) < approx)
                index += 2;
        }

        //Used for drawing
        public int currentSegment(float[] c) {
            if (isDone()) {
                throw new NoSuchElementException("poly approx iterator out of bounds");
            }
            c[0] = coords[index];
            c[1] = coords[index + 1];
            if (at != null) {
                at.transform(c, 0, c, 0, 1);
            }
            return pointtypes[index >> 1];
        }

        //method not implemented
        public int currentSegment(double[] coords) {
            throw new UnsupportedOperationException(
                "Unexpected call to PolygonApprox.contains(Rectangle2D)");
        }
    }
}
