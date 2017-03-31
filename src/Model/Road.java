package Model;

import Enums.RoadType;

import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road implements Element {
    private RoadType roadType;
    private String name;
    private Path2D path;

    public Road(RoadType roadType, Path2D path, String name){
        this.roadType = roadType;
        this.name = name;
        this.path = path;
    }
    public Road(RoadType roadType, Path2D path){
        this(roadType, path, "");
    }

    public RoadType getRoadType() {
        return roadType;
    }

    public String getName() {
        return name;
    }

    public Path2D getPath(){
        return path;
    }
}
