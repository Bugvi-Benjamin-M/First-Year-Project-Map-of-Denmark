package OSM;

import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * Created by Jakob on 06-03-2017.
 */
public class OSMWay extends ArrayList<OSMNode> {
    public Path2D toPath2D(){
        Path2D path = new Path2D.Float();
        OSMNode node = get(0);
        path.moveTo(node.getLongitude(), node.getLatitude());
        for(int i = 1 ; i < size() ; i++){
            node = get(i);
            path.lineTo(node.getLongitude(), node.getLatitude());
        }
        return path;
    }

}
