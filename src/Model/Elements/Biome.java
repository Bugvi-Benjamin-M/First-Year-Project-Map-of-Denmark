package Model.Elements;

import Helpers.Shapes.PolygonApprox;

import java.awt.geom.Path2D;


public class Biome extends Element {

    public Biome(PolygonApprox polygon)
    {
        super(polygon);
    }
    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }
}
