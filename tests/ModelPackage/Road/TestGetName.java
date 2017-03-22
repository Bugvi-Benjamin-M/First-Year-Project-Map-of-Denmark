package ModelPackage.Road;

import Enums.RoadType;
import org.junit.Test;
import Model.Road;

import java.awt.geom.Path2D;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestGetName {

    @Test
    public void testGetname() {
        Road road = new Road(RoadType.UNCLASSIFIED, new Path2D.Float(), "Road");
        assertEquals("Road", road.getName());
    }
}
