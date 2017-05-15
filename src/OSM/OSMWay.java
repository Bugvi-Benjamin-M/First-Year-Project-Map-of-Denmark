package OSM;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

public class OSMWay extends ArrayList<Point2D> implements Serializable {

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

    public Point2D getFromNode() { return this.get(0); }

    public Point2D getToNode() { return this.get(size() - 1); }
}
