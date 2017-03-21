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
    private Point[] pointsArray;

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
        pointsArray = points.toArray(new Point[points.size()]);
    }

    private Point findMedianLatitude(Point[] a){
        int median = a.length / 2;
            int low = 0;
        int high = a.length - 1;
        while(high > low){
            int j = partitionLatitude(a, low, high);
            if(j == median) return a[median];
            else if(j > median) high = j - 1;
            else if(j < median) low = j + 1;
        }
        return a[median];
    }
    private Point findMedianLongitude(Point[] a){
        int median = a.length / 2;
        int low = 0;
        int high = a.length - 1;
        while(high > low){
            int j = partitionLongitude(a, low, high);
            if(j == median) return a[median];
            else if(j > median) high = j - 1;
            else if(j < median) low = j + 1;
        }
        return a[median];
    }
    private int partitionLatitude(Point[] a, int low, int high){
        int i = low;
        int j = high +1;
        Point v = a[low];
        while(true){
            while(lessLatitude(a[++i], v)) if(i == high) break;
            while(lessLatitude(v, a[--j])) if(j == low) break;
            if(i >= j) break;
            exchange(a, i, j);
        }
        exchange(a, low, j);
        return j;
    }
    private int partitionLongitude(Point[] a, int low, int high){
        int i = low;
        int j = high +1;
        Point v = a[low];
        while(true){
            while(lessLongitude(a[++i], v)) if(i == high) break;
            while(lessLongitude(v, a[--j])) if(j == low) break;
            if(i >= j) break;
            exchange(a, i, j);
        }
        exchange(a, low, j);
        return j;
    }
    private boolean lessLatitude(Point a, Point b){
        return a.getLatitudeKey().compareTo(b.getLatitudeKey()) < 0;
    }
    private boolean lessLongitude(Point a, Point b){
        return a.getLongitudeKey().compareTo(b.getLongitudeKey()) < 0;
    }
    private void exchange(Point[] a, int i, int j){
        Point temp = a[i];
        a[i] = a[j];
        a[j] = temp;
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
