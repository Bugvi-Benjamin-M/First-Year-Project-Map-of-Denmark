package Helpers.Shapes;

import Enums.ZoomLevel;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Created by Nik on 12/04/17.
 */
public class DynamicMultiPolygonApprox extends MultiPolygonApprox {
    private int[] waySegments;

    public DynamicMultiPolygonApprox(
        List<? extends List<? extends Point2D> > rel)
    {
        super(rel);
        waySegments = new int[rel.size()];
        waySegments[0] = rel.get(0).size() * 2 - 1;
        int i = 1;
        while (i < rel.size()) {
            if (rel.get(i) != null) {
                waySegments[i] = ((rel.get(i).size() * 2)) + waySegments[i - 1];
            }
            i++;
        }
    }

    public PathIterator getPathIterator(AffineTransform at, float pixelsq)
    {
        return new DynamicMultiPolygonApproxIterator(at, pixelsq);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness)
    {
        return new DynamicMultiPolygonApproxIterator(at,
            (float)(flatness * flatness));
    }

    class DynamicMultiPolygonApproxIterator extends MultiPolygonApproxIterator {
        private int currentWay = 0;

        public DynamicMultiPolygonApproxIterator(AffineTransform _at,
            float _pixelsq)
        {
            super(_at, _pixelsq);
        }

        public void next()
        {
            float fx = coords[index];
            float fy = coords[index + 1];
            if (ZoomLevel.getZoomLevel().getNodesAtLevel() > 11) {
                switch (ZoomLevel.getZoomLevel()) {
                case LEVEL_5:
                    if (waySegments[currentWay] > index + 8) {
                        index += 8;
                    } else {
                        index += 2;
                    }
                    break;
                case LEVEL_6:
                    if (waySegments[currentWay] > index + 16) {
                        index += 16;
                    } else {
                        index += 2;
                    }
                    break;
                }

            } else {
                index += 2;
            }

            if (waySegments[currentWay] < index) {
                if (waySegments.length > currentWay + 1)
                    currentWay++;
            }

            while (index < coords.length - 2 && pointtypes[(index >> 1) + 1] == PathIterator.SEG_LINETO && distSq(fx, fy, coords[index], coords[index + 1]) < approx && index < waySegments[currentWay]) {

                index += 2;
                if (waySegments[currentWay] < index) {
                    if (waySegments.length > currentWay + 1)
                        currentWay++;
                }
            }
        }
    }
}
