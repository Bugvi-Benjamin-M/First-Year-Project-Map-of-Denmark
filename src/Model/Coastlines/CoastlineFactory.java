package Model.Coastlines;

import Enums.BoundType;
import Helpers.GlobalValue;
import Model.Model;

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
    private EnumMap<BoundType,Float> bounds;
    private static float longitudeFactor;

    protected CoastlineFactory() {
        coastlines = new ArrayList<>();
        bounds = new EnumMap<>(BoundType.class);
    }

    public void setLongitudeFactor(float minLatitude, float maxLatitude) {
        double avglat = minLatitude + (maxLatitude - minLatitude)/2;
        longitudeFactor = (float) Math.cos(avglat/180*Math.PI);
        // System.out.println("... set longitude factor to "+longitudeFactor);
    }

    public void setLongitudeFactor() {
        setLongitudeFactor(bounds.get(BoundType.MIN_LATITUDE),bounds.get(BoundType.MAX_LATITUDE));
    }

    public float getLongitudeFactor() {
        return longitudeFactor;
    }

    protected void addBound(BoundType type, float value) {
        bounds.put(type,value);
    }

    public float getBound(BoundType type) {
        // System.out.println(type + ": " + bounds.get(type));
        return bounds.get(type);
    }

    protected void insertCoastline(Collection<Point2D> nodes) {
        Coastline object = new Coastline();
        object.addAll(nodes);
        coastlines.add(object);
    }

    public int getNumberOfCoastlines() {
        return coastlines.size();
    }

    public int getNumberOfCoastlinePoints() {
        int size = 0;
        for (Coastline way: coastlines) {
            size += way.size();
        }
        return size;
    }

    public List<Path2D> getCoastlinePolygons() {
        List<Path2D> paths = new ArrayList<>();
        for (Coastline coast: coastlines) {
            switch (GlobalValue.getZoomLevel()) {
                case LEVEL_3:
                    if (coast.size() > 50) paths.add(coast.toPath2D());
                    break;
                case LEVEL_2:
                    if (coast.size() > 30) paths.add(coast.toPath2D());
                case LEVEL_1:
                    if (coast.size() > 10) paths.add(coast.toPath2D());
                default:
                    paths.add(coast.toPath2D());
                    break;
            }
        }
        return paths;
    }
}
