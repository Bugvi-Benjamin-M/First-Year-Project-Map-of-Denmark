package Model.Elements;

import Helpers.Shapes.PolygonApprox;

import java.awt.geom.Path2D;

/**
 * Created by Nik on 04/04/17.
 */
public class Water extends Element{
    private String name;

    public Water(PolygonApprox polygon, String name) {
        super(polygon);
        this.name = name;
    }
    public Water(PolygonApprox polygon){
        this(polygon, "");
    }

    public PolygonApprox getShape(){
        return (PolygonApprox) super.getShape();
    }

    public String getName() {
        return name;
    }
}
