package Model.Addresses;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        private boolean isSignificant = false;
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


    public HashMap<Boolean, ArrayList<String>> keysThatMatch(String pat)
    {
        HashMap<Boolean, ArrayList<String>> map = new HashMap<>();
        map.put(true, new ArrayList<>());
        map.put(false, new ArrayList<>());
        collect(root, "", pat, "",false, map);
        return map;
    }

    private void collect(Node x, String pre, HashMap<Boolean, ArrayList<String>> map){
        if(x == null) return;
        if(map.get(true).size() > 10) return;
        if(x.values != null){
            if(x.isSignificant){
                map.get(true).add(pre + x.c);
            }else
            map.get(false).add(pre + x.c);
        }
        collect(x.mid, pre + x.c, map);
        collect(x.right, pre, map);
        collect(x.left, pre, map);
    }

    public void collect(Node x, String pre, String pat, String address, boolean reachedEndOfPrefix, HashMap<Boolean, ArrayList<String>> map) {
        if(map.get(true).size() > 10) return;
        int d = pre.length();
        if (x == null) return;
        //
        if(d == pat.length() - 1 && x.values != null && pat.charAt(pat.length() - 1) == x.c){
            if(x.isSignificant){
                map.get(true).add(address + x.c);
            }else
                map.get(false).add(address + x.c);
        }
        //Switch to normal prefix collect method
        if (d == pat.length()){
            collect(x, address, map);
        }else{
            char next = pat.charAt(d);
            collect(x.left, pre, pat, address, reachedEndOfPrefix, map);
            collect(x.right, pre, pat, address, reachedEndOfPrefix, map);
            if (x.c == next) {
                collect(x.mid, pre + next, pat,address + next, true, map);
            }else{
                if(!reachedEndOfPrefix) collect(x.mid, pre, pat, address + x.c, false, map);
            }
        }

    }

    public void put(String key, Value v, boolean isSignificant) {
        root = put(root, key, v, isSignificant, 0);
    }

    private Node put(Node n, String key, Value v, boolean isSignificant, int d) {
        char c = key.charAt(d);
        if(n == null) {
            n = new Node();
            n.c = c;
        }
        if(c < n.c) n.left = put(n.left, key, v, isSignificant, d);
        else if(c > n.c) n.right = put(n.right, key, v, isSignificant, d);
        else if(d < key.length() - 1) n.mid = put(n.mid, key, v, isSignificant, d+1);
        else {
            if(n.values == null) n.values = new ArrayList<>();
            n.values.add(v);
            n.isSignificant = isSignificant;
        }
        return n;
    }

}