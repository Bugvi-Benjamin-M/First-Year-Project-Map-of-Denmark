package Model.Coastlines;

import Enums.BoundType;
import Enums.ZoomLevel;
import Helpers.GlobalValue;
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

    public Path2D toPath2D() {
        Path2D path = new Path2D.Float();
        Point2D node = this.getFromNode();
        path.moveTo(node.getX(), node.getY());

        // Draws all points
        for (int i = 0; i < size(); i += ZoomLevel.getZoomLevel().getNodesAtLevel()) {
            node = this.get(i);
            boolean isNear = isNodeNearCamera(node);
            if (isNear) path.lineTo(node.getX(), node.getY());
            else {
                path = addOutOfViewNodes( i, node, path);
            }
        }
        node = this.getFromNode();
        path.lineTo(node.getX(), node.getY());
        return path;
    }

    private Path2D addOutOfViewNodes(int i, Point2D node, Path2D path) {
        boolean isNear = false;
        int j = 0;
        while (!isNear) {
            j++;
            i += ZoomLevel.getNodesAtMaxLevel()/4;
            if (i < size()) {
                node = this.get(i);
                isNear = isNodeNearCamera(node);
            } else {
                isNear = true;
            }
            if (j % 4 == 0) path.lineTo(node.getX(), node.getY());
        }
        return path;
    }

    private boolean isNodeNearCamera(Point2D node) {
        boolean nodeIsNear = false;
        Model model = Model.getInstance();
        float buffer = 0.5f;
        float minlon = model.getCameraBound(BoundType.MIN_LONGITUDE) - buffer;
        float maxlon = model.getCameraBound(BoundType.MAX_LONGITUDE) + buffer;
        float minlat = model.getCameraBound(BoundType.MIN_LATITUDE) + buffer;
        float maxlat = model.getCameraBound(BoundType.MAX_LATITUDE) - buffer;

        if (minlon <= node.getX() && maxlon >= node.getX() &&
                minlat >= node.getY() && maxlat <= node.getY()) {
            nodeIsNear = true;
        }

        return nodeIsNear;
    }

}
