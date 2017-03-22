package ModelPackage.Road;

import Enums.RoadType;
import Model.Road;
import org.junit.Test;

import java.awt.geom.Path2D;

import static org.junit.Assert.assertTrue;

/**
 * Created by Búgvi Magnussen on 19-03-2017.
 */
public class TestConstructorWithoutName {

    @Test
    public void testConstructorWithoutName() {
        Road road = new Road(RoadType.SERVICE, new Path2D.Float());
        assertTrue(road.getName().equals(""));
    }

}
