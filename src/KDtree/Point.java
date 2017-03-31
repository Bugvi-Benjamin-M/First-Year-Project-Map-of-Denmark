package KDtree;

/**
 * Created by Jakob on 30-03-2017.
 */
public abstract class Point {
    private float x;
    private float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public int compareToX(Point point){
        if(x < point.getX()) return -1;
        else if(x > point.getX()) return 1;
        else{
            if(y < point.getY()) return -1;
            else if(y > point.getY()) return 1;
            else return 0;
        }
    }

    public int compareToY(Point point){
        if(y < point.getY()) return -1;
        else if(y > point.getY()) return 1;
        else{
            if(x < point.getX()) return -1;
            else if(x > point.getX()) return 1;
            else return 0;
        }
    }
}