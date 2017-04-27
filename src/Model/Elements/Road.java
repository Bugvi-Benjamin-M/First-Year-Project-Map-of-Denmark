package Model.Elements;

import Helpers.Shapes.MultiPolygonApprox;
import Helpers.Shapes.PolygonApprox;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road extends Element {
    private String name;
    private boolean oneWay;
    private int maxSpeed;
    private boolean area = false;

    public Road(PolygonApprox polygon, String name, boolean oneWay,
        int maxSpeed)
    {
        super(polygon);
        this.name = name;
        this.oneWay = oneWay;
        this.maxSpeed = maxSpeed;
    }
    public Road(PolygonApprox polygon) { this(polygon, "", false, 50); }
    public Road(PolygonApprox polygon, String name)
    {
        this(polygon, name, false, 50);
    }
    public Road(PolygonApprox polygon, String name, boolean area)
    {
        this(polygon, name, false, 10);
        this.area = area;
    }

    public boolean isArea() { return area; }

    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }

    public String getName() { return name; }

    public boolean isOneWay() {return oneWay;}

    public int getMaxSpeed() {return maxSpeed;}

    public Point2D getFromNode() {
        // FIXME: Needs to be able to retrieve a start point!
        return null;
    }

    public Point2D getToNode() {
        // FIXME: Needs to be able to retrieve an end point!
        return null;
    }
}
