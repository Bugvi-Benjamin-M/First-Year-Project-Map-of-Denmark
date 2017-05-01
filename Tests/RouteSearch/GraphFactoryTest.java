package RouteSearch;

import KDtree.KDTree;
import KDtree.Pointer;
import Model.Elements.Element;
import Model.Elements.Road;
import OSM.OSMWay;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 30-04-2017.
 * @project BFST
 */
public class GraphFactoryTest {

    private GraphFactory factory;

    @Before
    public void setUp() throws Exception {
        HashSet<Element> roads = new HashSet<>();
        Road road = new Road(null,"Vej 1");
        road.setMaxSpeed(60);
        road.setOneWay(false);
        road.setTravelByBikeAllowed(true);
        road.setTravelByCarAllowed(true);
        road.setTravelByFootAllowed(true);
        OSMWay way = new OSMWay();
        Point2D point = new Point2D.Float(0.5f,0.2f);
        System.out.println(point.hashCode());
        way.add(point);
        point = new Point2D.Float(0.2f,0.5f);
        System.out.println(point.hashCode());
        way.add(point);
        point = new Point2D.Float(0.1f,0.6f);
        System.out.println(point.hashCode());
        way.add(point);
        road.setWay(way);
        roads.add(road);
        factory = new GraphFactory(roads);
    }

    @Test
    public void getGraph() throws Exception {

    }

}