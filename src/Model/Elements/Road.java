package Model.Elements;

import Enums.OSMEnums.ElementType;
import Helpers.HelperFunctions;
import Helpers.Shapes.MultiPolygonApprox;
import Helpers.Shapes.PolygonApprox;
import OSM.OSMRelation;
import OSM.OSMWay;
import OSM.OSMWayRef;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.List;

/**
 * Created by Jakob on 06-03-2017.
 *
 * nBytes: approx. 32 bytes
 */
public class Road extends Element {
    private String name = "";
    private boolean oneWay = false;
    private int maxSpeed = 0;
    private boolean area = false;
    private boolean travelByBikeAllowed = false;
    private boolean travelByFootAllowed = false;
    private boolean travelByCarAllowed = false;
    private java.util.List<OSMWayRef> relation;

    public Road(PolygonApprox polygon, String name)
    {
        super(polygon);
        this.name = name;
        relation = new ArrayList<>();
    }

    public Road(PolygonApprox polygon, String name, boolean area)
    {
        this(polygon, name);
        this.area = area;
    }

    public void setTravelByBikeAllowed(boolean travelByBikeAllowed) {
        this.travelByBikeAllowed = travelByBikeAllowed;
    }

    public void setTravelByFootAllowed(boolean travelByFootAllowed) {
        this.travelByFootAllowed = travelByFootAllowed;
    }

    public void setTravelByCarAllowed(boolean travelByCarAllowed) {
        this.travelByCarAllowed = travelByCarAllowed;
    }

    public boolean isTravelByBikeAllowed() {
        return travelByBikeAllowed;
    }

    public boolean isTravelByFootAllowed() {
        return travelByFootAllowed;
    }

    public boolean isTravelByCarAllowed() {
        return travelByCarAllowed;
    }

    public boolean isArea() { return area; }

    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }

    public String getName() { return name; }

    public boolean isOneWay() {return oneWay;}

    public int getMaxSpeed() {return maxSpeed;}

    public List<OSMWayRef> getRelation() {
        return relation;
    }

    public void setRelation(List<OSMWayRef> relation) {
        this.relation = new ArrayList<>();
        relation.forEach(this::setWay);
    }

    public void setWay(OSMWayRef way) {
        this.relation.add(way);
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }
}
