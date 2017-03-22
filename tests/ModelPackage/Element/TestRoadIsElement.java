package ModelPackage.Element;

import Enums.RoadType;
import Model.Element;
import Model.UnknownWay;
import org.junit.Test;
import Model.Road;

import java.awt.geom.Path2D;

import static org.junit.Assert.assertTrue;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestRoadIsElement {

    @Test
    public void TestRoadIsElement() {
        Road road = new Road(RoadType.SERVICE, new Path2D.Float());
        UnknownWay unknownWay = new UnknownWay(new Path2D.Float());
        assertTrue(road instanceof Element);
        assertTrue(unknownWay instanceof Element);
    }
}
