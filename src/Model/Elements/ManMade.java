package Model.Elements;

import Helpers.Shapes.PolygonApprox;

public class ManMade extends Element{

    private boolean isArea;

    /**
     * Constructs a new man made object
     */
    public ManMade(PolygonApprox polygon, boolean isArea){
        super(polygon);
        this.isArea = isArea;
    }

    /**
     * Returns the shape of the man made object.
     */
    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }

    /**
     * Returns whether the way is an area.
     */
    public boolean isArea() { return isArea; }
}
