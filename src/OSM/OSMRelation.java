package OSM;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A OSMRelation is a collection of OSMWays and presents paths
 * or ways that are connected in some way, such as with buildings
 * or similar connected objects.
 */
public class OSMRelation extends ArrayList<OSMWay> {

    private long ref;

    /**
     * Creates a OSMRelation with an osm reference stored
     */
    public OSMRelation(long ref) {
        super();
        this.ref = ref;
    }

    /**
     * Produces a string representation of this object
     */
    @Override
    public String toString() {
        int size = 0;
        for (OSMWay way: this) {
            size++;
        }
        return "ref "+ref + " with size "+size;
    }

    /**
     * Produces a Path2D shape representing the OSMRelation
     * @param connected Whether the shapes of the OSMRelation's
     *                  ways should be connected
     */
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

    /**
     * Sorts the ways contained in the OSMRelation
     * @return The sorted OSMRelation
     */
    public static OSMRelation sortWays(OSMRelation relation){
        OSMWay[] temp = relation.toArray(new OSMWay[relation.size()]);
        OSMWay tempWay;
        relation.clear();
        int counter = 1;
        while(counter < temp.length){
            for(int i = counter ; i < temp.length ; i++){
                double lastSortedX = temp[counter-1].get(temp[counter-1].size() -1).getX();
                double lastSortedY = temp[counter-1].get(temp[counter-1].size() -1).getY();

                double checkingXFirst = temp[i].get(0).getX();
                double checkingYFirst = temp[i].get(0).getY();

                double checkingXLast = temp[i].get(temp[i].size()-1).getX();
                double checkingYLast = temp[i].get(temp[i].size()-1).getY();

                if (lastSortedX == checkingXFirst && lastSortedY == checkingYFirst){
                    if(counter != i){
                        tempWay = temp[counter];
                        temp[counter] = temp[i];
                        temp[i] = tempWay;
                    }
                    break;
                }
                if(lastSortedX == checkingXLast && lastSortedY == checkingYLast){
                    Collections.reverse(temp[i]);
                    if(counter != i){
                        tempWay = temp[counter];
                        temp[counter] = temp[i];
                        temp[i] = tempWay;
                    }
                    break;
                }
            }
            counter++;
        }
        for(int k = 0 ; k < temp.length ; k++){
            relation.add(k, temp[k]);
        }
        return relation;
    }
}
