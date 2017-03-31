package Model.Coastlines;

import Enums.ZoomLevel;
import Model.Model;
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
 *
 * @project BFST
 */
public class Coastline extends OSMWay {

    public static final String OSM_IDENTIFIER = "coastline";

    public Path2D toPath2D(float longFactor) {
        Path2D path = new Path2D.Float();
        Point2D node = this.getFromNode();
        path.moveTo(node.getX()*longFactor, node.getY());
        for(int i = 1 ; i < size() ; i += Model.getInstance().getZoomLevel().getNodesAtLevel()){
            node = this.get(i);
            path.lineTo(node.getX()*longFactor, node.getY());
        }
        node = this.getFromNode();
        path.lineTo(node.getX()*longFactor, node.getY());
        return path;
    }
}
