package RouteSearch;

import Helpers.GlobalValue;
import Model.Elements.Road;
import Model.Elements.RoadEdge;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RoadGraph implements Serializable {

    private int nEdges;
    private Map<Point2D,ArrayList<RoadEdge>> adjacencyList;

    public RoadGraph() {
        adjacencyList = new CachedHashMap<>(10000000);
    }

    public int getNumberOfEdges() {return nEdges;}

    public int getNumberOfVertices() {return adjacencyList.size();}

    public void addEdge(RoadEdge road, Point2D from) {
        if (road == null || from == null) throw new NullPointerException("RoadEdge or from point has not been initiliazed");
        ArrayList<RoadEdge> listFrom = adjacencyList.get(from);
        if (listFrom == null) listFrom = new ArrayList<>();
        listFrom.add(road);
        adjacencyList.put(from, listFrom);
        nEdges++;
    }

    public List<RoadEdge> adjacent(Point2D point) {
        return adjacencyList.get(point);
    }

    public int degree(Point2D point) {
        return adjacencyList.get(point).size();
    }

    public Map<Point2D,ArrayList<RoadEdge>> getAdjacencyList() {
        return adjacencyList;
    }

    @Override
    public String toString() {
        return "Graph containing "+getNumberOfEdges() +
                " roads connected by "+getNumberOfVertices() +
                " points";
    }
}
