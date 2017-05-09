package RouteSearch;

import Model.Elements.Element;
import Model.Elements.Road;
import OSM.OSMWay;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.HashSet;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 30-04-2017.
 * @project BFST
 */
public class GraphFactoryTest {

    //private GraphFactory factory;

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
        way.add(point);
        point = new Point2D.Float(0.2f,0.5f);
        way.add(point);
        point = new Point2D.Float(0.1f,0.6f);
        way.add(point);
        road.setWay(way);
        roads.add(road);
        //factory = new GraphFactory();
    }

    @Test
    public void getGraph() throws Exception {

    }

}