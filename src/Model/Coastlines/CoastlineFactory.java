package Model.Coastlines;

import Enums.BoundType;
import Helpers.GlobalValue;
import Helpers.HelperFunctions;
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

    private HashSet<Coastline> coastlines;
    private EnumMap<BoundType,Float> bounds;
    private static float longitudeFactor;

    protected CoastlineFactory() {
        coastlines = new HashSet<>();
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
        HashSet<Path2D> paths = new HashSet<>();
        for (Coastline coast: coastlines) {
            double size = (HelperFunctions.sizeOfPolygon(coast)*100000);

            /*
            System.out.println("Coast: "+coast.size()+" points ("+ size +" size)");
            System.out.println("... From: "+coast.getFromNode().getX()+", "+coast.getFromNode().getY());
            System.out.println("... To:   "+coast.getToNode().getX()+", "+coast.getToNode().getY()+"\n");
            */
            Path2D path = null;
            switch (GlobalValue.getZoomLevel()) {
                case LEVEL_6:
                    if (size > 1) path = coast.toPath2D();
                    break;
                case LEVEL_5:
                    if (size > 0.1) path = coast.toPath2D();
                case LEVEL_4:
                    if (size > 0.01) path = coast.toPath2D();
                case LEVEL_3:
                    if (size > 0.001) path = coast.toPath2D();
                default:
                    path = coast.toPath2D();
                    break;
            }
            if (path != null) {
                paths.add(path);
            }
        }
        List<Path2D> returnable = new ArrayList<>();
        for (Path2D path : paths) {
            returnable.add(path);
        }
        return returnable;
    }
}
