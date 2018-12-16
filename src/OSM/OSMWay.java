package OSM;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A OSMWay is a collection of OSMNodes (more precisely, a collection
 * of Point2D's). OSMWay objects is also able to produce a shape
 * called a Path2D based on their points.
 */
public class OSMWay extends ArrayList<Point2D> implements Serializable {

    /**
     * Produces a Path2D shape based on the ordering of the OSMWay's
     * points.
     */
    public Path2D toPath2D()
    {
        Path2D path = new Path2D.Float();
        Point2D node = get(0);
        path.moveTo(node.getX(), node.getY());
        for (int i = 1; i < size(); i++) {
            node = get(i);
            path.lineTo(node.getX(), node.getY());
        }
        return path;
    }

    /**
     * Returns the point in the beginning of the way
     */
    public Point2D getFromNode() { return this.get(0); }

    /**
     * Returns the last point of the way
     */
    public Point2D getToNode() { return this.get(size() - 1); }
}
