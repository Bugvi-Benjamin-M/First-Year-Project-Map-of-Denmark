package Helpers;
import java.awt.geom.Point2D;

/**
 * Originally created by Troels Bjerre Lund on 17 March 2017
 *
 *
 * This class is a symbol table that maps a long key to a Node.
 * A Node is a subtype of Point2D.Float.
 *
 */

public class LongToPointMap {

    private int MASK;
    private Node[] base;

    /**
   * Creates a LongToPointMap object.
   * @param capacity
   */
    public LongToPointMap(int capacity)
    {
        base = new Node[1 << capacity];
        MASK = base.length - 1;
    }

    /**
   * This methods maps a given key to a set of coordinates
   * @param key
   * @param x
   * @param y
   */

    public void put(long key, float x, float y)
    {
        int index = Long.hashCode(key) & MASK;
        base[index] = new Node(key, x, y, base[index]);
    }

    /**
   * Returns the Point2D that was mapped to the given key. Null if the key
   * does not exist.
   * @param key
   * @return the requested point
   */
    public Point2D get(long key)
    {
        for (Node node = base[Long.hashCode(key) & MASK]; node != null;
             node = node.next) {
            if (node.key == key)
                return node;
        }
        return null;
    }

    /**
   * Used by LongToNodeMap to create nodes and connect them.
   */
    static class Node extends Point2D.Float {
        public static final long serialVersionUID = 20160216;
        private Node next;
        private long key;

        /**
     * Creates a new node
     * @param key
     * @param x
     * @param y
     * @param next
     */

        public Node(long key, float x, float y, Node next)
        {
            super(x, y);
            this.key = key;
            this.next = next;
        }
    }
}
