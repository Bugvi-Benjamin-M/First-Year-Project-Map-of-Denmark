package ModelPackage.Road;

import Enums.RoadType;
import Model.Road;
import org.junit.Test;

import java.awt.geom.Path2D;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestGetRoadType {

    @Test
    public void testGetRoadType() {
        Road service = new Road(RoadType.SERVICE, new Path2D.Float());
        Road unclassified = new Road(RoadType.UNCLASSIFIED, new Path2D.Float());
        Road highway = new Road(RoadType.HIGHWAY, new Path2D.Float());
        Road tertiary = new Road(RoadType.TERTIARY, new Path2D.Float());
        Road secondary = new Road(RoadType.SECONDARY, new Path2D.Float());
        Road primary = new Road(RoadType.PRIMARY, new Path2D.Float());
        assertEquals(RoadType.SERVICE, service.getRoadType());
        assertEquals(RoadType.UNCLASSIFIED, unclassified.getRoadType());
        assertEquals(RoadType.HIGHWAY, highway.getRoadType());
        assertEquals(RoadType.TERTIARY, tertiary.getRoadType());
        assertEquals(RoadType.SECONDARY, secondary.getRoadType());
        assertEquals(RoadType.PRIMARY, primary.getRoadType());
    }
}
