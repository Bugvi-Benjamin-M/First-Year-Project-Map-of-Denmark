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

    private float h(Point2D current){
        return (float)HelperFunctions.lazyDistance(current, end);
    }

    // relax edge e and update pq if changed
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

    private void handleNextNode(List<RoadEdge> adj){
        for (RoadEdge e : adj) {
            relax(e);
        }
    }

    public float distTo(Point2D point) {
        Float dist = distTo.get(point);
        return dist != null ? dist : Float.POSITIVE_INFINITY;
    }

    public boolean hasPathTo(Point2D point) {
        Float dist = distTo.get(point);
        return dist != null ? dist < Double.POSITIVE_INFINITY : false;
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
        public int hashCode(){
            return point.hashCode();
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
