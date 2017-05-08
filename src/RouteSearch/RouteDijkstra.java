package RouteSearch;

import Enums.TravelType;
import Model.Elements.RoadEdge;
import Model.Model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * @author Niclas Hedam
 * @author Andreas Blanke
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @version 05-05-2017
 */
public class RouteDijkstra {

    private Map<Point2D,Float> distTo;
    private Map<Point2D,RoadEdge> edgeTo;
    private PriorityQueue<Node> pQ;

    private Point2D start;
    private Point2D end;
    private TravelType type;
    private RoadGraphFactory factory;

    public RouteDijkstra(RoadGraph graph, Point2D start, Point2D end, TravelType type) {
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        this.type = type;
        factory = Model.getInstance().getGraphFactory();
        this.start = start;
        this.end = end;


        for (Point2D v : graph.getAdjacencyList().keySet()) {
            distTo.put(v, Float.POSITIVE_INFINITY);
        }
        distTo.put(start, 0.0f);

        // relax vertices in order of distance from s
        pQ = new PriorityQueue<Node>(graph.getNumberOfVertices(), new WeightComperator());
        pQ.add(new Node(start, distTo.get(start)));

        Node next;
        while ((next = pQ.poll()) != null) {
            Point2D v = next.point;
            for (RoadEdge e : graph.adjacent(v)) {
                relax(e);
            }
            if (v.equals(end)) {
                System.out.println("Found route!");
                return;
            }
        }
    }

    // relax edge e and update pq if changed
    private void relax(RoadEdge e) {
        Point2D v = e.getEither(), w = e.getOther(v);
        if (distTo.get(w) > distTo.get(v) + e.getWeight(type, start, end)) {
            distTo.put(w, distTo.get(v) + e.getWeight(type, start, end));
            edgeTo.put(w, e);

            //FIXME: Optimization if we have time
            if(w == end){
                pQ.remove(new Node(w, 0));      //The weight does not matter ..
            }
            pQ.add(new Node(w, distTo.get(w)));
        }
    }

    public float distTo(Point2D point) {
        return distTo.get(point);
    }

    public boolean hasPathTo(Point2D point) {
        return distTo.get(point) < Double.POSITIVE_INFINITY;
    }

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

    private class Node{
        public Point2D point;
        public Float weight;

        public Node(Point2D point, float weight){
            this.point = point;
            this.weight = weight;
        }

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

    private class WeightComperator implements Comparator<Node>
    {
        @Override
        public int compare(Node x, Node y)
        {
            return (x.weight.compareTo(y.weight));
        }
    }

}
