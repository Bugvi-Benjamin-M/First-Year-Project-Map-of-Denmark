package Model.Elements;

import Enums.OSMEnums.AmenityType;

/**
 * Created by Jakob on 19-04-2017.
 */
public class Amenity extends Element {

    private float x;
    private float y;
    private String name;
    private AmenityType type;

    public Amenity(AmenityType type, float x, float y, String name) {
        super(null);
        this.type = type;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public String getName() { return name; }

    public AmenityType getAmenityType() {
        return type;
    }
}
