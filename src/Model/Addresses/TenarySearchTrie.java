package Model.Addresses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Character.toLowerCase;


public class TenarySearchTrie implements Serializable {

    private Node root;

    private class Node implements Serializable {

        private char c;
        private Node left;
        private Node right;
        private Node mid;
        private ArrayList<Value> values;
    }

    /**
     * @param key The string used to find the specific value.
     * @return Returns the value that matches to the key,
     * there is a possiblity of 2 equal strings with 2 different values, which is why a list is returned.
     */
    public ArrayList<Value> get(String key) {
        Node x = get(root, key, 0);
        if(x == null) return null;
        return x.values;
    }

    /**
     *
     * @param x The current Node looked at.
     * @param key The string used to find the specific value.
     * @param d the current character position of the string
     * @return
     */
    private Node get(Node x, String key, int d) {
        if (x == null || key.equals("")) return null;
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        // if the character of the node and the character of the string are equal but d is still smaller than the length
            // of the key.
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        // if d is the same size as the length of the key and the character of the node equals the character at the
            // last position of the string.
        else return x;
    }


    /**
     * This method returns a collection of strings that matches the pattern.
     * @param pat The pattern to look for in the trie.
     * @return A hashmap that contains a list of Cities and Streets and matches the pattern.
     * The hashmap makes it possible to give each string a significance.
     * A city is more significant than a street, which means it has to be prioritized in the autocomplete window.
     */
    public HashMap<Boolean, ArrayList<String>> keysThatMatch(String pat)
    {
        HashMap<Boolean, ArrayList<String>> map = new HashMap<>();
        //City list
        map.put(true, new ArrayList<>());
        //Street list
        map.put(false, new ArrayList<>());
        collect(root, "", pat, "", map);
        return map;
    }


    /**
     * This method is an extension of the standard collect method in the book Algorithms, 4th ed. page 738.
     *
     * @param x The current Node looked at.
     * @param pre The current position in the pattern. The pre string resets to the empty string if the recursive method meets
     *            a node that does not match the pattern.
     * @param pat The pattern which is used to find a match.
     * @param address The "path" through the trie which leads to the current node. This string grows every time the recursive method moves to the
     *                next node in the middle. The string will be added to the list to return if it fully matches the pattern and the current node contains a value.
     * @param map     The map that matching strings will be added to.
     */
    public void collect(Node x, String pre, String pat, String address, HashMap<Boolean, ArrayList<String>> map) {
        //More than 10 cities are not needed.
        if(map.get(true).size() > 10) return;
        //Current position in the pattern.
        int d = pre.length();
        if (x == null) return;
        //If a node directly at the end of the pattern contains a value it is added to the list to return.
        if(d == pat.length() - 1 && x.values != null && pat.charAt(pat.length() - 1) == x.c){
            for(Value v : x.values)
            if(v.isSignificant()){
                map.get(true).add(address + x.c);
            }else
                map.get(false).add(address + x.c);
        }
        //Switch to standard prefix collect method
        if (d == pat.length()){
            collect(x, address, map);
        }else{
            //Move to the left and right side of the tree with the same string values.
            collect(x.left, pre, pat, address, map);
            collect(x.right, pre, pat, address, map);
            //look at the next char in the pattern.
            char next = pat.charAt(d);
            Character c = x.c;
            //compare the char in the pattern with the char at the node.
                //if its a match, move down the middle and increase the pre string and the address string.
                    //else move down the middle and reset the pre string.
            if (toLowerCase(c) == toLowerCase(next)) {
                collect(x.mid, pre + x.c, pat,address + x.c, map);
            }else{
                    collect(x.mid, "", pat, address + x.c, map);
            }
        }

    }

    /**
     * This recursive method is very similar to the collect() method in the book
     * Algorithms, 4th ed. on page 738. It has been adjusted to work for a ternary search trie.
     * The method is used as soon as the end of the given pattern is reached. It will collect every string that is below
     * the node that contains the last character of the pattern.
     * @param x The current Node looked at.
     * @param pre The "path" through the trie which leads to the current node. This string grows every time the recursive method moves to the
     *            next node in the middle. The string will be added to the list to return if it fully matches the pattern and the current node contains a value.
     * @param map The map that matching strings will be added to.
     */
    private void collect(Node x, String pre, HashMap<Boolean, ArrayList<String>> map){
        if(x == null) return;
        // More than 10 cities are not needed.
        if(map.get(true).size() > 10) return;
        if(x.values != null){
            for(Value v : x.values) {
                if (v.isSignificant()) {
                    // It is a City
                    map.get(true).add(pre + x.c);
                } else
                    // It is a Street
                    map.get(false).add(pre + x.c);
            }
        }
        // The pre string grows when moving down the middle node because we know that every string beyond contains
        // the character of the node.
        collect(x.mid, pre + x.c, map);
        collect(x.right, pre, map);
        collect(x.left, pre, map);
    }

    /**
     * Adds a key - value pair to the trie.
     * @param key The key which specifies at which node in the trie the value is to be stored.
     * @param v The value which will be stored at the node that contains the last character of the key.
     */
    public void put(String key, Value v) {
        root = put(root, key, v, 0);
    }

    /**
     * This recursive put method is very similar to the method in the book
     * Algorithms, 4th ed. by Sedgewick and Wayne page 747.
     * @param x The current node looked at.
     * @param key The key which specifies at which node in the trie the value is to be stored.
     * @param v The value which will be stored at the node that contains the last character of the key.
     * @param d The current position in the trie. If d equals the length of the string and the char at the node matches the string,
     *          the value is added.
     * @return
     */
    private Node put(Node x, String key, Value v, int d) {
        char c = key.charAt(d);
        if(x == null) {
            x = new Node();
            x.c = c;
        }
        if(c < x.c) x.left = put(x.left, key, v, d);
        else if(c > x.c) x.right = put(x.right, key, v, d);
        else if(d < key.length() - 1) x.mid = put(x.mid, key, v, d+1);
        else {
            if(x.values == null) x.values = new ArrayList<>();
            x.values.add(v);
        }
        return x;
    }

}