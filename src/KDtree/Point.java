package KDtree;

import java.io.Serializable;

/**
 * Created by Jakob on 30-03-2017.
 */
public abstract class Point implements Serializable{
    private float x;
    private float y;

    protected Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    protected float getY() {
        return y;
    }

    protected void setY(float y) {
        this.y = y;
    }

    protected float getX() {
        return x;
    }

    protected void setX(float x) {
        this.x = x;
    }

    protected int compareToX(Point point){
        if(x < point.getX()) return -1;
        else if(x > point.getX()) return 1;
        else{
            if(y < point.getY()) return -1;
            else if(y > point.getY()) return 1;
            else return 0;
        }
    }

    protected int compareToY(Point point){
        if(y < point.getY()) return -1;
        else if(y > point.getY()) return 1;
        else{
            if(x < point.getX()) return -1;
            else if(x > point.getX()) return 1;
            else return 0;
        }
    }
}