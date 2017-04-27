package Model.Elements;

import Helpers.Shapes.PolygonApprox;
/**
 * Created by Jakob on 26-04-2017.
 */
public class ManMade extends Element{

    private boolean isArea;

    public ManMade(PolygonApprox polygon, boolean isArea){
        super(polygon);
        this.isArea = isArea;
    }

    public PolygonApprox getShape() { return (PolygonApprox)super.getShape(); }
    public boolean isArea() { return isArea; }
}