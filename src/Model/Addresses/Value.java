package Model.Addresses;

import java.io.Serializable;

public class Value implements Serializable {
    private int citynameindex;
    private float x;
    private float y;
    private  boolean isSignificant;

    /**
     * A tenary search trie value, created with a city index, coordinates and
     * wheter is is significant
     */
    public Value(int citynameindex, float x, float y, boolean isSignificant){
        this.citynameindex = citynameindex;
        this.x = x;
        this.y = y;
        this.isSignificant = isSignificant;
    }

    /**
     * Returns the city name index
     */
    public int getCitynameindex() {
        return citynameindex;
    }

    /**
     * returns the x-coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * returns the y-coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * returns whether the TST value is significant
     */
    public boolean isSignificant() {
        return isSignificant;
    }
}
