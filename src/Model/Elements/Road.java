package Model.Elements;

import Helpers.Shapes.PolygonApprox;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Road extends Element {
    private String name;
    private boolean oneWay;
    private int maxSpeed;
    private boolean area = false;

    public Road(PolygonApprox polygon, String name, boolean oneWay, int maxSpeed){
        super(polygon);
        this.name = name;
        this.oneWay = oneWay;
        this.maxSpeed = maxSpeed;
    }
    public Road(PolygonApprox polygon){
        this(polygon, "", false, 50);
    }
    public Road(PolygonApprox polygon, String name){
        this(polygon, name, false, 50);
    }
    public Road(PolygonApprox polygon, String name, boolean area){
        this(polygon, name, false, 10);
        this.area = area;
    }

    public boolean isArea(){
        return area;
    }

    public String getName() {
        return name;
    }

}
