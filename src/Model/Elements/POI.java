package Model.Elements;

import java.awt.geom.Point2D;

public class POI extends Point2D.Float{

    private String description;

    public POI(float x, float y, String description){
        super(x,y);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
