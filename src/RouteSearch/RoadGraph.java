package RouteSearch;

import Model.Elements.RoadEdge;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.*;

public class RoadGraph implements Serializable {

    private int nEdges;
    private Map<Point2D,List<RoadEdge>> adjacencyList;


    /**
     * Constructor
     */
    public RoadGraph() {
        // because of .75 loadFactor and approx. 8,5 mil elements
        adjacencyList = new HashMap<>(12000000);
    }

    /**
     * Returning the number of edges in the graph
     * @return number of edges
     */
    public int getNumberOfEdges() {return nEdges;}

    /**
     * Returning the number of vertices in the graph
     * @return the number of vertices
     */
    public int getNumberOfVertices() {return adjacencyList.size();}

    /**
     * Add an edge to the graph adding the edge and what point the edge should be added on.
     */
    public void addEdge(RoadEdge road, Point2D from) {
        if (road == null || from == null) throw new NullPointerException("RoadEdge or from point has not been initiliazed");
        List<RoadEdge> listFrom = adjacencyList.get(from);
        if (listFrom == null) listFrom = new ArrayList<>();
        listFrom.add(road);
        adjacencyList.put(from, listFrom);
        nEdges++;
    }

    /**
     * Return all the edges, that are connected from a given vertex
     */
    public List<RoadEdge> adjacent(Point2D point) {
        return adjacencyList.get(point);
    }

    /**
     * How many edges that are connected from a given vertex
     */
    public int degree(Point2D point) {
        return adjacencyList.get(point).size();
    }

    /**
     * Returns the hashmap containing all the edges connected from a given vertex
     */
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
