package Model.Addresses;

import java.io.Serializable;

/**
 * Created by Nik on 24/04/17.
 */
public class Value implements Serializable {
    private int citynameindex;
    private float x;
    private float y;

    public Value(int citynameindex, float x, float y){
        this.citynameindex = citynameindex;
        this.x = x;
        this.y = y;
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
}
