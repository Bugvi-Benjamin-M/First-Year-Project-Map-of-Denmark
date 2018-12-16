package Model.Elements;

import Enums.OSMEnums.AmenityType;


public class Amenity extends Element {

    private float x;
    private float y;
    private String name;
    private AmenityType type;

    /**
     * Constructs a new amenity
     */
    public Amenity(AmenityType type, float x, float y, String name) {
        super(null);
        this.type = type;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    /**
     * Returns the x coordinate of the amenity
     */
    public float getX() { return x; }

    /**
     * Returns the y coordinate of the amenity
     */
    public float getY() { return y; }

    /**
     * Returns the name of the amenity
     */
    public String getName() { return name; }

    /**
     * Returns the amenity typet
     */
    public AmenityType getAmenityType() {
        return type;
    }
}
