package OSM;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 01-05-2017
 */
public class OSMWayRef implements Iterable {

    private List<Long> references;
    private OSMWay way;

    public OSMWayRef() {
        this.way = new OSMWay();
        references = new ArrayList<>();
    }

    public void add(Point2D point, long ref) {
        way.add(point);
        references.add(ref);
    }

    public Point2D get(long ref) {
        int id = references.indexOf(ref);
        if (id != -1) {
            return way.get(id);
        } else {
            throw new ArrayIndexOutOfBoundsException("ref "+ref+" is out of bounds");
        }
    }

    public Point2D get(int id) {
        if (id < 0 || id > way.size()) {
            throw new ArrayIndexOutOfBoundsException("id "+id+" is out of bounds");
        }
        return way.get(id);
    }

    public int indexOf(Point2D point) {
        return way.indexOf(point);
    }

    public long refOf(Point2D point) {
        int index = indexOf(point);
        if (index < 0 || index >= references.size()) return -1;
        else return references.get(index);
    }

    public boolean contains(Point2D point) {
        return way.contains(point);
    }

    @Override
    public Iterator iterator() {
        return way.iterator();
    }

    public Iterator references() {
        return references.iterator();
    }

    public int size() {
        return way.size();
    }

    public Point2D getFromNode() {
        return way.getFromNode();
    }

    public Point2D getToNode() {
        return way.getToNode();
    }
}