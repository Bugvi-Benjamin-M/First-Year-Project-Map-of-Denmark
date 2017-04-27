package RouteSearch;

import Helpers.HelperFunctions;
import KDtree.KDTree;
import Model.Elements.Element;
import Model.Elements.Road;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class GraphFactory {

    private KDTree roads;
    private List<Edge> roadSegments;
    private Map<Point2D,LinkedList<Road>> adjacent;
    private Graph graph;

    public GraphFactory(KDTree roads) {
        if (roads == null) throw new NullPointerException("KDTree has not been initialized");
        this.roads = roads;
        roadSegments = new LinkedList<>();
        adjacent = new HashMap<>();
        makeGraph();
    }

    private void makeGraph() {
        for (Object way: roads) {
            if (way instanceof Road) {
                Road road = (Road) way;
                // FIXME: READ END POINTS OF THE ROAD PLZ!!!
                Edge edge = new Edge(1,2,road.getMaxSpeed(),1.0);
                // FIXME: CALCULATE THE LENGTH PROPERLY!!!
                roadSegments.add(edge);
                addRoadToAdjacent(new Point2D.Float(1,2),road);

                if (!road.isOneWay()) {  // multi directional road
                    edge = new Edge(2,1,road.getMaxSpeed(),1.0);
                    roadSegments.add(edge);
                    addRoadToAdjacent(new Point2D.Float(2,1),road);
                }
            } // else ignore
        }
        graph = new Graph(adjacent.size(), new LongToIntMap(10));
        for (Edge edge : roadSegments) {
            graph.addEdge(edge);
        }
    }

    private void addRoadToAdjacent(Point2D point, Road road) {
        if (adjacent.containsKey(point)) {
            adjacent.get(point).add(road);
        } else {    // make new adjacency list
            LinkedList<Road> adj = new LinkedList<>();
            adj.add(road);
            adjacent.put(point,adj);
        }
    }

    public Graph getGraph() {
        return graph;
    }
}
