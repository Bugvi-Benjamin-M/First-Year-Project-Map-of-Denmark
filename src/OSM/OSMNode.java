package OSM;

/**
 * Created by Jakob on 06-03-2017.
 */
public class OSMNode {
    private float longitude;
    private float latitude;

    public OSMNode(float longitude, float latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public float getLatitude() { return latitude; }

    public float getLongitude() { return longitude; }
}
