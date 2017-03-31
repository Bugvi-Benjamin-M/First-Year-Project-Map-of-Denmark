package KDtree;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakob on 30-03-2017.
 */
public class NodeGenerator {

    private static final int AMOUNT_OF_NODES_DEFAULT = 18788597;
    private static final int DEPTH_DEFAULT = 14;
    private int depth;
    private int amountOfNodes;

    private Point2D.Float[] points;

    private List<Node> medians;
    private int pointsIndex;

    public NodeGenerator() {
        this(AMOUNT_OF_NODES_DEFAULT, DEPTH_DEFAULT);
    }

    public NodeGenerator(int amountOfNodes, int depth) {
        medians = new ArrayList<>();
        this.amountOfNodes = amountOfNodes;
        this.depth = depth;
        pointsIndex = 0;
        pointsIndex = 0;
        points = new Point2D.Float[amountOfNodes];
    }

    public void addPoint(Point2D.Float point) {
        points[pointsIndex] = point;
        pointsIndex++;
    }



}
