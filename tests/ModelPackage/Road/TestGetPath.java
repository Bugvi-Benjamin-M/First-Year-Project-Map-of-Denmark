package ModelPackage.Road;

import Enums.RoadType;
import org.junit.Test;
import Model.Road;

import java.awt.geom.Path2D;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestGetPath {

    @Test
    public void testGetPath() {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(0f, 0f);
        path.lineTo(1f, 1f);
        path.closePath();
        Road road = new Road(RoadType.PRIMARY, path);
        assertEquals(path, road.getPath());
        assertTrue(path.getBounds().equals(
                road.getPath().getBounds()));
    }


}
