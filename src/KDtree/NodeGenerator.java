package KDtree;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class NodeGenerator {

    private static final int AMOUNT_OF_NODES_DEFAULT = 18788597;
    private static final int DEPTH_DEFAULT = 14;
    
    private int depth;
    private int amountOfNodes;
    private Point2D.Float[] points;
    private List<Node> medians;
    private int pointsIndex;

    public NodeGenerator() { this(AMOUNT_OF_NODES_DEFAULT, DEPTH_DEFAULT); }

    /**
     * Sets fields and variables that are required to contruct the KDTree
     * @param amountOfNodes the amount of data in the KDTree
     * @param depth the depth of the KDTree
     */
    public NodeGenerator(int amountOfNodes, int depth)
    {
        medians = new ArrayList<>();
        this.amountOfNodes = amountOfNodes;
        this.depth = depth;
        pointsIndex = 0;
        points = new Point2D.Float[amountOfNodes];
    }

    /**
     * Builds a KD tree by putting nodes into the KDTree, which represent medians of the input data.
     * @param tree
     */
    public void setupTree(KDTree tree)
    {
        for (int i = 0; i < medians.size(); i++) {
            Node median = medians.get(i);
            Node node = new Node(median.getX(), median.getY(), median.getDepth());
            if (median.getElements() != null)
                node.makeLeaf();
            tree.putNode(node);
        }
    }

    /**
     * creates a list of medians, which are afterwards put into the KDTree.
     */
    public void initialise() { initialise(null, 0, amountOfNodes - 1); }

    /**
     * Builds a list of medians of the given input data.
     * The amount of medians depend on the specified depth and the size of the input.
     * @param parent the current node to be looked at.
     * @param low the lower bound of the section of the array looked at.
     * @param high the higher bound of the section of the array looked at.
     */
    private void initialise(Node parent, int low, int high)
    {
        Point2D.Float median;
        int medianDepth = 0;

        //Sorting the input points by the X coordinate and finding the median.
        if (parent == null || parent.getDepth() % 2 == 1) {
            median = findMedianX(low, high);
            if (parent == null)
                medianDepth = 0;
            else
                medianDepth = parent.getDepth() + 1;
        }
        //Sorting the input points by the Y coordinate and finding the median.
        else {
            median = findMedianY(low, high);
            medianDepth = parent.getDepth() + 1;
        }
        float floatX = (float)median.getX();
        float floatY = (float)median.getY();
        Node medianNode;
        //The depth is below the specified max depth.
            //A new median is added to the list and the recursion continues.
        if (medianDepth < depth) {
            medianNode = new Node(floatX, floatY, medianDepth);
            medians.add(medianNode);
            initialise(medianNode, low, ((low + high) / 2) - 1);
            initialise(medianNode, ((low + high) / 2) + 1, high);
        }
        //The specified max depth is reached.
            //The new median is turned into a Leaf and added to the median list.
        else if (medianDepth == depth) {
            medianNode = new Node(floatX, floatY, medianDepth);
            medianNode.makeLeaf();
            medians.add(medianNode);
        }
    }

    public void addPoint(Point2D.Float point)
    {
        points[pointsIndex] = point;
        pointsIndex++;
    }

    /**
     * Finds the median of an array of points by the x coordinate.
     * @param low the minimum bound of the current section of the array.
     * @param high the maximum bound of the current section of the array.
     * @return the median.
     */
    private Point2D.Float findMedianX(int low, int high)
    {
        int median = (low + high) / 2;
        while (high > low) {
            int j = partitionX(low, high);
            if (j == median)
                return points[median];
            else if (j > median)
                high = j - 1;
            else if (j < median)
                low = j + 1;
        }
        return points[median];
    }

    /**
     * Finds the median of an array of points by the y coordinate.
     * @param low the minimum bound of the current section of the array.
     * @param high the maximum bound of the current section of the array.
     * @return the median.
     */
    private Point2D.Float findMedianY(int low, int high)
    {
        int median = (low + high) / 2;
        while (high > low) {
            int j = partitionY(low, high);
            if (j == median)
                return points[median];
            else if (j > median)
                high = j - 1;
            else if (j < median)
                low = j + 1;
        }
        return points[median];
    }

    private int partitionX(int low, int high)
    {
        int i = low;
        int j = high + 1;
        Point2D.Float v = points[low];
        while (true) {
            while (lessX(points[++i], v))
                if (i == high)
                    break;
            while (lessX(v, points[--j]))
                if (j == low)
                    break;
            if (i >= j)
                break;
            exchange(i, j);
        }
        exchange(low, j);
        return j;
    }

    private int partitionY(int low, int high)
    {
        int i = low;
        int j = high + 1;
        Point2D.Float v = points[low];
        while (true) {
            while (lessY(points[++i], v))
                if (i == high)
                    break;
            while (lessY(v, points[--j]))
                if (j == low)
                    break;
            if (i >= j)
                break;
            exchange(i, j);
        }
        exchange(low, j);
        return j;
    }

    private boolean lessX(Point2D.Float a, Point2D.Float b)
    {
        Double x1 = a.getX();
        Double x2 = b.getX();
        Double y1 = a.getY();
        Double y2 = b.getY();
        if (x1.compareTo(x2) == 0) {
            if (y1.compareTo(y2) == 0)
                return false;
            else
                return y1.compareTo(y2) < 0;
        } else {
            return x1.compareTo(x2) < 0;
        }
    }

    private boolean lessY(Point2D.Float a, Point2D.Float b)
    {
        Double x1 = a.getX();
        Double x2 = b.getX();
        Double y1 = a.getY();
        Double y2 = b.getY();
        if (y1.compareTo(y2) == 0) {
            if (x1.compareTo(x2) == 0)
                return false;
            else
                return x1.compareTo(x2) < 0;
        } else {
            return y1.compareTo(y2) < 0;
        }
    }

    private void exchange(int i, int j)
    {
        Point2D.Float temp = points[i];
        points[i] = points[j];
        points[j] = temp;
    }
}
