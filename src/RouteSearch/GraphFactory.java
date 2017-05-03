package RouteSearch;

import Enums.TravelType;
import Helpers.HelperFunctions;
import Helpers.LongToPointMap;
import KDtree.KDTree;
import Model.Elements.Element;
import Model.Elements.Road;
import Model.Model;
import OSM.OSMWay;

import java.awt.geom.Point2D;
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
    private Graph graph;
    private int counter = 0;

    public GraphFactory(Graph graph, List<Road> roads) {
        this.graph = graph;
        this.roads = roads;
        references = new ArrayList<>();
        for (Long lon: graph.getAdjacencyMap().keySet()) {
            references.add(lon);
            counter++;
        }
    }

    public int getID(long value) {
        int indexOf = references.indexOf(value);
        if (indexOf == -1) throw new IllegalArgumentException("N/A value!");
        return indexOf;
    }
}
