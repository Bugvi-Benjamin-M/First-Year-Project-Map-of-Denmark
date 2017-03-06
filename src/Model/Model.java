package Model;
import Enums.*;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Model {
    private List<Road> roads;
    private List<Shape> unknown;

    public Model(){
        roads = new ArrayList<>();
        unknown = new ArrayList<>();
    }

    public void addRoad(Road road){
        roads.add(road);
    }

    public List<Road> getRoads(){
        return roads;
    }

    public void addUnknown(Shape shape){
        unknown.add(shape);
    }

    public List<Shape> getUnknown(){
        return unknown;
    }
}
