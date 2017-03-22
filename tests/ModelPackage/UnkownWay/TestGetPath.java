package ModelPackage.UnkownWay;

import Model.UnknownWay;
import org.junit.Test;

import java.awt.geom.Path2D;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestGetPath {

    @Test
    public void testGetPath() {
        Path2D path = new Path2D.Float();
        path.moveTo(0f, 0f);
        path.lineTo(1f, 1f);
        path.closePath();
        UnknownWay unknownWay = new UnknownWay(path);
        assertEquals(path, unknownWay.getPath());
        assertTrue(path.getBounds().equals(
                unknownWay.getPath().getBounds()));
    }

}
