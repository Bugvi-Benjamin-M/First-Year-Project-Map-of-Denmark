package OSM;

import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * Created by Jakob on 06-03-2017.
 */
public class OSMRelation extends ArrayList<OSMWay>{

    public Path2D toPath2D() {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        for (OSMWay way : this) {
            if (way != null) {
                path.append(way.toPath2D(), true);
            }
        }
        return path;
    }
}
