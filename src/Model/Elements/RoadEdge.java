package Model.Elements;

import Enums.TravelType;
import Helpers.GlobalValue;
import Helpers.HelperFunctions;
import Helpers.Shapes.PolygonApprox;
import KDtree.*;
import OSM.OSMWay;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * A RoadEdge object represent both an segment of an actual road
 * and an weighted directed edge in graph theory.
 * TODO: write javadoc
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RoadEdge implements Comparable<RoadEdge>, Serializable {

    private static float SPEED_TO_METERS_PER_SECOND = 0.277777777778f;

    private Road road;
    private Point2D from;
    private Point2D to;

    public RoadEdge(Point2D from, Point2D to) {
        this.from = from;
        this.to = to;
    }

    public RoadEdge(Point2D from, Point2D to, Road road) {
        this(from,to);
        this.road = road;
    }

    public RoadEdge createReverse() {
        return new RoadEdge(this.to,this.from,this.road);
    }

    public Point2D getEither() {
        return from;
    }

    public Point2D getOther(Point2D point) {
        if (from.equals(point)) return to;
        else if (to.equals(point)) return from;
        else {
            throw new IllegalArgumentException("Not an endpoint");
        }
    }

    @Override
    public int compareTo(RoadEdge other) {
        return Double.compare((float) HelperFunctions.distanceInMeters(from,to),
                (float) HelperFunctions.distanceInMeters(other.from,other.to));
    }

    public float getWeight(TravelType type, Point2D start, Point2D end) {
        boolean fast = GlobalValue.isFastestRouteSet();

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
            return (getLengthInCoords()/getSpeed()) + ((float)from.distance(end) / getSpeed());
        } else {
            return getLengthInCoords() +  (float)from.distance(end);
        }
    }

    @Override
    public String toString() {
        return "Road:'"+getName()+"'; "+getLength()+" m;";
    }

    public String describe(float length) {
        String name = getName();
        if (name.equals("") || name.equals(" ")){
            name = road.getRoadType().name();
        }
        return "Travel via "+name+" for " + (length) + " meters";
    }

    public int compareToRoad(RoadEdge other) {
        if (other == null) throw new NullPointerException("RoadEdge not initialized!");
        if (other.getName().equals(getName())) return 0;
        double angle = HelperFunctions.angle(this.from,this.to,
                other.from,other.to);
        if (angle < 0) return -1;       // to the left
        else if (angle > 0) return 1;   // to the right
        else return 0;                  // same angle
    }

    public float getLength() {
        return (float) HelperFunctions.distanceInMeters(from,to);
    }

    public float getLengthInCoords() {
        return (float)from.distance(to);
    }

    public float getSpeed() {
        return road.getMaxSpeed()*SPEED_TO_METERS_PER_SECOND;
    }

    public float getTime() {
        return getLength() / getSpeed();
    }

    public String getName() {
        return road.getName();
    }

}
