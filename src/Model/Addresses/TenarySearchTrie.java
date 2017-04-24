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
        private ArrayList<Value> values;
        // private Point2D.Float val;

    }


    public ArrayList<Value> get(String key) {
        Node x = get(root, key, 0);
        if(x == null) return null;
        return x.values;
    }

    private Node get(Node x, String key, int d) {
        if (x == null || key.equals("")) return null;
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        else return x;
    }


    public ArrayList<String> keysThatMatch(String pat)
    {
        ArrayList<String> list = new ArrayList<>();
        collect(root, "", pat, "",false, list);
        return list;
    }

    private void collect(Node x, String pre, ArrayList<String> list){
        if(x == null) return;
        if(list.size() >= 10) return;
        if(x.values != null) list.add(pre + x.c);
        collect(x.mid, pre + x.c, list);
        collect(x.right, pre, list);
        collect(x.left, pre, list);
    }

    public void collect(Node x, String pre, String pat, String address, boolean reachedEndOfPrefix, ArrayList<String> list) {
        if(list.size() >= 10) return;
        int d = pre.length();
        if (x == null) return;
        if (d == pat.length() && x.values != null) list.add(pre);
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

    public void put(String key, Value v) {
        root = put(root, key, v, 0);
    }

    private Node put(Node n, String key, Value v, int d) {
        char c = key.charAt(d);
        if(n == null) {
            n = new Node();
            n.c = c;
        }
        if(c < n.c) n.left = put(n.left, key, v, d);
        else if(c > n.c) n.right = put(n.right, key, v, d);
        else if(d < key.length()-1) n.mid = put(n.mid, key, v, d+1);
        else {
            if(n.values == null) n.values = new ArrayList<>();
            n.values.add(v);
        }
        return n;
    }

}