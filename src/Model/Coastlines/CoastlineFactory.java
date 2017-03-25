package Model.Coastlines;

import Enums.BoundType;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 23-03-2017.
 * @project BFST
 */
public class CoastlineFactory {

    private List<Coastline> coastlines;
    private EnumMap<BoundType,Double> bounds;
    private static double longitudeFactor;

    protected CoastlineFactory() {
        coastlines = new ArrayList<>();
        bounds = new EnumMap<>(BoundType.class);
    }

    public void setLongitudeFactor(double minLatitude, double maxLatitude) {
        double avglat = minLatitude + (maxLatitude - minLatitude)/2;
        longitudeFactor = Math.cos(avglat/180*Math.PI);
    }

    protected static double getLongitudeFactor() {
        return longitudeFactor;
    }

    protected void addBound(BoundType type, Double value) {
        bounds.put(type,value);
    }

    protected void insertCoastline(Collection<Point2D> nodes) {
        Coastline object = new Coastline();
        object.addAll(nodes);
        coastlines.add(object);
    }

    public List<Path2D> getCoastlinePolygons() {
        return coastlines.stream().map(Coastline::toPath2D).collect(Collectors.toList());
    }
}
