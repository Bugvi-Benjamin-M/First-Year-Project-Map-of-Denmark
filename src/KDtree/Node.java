package KDtree;

import Model.Elements.SuperElement;

import java.util.*;


public class Node extends Point {
    private HashSet<SuperElement> elements;
    private int depth;
    private Node left;
    private Node right;

    protected Node(float x, float y, int depth)
    {
        super(x, y);
        this.depth = depth;
    }

    protected Node getLeft() { return left; }

    protected void setLeft(Node left) { this.left = left; }

    protected Node getRight() { return right; }

    protected void setRight(Node right) { this.right = right; }

    protected int getDepth() { return depth; }

    protected void addElement(Pointer pointer) {
        elements.add(pointer.getElement());
    }

    protected void makeLeaf() { elements = new HashSet<>(); }

    protected HashSet<SuperElement> getElements() { return elements; }
}
