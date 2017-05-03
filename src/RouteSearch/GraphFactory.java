package RouteSearch;

import Model.Elements.Road;
import OSM.OSMWayRef;

import java.util.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class GraphFactory {

    private List<Road> roads;
    private List<Long> references;
    private Map<Long,List<Road>> nodeBelongs;
    private Graph graph;

    public GraphFactory(Graph graph, List<Road> roads) {
        this.graph = graph;
        this.roads = roads;
        references = new ArrayList<>();
        for (Long lon: graph.getAdjacencyMap().keySet()) {
            references.add(lon);
        }
        nodeBelongs = new HashMap<>();
        for (Road road : roads) {
            for (OSMWayRef way: road.getRelation()) {
                for (long lon : way.references()) {
                    List list = nodeBelongs.get(lon);
                    if (list == null) nodeBelongs.put(lon,new LinkedList<>());
                    nodeBelongs.get(lon).add(road);
                }
            }
        }
    }

    public int getID(long value) {
        int indexOf = references.indexOf(value);
        if (indexOf == -1) throw new IllegalArgumentException("N/A value!");
        return indexOf;
    }

    public long getLong(int id){
        return references.get(id);
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public List<Road> getRoads(long ref) {
        return nodeBelongs.get(ref);
    }
}
