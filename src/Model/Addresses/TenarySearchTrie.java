package Model.Addresses;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

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
        if (x == null || key.equals("")) return null;
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        else return x;
    }

    public ArrayList<String> keysWithPrefix(String pre){
        ArrayList<String> list = new ArrayList<>();
        collect(get(root, pre, 0), pre, list);
        return list;
    }

    private void collect(Node x, String pre, ArrayList<String> list){
        if(x == null) return;
        if(list.size() >= 10) return;
        if(x.x != 0) list.add(pre + x.c);
        collect(x.mid, pre + x.c, list);
        collect(x.right, pre, list);
        collect(x.left, pre, list);
    }

    public ArrayList<String> keysThatMatch(String pat)
    {
        ArrayList<String> list = new ArrayList<>();
        collect(root, "", pat, "",false, list);
        return list;
    }
    public void collect(Node x, String pre, String pat, String address, boolean reachedEndOfPrefix, ArrayList<String> list) {
        if(list.size() >= 10) return;
        int d = pre.length();
        if (x == null) return;
        if (d == pat.length() && x.x != 0) list.add(pre);
        if (d == pat.length()){
            collect(x.left, address, list);
            collect(x.right, address, list);
            collect(x.mid, address + x.c, list);
        }else{
            char next = pat.charAt(d);
            collect(x.left, pre, pat, address, reachedEndOfPrefix, list);
            collect(x.right, pre, pat, address, reachedEndOfPrefix, list);
            if (x.c == next) {
                collect(x.mid, pre + next, pat,address + next, true, list);
            }else{
                if(!reachedEndOfPrefix) collect(x.mid, pre, pat, address + x.c, false, list);
            }
        }

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
