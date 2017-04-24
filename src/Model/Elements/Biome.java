package Model.Elements;

import Helpers.Shapes.PolygonApprox;

import java.awt.geom.Path2D;

/**
 * Created by Nik on 12/04/17.
 */
public class Biome extends Element {

    public Biome(PolygonApprox polygon) {
        super(polygon);
    }

    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }
}
