package Helpers.Shapes;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A polygon class that save the points as float values in a float[], and make use
 * of path generalisation depending on the zoom factor in the Affine Transform, that
 * is in use.
 *
 * @author Troels Lund Bjerre
 */
public class PolygonApprox implements Shape, Serializable {
    private static final long serialVersionUID = 20160224L;
    float[] coords;
    float bx, by, bw, bh;  //The bounds of the rectangle surrounding the polygon
    static double PIXEL = 1;

    protected PolygonApprox(){
    }

    /**
     * @param points - a List that contains elements that extends Point2D, to ensure that the elements
     *               have x and y coordinates. (Fx: OSMWay extends ArrayList<Point2D>)
     */
    public PolygonApprox(List<? extends Point2D> points) {
        int npoints = points.size();
        coords = new float[npoints << 1]; //Make an array twice the size of the amount of point 2
        for (int i = 0; i < npoints; i++) { //Fills up the coords float[] with the x- and y-values from the Point2Ds
            Point2D p = points.get(i);
            coords[(i << 1)] = (float)p.getX();
            coords[(i << 1) + 1] = (float)p.getY();
        }
        init();
    }

    /*
     * this method calculates the bx, by, bw and bh, which are the bounds of the
     * rectangle surrounding the polygon given in the List<? extends Point2D>.
     * bx is the rectangles x-coordinate
     * by is the rectangles y-coordinate
     * bw is the rectangles width
     * bh is the rectangles height
     */
    protected void init() {
        if(coords.length > 2){
            bx = coords[0];
            by = coords[1];
            bw = coords[0];
            bh = coords[1];
            for (int i = 2; i < coords.length; i += 2) {
                bx = Math.min(bx, coords[i]);
                bw = Math.max(bw, coords[i]);
                by = Math.min(by, coords[i + 1]);
                bh = Math.max(bh, coords[i + 1]);
            }
            bw -= bx;
            bh -= by;
        }
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
        for (int i = 2; i < coords.length; i += 2) {  //Iterates though coords (float[]) and check the distance to east vector in the polygon.
            dist = Math.min(dist, Line2D.ptSegDist(coords[i - 2], coords[i - 1], coords[i], coords[i + 1], px, py));
        }
        return dist;
    }

    //method not implemented.
    public boolean contains(double _x, double _y) {
        throw new UnsupportedOperationException(
            "Unexpected call to PolygonApprox.contains(double,double)");
    }

    //method not implemented.
    public boolean contains(Point2D p) {
        throw new UnsupportedOperationException(
            "Unexpected call to PolygonApprox.contains(Point2D)");
    }

    /*
     * method not implemented. Use public boolean intersects(Rectangle2D r)
     */
    public boolean intersects(double x, double y, double w, double h) {
        throw new UnsupportedOperationException(
            "Unexpected call to PolygonApprox.intersects(double,double,double,double)");
    }

    public boolean intersects(Rectangle2D r)
    {
        if (bx > r.getMaxX() || by > r.getMaxY() || bx + bw < r.getMinX() || by + bh < r.getMinY())
            return false;
        return true;
    }

    //method not implemented.
    public boolean contains(double x, double y, double w, double h) {
        throw new UnsupportedOperationException(
            "Unexpected call to PolygonApprox.contains(double,double,double,double)");
    }

    //method not implemented.
    public boolean contains(Rectangle2D r) {
        throw new UnsupportedOperationException(
            "Unexpected call to PolygonApprox.contains(Rectangle2D)");
    }

    /**
     * invokes PathIterator getPathIterator(AffineTransform at, float pixelsq)
     * @param at - The current Affine Transform.
     * @return an iterator over the points to make the polygon.
     */
    public PathIterator getPathIterator(AffineTransform at) {
        //Path generalisation - sets the rate for when to skip a point.
        //pixelsq grows when at.getDeterminant() decreases, which happens when the at zooms in
        float pixelsq = (float)(PIXEL / Math.abs(at.getDeterminant()));
        return getPathIterator(at, pixelsq);
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
        return new PolygonApproxIterator(at, pixelsq);
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
        return new PolygonApproxIterator(at, (float)(flatness * flatness));
    }

    //The Iterator over the polygon
    class PolygonApproxIterator implements PathIterator {
        AffineTransform at;
        int index;
        float approx;

        /**
         *
         * @param _at The current Affine Transform
         * @param _pixelsq  The rate of path generalisation
         */
        public PolygonApproxIterator(AffineTransform _at, float _pixelsq) {
            at = _at;
            approx = _pixelsq;
        }

        //return the integer that corresponds to the WIND_EVEN_ODD-rule
        public int getWindingRule() { return WIND_EVEN_ODD; }

        //When the Iterator is done iterating
        public boolean isDone() { return index >= coords.length; }

        float distSq(float x1, float y1, float x2, float y2) {
            float dx = x1 - x2;
            float dy = y1 - y2;
            return dx * dx + dy * dy;
        }

        //Give the next set of coordinates
        public void next() {
            float fx = coords[index];
            float fy = coords[index + 1];
            index += 2;
            while (index < coords.length - 2 && distSq(fx, fy, coords[index], coords[index + 1]) < approx)
                index += 2;
        }

        //Used for drawing
        public int currentSegment(float[] c) {
            if (isDone()) {
                throw new NoSuchElementException("polygon approx iterator out of bounds");
            }
            int type;
            if (index == 0) {
                c[0] = coords[0];
                c[1] = coords[1];
                type = SEG_MOVETO;  //Move to the first points
            } else {
                c[0] = coords[index];
                c[1] = coords[index + 1];
                type = SEG_LINETO;   //Draw line to the next point, and next point.. etc etc
            }
            if (at != null) {
                at.transform(c, 0, c, 0, 1);
            }
            return type;
        }

        //Method not implemeneterd - We use floats, not doubles
        public int currentSegment(double[] coords) {
            throw new UnsupportedOperationException(
                "Unexpected call to PolygonApprox.contains(Rectangle2D)");
        }
    }

    /**
     *
     * @return The largest value of the height and width of the rectangle surrounding the polygon
     */
    public float getSize() { return Math.max(bw, bh); }

    /**
     *
     * @return The maximum x-coordinate for the rectangle surrounding the polygon
     */
    public float getMaxX() { return bx + bw; }

    /**
     *
     * @return The minimum x-coordinate for the rectangle surrounding the polygon
     */
    public float getMinX() { return bx; }

    /**
     *
     * @return  The maximum y-coordinate for the rectangle surrounding the polygon
     */
    public float getMaxY() { return by + bh; }

    /**
     *
     * @return The minimum y-coordinate for the rectangle surrounding the polygon
     */
    public float getMinY() { return by; }

    /**
     *
     * @return The x-coordinate of the center of the rectangle surrounding the polygon
     */
    public float getCenterX() { return bx + bw / 2; }

    /**
     *
     * @return The y-coordinate of the center of the rectangle surrounding the polygon
     */
    public float getCenterY() { return by + bh / 2; }

    /**
     *
     * @return The width of the reactangle surrounding the polygon
     */
    public float getWidth() { return bw; }

    /**
     *
     * @return The height of the reactangle surrounding the polygon
     */
    public float getHeight() { return bh; }

    /**
     *
     * @return The rectangle (Rectangle2D.double) that surrounds the polygon
     */
    public Rectangle getBounds() { return getBounds2D().getBounds(); }

    /**
     *
     * @return The rectangle (Rectangle2D.double) that surrounds the polygon
     */
    public Rectangle2D getBounds2D()
    {
        return new Rectangle2D.Double(bx, by, bw, bh);
    }

    /**
     * Return the float[] that contains all the coordinates used in the polygon.
     * The float[] is sorted, such that the points in the polygon comes in
     * chronological order for how they should be drawn.
     *
     * @return a float[] that contains all the coordinates used in the polygon
     */
    public float[] getCoords() { return coords; }
}