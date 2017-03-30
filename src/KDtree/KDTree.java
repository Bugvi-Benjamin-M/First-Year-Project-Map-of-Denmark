package KDtree;

/**
 * Created by Jakob on 30-03-2017.
 */
public class KDTree {
    private Node root;
    private static final int AMOUNT_OF_NODES_DEFAULT = 18788597;
    private static final int DEPTH_DEFAULT = 14;

    public KDTree(){

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

    }
}
