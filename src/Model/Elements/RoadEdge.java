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
    private float length;
    private Point2D from;
    private Point2D to;

    public RoadEdge(Point2D from, Point2D to) {
        this.from = from;
        this.to = to;
        length = (float) HelperFunctions.distanceInMeters(from,to);
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
        return Double.compare(length,other.length);
    }

    public float getWeight(TravelType type, Point2D start, Point2D end) {
        boolean fast = GlobalValue.isFastestRouteSet();
        boolean ok = false;

        //FIXME: clean this
        switch (type) {
            case VEHICLE:
                if (road.isTravelByCarAllowed()) {
                    ok = true;
                }
                break;
            case BICYCLE:
                if (road.isTravelByBikeAllowed()) {
                    ok = true;
                }
                break;
            case WALK:
                if (road.isTravelByFootAllowed()) {
                    ok = true;
                }
                break;
        }
        if (!ok) {
            return Float.POSITIVE_INFINITY;
        } else {
            if (fast) {
                return (length/getSpeed()) + ((float)from.distance(end) / getSpeed());
            } else {
                return length +  (float)from.distance(end);
            }
        }
    }

    @Override
    public String toString() {
        return "Road:'"+getName()+"'; "+length+" m;";
    }

    public String describe(float length) {
        return "Travel via "+getName()+" for " + (length) + " meters";
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
        return length;
    }

    public float getSpeed() {
        return road.getMaxSpeed()*SPEED_TO_METERS_PER_SECOND;
    }

    public float getTime() {
        return length / getSpeed();
    }

    public String getName() {
        return road.getName();
    }

}
