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
    private List<Road> route;
    private List<Long> routeRefs;
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

    public void setRoute(List<Long> refs) {
        if (refs == null) throw new NullPointerException("References not found...");
        route = new ArrayList<>();
        List<Road> lastRoads, roads;
        for (int i = 1; i < refs.size(); i++) {
            boolean doBreak = false;
            lastRoads = getRoads(refs.get(i-1));
            roads = getRoads(refs.get(i));
            for (Road last : lastRoads) {
                for (Road road : roads) {
                    if (last.equals(road)) {
                        /*if (route.size() > 0 &&
                                !route.get(route.size()-1).equals(road)) {
                            route.add(road);
                        } else if (route.size() == 0) route.add(road);*/
                        route.add(road);
                        doBreak = true;
                        break;
                    }
                }
                if (doBreak) break;
            }
        }
        routeRefs = refs;
    }

    public void setRoute(Iterable<Edge> iterator) {
        if (iterator == null) throw new NullPointerException("Iterator not found...");
        List<Long> refs = new ArrayList<>();
        for (Edge edge : iterator) {
            if (edge != null) refs.add(edge.either());
        }
        setRoute(refs);
        routeRefs = refs;
    }

    public List<Road> getRoute() {
        return route;
    }

    public List<Long> getRouteRefs() {
        return routeRefs;
    }
}
