package Model.Addresses;

import java.io.Serializable;

public class Value implements Serializable {
    private int citynameindex;
    private float x;
    private float y;
    private  boolean isSignificant;

    public Value(int citynameindex, float x, float y, boolean isSignificant){
        this.citynameindex = citynameindex;
        this.x = x;
        this.y = y;
        this.isSignificant = isSignificant;
    }

    public int getCitynameindex() {
        return citynameindex;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isSignificant() {
        return isSignificant;
    }
}
