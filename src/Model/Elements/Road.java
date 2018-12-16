package Model.Elements;

import Enums.OSMEnums.RoadType;
import Helpers.HelperFunctions;
import Helpers.Shapes.PolygonApprox;

import java.awt.geom.Point2D;

/**
 * A Road is both a visual component on the map and an object
 * in the model. Each road has a name, a shape and a type, such
 * as 'Østfynsk Motervej", which is of type MOTORWAY. Besides that
 * a road can also be oneway, have a maximum allowed speed, be an
 * area as well as be allowed to travel via bike, foot and car.
 */
public class Road extends Element {
    private String name = "";
    private boolean oneWay = false;
    private int maxSpeed = 0;
    private boolean area = false;
    private boolean travelByBikeAllowed = false;
    private boolean travelByFootAllowed = false;
    private boolean travelByCarAllowed = false;
    private RoadType roadType;

    /**
     * Simple Road constructor
     * @param polygon The shape of the road
     * @param name The name of the road
     * @param roadType The type of the road
     */
    public Road(PolygonApprox polygon, String name, RoadType roadType)
    {
        super(polygon);
        this.name = name.intern();
        this.roadType = roadType;
    }

    /**
     * Road Area constructor
     * @param polygon The shape of the road
     * @param name The name of the road
     * @param area Whether the road is an area
     * @param roadType The type of the road
     */
    public Road(PolygonApprox polygon, String name, boolean area, RoadType roadType)
    {
        this(polygon, name, roadType);
        this.area = area;
    }

    /**
     * Change whether travel by bicycle is allowed on this road
     */
    public void setTravelByBikeAllowed(boolean travelByBikeAllowed) {
        this.travelByBikeAllowed = travelByBikeAllowed;
    }

    /**
     * Change whether travel by foot is allowed on this road
     */
    public void setTravelByFootAllowed(boolean travelByFootAllowed) {
        this.travelByFootAllowed = travelByFootAllowed;
    }

    /**
     * Change whether travel by car is allowed on this road
     */
    public void setTravelByCarAllowed(boolean travelByCarAllowed) {
        this.travelByCarAllowed = travelByCarAllowed;
    }

    /**
     * Returns whether it is possible to travel via bicycle on
     * this road
     */
    public boolean isTravelByBikeAllowed() {
        return travelByBikeAllowed;
    }

    /**
     * Returns whether one is allowed to walk beside this road
     */
    public boolean isTravelByFootAllowed() {
        return travelByFootAllowed;
    }

    /**
     * Returns whether cars is allowed to travel on this road
     */
    public boolean isTravelByCarAllowed() {
        return travelByCarAllowed;
    }

    /**
     * Returns whether the road is actually an area
     * (consists of multiple segments of road, e.g. a square)
     */
    public boolean isArea() { return area; }

    /**
     * Returns the name of the road
     */
    public String getName() { return name; }

    /**
     * Returns whether or not this road is oneway
     */
    public boolean isOneWay() {return oneWay;}

    /**
     * Returns the maximum allowed speed on the road
     */
    public int getMaxSpeed() {return maxSpeed;}

    /**
     * Change the maximum allowed speed on this road
     */
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Change whether this road is oneway or not
     */
    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    /**
     * Finds and returns the point on this road that has the
     * minimum distance to a given point.
     * Amortized run time: O(N) - has to check every point in
     * the path of this road.
     * @param point The point to which the road finds closest point to.
     */
    public Point2D getNearestPoint(Point2D point) {
        if (point == null) throw new NullPointerException("Point cannot be null");
        PolygonApprox shape = (PolygonApprox) super.getShape();
        float[] coords = shape.getCoords();
        if (coords.length == 0) throw new NullPointerException("This way contains no points");
        else if (coords.length == 2) return new Point2D.Float(coords[0], coords[1]);
        else {
            float minDistance = Float.POSITIVE_INFINITY;
            Point2D closest = null;
            for (int i = 0; i < coords.length; i += 2) {
                Point2D.Float newPpoint = new Point2D.Float(coords[i], coords[i + 1]);
                float distance = (float) HelperFunctions.distanceBetweenTwoPoints(newPpoint, point);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = newPpoint;
                }
            }
            return closest;
        }
    }

    /**
     * Returns the RoadType of this road
     * @see RoadType
     */
    public RoadType getRoadType() {
        return roadType;
    }

    /**
     * Returns the shape of this road
     * @see PolygonApprox
     */
    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }
}
