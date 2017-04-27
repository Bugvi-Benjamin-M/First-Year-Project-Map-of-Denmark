package RouteSearch;

import KDtree.KDTree;
import Model.Elements.Element;
import Model.Elements.Road;

import java.util.LinkedList;
import java.util.List;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 27-04-2017
 */
public class GraphFactory {

    private KDTree roads;
    private List<Edge> roadSegments;
    private Graph graph;

    public GraphFactory(KDTree roads) {
        if (roads == null) throw new NullPointerException("KDTree has not been initialized");
        this.roads = roads;
        roadSegments = new LinkedList<>();
        makeGraph();
    }

    private void makeGraph() {
        for (Object road: roads) {
            if (road instanceof Road) {

            } // else ignore
        }
    }

    public Graph getGraph() {
        return graph;
    }
}
