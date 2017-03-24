package Model.Coastlines;

import OSM.OSMNode;
import OSM.OSMWay;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 23-03-2017.
 * @project BFST
 */
public class Coastline extends OSMWay {

    public static final String OSM_IDENTIFIER = "coastline";

    @Override
    public Path2D toPath2D() {
        List<Point2D> points = new ArrayList<>();
        double longFactor = CoastlineFactory.getLongitudeFactor();
        for (OSMNode node: this) {
            points.add(new Point2D.Float((float) (node.getLongitude()*longFactor),node.getLatitude()));
        }
        Path2D path = new Path2D.Float();
        Point2D node = points.get(0);
        path.moveTo(node.getX(), node.getY());
        for(int i = 1 ; i < size() ; i++){
            node = points.get(i);
            path.lineTo(node.getX(), node.getY());
        }
        node = points.get(0);
        path.lineTo(node.getX(), node.getY());
        return path;
    }
}
