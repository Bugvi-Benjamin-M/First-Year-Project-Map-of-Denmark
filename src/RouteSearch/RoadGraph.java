package RouteSearch;

import Helpers.GlobalValue;
import Model.Elements.Road;
import Model.Elements.RoadEdge;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Andreas Blanke
 * @version 05-05-2017
 */
public class RoadGraph implements Serializable {

    private int nEdges;
    private Map<Point2D,LinkedList<RoadEdge>> adjacencyList;

    public RoadGraph() {
        adjacencyList = new HashMap<>(10000000);
    }

    public int getNumberOfEdges() {return nEdges;}

    public int getNumberOfVertices() {return adjacencyList.size();}

    public void addEdges(RoadEdge road) {
        if (road != null) {
            addEdge(road,road.getEither());
            if (!road.isOneWay()){
                road = road.createReverse();
                addEdge(road,road.getEither());
            }
        } else {
            throw new NullPointerException("Road not initialized...");
        }
    }

    public void addEdge(RoadEdge road, Point2D from) {
        LinkedList<RoadEdge> listFrom = adjacencyList.get(from);
        if (listFrom == null) listFrom = new LinkedList<>();
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

    public Map<Point2D,LinkedList<RoadEdge>> getAdjacencyList() {
        return adjacencyList;
    }

    @Override
    public String toString() {
        return "Graph containing "+getNumberOfEdges() +
                " roads connected by "+getNumberOfVertices() +
                " points";
    }
}
