package ModelPackage;

import Model.Element;
import Model.UnknownWay;
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
public class TestUnknownWay {

    @Test
    public void TestRoadIsElement() {
        UnknownWay unknownWay = new UnknownWay(new Path2D.Float());
        assertTrue(unknownWay instanceof Element);
    }

    @Test
    public void testGetPath2() {
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
