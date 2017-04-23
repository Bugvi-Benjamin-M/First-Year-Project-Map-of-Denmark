package Model.Elements;

/**
 * Created by Jakob on 05-04-2017.
 */
public class PlaceName extends Element {

    private float x;
    private float y;
    private String name;

    public PlaceName(float x, float y, String name)
    {
        super(null);
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public String getName() { return name; }
}
