package KDtree;

import Model.Element;

import java.util.HashSet;

/**
 * Created by Jakob on 30-03-2017.
 */
public class KDTree {
    private Node root;
    private static final int AMOUNT_OF_NODES_DEFAULT = 18788597;
    private static final int DEPTH_DEFAULT = 14;
    private HashSet<Element> elementsToReturn;

    public KDTree(){

    }

    public HashSet<Element> getManyPointers(float minX, float minY, float maxX,float maxY){
        elementsToReturn = new HashSet<>();

        if(root != null) {
            getManyPointers(root, minX, minY, maxX, maxY);
            return elementsToReturn;
        }
        //TODO ?make null return value to an exception?
        return null;
    }

    private void getManyPointers(Node currentNode, float minX, float minY, float maxX, float maxY){
        if(currentNode.getPointers() == null){
            if(currentNode.getDepth() % 2 == 0){
                if(currentNode.getX() > minX && currentNode.getX() > maxX){
                    getManyPointers(currentNode.getLeft(), minX, minY, maxX, maxY);
                }
                else if (currentNode.getX() < minX && currentNode.getX() < maxX){
                    getManyPointers(currentNode.getRight(), minX, minY, maxX, maxY);
                }
                else{
                    getManyPointers(currentNode.getLeft(), minX, minY, currentNode.getX(), maxY);
                    getManyPointers(currentNode.getRight(), currentNode.getX(), minY, maxX, maxY);
                }
            }
            else{
                if(currentNode.getY() > minY && currentNode.getY() > maxY){
                    getManyPointers(currentNode.getLeft(), minX, minY, maxX, maxY);
                }
                else if (currentNode.getY() < minY && currentNode.getY() < maxY){
                    getManyPointers(currentNode.getRight(), minX, minY, maxX, maxY);
                }
                else{
                    getManyPointers(currentNode.getLeft(), minX, minY, maxX, currentNode.getY());
                    getManyPointers(currentNode.getRight(), minX, currentNode.getY(), maxX, maxY);
                }
            }
        }
        else{
            for(Pointer pointer : currentNode.getPointers()){
                elementsToReturn.add(pointer.getElement());
            }
        }
    }

    public void putNode(Node node){
        root = putNode(root, node);
    }

    private Node putNode(Node parent, Node nodeToPut){
        if(parent.getDepth() % 2 == 0){
            int compare = nodeToPut.compareToX(parent);
            if(compare <= 0) parent.setLeft(putNode(parent.getLeft(), nodeToPut));
            if(compare > 0) parent.setRight(putNode(parent.getRight(), nodeToPut));
        }
        else{
            int compare = nodeToPut.compareToY(parent);
            if(compare <= 0) parent.setLeft(putNode(parent.getLeft(), nodeToPut));
            if(compare > 0) parent.setRight(putNode(parent.getRight(), nodeToPut));
        }
        return parent;
    }

    public void putPointer(Pointer pointer){
        putPointer(root, pointer);
    }

    private void putPointer(Node currentNode, Pointer pointer){
        if(currentNode.getPointers() == null){
            if(currentNode.getDepth() % 2 == 0){
                int compare = pointer.compareToX(currentNode);
                if(compare <= 0 ) putPointer(currentNode.getLeft(), pointer);
                else putPointer(currentNode.getRight(), pointer);
            }
            else{
                int compare = pointer.compareToY(currentNode);
                if(compare <= 0 ) putPointer(currentNode.getLeft(), pointer);
                else putPointer(currentNode.getRight(), pointer);
            }
        }
        else{
            currentNode.addPointer(pointer);
        }
    }
}
