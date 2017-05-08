package Model.Elements;

import Enums.TravelType;
import Helpers.GlobalValue;
import Helpers.HelperFunctions;
import Helpers.Shapes.PolygonApprox;
import OSM.OSMWay;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RoadEdge extends Element implements Comparable<RoadEdge>, Serializable {

    private static float SPEED_TO_METERS_PER_SECOND = 0.277777777778f;

    private OSMWay way;
    private boolean isOneWay;
    private boolean isTravelByBikeAllowed;
    private boolean isTravelByCarAllowed;
    private boolean isTravelByWalkAllowed;
    private float length;
    private float speed;
    private String name;

    public RoadEdge(OSMWay way) {
        super(new PolygonApprox(way));
        this.way = way;
        length = (float) HelperFunctions.distanceInMeters(way);
    }

    public RoadEdge(OSMWay way, String name, int speed) {
        this(way);
        this.name = name.intern();
        this.speed = speed * SPEED_TO_METERS_PER_SECOND;
    }

    public RoadEdge(OSMWay way, String name, float speed) {
        this(way);
        this.name = name;
        this.speed = speed;
    }

    public RoadEdge createReverse() {
        OSMWay way = new OSMWay();
        for (int i = this.way.size()-1; i >= 0; i--) {
            way.add(this.way.get(i));
        }
        RoadEdge reverse = new RoadEdge(way,this.name,this.speed);
        reverse.setOneWay(this.isOneWay);
        reverse.setTravelByCarAllowed(this.isTravelByCarAllowed);
        reverse.setTravelByBikeAllowed(this.isTravelByBikeAllowed);
        reverse.setTravelByWalkAllowed(this.isTravelByWalkAllowed);
        return reverse;
    }

    public Point2D getEither() {
        return way.get(0);
    }

    public Point2D getOther(Point2D point) {
        if (way.get(0).equals(point)) return way.get(way.size()-1);
        else if (way.get(way.size()-1).equals(point)) return way.get(0);
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
                if (isTravelByCarAllowed) {
                    ok = true;
                }
                break;
            case BICYCLE:
                if (isTravelByBikeAllowed) {
                    ok = true;
                }
                break;
            case WALK:
                if (isTravelByWalkAllowed) {
                    ok = true;
                }
                break;
        }
        if (!ok) {
            return Float.POSITIVE_INFINITY;
        } else {
            if (fast) {
                return (length/speed) + ((float)way.getFromNode().distance(end) / speed);
            } else {
                return length +  (float)way.getFromNode().distance(end);
            }
        }
    }

    @Override
    public String toString() {
        return "Road:'"+name+"'; "+length+" m;";
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public float getLength() {
        return length;
    }


    public float getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }


    public void setOneWay(boolean oneWay) {
        isOneWay = oneWay;
    }

    public void setTravelByBikeAllowed(boolean travelByBikeAllowed) {
        isTravelByBikeAllowed = travelByBikeAllowed;
    }

    public void setTravelByCarAllowed(boolean travelByCarAllowed) {
        isTravelByCarAllowed = travelByCarAllowed;
    }

    public void setTravelByWalkAllowed(boolean travelByWalkAllowed) {
        isTravelByWalkAllowed = travelByWalkAllowed;
    }


}
