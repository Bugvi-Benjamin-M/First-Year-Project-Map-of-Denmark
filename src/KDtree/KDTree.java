package KDtree;

import Model.Elements.SuperElement;
import java.io.Serializable;
import java.util.HashSet;

/**
 * This class will make a KDTree, that is ment to store items for a map application.
 * These items shall have a set of coordinates assigned, such that they can be stored
 * and retrieved from the KDTree using these coordinates. This is done by wrapping
 * the map items into a Pointer, which takes a set of coordinates and a sub-instance
 * of the class SuperElement as parameters.
 *
 * The KDTree is setup by using an instance of the NodeGenerator class. After
 * the KDTree have been setup it is ready for having pointers put.
 */
public class KDTree implements Serializable {
    private Node root;
    private HashSet<SuperElement> elementsToReturn;

    /**
     * Clear all the hashSets of pointers in the leaves of the KDTree.
     */
    public void clear(){
        if (root != null) clear(root);
    }

    //private method used recursively
    private void clear(Node node) {
        if (node.getElements() == null) {
            clear(node.getLeft());
            clear(node.getRight());
        } else {
            node.getElements().clear();
        }
    }

    /**
     * Lazy implementation of the getAllSections method.
     * Coordinates on earth will never come somewhat near the minimum and
     * maximum values set by this method, and it can therefore function as
     * a getAllSection method in a normal map application.
     */
    public HashSet<SuperElement> getAllSections(){
        return getManySections(-1000f, -1000f, 1000f, 1000f);
    }

    /**
     * The parameters taken in this method correspond to the top right and bottom left corners in a rectangle.
     * This rectangle will intersect and contain the areas of map divided by the KDTree.
     *
     * @param - float minX, float minY, float maxX, float maxY in the recangle in where the desired elements are contained.
     */
    public HashSet<SuperElement> getManySections(float minX, float minY, float maxX, float maxY) {
        elementsToReturn = new HashSet<>();

        if (root != null) {
            getManySections(root, minX, minY, maxX, maxY);
            return elementsToReturn;
        }
        return null;
    }

    //private method used recursively
    private void getManySections(Node currentNode, float minX, float minY, float maxX, float maxY) {
        if (currentNode.getElements() == null) {
            if (currentNode.getDepth() % 2 == 0) {
                if (currentNode.getX() > minX && currentNode.getX() > maxX) {
                    getManySections(currentNode.getLeft(), minX, minY, maxX, maxY);
                } else if (currentNode.getX() < minX && currentNode.getX() < maxX) {
                    getManySections(currentNode.getRight(), minX, minY, maxX, maxY);
                } else {
                    getManySections(currentNode.getLeft(), minX, minY, currentNode.getX(), maxY);
                    getManySections(currentNode.getRight(), currentNode.getX(), minY, maxX, maxY);
                }
            } else {
                if (currentNode.getY() > minY && currentNode.getY() > maxY) {
                    getManySections(currentNode.getLeft(), minX, minY, maxX, maxY);
                } else if (currentNode.getY() < minY && currentNode.getY() < maxY) {
                    getManySections(currentNode.getRight(), minX, minY, maxX, maxY);
                } else {
                    getManySections(currentNode.getLeft(), minX, minY, maxX, currentNode.getY());
                    getManySections(currentNode.getRight(), minX, currentNode.getY(), maxX, maxY);
                }
            }
        } else {
            for (SuperElement element : currentNode.getElements()) {
                elementsToReturn.add(element);
            }
        }
    }

    /**
     * This method will put nodes into the KDTree and build it. The first node
     * will be the root and the rest of the nodes will be placed accordingly to that.
     *
     * @param node - The node toi be put
     */
    public void putNode(Node node) { root = putNode(root, node); }

    //private method used recursively
    private Node putNode(Node parent, Node nodeToPut)
    {
        if (parent == null) {
            return nodeToPut;
        }

        if (parent.getDepth() % 2 == 0) {
            int compare = nodeToPut.compareToX(parent);
            if (compare <= 0)
                parent.setLeft(putNode(parent.getLeft(), nodeToPut));
            else
                parent.setRight(putNode(parent.getRight(), nodeToPut));
        } else {
            int compare = nodeToPut.compareToY(parent);
            if (compare <= 0)
                parent.setLeft(putNode(parent.getLeft(), nodeToPut));
            else
                parent.setRight(putNode(parent.getRight(), nodeToPut));
        }
        return parent;
    }

    public void putPointer(Pointer pointer) { putPointer(root, pointer); }

    //private method used recursively
    private void putPointer(Node currentNode, Pointer pointer) {
        if (currentNode.getElements() == null) {
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
            currentNode.addElement(pointer);
        }
    }
}