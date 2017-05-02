package RouteSearch;

import Helpers.HelperFunctions;
import Model.Elements.Road;
import OSM.OSMWay;
import OSM.OSMWayRef;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class Graph {

    public static final String NEWLINE = System.getProperty("line.separator");

    private int nEdges;
    private ArrayList<LinkedList<Edge>> adjacencyLists;
    private LongToIntMap idMap;

    public Graph() {
        adjacencyLists = new ArrayList<>(1000000);
        idMap = new LongToIntMap(1000000);
    }

    public int getNumberOfNodes() {
        return adjacencyLists.size();
    }

    public int getNumberOfEdges() {
        return nEdges;
    }

    private void validateNode(int id) {
        if (id < 0 || id > adjacencyLists.size()) {
            throw new ArrayIndexOutOfBoundsException("id "+id+" is out of bounds");
        }
    }

    public void addEdges(Road road) {
        byte type = Edge.getTravelTypeValue(road.isTravelByFootAllowed(),
                road.isTravelByBikeAllowed(),road.isTravelByCarAllowed(),
                road.isOneWay());
        for (OSMWayRef way : road.getRelation()){
            long ref, lastRef = way.refOf(way.getFromNode());
            for (int i = 1; i < way.size(); i++) {
                ref = way.refOf(way.get(i));
                float length = (float) HelperFunctions.distanceInMeters(way.get(i-1),way.get(i));
                addEdge(lastRef,ref,type,length,road.getMaxSpeed());
                lastRef = ref;
            }
        }
    }

    public void addEdge(long lastRef, long ref, byte type, float length, int speed) {
        idMap.insert(lastRef);
        idMap.insert(ref);
        int lastID = idMap.getInt(lastRef);
        int ID = idMap.getInt(ref);
        if (lastID >= adjacencyLists.size()) {
            if (lastID == adjacencyLists.size()) adjacencyLists.add(new LinkedList<>());
            else {
                for (int i = adjacencyLists.size();
                     i <= lastID; i++) {
                    adjacencyLists.add(new LinkedList<>());
                }
            }
        }
        adjacencyLists.get(lastID).add(new Edge(lastID,ID,speed,length,type));
        nEdges++;
        if (nEdges % 1000 == 0) System.out.println("... added edge - total: "+nEdges);
    }

    public Iterable<Edge> adjacent(int id) {
        validateNode(id);
        return adjacencyLists.get(id);
    }

    public int degree(int id) {
        validateNode(id);
        return adjacencyLists.get(id).size();
    }

    public Iterable<Edge> edges() {
        return null;
    }

    @Override
    public String toString() {
        return null;
    }
}
