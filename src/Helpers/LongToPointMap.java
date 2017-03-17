package Helpers;
import java.awt.geom.Point2D;

/**
 * Created by Búgvi Magnussen on 17 March 2017
 *
 * @author bugvimagnussen
 * @version 17/03/2017
 */

public class LongToPointMap {
    int MASK;
    public Node[] tab;

    public LongToPointMap(int capacity) {
        tab = new Node[1 << capacity];
        MASK = tab.length - 1;
    }

    public void put(long key, float x, float y) {
        int h = Long.hashCode(key) & MASK;
        tab[h] = new Node(key, x, y, tab[h]);
    }

    public Point2D get(long key) {
        for (Node n = tab[Long.hashCode(key) & MASK] ; n != null ; n = n.next) {
            if (n.key == key) return n;
        }
        return null;
    }

    static class Node extends Point2D.Float {
        public static final long serialVersionUID = 20160216;
        Node next;
        long key;

        public Node(long _key, float x, float y, Node _next) {
            super(x, y);
            key = _key;
            next = _next;
        }
    }

}
