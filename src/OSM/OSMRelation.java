package OSM;

import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
                    counter++;
                    break;
                }
                else if(lastSortedX == checkingXLast && lastSortedY == checkingYLast){
                    Collections.reverse(temp[i]);
                    if(counter != i){
                        tempWay = temp[counter];
                        temp[counter] = temp[i];
                        temp[i] = tempWay;
                    }
                    counter++;
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

    @Override
    public boolean add(OSMWay way){
        if(way == null) return true;
        if(size() == 0){
            return super.add(way);
        }
        OSMWay before = get(0);
        OSMWay current = way;

        double beforeXfirst = before.get(0).getX();
        double beforeYfirst  = before.get(0).getX();

        double currentXlast = current.get(current.size()-1).getX();
        double currentYlast = current.get(current.size()-1).getX();

        if(beforeXfirst == currentXlast && beforeYfirst == currentYlast){
            super.add(0, current);
            return true;
        }

        for(int i = 0 ; i < size() ; i++) {
            before = get(i);
            current = way;

            double x = before.get(before.size() - 1).getX();
            double y = before.get(before.size() - 1).getY();

            double x1 = current.get(0).getX();
            double y1 = current.get(0).getY();

            if (x == x1 && y == y1) {
                super.add(i + 1, current);
                return true;
            } else {
                double x2 = current.get(current.size() - 1).getX();
                double y2 = current.get(current.size() - 1).getY();
                if (x == x2 && y == y2) {
                    Collections.reverse(current);
                    super.add(i + 1, current);
                    return true;
                }
            }
        }
        super.add(way);
        return true;
    }
}
