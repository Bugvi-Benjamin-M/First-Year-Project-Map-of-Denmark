package RouteSearch;

import Model.Elements.Road;
import Model.Elements.RoadEdge;
import OSM.OSMWay;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * TODO: write javadoc
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RoadGraphFactory {

    private List<RoadEdge> route;
    private List<RoadEdge> roads;
    private RoadGraph graph;

    public RoadGraphFactory(RoadGraph graph, List<RoadEdge> roads) {
        this.graph = graph;
        this.roads = roads;
    }

    public RoadGraphFactory(Iterable<Road> roads) {
        graph = new RoadGraph();
        this.roads = new ArrayList<>();
        constructGraph(roads);
    }

    private void constructGraph(Iterable<Road> roads) {
        int counter = 0;
        for (Road road : roads) {
            for (OSMWay way: road.getRelation()) {
                for (int i = 1; i < way.size(); i++) {
                    OSMWay shape = new OSMWay();
                    shape.add(way.get(i-1));
                    shape.add(way.get(i));
                    RoadEdge edge = new RoadEdge(shape,road.getName(),road.getMaxSpeed());
                    edge.setOneWay(road.isOneWay());
                    edge.setTravelByBikeAllowed(road.isTravelByBikeAllowed());
                    edge.setTravelByWalkAllowed(road.isTravelByFootAllowed());
                    edge.setTravelByCarAllowed(road.isTravelByCarAllowed());
                    graph.addEdges(edge);
                    this.roads.add(edge);
                    if (counter % 1000 == 0) System.out.println("... added edges: "+counter);
                    counter++;
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
}
