package OSM;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jakob on 06-03-2017.
 */
public class OSMRelation extends ArrayList<OSMWay> {

    public Path2D toPath2D(boolean connected)
    {
        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        for (OSMWay way : this) {
            if (way != null) {
                path.append(way.toPath2D(), connected);
            }
        }
        return path;
    }

    @Override
    public boolean add(OSMWay way)
    {
        boolean b = super.add(way);
        if (size() > 1) {
            OSMWay before = get(size() - 2);
            OSMWay current = get(size() - 1);
            if (before != null && current != null) {
                double x = before.get(before.size() - 1).getX();
                double y = before.get(before.size() - 1).getY();

                double x1 = current.get(0).getX();
                double y1 = current.get(0).getY();

                if (x != x1 || y != y1) {
                    double x2 = current.get(current.size() - 1).getX();
                    double y2 = current.get(current.size() - 1).getY();

                    if (x == x2 && y == y2)
                        Collections.reverse(current);
                }
            }
        }
        return b;
    }
}
