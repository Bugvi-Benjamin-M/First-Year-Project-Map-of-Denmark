package Model.Coastlines;

import Enums.BoundType;
import Helpers.GlobalValue;
import Helpers.HelperFunctions;
import Model.Model;
import OSM.OSMWay;

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class details:
 * Data structure used to store and generating coastline paths.
 *
 * @see Coastline for path generation
 */
public class CoastlineFactory {

    private HashSet<Coastline> coastlines;
    private EnumMap<BoundType, Float> bounds;
    private static float longitudeFactor;

    /**
     * Constructor for the coastline factory, initializes data structures
     */
    protected CoastlineFactory()
    {
        coastlines = new HashSet<>();
        bounds = new EnumMap<>(BoundType.class);
    }

    /**
     * Calculates the longitude factor used for handling the curvature
     * of the Earth and displaying a "flat" map
     */
    public void setLongitudeFactor(float minLatitude, float maxLatitude)
    {
        double avglat = minLatitude + (maxLatitude - minLatitude) / 2;
        longitudeFactor = (float)Math.cos(avglat / 180 * Math.PI);
    }

    /**
     * Calculates the longitude factor based on the minimum latitude bound
     * and the maximum latitude bound
     */
    public void setLongitudeFactor()
    {
        setLongitudeFactor(bounds.get(BoundType.MIN_LATITUDE),
            bounds.get(BoundType.MAX_LATITUDE));
    }

    /**
     * Retrieves the current calculated longitude factor
     */
    public float getLongitudeFactor() { return longitudeFactor; }

    /**
     * Adds or changes a bound of a specific type
     * @param type the type of the value to change or add
     * @param value the value of the bound
     */
    protected void addBound(BoundType type, float value)
    {
        bounds.put(type, value);
    }

    /**
     * Retrieves a specific bound value based on the bound type
     * @param type One of the four possible boundary types
     * @see BoundType
     */
    public float getBound(BoundType type)
    {
        return bounds.get(type);
    }

    /**
     * Creates and inserts a new coastline into the data structure
     * via a OSMWay containing a collection of nodes
     * @param nodes A OSMWay containing all nodes for the coastline
     */
    protected void insertCoastline(OSMWay nodes)
    {
        Coastline object = new Coastline();
        object.addAll(nodes);
        coastlines.add(object);
    }

    /**
     * Returns the total number of coastlines
     */
    public int getNumberOfCoastlines() { return coastlines.size(); }

    /**
     * Returns the total number of points contained in coastlines
     */
    public int getNumberOfCoastlinePoints()
    {
        int size = 0;
        for (Coastline way : coastlines) {
            size += way.size();
        }
        return size;
    }

    /**
     * Calculates and returns a collection of coastline paths
     * with a size greater than what is specified by the zoom level
     */
    public List<Path2D> getCoastlinePolygons()
    {
        HashSet<Path2D> paths = new HashSet<>();
        for (Coastline coast : coastlines) {
            double size = (HelperFunctions.sizeOfPolygon(coast) * 100000);
            Path2D path = null;
            switch (GlobalValue.getZoomLevel()) {
            case LEVEL_6:
                if (size > 1)
                    path = coast.toPath2D();
                break;
            case LEVEL_5:
                if (size > 0.1)
                    path = coast.toPath2D();
            case LEVEL_4:
                if (size > 0.01)
                    path = coast.toPath2D();
            case LEVEL_3:
                if (size > 0.001)
                    path = coast.toPath2D();
            default:
                path = coast.toPath2D();
                break;
            }
            if (path != null) {
                paths.add(path);
            }

        }
        List<Path2D> returnable = new ArrayList<>();
        returnable.addAll(paths);

        return returnable;
    }
}
