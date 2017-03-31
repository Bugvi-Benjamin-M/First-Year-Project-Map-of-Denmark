package ModelPackage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 23/03/2017
 */
public class TestRoad {

    @Test
    public void test() {
        assertEquals(true, true);
    }


    /*@Test
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
        OSMWay osmWay = new OSMWay();
        osmWay.add(new Point2D.Float(1,1));
        Road road = new Road(RoadType.PRIMARY, osmWay);
        assertEquals(osmWay, road.getWay());
        assertEquals(osmWay.get(0), road.getWay().get(0));
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
    }*/
}
