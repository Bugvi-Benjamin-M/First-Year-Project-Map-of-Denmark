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
    private float time;
    private float speed;
    private String name;
    private byte type;

    public RoadEdge(OSMWay way) {
        super(new PolygonApprox(way));
        this.way = way;
        length = (float) HelperFunctions.distanceInMeters(way);
    }

    public RoadEdge(OSMWay way, String name, int speed) {
        this(way);
        this.name = name.intern();
        this.speed = speed * SPEED_TO_METERS_PER_SECOND;
        this.time = length / speed;
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

    public float getWeight(TravelType type) {
        String travel = getTravelTypeName(getType());
        boolean fast = GlobalValue.isFastestRouteSet();
        boolean returned = false;
        switch (type) {
            case VEHICLE:
                if (travel.contains("DRIVE") || travel.contains("ALL")) {
                    returned = true;
                }
                break;
            case BICYCLE:
                if (travel.contains("CYCLE") || travel.contains("ALL")) {
                    returned = true;
                }
                break;
            case WALK:
                if (travel.contains("WALK") || travel.contains("ALL")) {
                    returned = true;
                }
                break;
        }
        if (!returned) {
            return Float.POSITIVE_INFINITY;
        } else {
            if (fast) {
                return time;
            } else {
                return length;
            }
        }
    }

    @Override
    public String toString() {
        return "Road:'"+name+"' ("+length+" m; "+time+
                " s) type:"+getTravelTypeName(getType());
    }

    public float getLength() {
        return length;
    }

    public float getTime() {
        return time;
    }

    public float getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public byte getType() {
        return type;
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

    public void setType() {
        this.type = getTravelTypeValue(isTravelByWalkAllowed, isTravelByBikeAllowed,
                isTravelByCarAllowed, isOneWay);
    }

    public static byte getTravelTypeValue(boolean canWalk, boolean canCycle, boolean canDrive, boolean isOneway) {
        byte travel = 0;
        if (canWalk) travel += 1;
        if (canCycle) travel += 2;
        if (canDrive) travel += 4;
        if (isOneway) travel += 8;
        return travel;
    }

    public static String getTravelTypeName(byte type) {
        switch (type) {
            case 1:
                return "WALK_ONLY";
            case 2:
                return "CYCLE_ONLY";
            case 3:
                return "WALK&CYCLE";
            case 4:
                return "DRIVE";
            case 5:
                return "DRIVE&WALK";
            case 6:
                return "DRIVE&CYCLE";
            case 7:
                return "ALL_ALLOWED";
            case 8:
                return "ONEWAY N/A";
            case 9:
                return "ONEWAYWALK";
            case 10:
                return "ONEWAYCYCLE";
            case 11:
                return "ONEWALKCYCLE";
            case 12:
                return "ONEWAYDRIVE";
            case 13:
                return "ONEDRIVEWALK";
            case 14:
                return "ONEDRIVECYCLE";
            case 15:
                return "ONEWAY_ALLALLOWED";
            default:
                return "N/A";
        }
    }
}
