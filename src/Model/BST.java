package Model;

import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Jakob on 16-03-2017.
 */
public class BST {

    private Node root;

    private Point2D[] points = new Point2D[18788597];
    int pointsIndex = 0;

    private class Node {
        private Double latitudeKey;   //lodret
        private Double longitudeKey;  //vandret
        private Node left;
        private Node right;
        private int depth;
        private ArrayList<Element> elements;

        public Node(Double latitudeKey, Double longitudeKey, int depth) {
            this.latitudeKey = latitudeKey;
            this.longitudeKey = longitudeKey;
            this.depth = depth;
        }

        public void addElement(Element element) {
            elements.add(element);
        }

        public ArrayList<Element> getElements() {
            return elements;
        }
    }

    public void initialize(){
        initialize(null, 0, 18788597 - 1);

    }

    private void initialize(Node parent, int low, int high){
        int size = high - low;
        int lowTemp = low;
        int highTemp = high;
        Point2D median;
        int medianDepth;
        if(size > 40000){
            if(parent == null || parent.depth % 2 == 1){
                System.out.println("Sorting X (longitude)");
                median = findMedianLongitude(lowTemp, highTemp);
                if(parent == null){
                    medianDepth = 0;
                }
                else{
                    medianDepth = parent.depth + 1;
                }
            }
            else{
                System.out.println("Sorting Y (latitude)");
                median = findMedianLatitude(lowTemp, highTemp);
                medianDepth = parent.depth + 1;
            }
            System.out.println(medianDepth);
            Node medianNode = new Node(median.getX(), median.getY(),medianDepth);
            putNode(medianNode);
            initialize(medianNode, low, low + (high-low)/2);

            initialize(medianNode, low + (high-low)/2, high);
        }
    }

    private Point2D findMedianLatitude(int low, int high){ //y
        int median = low + ((high-low)/2);
        while(high > low){
            int j = partitionLatitude(low, high);
            if(j == median) return points[median];
            else if(j > median) high = j - 1;
            else if(j < median) low = j + 1;
        }
        return points[median];
    }
    private Point2D findMedianLongitude(int low, int high){  //x
        int median = low + ((high-low)/2);
        while(high > low){
            int j = partitionLongitude(low, high);
            if(j == median) return points[median];
            else if(j > median) high = j - 1;
            else if(j < median) low = j + 1;
        }
        return points[median];
    }
    private int partitionLatitude(int low, int high){
        int i = low;
        int j = high +1;
        Point2D v = points[low];
        while(true){
            while(lessLatitude(points[++i], v)) if(i == high) break;
            while(lessLatitude(v, points[--j])) if(j == low) break;
            if(i >= j) break;
            exchange(i, j);
        }
        exchange(low, j);
        return j;
    }
    private int partitionLongitude(int low, int high){
        int i = low;
        int j = high +1;
        Point2D v = points[low];
        while(true){
            while(lessLongitude(points[++i], v)) if(i == high) break;
            while(lessLongitude(v, points[--j])) if(j == low) break;
            if(i >= j) break;
            exchange(i, j);
        }
        exchange(low, j);
        return j;
    }
    private boolean lessLatitude(Point2D a, Point2D b){
        Double y1 = a.getY();
        Double y2 = b.getY();
        return y1.compareTo(y2) < 0;
    }

    private boolean lessLongitude(Point2D a, Point2D b){
        Double x1 = a.getX();
        Double x2 = b.getX();
        return x1.compareTo(x2) < 0;
    }

    private void exchange(int i, int j){
        Point2D temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }

    public void addPoint(Point2D point){
        points[pointsIndex] = point;
        pointsIndex++;
    }

    public Point2D[] getPoints(){
        return points;
    }

    public ArrayList<Element> getSection(Double latitudeKey, Double longitudeKey) {
        return getSection(root, latitudeKey, longitudeKey);
    }

    private ArrayList<Element> getSection(Node x, Double latitudeKey, Double longitudeKey) {
        // Return the Long associated with the subtree rooted at x.
        // Return null if keys are not both present in the subtree rooted at x.
        if (x == null) return null;
        if (x.elements == null) {
            if(x.depth % 2 == 0){
                int compare = longitudeKey.compareTo(x.longitudeKey);
                if (compare <= 0) return getSection(x.left, latitudeKey, longitudeKey);
                else return getSection(x.right, latitudeKey, longitudeKey);
            }
            else{
                int compare = latitudeKey.compareTo(x.latitudeKey);
                if (compare <= 0) return getSection(x.left, latitudeKey, longitudeKey);
                else return getSection(x.right, latitudeKey, longitudeKey);
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
        if(x.depth % 2 == 0){
            int compare = nodeToPut.longitudeKey.compareTo(x.longitudeKey);
            if(compare <= 0){
                x.left = putNode(x.left, nodeToPut);
            }
            else{
                x.right = putNode(x.right, nodeToPut);
            }
        }
        else{
            int compare = nodeToPut.latitudeKey.compareTo(x.latitudeKey);
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
        if(x.elements == null && x.left == null && x.right == null){
            x.elements = new ArrayList<>();
            x.addElement(element);
        }
        if(x.elements == null) {
            if(x.depth % 2 == 0){
                Road road = (Road) element;
                Double x1 = road.getWay().get(0).getX();
                Double x2 = x.longitudeKey;
                int compare = x1.compareTo(x2);
                if (compare <= 0) {
                    putElement(x.left, element);
                } else {
                    putElement(x.right, element);
                }
            } else {
                Road road = (Road) element;
                Double y1 = road.getWay().get(0).getY();
                Double y2 = x.latitudeKey;
                int compare = y1.compareTo(y2);
                if (compare <= 0) {
                    putElement(x.left, element);
                } else {
                    putElement(x.right, element);
                }
            }
        }
        else{
            x.addElement(element);
        }
    }
}