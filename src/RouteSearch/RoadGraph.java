package RouteSearch;

import Helpers.GlobalValue;
import Model.Elements.Road;
import Model.Elements.RoadEdge;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RoadGraph implements Serializable {

    private int nEdges;
    private Map<Point2D,List<RoadEdge>> adjacencyList;

    public RoadGraph() {
        adjacencyList = new CachedHashMap<>(12000000);
    }

    public int getNumberOfEdges() {return nEdges;}

    public int getNumberOfVertices() {return adjacencyList.size();}

    public void addEdge(RoadEdge road, Point2D from, Point2D to) {
        if (road == null || from == null) throw new NullPointerException("RoadEdge or from point has not been initiliazed");
        List<RoadEdge> listFrom = adjacencyList.get(from);
        //List<RoadEdge> listTo = adjacencyList.get(to);
        if (listFrom == null) listFrom = new ArrayList<>();
        //if (listTo == null) listTo= new ArrayList<>();
        listFrom.add(road);
        //listTo.add(road);
        adjacencyList.put(from, listFrom);
        //adjacencyList.put(to, listTo);
        nEdges++;
    }

    public List<RoadEdge> adjacent(Point2D point) {
        return adjacencyList.get(point);
    }

    public int degree(Point2D point) {
        return adjacencyList.get(point).size();
    }

    public Map<Point2D,List<RoadEdge>> getAdjacencyList() {
        return adjacencyList;
    }

    @Override
    public String toString() {
        return "Graph containing "+getNumberOfEdges() +
                " roads connected by "+getNumberOfVertices() +
                " points";
    }
}
