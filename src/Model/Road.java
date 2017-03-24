package Model;

import Enums.DrawType;
import Enums.RoadType;
import OSM.OSMWay;

import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road implements Element {
    private RoadType roadType;
    private String name;
    private OSMWay way;

    public Road(RoadType roadType, OSMWay way, String name){
        this.roadType = roadType;
        this.name = name;
        this.way = way;
    }
    public Road(RoadType roadType, OSMWay way){
        this(roadType, way, "");
    }

    public RoadType getRoadType() {
        return roadType;
    }

    public String getName() {
        return name;
    }

    public OSMWay getWay(){
        return way;
    }
}
