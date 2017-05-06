package RouteSearch;

import Enums.TravelType;
import Model.Elements.RoadEdge;
import Model.Model;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RouteDijkstra {

    private Map<Point2D,Float> distTo;
    private Map<Point2D,RoadEdge> edgeTo;
    private IndexMinPQ<Float> pQ;

    private Point2D end;
    private TravelType type;
    private RoadGraphFactory factory;

    public RouteDijkstra(RoadGraph graph, Point2D start, Point2D end, TravelType type) {
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();
        this.type = type;
        factory = Model.getInstance().getGraphFactory();
        this.end = end;

        for (Point2D v : graph.getAdjacencyList().keySet()) {
            distTo.put(v, Float.POSITIVE_INFINITY);
        }
        distTo.put(start, 0.0f);

        // relax vertices in order of distance from s
        pQ = new IndexMinPQ<>(graph.getNumberOfVertices());
        pQ.insert(factory.getID(start), distTo.get(start));
        while (!pQ.isEmpty()) {
            Point2D v = factory.getPoint(pQ.delMin());
            for (RoadEdge e : graph.adjacent(v)) {
                relax(e);
                // System.out.println("Relaxing " + v + "; Distance = " + distTo.get(v));
            }
            if (v.equals(end)) {
                return;
            }
        }
    }

    // relax edge e and update pq if changed
    private void relax(RoadEdge e) {
        Point2D v = e.getEither(), w = e.getOther(v);
        if (distTo.get(w) > distTo.get(v) + e.getWeight(type)) {
            distTo.put(w, distTo.get(v) + e.getWeight(type));
            edgeTo.put(w, e);
            if (pQ.contains(factory.getID(w))) {
                pQ.decreaseKey(factory.getID(w), distTo.get(w));
            } else {
                pQ.insert(factory.getID(w), distTo.get(w));
            }
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

}
