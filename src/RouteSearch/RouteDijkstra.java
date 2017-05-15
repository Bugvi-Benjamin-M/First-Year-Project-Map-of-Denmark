package RouteSearch;

import Enums.TravelType;
import Model.Elements.RoadEdge;
import Model.Model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import java.util.Comparator;
import java.util.PriorityQueue;

import Helpers.HelperFunctions;
import Helpers.GlobalValue;
import Controller.PreferencesController;



public class RouteDijkstra {

    private Map<Point2D,Float> distTo;
    private Map<Point2D,RoadEdge> edgeTo;
    private PriorityQueue<Node> pQ;

    private Point2D start;
    private Point2D end;
    private TravelType type;
    private RoadGraphFactory factory;
    private boolean fast = PreferencesController.getInstance().getUseFastestRouteSetting();

    /**
     * Constructor.
     * Takes a graph as argument, two points and the traveltype.
     * The end point is required to automatically stop the algorithm, when we reach the final destination.
     * The travel type is needed to stop people from biking on the motorway.
     */
    public RouteDijkstra(RoadGraph graph, Point2D start, Point2D end, TravelType type) {
        distTo = new HashMap<Point2D,Float>(graph.getNumberOfVertices());
        edgeTo = new HashMap<Point2D,RoadEdge>(graph.getNumberOfVertices());
        this.type = type;
        factory = Model.getInstance().getGraphFactory();
        this.start = start;
        this.end = end;

        distTo.put(start, 0.0f);

        // relax vertices in order of distance from s
        pQ = new PriorityQueue<Node>(graph.getNumberOfVertices(), new WeightComperator());
        pQ.add(new Node(start, distTo.get(start)));

        Node next;
        while ((next = pQ.poll()) != null) {
            Point2D v = next.point;
            List<RoadEdge> adj = graph.adjacent(v);
            if(adj != null){
                handleNextNode(adj);
            }

            if (v.equals(end)) {
                return; //done
            }
        }
    }

    /**
     * A* function.
     * Calculates the ever-smallest possible distance to the final destination.
     * It's used to weight roads, so that going in the right direction weights
     * less than going in the wrong directon.
     */
    private float h(Point2D current){
        return (float)HelperFunctions.lazyDistance(current, end);
    }

    /**
     * "Relaxing" the edge, checking whether it has found a faster road and
     * inserts the edge into a priority queue.
     *
     * The queue is lazy.
     */
    private void relax(RoadEdge e) {
        Point2D v = e.getEither(), w = e.getOther(v);
        Float distV = distTo.get(v), distW = distTo.get(w);
        if (distW == null || distW > distV + e.getWeight(type, start, end, fast)) {
            distTo.put(w, distV + e.getWeight(type, start, end, fast));
            edgeTo.put(w, e);

            Node next;
            float weight = distTo.get(w);
            if(fast){
                next = new Node(w, weight + h(w) / 130);
            }else{
                next = new Node(w, weight + h(w));
            }

            if(w == end){
                pQ.remove(next); //Takes O(n)
            }
            pQ.add(next); //Takes O(n)
        }
    }

    /**
     * A for loop releaxing all edges on a given vertex, given as a list.
     */
    private void handleNextNode(List<RoadEdge> adj){
        for (RoadEdge e : adj) {
            relax(e);
        }
    }

    /**
     * Returns the distance from our startpoint to the final destination or any
     * given destination, that Dijkstra reached before the end-point.
     */
    public float distTo(Point2D point) {
        Float dist = distTo.get(point);
        return dist != null ? dist : Float.POSITIVE_INFINITY;
    }

    /**
     * Checks whether we have a path to the final destination or any
     * given destination, that Dijkstra reached before the end-point.
     */
    public boolean hasPathTo(Point2D point) {
        Float dist = distTo.get(point);
        return dist != null ? dist < Double.POSITIVE_INFINITY : false;
    }

    /**
     * Returns a stack of the optimal road to the end point.
     * The stack contains RoadEdges.
     */
    public Iterable<RoadEdge> path() {
        if (!hasPathTo(end))
            return null;
        Stack<RoadEdge> path = new Stack<>();
        for (RoadEdge e = edgeTo.get(end);
             e != null; e = edgeTo.get(e.getEither())) {
            path.push(e);
        }
        return path;
    }

    /**
     * An internal class describing a vertex and it's weight.
     */
    private class Node{
        public Point2D point;
        public Float weight;

        /**
         * Constructor
         */
        public Node(Point2D point, float weight){
            this.point = point;
            this.weight = weight;
        }

        /**
         * Overrides the hashCode and states that the hashCode is equal to the point's hashcode.
         */
        @Override
        public int hashCode(){
            return point.hashCode();
        }

        /**
         * Overrides the equals function, and determines that all Nodes on
         * the same point are equal, ignoring the weight completely.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if(obj instanceof Node == false){
                return false;
            }

            return this.point.equals(((Node)obj).point);
        }
    }

    /**
     * A comparator to compare nodes based on weight.
     */
    private class WeightComperator implements Comparator<Node>
    {
        /**
         * This compares the two nodes based on their weight.
         */
        @Override
        public int compare(Node x, Node y)
        {
            return (x.weight.compareTo(y.weight));
        }
    }

}
