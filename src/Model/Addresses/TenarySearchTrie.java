package Model.Addresses;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 21/04/2017
 */
public class TenarySearchTrie implements Serializable {

    private Node root;

    private class Node implements Serializable {

        private char c;
        private Node left;
        private Node right;
        private Node mid;
        private float x;
        private float y;
       // private Point2D.Float val;

    }


    public Point2D.Float get(String key) {
        Node x = get(root, key, 0);
        if(x == null) return null;
        return new Point2D.Float(x.x, x.y);
    }

    private Node get(Node x, String key, int d) {
        if(x == null) return null;
        char c = key.charAt(d);
        if(c < x.c) return get(x.left, key, d);
        else if(c > x.c) return get(x.right, key, d);
        else if(d < key.length()-1) return get(x.mid, key, d+1);
        else return x;
    }

    public void put(String key, Point2D.Float val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Point2D.Float val, int d) {
        char c = key.charAt(d);
        if(x == null) {
            x = new Node();
            x.c = c;
        }
        if(c < x.c) x.left = put(x.left, key, val, d);
        else if(c > x.c) x.right = put(x.right, key, val, d);
        else if(d < key.length()-1) x.mid = put(x.mid, key, val, d+1);
        else {
            x.x = val.x;
            x.y = val.y;
        }
        return x;
    }

}
