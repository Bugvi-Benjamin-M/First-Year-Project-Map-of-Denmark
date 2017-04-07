package Model.Coastlines;

import Enums.BoundType;
import Enums.ZoomLevel;
import Helpers.GlobalValue;
import Helpers.HelperFunctions;
import KDtree.Point;
import Model.Model;
import OSM.OSMNode;
import OSM.OSMWay;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.nio.file.Path;
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
        // Setup
        Path2D path = new Path2D.Float();
        Point2D node = this.getFromNode();
        path.moveTo(node.getX(), node.getY());

        ZoomLevel level = ZoomLevel.getZoomLevel();
        if (level != ZoomLevel.LEVEL_6 && level != ZoomLevel.LEVEL_5 &&
                level != ZoomLevel.LEVEL_4 && level != ZoomLevel.LEVEL_3) {
            // Make path
            boolean isWayNearby = false;
            int lastQuickIndex = 0, lastQualityIndex = 0;
            for (int i = 0; i < this.size(); i++) {
                node = this.get(i);
                boolean isNear = isNodeNearCamera(node);
                if (isWayNearby) {
                    if (!isNear) {
                        isWayNearby = false;
                        qualityGeneratePath(path, lastQualityIndex, i - 1);
                        lastQuickIndex = i;
                    }
                } else {
                    if (isNear) {
                        isWayNearby = true;
                        quickGeneratePath(path, lastQuickIndex, i - 1);
                        lastQualityIndex = i;
                    }
                }
            }

            if (path.getCurrentPoint().equals(this.get(size() - 1))) {
                quickGeneratePath(path, 0, this.size() - 1);
            }
        } else {
            quickGeneratePath(path,0,this.size()-1);
        }

        // Finish path (loop back)
        node = this.getFromNode();
        path.lineTo(node.getX(), node.getY());
        return path;
    }

    private Path2D qualityGeneratePath(Path2D path, int startpoint, int endpoint) {
        List<Point2D> copy = new ArrayList<>();
        for (int i = startpoint; i <= endpoint; i++) {
            copy.add(this.get(i));
        }
        System.out.println("Copy size: "+copy.size());
        double epsilon = ZoomLevel.getEpsilonValueBasedOnZoomLevel(ZoomLevel.getZoomLevel());
        List<Point2D> newPoints = HelperFunctions.pathGeneralization(copy,epsilon);
        for (Point2D point: newPoints) {
            path.lineTo(point.getX(),point.getY());
        }
        System.out.println("New size: "+newPoints.size());
        return path;
    }

    private Path2D quickGeneratePath(Path2D path, int start, int end) {
        for (int i = start; i < end; i += ZoomLevel.getZoomLevel().getNodesAtLevel()) {
            Point2D point = this.get(i);
            path.lineTo(point.getX(),point.getY());
        }
        return path;
    }

    /*
    private Path2D quickGeneratePath(Path2D path) {
        for (int i = start; i < end; i += ZoomLevel.getZoomLevel().getNodesAtLevel()) {
            Point2D node = this.get(i);
            boolean isNear = isNodeNearCamera(node);
            if (isNear) path.lineTo(node.getX(), node.getY());
            else {
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
            }
        }
        return path;
    }*/

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
