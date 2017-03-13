package Model;

import Enums.DrawType;
import Enums.RoadType;

import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road extends Element {
    private RoadType roadType;
    private Path2D path;
    private String name;
    private DrawType drawType;

    public Road(RoadType roadType, Path2D path, String name){
        this.roadType = roadType;
        this.path = path;
        this.name = name;
        drawType = DrawType.DRAW;
    }
    public Road(RoadType roadType, Path2D path){
        this(roadType, path, "");
    }

    public RoadType getRoadType() {
        return roadType;
    }

    public Path2D getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

}
