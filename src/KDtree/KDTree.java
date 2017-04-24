package KDtree;

import Model.Elements.Element;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Jakob on 30-03-2017.
 */
public class KDTree implements Serializable {
    private Node root;
    private HashSet<Element> elementsToReturn;
    private HashSet<Element> sectionToReturn;

    public void clear()
    {
        if (root != null)
            clear(root);
    }

    private void clear(Node node)
    {
        if (node.getPointers() == null) {
            clear(node.getLeft());
            clear(node.getRight());
        } else {
            node.getPointers().clear();
        }
    }

    public HashSet<Element> getManySections(float minX, float minY, float maxX,
        float maxY)
    {
        elementsToReturn = new HashSet<>();

        if (root != null) {
            getManySections(root, minX, minY, maxX, maxY);
            return elementsToReturn;
        }
        // TODO ?make null return value to an exception?
        return null;
    }

    private void getManySections(Node currentNode, float minX, float minY,
        float maxX, float maxY)
    {
        if (currentNode.getPointers() == null) {
            if (currentNode.getDepth() % 2 == 0) {
                if (currentNode.getX() > minX && currentNode.getX() > maxX) {
                    getManySections(currentNode.getLeft(), minX, minY, maxX, maxY);
                } else if (currentNode.getX() < minX && currentNode.getX() < maxX) {
                    getManySections(currentNode.getRight(), minX, minY, maxX, maxY);
                } else {
                    getManySections(currentNode.getLeft(), minX, minY, currentNode.getX(),
                        maxY);
                    getManySections(currentNode.getRight(), currentNode.getX(), minY,
                        maxX, maxY);
                }
            } else {
                if (currentNode.getY() > minY && currentNode.getY() > maxY) {
                    getManySections(currentNode.getLeft(), minX, minY, maxX, maxY);
                } else if (currentNode.getY() < minY && currentNode.getY() < maxY) {
                    getManySections(currentNode.getRight(), minX, minY, maxX, maxY);
                } else {
                    getManySections(currentNode.getLeft(), minX, minY, maxX,
                        currentNode.getY());
                    getManySections(currentNode.getRight(), minX, currentNode.getY(),
                        maxX, maxY);
                }
            }
        } else {
            for (Pointer pointer : currentNode.getPointers()) {
                elementsToReturn.add(pointer.getElement());
            }
        }
    }

    public HashSet<Element> getSection(float x, float y)
    {
        sectionToReturn = new HashSet<>();
        if (root != null) {
            getSection(root, x, y);
            return sectionToReturn;
        }
        return null;
    }

    public void getSection(Node currentNode, float x, float y)
    {
        if (currentNode.getPointers() == null) {
            if (currentNode.getDepth() % 2 == 0) {
                if (currentNode.getX() >= x) {
                    getSection(currentNode.getLeft(), x, y);
                } else {
                    getSection(currentNode.getRight(), x, y);
                }
            }
            if (currentNode.getDepth() % 2 == 1) {
                if (currentNode.getY() >= y) {
                    getSection(currentNode.getLeft(), x, y);
                } else {
                    getSection(currentNode.getRight(), x, y);
                }
            }
        } else {
            for (Pointer pointer : currentNode.getPointers()) {
                sectionToReturn.add(pointer.getElement());
            }
        }
    }

    public void putNode(Node node) { root = putNode(root, node); }

    private Node putNode(Node parent, Node nodeToPut)
    {
        if (parent == null) {
            return nodeToPut;
        }

        if (parent.getDepth() % 2 == 0) {
            int compare = nodeToPut.compareToX(parent);
            if (compare <= 0)
                parent.setLeft(putNode(parent.getLeft(), nodeToPut));
            if (compare > 0)
                parent.setRight(putNode(parent.getRight(), nodeToPut));
        } else {
            int compare = nodeToPut.compareToY(parent);
            if (compare <= 0)
                parent.setLeft(putNode(parent.getLeft(), nodeToPut));
            if (compare > 0)
                parent.setRight(putNode(parent.getRight(), nodeToPut));
        }
        return parent;
    }

    public void putPointer(Pointer pointer) { putPointer(root, pointer); }

    private void putPointer(Node currentNode, Pointer pointer)
    {
        if (currentNode.getPointers() == null) {
            if (currentNode.getDepth() % 2 == 0) {
                int compare = pointer.compareToX(currentNode);
                if (compare <= 0)
                    putPointer(currentNode.getLeft(), pointer);
                else
                    putPointer(currentNode.getRight(), pointer);
            } else {
                int compare = pointer.compareToY(currentNode);
                if (compare <= 0)
                    putPointer(currentNode.getLeft(), pointer);
                else
                    putPointer(currentNode.getRight(), pointer);
            }
        } else {
            currentNode.addPointer(pointer);
        }
    }
}
