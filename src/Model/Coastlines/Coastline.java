package Model.Coastlines;

import Enums.BoundType;
import Enums.ZoomLevel;
import Helpers.HelperFunctions;
import Model.Model;
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
        // Setup
        Path2D path = new Path2D.Float();
        Point2D node = this.getFromNode();
        path.moveTo(node.getX(), node.getY());

        int lastI = 0; int increase = ZoomLevel.getNodesAtMaxLevel();
        for (int i = 0; i < size() - 1; ) {
            i += increase;
            if (i >= size()) i = size()-1;
            node = get(lastI);
            boolean isFromNear = isNodeNearCamera(node);
            path.lineTo(node.getX(),node.getY());
            node = get(i);
            boolean isToNear = isNodeNearCamera(node);
            if (isToNear || isFromNear) {
                path.append(qualityGeneratePath(lastI,i,ZoomLevel.LEVEL_6),true);
            } else {
                path.append(quickGeneratePath(lastI,i),true);
            }
            lastI = i;
        }

        // Finish path (loop back)
        path.closePath();
        return path;
    }

    private void allGeneratePath(Path2D path, int startpoint, int endpoint) {
        for (int i = startpoint; i < endpoint; i++) {
            Point2D point = get(i);
            path.lineTo(point.getX(),point.getY());
        }
    }

    private Path2D qualityGeneratePath(int startpoint, int endpoint, ZoomLevel level) {
        Path2D path = new Path2D.Float();
        // Copy array
        List<Point2D> copy = new ArrayList<>();
        for (int i = startpoint; i <= endpoint; i++) {
            copy.add(this.get(i));
        }

        // Generate simplified path
        double epsilon = level.getEpsilonValueBasedOnZoomLevel();
        List<Point2D> newPoints = HelperFunctions.pathGeneralization(copy,epsilon);

        // Add start point
        Point2D start = newPoints.get(0);
        path.moveTo(start.getX(),start.getY());

        // Add generalized points
        for (int i = 1; i < newPoints.size(); i++) {
            Point2D point = newPoints.get(i);
            path.lineTo(point.getX(),point.getY());
        }
        return path;
    }

    private Path2D quickGeneratePath(int start, int end) {
        Path2D path = new Path2D.Float();
        // Add start point
        Point2D startPoint = this.get(start);
        path.moveTo(startPoint.getX(),startPoint.getY());

        // Add points
        for (int i = start; i < end; i += ZoomLevel.getZoomLevel().getNodesAtLevel()) {
            Point2D point = this.get(i);
            path.lineTo(point.getX(),point.getY());
        }

        // Add end point
        Point2D endPoint = this.get(end);
        path.lineTo(endPoint.getX(),endPoint.getY());
        return path;
    }

    private void oldQuickSimplify(Path2D path) {
        for (int i = 0; i < this.size(); i += ZoomLevel.getZoomLevel().getNodesAtLevel()) {
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
    }

    private void simpleSimply(Path2D path, int start, int end) {
        int range = end - start;
        int difference = range / 4;
        Point2D point = this.get(start);
        path.lineTo(point.getX(),point.getY());
        point = this.get(start + difference);
        path.lineTo(point.getX(),point.getY());
        point = this.get(start + difference + difference);
        path.lineTo(point.getX(),point.getY());
        point = this.get(start + difference + difference + difference);
        path.lineTo(point.getX(),point.getY());
        point = this.get(end);
        path.lineTo(point.getX(),point.getY());
    }

    private boolean isNodeNearCamera(Point2D node) {
        boolean nodeIsNear = false;
        Model model = Model.getInstance();
        float buffer = 0.2f;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Coastline of size: ");
        sb.append(size());
        sb.append("\n");
        for (int i = 0; i < size(); i++) {
            Point2D point = get(i);
            //sb.append("(");
            //sb.append(point.getX());
            //sb.append("; ");
            sb.append(-point.getY());
            //sb.append(")");
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

}
