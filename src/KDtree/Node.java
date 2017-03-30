package KDtree;

import java.util.*;

/**
 * Created by Jakob on 30-03-2017.
 */
public class Node extends Point{
    private List<Pointer> pointers;
    private int depth;
    private Node left;
    private Node right;

    public Node(float x, float y, int depth){
        super(x, y);
        this.depth = depth;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getDepth() {
        return depth;
    }

    public void addPointer(Pointer pointer){
        pointers.add(pointer);
    }

    public void makeLeaf(){
        pointers = new ArrayList<>();
    }

    public List<Pointer> getPointers(){
        return pointers;
    }
}
