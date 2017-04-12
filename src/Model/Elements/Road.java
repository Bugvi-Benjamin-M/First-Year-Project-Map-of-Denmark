package Model.Elements;

import Model.Elements.Element;
import Model.Model;

import java.awt.*;
import java.awt.geom.Path2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road extends Element {
    private String name;
    private boolean oneWay;
    private int maxSpeed;
    private boolean area = false;

    public Road(Shape shape, String name, boolean oneWay, int maxSpeed){
        super(shape);
        this.name = name;
        this.oneWay = oneWay;
        this.maxSpeed = maxSpeed;
    }
    public Road(Shape shape){
        this(shape, "", false, 50);
    }
    public Road(Shape shape, String name){
        this(shape, name, false, 50);
    }
    public Road(Shape shape, String name, boolean area){
        this(shape, name, false, 10);
        this.area = area;
    }

    public boolean isArea(){
        return area;
    }

    public String getName() {
        return name;
    }

}
