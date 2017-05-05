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
import java.util.stream.Collectors;

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
        this.name = name.intern();
        relation = new ArrayList<>();
    }

    public Road(PolygonApprox polygon, String name, boolean area)
    {
        this(polygon, name);
        this.area = area;
    }

    public Road(String name) {
        this(null,name);
    }

    public Road(String name, boolean area) {
        this(null,name);
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

    public PolygonApprox getShape() {
        if (super.getShape() == null) {
            if (relation.size() > 1) {
                //FIXME
                OSMRelation osmRelation = new OSMRelation(12L);
                for (OSMWayRef way: relation) {
                    if(way != null){
                        OSMWay osmway = way.getWay();
                        if (osmway != null) osmRelation.add(osmway);
                    }
                }
                if(osmRelation.size() == 0){
                    return null;
                }else if(osmRelation.size() == 1){
                    super.setShape(new PolygonApprox(relation.get(0).getWay()));

                }else{
                    super.setShape(new MultiPolygonApprox(osmRelation));
                }

            } else if (relation.size() == 1) {
                super.setShape(new PolygonApprox(relation.get(0).getWay()));
            }
        }
        return (PolygonApprox)super.getShape();
    }

    public PolygonApprox getShapeSection(long start, long end) {
        if (area) {
            int sI = -1, eI = -1, sR = -1, eR = -1;
            for (int j = 0; j < relation.size(); j++) {
                OSMWayRef way = relation.get(j);
                int s = way.indexOf(start);
                if (s != -1) {sI = s; sR = j;}
                int e = way.indexOf(end);
                if (e != -1) {eI = e; eR = j;}
            }
            if (sI != -1 && eI != -1 && sR != -1 && eR != -1) {
                OSMWay way = new OSMWay();
                way.add(relation.get(sR).get(sI));
                way.add(relation.get(eR).get(eI));
                return new PolygonApprox(way);
            }
        } else {
            OSMWay osmWay = new OSMWay();
            OSMWayRef way = relation.get(0);
            int s = way.indexOf(start);
            int e = way.indexOf(end);
            if (s != -1 && e != -1) {
                if (s < e) {
                    for (int i = s; i <= e; i++) {
                        osmWay.add(way.get(i));
                    }
                } else {
                    for (int i = e; i <= s; i++) {
                        osmWay.add(way.get(i));
                    }
                }
            }
            return new PolygonApprox(osmWay);
        }
        return null;
    }

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
