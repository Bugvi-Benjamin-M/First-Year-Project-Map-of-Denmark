package RouteSearch;

import Helpers.Shapes.PolygonApprox;
import Model.Elements.Road;
import Model.Elements.RoadEdge;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import Model.Elements.SuperElement;

/**
 * TODO: write javadoc
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RoadGraphFactory {

    public enum LoadType {
        ROADS, ROADEDGES
    }

    private List<RoadEdge> route;
    //private List<RoadEdge> roads;
    private RoadGraph graph;

    public RoadGraphFactory(RoadGraph graph) {
        this.graph = graph;
        //this.roads = roads;
    }

    public RoadGraphFactory(HashSet<SuperElement> roads) {
        if (roads == null) throw new NullPointerException("Roads not initialized");

        graph = new RoadGraph();
        constructGraph(roads);

        System.out.println("Done building graph");
    }

    private void constructGraph(HashSet<SuperElement> roads) {
        int counter = 0;
        for (SuperElement superElement : roads) {
            Road road = (Road)superElement;
            PolygonApprox shape = road.getShape();
            float[] coords = shape.getCoords();
            for (int i = 2; i < coords.length; i += 2){
                Point2D from = new Point2D.Float(coords[i-2], coords[i-1]);
                Point2D to = new Point2D.Float(coords[i], coords[i+1]);
                RoadEdge edge = new RoadEdge(from,to,road);
                graph.addEdge(edge, from, to);
                //this.roads.add(edge);
                if(!road.isOneWay()) {
                    RoadEdge reverse = edge.createReverse();
                    graph.addEdge(reverse,to, from);
                    //this.roads.add(reverse);
                    counter++;
                }
                counter++;
                if (counter % 1000 == 0) System.out.println("... added edges: "+counter);
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

    /*
    public List<RoadEdge> getEdges() {
        if (roads == null) throw new NullPointerException("Edges not initialized");
        return roads;
    }

    public RoadEdge getRoad(String name) {
        for (RoadEdge road: roads) {
            if (road.getName().equals(name)) return road;
        }
        return null;
    }
    */
}
