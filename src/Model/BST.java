package Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jakob on 16-03-2017.
 */
public class BST {

    private Node root;
    private boolean isCompareToLatitude;

    private ArrayList<Point> points = new ArrayList<>();

    private class Point{
        private Float latitudeKey;
        private Float longitudeKey;

        public Point(Float latitudeKey, Float longitudeKey){
            this.latitudeKey = latitudeKey;
            this.longitudeKey = longitudeKey;
        }

        public Float getLatitudeKey(){
            return latitudeKey;
        }

        public Float getLongitudeKey() {
            return longitudeKey;
        }
    }

    private class Node {
        private Float latitudeKey;   //lodret
        private Float longitudeKey;  //vandret
        private Node left;
        private Node right;
        private boolean compareToLatitude;
        private ArrayList<Element> elements;

        public Node(Float latitudeKey, Float longitudeKey, boolean compareToLatitude) {
            this.latitudeKey = latitudeKey;
            this.longitudeKey = longitudeKey;
            this.compareToLatitude = compareToLatitude;
        }

        public void addElement(Element element) {
            if (elements == null) {
                elements = new ArrayList<>();
            }
            elements.add(element);
        }

        public ArrayList<Element> getElements() {
            return elements;
        }
    }

    public void initialize(){
        int amountOfPoints = points.size();
        if(amountOfPoints > 1000){
            //OPSPLIT
        }

    }

    public void addPoint(Float latitudeKey, Float longitudeKey){
        points.add(new Point(latitudeKey, longitudeKey));
    }

    public ArrayList<Point> getPoints(){
        return points;
    }

    public ArrayList<Element> get(Float latitudeKey, Float longitudeKey) {
        return get(root, latitudeKey, longitudeKey);
    }

    private ArrayList<Element> get(Node x, Float latitudeKey, Float longitudeKey) {
        // Return the Long associated with the subtree rooted at x.
        // Return null if keys are not both present in the subtree rooted at x.
        if (x == null) return null;
        if (x.elements == null) {
            if (x.compareToLatitude) {
                int compare = latitudeKey.compareTo(x.latitudeKey);
                if (compare <= 0) return get(x.left, latitudeKey, longitudeKey);
                else return get(x.right, latitudeKey, longitudeKey);
            } else {
                int compare = longitudeKey.compareTo(x.longitudeKey);
                if (compare <= 0) return get(x.left, latitudeKey, longitudeKey);
                else return get(x.right, latitudeKey, longitudeKey);
            }
        }
        return x.getElements();
    }

    public void putNode(Node node){
        root = putNode(root, node);
    }

    private Node putNode(Node x, Node nodeToPut){
        if(x == null){
            return nodeToPut;
        }
        if(x.compareToLatitude){
            int compare = nodeToPut.latitudeKey.compareTo(x.latitudeKey);
            if(compare <= 0){
                x.left = putNode(x.left, nodeToPut);
            }
            else{
                x.right = putNode(x.right, nodeToPut);
            }
        }else{
            int compare = nodeToPut.longitudeKey.compareTo(x.longitudeKey);
            if(compare <= 0){
                x.left = putNode(x.left, nodeToPut);
            }
            else{
                x.right = putNode(x.right, nodeToPut);
            }
        }
        return x;
    }


    public void putElement(Element element){
        putElement(root, element);
    }

    private void putElement(Node x, Element element){
        if(x.elements == null) {
            if (x.compareToLatitude) {
                Road road = (Road) element;
                double d = road.getWay().get(0).getX();
                Float f = (float) d;
                int compare = f.compareTo(x.latitudeKey);
                if (compare <= 0) {
                    putElement(x.left, element);
                } else {
                    putElement(x.right, element);
                }
            } else {
                Road road = (Road) element;
                double d = road.getWay().get(0).getY();
                Float f = (float) d;
                int compare = f.compareTo(x.longitudeKey);
                if (compare <= 0) {
                    putElement(x.left, element);
                } else {
                    putElement(x.right, element);
                }
            }
        }else{
            x.addElement(element);
        }
    }
}
