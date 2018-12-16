package Model.Elements;

import Enums.OSMEnums.RoadType;
import Enums.TravelType;
import Helpers.GlobalValue;
import Helpers.HelperFunctions;
import Helpers.Shapes.PolygonApprox;
import KDtree.*;
import OSM.OSMWay;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.prefs.Preferences;
import Controller.PreferencesController;

/**
 * A RoadEdge object represent both an segment of an actual road
 * and an weighted directed edge in graph theory.
 */
public class RoadEdge implements Comparable<RoadEdge>, Serializable {

    private static float SPEED_TO_METERS_PER_SECOND = 0.277777777778f;

    private Road road;
    private Point2D from;
    private Point2D to;

    /**
     * Creates an edge from a point to another
     */
    public RoadEdge(Point2D from, Point2D to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Creates an edge from a point to another and attaches a road.
     */
    public RoadEdge(Point2D from, Point2D to, Road road) {
        this(from,to);
        this.road = road;
    }

    /**
     * Creates an edge in the other direction
     */
    public RoadEdge createReverse() {
        return new RoadEdge(this.to,this.from,this.road);
    }

    /**
     * Returns one of the two points
     */
    public Point2D getEither() {
        return from;
    }

    /**
     * Returns the other point, if you have one of them.
     */
    public Point2D getOther(Point2D point) {
        if (from.equals(point)) return to;
        else if (to.equals(point)) return from;
        else {
            throw new IllegalArgumentException("Not an endpoint");
        }
    }

    /**
     * Compares two edges to eachother by the distance
     */
    @Override
    public int compareTo(RoadEdge other) {
        return Double.compare((float) HelperFunctions.distanceInMeters(from,to),
                (float) HelperFunctions.distanceInMeters(other.from,other.to));
    }

    /**
     * Calculates the Dijkstra weight of this edge
     */
    public float getWeight(TravelType type, Point2D start, Point2D end, boolean fast) {
        if(type == TravelType.VEHICLE && !road.isTravelByCarAllowed()){
            return Float.POSITIVE_INFINITY;
        }
        if(type == TravelType.BICYCLE && !road.isTravelByBikeAllowed()){
            return Float.POSITIVE_INFINITY;
        }
        if(type == TravelType.WALK && !road.isTravelByFootAllowed()){
            return Float.POSITIVE_INFINITY;
        }

        if (fast && type == TravelType.VEHICLE) {
            return (float)(HelperFunctions.lazyDistance(from,to) / getSpeed());
        } else {
            return (float)HelperFunctions.lazyDistance(from,to);
        }
    }

    @Override
    public String toString() {
        return "Road:'"+getName()+"'; "+getLength()+" m;";
    }

    /**
     * Describes this edge in words
     */
    public String describe(float length) {
        String name = getName();
        if (name.equals("") || name.equals(" ")){
            name = road.getRoadType().name();
        }
        String returnable = "Travel via "+name+" for ";
        int len = (int) Math.floor(length/10)*10;
        if (len == 0) {
            return returnable + Math.round(length)
                    + " meters";
        } else if (len - 1000 > 0) {
            return returnable + Math.round(len/1000) + " kilometers";
        } else {
            return returnable + len + " meters";
        }
    }

    /**
     * Compares the edge to another edge based on the direction of them
     */
    public int compareToRoad(RoadEdge other) {
        if (other == null) throw new NullPointerException("RoadEdge not initialized!");
        if (other.getName().equals(getName())) return 0;
        return HelperFunctions.direction(this.from,this.to,
                other.from,other.to);
    }

    /**
     * Returns the length of the edge in meters
     */
    public float getLength() {
        return (float) HelperFunctions.distanceInMeters(from,to);
    }

    /**
     * Returns the mathematical length of the edge
     */
    public float getLengthInCoords() {
        return (float)from.distance(to);
    }

    /**
     * Returns the max speed in meters per second
     */
    public float getSpeed() {
        return road.getMaxSpeed()*SPEED_TO_METERS_PER_SECOND;
    }

    /**
     * Returns the time is takes to drive the edge
     */
    public float getTime() {
        return getLength() / getSpeed();
    }

    /**
     * Return the edge name
     */
    public String getName() {
        return road.getName();
    }

    /**
     * Returns the edge type
     */
    public RoadType getRoadType() {
        return road.getRoadType();
    }
}
