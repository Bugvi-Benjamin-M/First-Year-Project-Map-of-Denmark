package Model;

import Enums.DrawType;
import Enums.RoadType;
import OSM.OSMWay;

import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road extends Element {
    private RoadType roadType;
    // Path2D path;
    private String name;
    private OSMWay way;

    public Road(RoadType roadType, OSMWay way, String name){
        this.roadType = roadType;
        //this.path = path;
        this.name = name;
        this.way = way;
    }
    public Road(RoadType roadType, OSMWay way){
        this(roadType, way, "");
    }

    public RoadType getRoadType() {
        return roadType;
    }

    /*
    public Path2D getPath() {
        return path;
    }
    */

    public String getName() {
        return name;
    }

    public OSMWay getWay(){
        return way;
    }
}
