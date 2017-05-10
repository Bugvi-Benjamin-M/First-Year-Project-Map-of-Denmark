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

    public enum LoadType {
        ROADS, ROADEDGES
    }

    private List<RoadEdge> route;
    private List<RoadEdge> roads;
    private RoadGraph graph;

    public RoadGraphFactory(RoadGraph graph, List<RoadEdge> roads) {
        this.graph = graph;
        this.roads = roads;
    }

    public RoadGraphFactory(Iterable roads, LoadType type) {
        if (roads == null) throw new NullPointerException("Roads not initialized");
        this.roads = new ArrayList<>();
        if (type == LoadType.ROADS) {
            graph = new RoadGraph();
            constructGraph((Iterable<Road>) roads);
        } else if (type == LoadType.ROADEDGES) {
            graph = new RoadGraph();
            for (Object road : roads) {
                graph.addEdge((RoadEdge) road,((RoadEdge) road).getEither());
                this.roads.add((RoadEdge) road);
            }
        } else {
            throw new IllegalArgumentException("Type not defined");
        }
        System.out.println("Done building graph");
    }

    private void constructGraph(Iterable<Road> roads) {
        int counter = 0;
        for (Road road : roads) {
            for (OSMWay way: road.getRelation()) {
                for (int i = 1; i < way.size(); i++) {
                    Point2D from = way.get(i-1);
                    Point2D to = way.get(i);
                    RoadEdge edge = new RoadEdge(from,to,road.getName(),road.getMaxSpeed());
                    edge.setTravelByBikeAllowed(road.isTravelByBikeAllowed());
                    edge.setTravelByWalkAllowed(road.isTravelByFootAllowed());
                    edge.setTravelByCarAllowed(road.isTravelByCarAllowed());
                    graph.addEdge(edge,from);
                    this.roads.add(edge);
                    if(!road.isOneWay()) {
                        RoadEdge reverse = edge.createReverse();
                        graph.addEdge(reverse,to);
                        this.roads.add(reverse);
                        counter++;
                    }
                    counter++;
                    if (counter % 1000 == 0) System.out.println("... added edges: "+counter);
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
