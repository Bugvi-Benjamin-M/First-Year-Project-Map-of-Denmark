package Helpers.Shapes;

import Enums.ZoomLevel;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Nik on 12/04/17.
 */
public class DynamicPolygonApprox extends PolygonApprox{
    public DynamicPolygonApprox(List<? extends Point2D> points){
        super(points);
    }


    public PathIterator getPathIterator(AffineTransform at, float pixelsq) {
        return new DynamicPolygonApproxIterator(at, pixelsq);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new DynamicPolygonApproxIterator(at, (float) (flatness * flatness));
    }

    class DynamicPolygonApproxIterator extends PolygonApproxIterator {

        public DynamicPolygonApproxIterator(AffineTransform _at, float _pixelsq) {
            super(_at, _pixelsq);
        }

        public void next() {
            float fx = coords[index];
            float fy = coords[index + 1];
            if (ZoomLevel.getZoomLevel().getNodesAtLevel() % 2 == 1) {
                index += ZoomLevel.getZoomLevel().getNodesAtLevel() + 1;
            } else index += ZoomLevel.getZoomLevel().getNodesAtLevel();
            while (index < coords.length - 2 &&
                    distSq(fx, fy, coords[index], coords[index + 1]) < approx) {
                if (ZoomLevel.getZoomLevel().getNodesAtLevel() % 2 == 1) {
                    index += ZoomLevel.getZoomLevel().getNodesAtLevel() + 1;
                } else index += ZoomLevel.getZoomLevel().getNodesAtLevel();
            }
        }
    }
}
