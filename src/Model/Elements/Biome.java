package Model.Elements;

import Helpers.Shapes.PolygonApprox;

import java.awt.geom.Path2D;

/**
 * Created by Nik on 12/04/17.
 */
public class Biome extends Element {
    private String name;

    public Biome(PolygonApprox polygon, String name)
    {
        super(polygon);
        this.name = name;
    }
    public Biome(PolygonApprox polygon) { this(polygon, ""); }

    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }

    public String getName() { return name; }
}
