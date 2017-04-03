package Model.Elements;

import Model.Elements.Element;
import Model.Model;

import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road extends Element {
    private String name;
    private boolean oneWay;
    private int maxSpeed;

    public Road(Path2D path, String name, boolean oneWay, int maxSpeed){
        super(path);
        this.name = name;
        this.oneWay = oneWay;
        this.maxSpeed = maxSpeed;
    }
    public Road(Path2D path){
        this(path, "", false, 50);
    }
    public Road(Path2D path, String name){
        this(path, name, false, 50);
    }

    public String getName() {
        return name;
    }

}
