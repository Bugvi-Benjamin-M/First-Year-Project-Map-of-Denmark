package Helpers.Shapes;

import Enums.ZoomLevel;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by Nik on 12/04/17.
 */
public class DynamicMultiPolygonApprox extends  MultiPolygonApprox{
    private int[] waySegments;

    public DynamicMultiPolygonApprox(List<? extends List<? extends Point2D>> rel){
        super(rel);
        waySegments = new int[rel.size()];
        waySegments[0] = rel.get(0).size() * 2 - 1;
        int i = 1;
        while(i < rel.size()){
            waySegments[i] = ((rel.get(i).size() * 2)) + waySegments[i - 1];
            i++;
        }

    }

    public PathIterator getPathIterator(AffineTransform at, float pixelsq) {
        return new DynamicMultiPolygonApproxIterator(at, pixelsq);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new DynamicMultiPolygonApproxIterator(at, (float) (flatness * flatness));
    }

    class DynamicMultiPolygonApproxIterator extends MultiPolygonApproxIterator {
        private int currentWay = 0;

        public DynamicMultiPolygonApproxIterator(AffineTransform _at, float _pixelsq) {
            super(_at, _pixelsq);
        }

        public void next() {
            float fx = coords[index];
            float fy = coords[index + 1];
            if(ZoomLevel.getZoomLevel().getNodesAtLevel() > 3) {
                if (ZoomLevel.getZoomLevel().getNodesAtLevel() % 2 == 1) {
                    if (waySegments[currentWay] > index + ZoomLevel.getZoomLevel().getNodesAtLevel() + 1) {
                        index += ZoomLevel.getZoomLevel().getNodesAtLevel() + 1;
                    } else {
                        index += 2;
                        if (waySegments[currentWay] < index) {
                            if (waySegments.length > currentWay + 1) currentWay++;
                        }
                    }
                } else {
                    if (waySegments[currentWay] > ZoomLevel.getZoomLevel().getNodesAtLevel()) {
                        index += ZoomLevel.getZoomLevel().getNodesAtLevel();
                    } else {
                        index += 2;
                        if (waySegments[currentWay] < index) {
                            if (waySegments.length > currentWay + 1) currentWay++;
                        }
                    }
                }
            }else{
                index += 2;
                if (waySegments[currentWay] < index) {
                    if (waySegments.length > currentWay + 1) currentWay++;
                }
            }

            while (index < coords.length - 2 && pointtypes[(index >> 1) + 1] == PathIterator.SEG_LINETO &&
                    distSq(fx, fy, coords[index], coords[index + 1]) < approx && index < waySegments[currentWay]) {

                    index += 2;
                    if (waySegments[currentWay] < index) {
                        if (waySegments.length > currentWay + 1) currentWay++;
                    }


            }
        }
    }
}
