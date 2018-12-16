package Model.Elements;

import java.awt.geom.Point2D;

public class POI extends Point2D.Float{

    private String description;

    /**
     * Construct a new point of interest
     */
    public POI(float x, float y, String description){
        super(x,y);
        this.description = description;
    }

    /**
     * Return the description of the name
     */
    public String getDescription() {
        return description;
    }

}
