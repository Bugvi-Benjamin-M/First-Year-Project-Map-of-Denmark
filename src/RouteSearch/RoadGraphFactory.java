package RouteSearch;

import Model.Elements.RoadEdge;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * DESCRIPTION
 * <p>
 * CLASS Created by
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 * @project BFST
 */
public class RoadGraphFactory {

    private List<Point2D> points;
    private List<RoadEdge> route;
    private List<RoadEdge> roads;
    private RoadGraph graph;

    public RoadGraphFactory(RoadGraph graph, List<RoadEdge> roads, Collection<Point2D> points) {
        this.graph = graph;
        System.out.println("Building factory...");
        this.points = new ArrayList<>();
        this.points.addAll(points);
        this.roads = roads;
        System.out.println("Finished building factory...");
    }

    public int getID(Point2D point) {
        return points.indexOf(point);
    }

    public Point2D getPoint(int id) {
        return points.get(id);
    }

    public int getNumberOfPoints() {return points.size();}

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
        return roads;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public RoadEdge getRoad(String name) {
        for (RoadEdge road: roads) {
            if (road.getName().equals(name)) return road;
        }
        return null;
    }
}
