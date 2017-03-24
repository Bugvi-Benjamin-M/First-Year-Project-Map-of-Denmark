package ModelPackage;

import Enums.RoadType;
import Model.Element;
import Model.Road;
import OSM.OSMWay;
import org.junit.Test;

import java.awt.geom.Path2D;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 23/03/2017
 */
public class TestRoad {


    @Test
    public void TestRoadIsElement() {
        Road road = new Road(RoadType.SERVICE, new OSMWay());
        assertTrue(road instanceof Element);
    }

    @Test
    public void testConstructorWithoutName() {
        Road road = new Road(RoadType.SERVICE, new OSMWay());
        assertTrue(road.getName().equals(""));
    }

    @Test
    public void testGetname() {
        Road road = new Road(RoadType.UNCLASSIFIED, new OSMWay(), "Road");
        assertEquals("Road", road.getName());
    }

    @Test
    public void testGetPath() {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(0f, 0f);
        path.lineTo(1f, 1f);
        path.closePath();
        Road road = new Road(RoadType.PRIMARY, new OSMWay());
        assertEquals(path, road.getWay());
        assertTrue(path.getBounds().equals(
                road.getWay().toPath2D().getBounds()));
    }

    @Test
    public void testGetRoadType() {
        Road service = new Road(RoadType.SERVICE, new OSMWay());
        Road unclassified = new Road(RoadType.UNCLASSIFIED, new OSMWay());
        Road highway = new Road(RoadType.HIGHWAY, new OSMWay());
        Road tertiary = new Road(RoadType.TERTIARY, new OSMWay());
        Road secondary = new Road(RoadType.SECONDARY, new OSMWay());
        Road primary = new Road(RoadType.PRIMARY, new OSMWay());
        assertEquals(RoadType.SERVICE, service.getRoadType());
        assertEquals(RoadType.UNCLASSIFIED, unclassified.getRoadType());
        assertEquals(RoadType.HIGHWAY, highway.getRoadType());
        assertEquals(RoadType.TERTIARY, tertiary.getRoadType());
        assertEquals(RoadType.SECONDARY, secondary.getRoadType());
        assertEquals(RoadType.PRIMARY, primary.getRoadType());
    }
}
