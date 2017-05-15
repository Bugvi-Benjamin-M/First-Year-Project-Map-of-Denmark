package RouteSearch;

import Helpers.Shapes.PolygonApprox;
import Model.Elements.Road;
import Model.Elements.RoadEdge;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import Model.Elements.SuperElement;

public class RoadGraphFactory {

    private List<RoadEdge> route;
    private RoadGraph graph;

    public RoadGraphFactory(RoadGraph graph) {
        this.graph = graph;
    }

    public RoadGraphFactory(HashSet<SuperElement> roads) {
        if (roads == null) throw new NullPointerException("Roads not initialized");
        graph = new RoadGraph();
        constructGraph(roads);
    }

    private void constructGraph(HashSet<SuperElement> roads) {
        for (SuperElement superElement : roads) {
            Road road = (Road)superElement;
            PolygonApprox shape = road.getShape();
            float[] coords = shape.getCoords();
            for (int i = 2; i < coords.length; i += 2){
                Point2D from = new Point2D.Float(coords[i-2], coords[i-1]);
                Point2D to = new Point2D.Float(coords[i], coords[i+1]);
                RoadEdge edge = new RoadEdge(from,to,road);
                graph.addEdge(edge, from, to);
                if(!road.isOneWay()) {
                    RoadEdge reverse = edge.createReverse();
                    graph.addEdge(reverse,to, from);
                }
                }
        }
    }

    public RoadGraph getGraph() {
        return graph;
    }

    public void setGraph(RoadGraph graph) {
        this.graph = graph;
    }

    public List<RoadEdge> getRoute() {
        return route;
    }

    public void setRoute(List<RoadEdge> route) {
        this.route = route;
    }

    public void setRoute(Iterable<RoadEdge> iterator) {
        List<RoadEdge> roadEdges = new LinkedList<>();
        for (RoadEdge edge:iterator) {
            roadEdges.add(edge);
        }
        setRoute(roadEdges);
    }
}
