package Model.Elements;

import Helpers.Shapes.PolygonApprox;

/**
 * Created by Nik on 11/04/17.
 */
public class Building extends Element {
    private String name;

    public Building(PolygonApprox polygon, String name)
    {
        super(polygon);
        this.name = name;
    }

    public Building(PolygonApprox polygon) { this(polygon, ""); }

    public String getName() { return name; }
}
