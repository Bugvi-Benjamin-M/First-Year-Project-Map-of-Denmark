import KDtree.*;
import org.junit.Before;
import org.junit.Test;
import Model.Elements.*;

import java.awt.geom.*;
import java.util.HashSet;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by  on .
 *
 * @author santa
 * @version 14/10/2012
 */
public class TestKD {

    private KDTree tree;

    @Before
    public void buildUp()
    {
        NodeGenerator gen = new NodeGenerator(10, 2);
        tree = new KDTree();

        for (int i = 0; i < 10; i++) {
            gen.addPoint(new Point2D.Float(i, i * 2));
        }

        gen.initialise();
        gen.setupTree(tree);

        for (int i = 0; i < 10; i++) {
            Path2D path = new Path2D.Double();
            path.moveTo(0, 0);
            path.lineTo(i, i * 2);
            Element el = new UnknownWay(path);
            tree.putPointer(new Pointer(i, i * 2, el));
        }
    }

    @Test
    public void testGetManyElementsLittleSquare()
    {
        HashSet<SuperElement> set = tree.getManySections(-1, -1, 3, 3);

        assertEquals(5, set.size());

        double[] correctX = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0 };
        double[] correctY = new double[] { 0.0, 2.0, 4.0, 6.0, 8.0 };

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testGetManyElementsBigSquare()
    {
        HashSet<SuperElement> set = tree.getManySections(-1, -1, 10, 20);

        assertEquals(10, set.size());

        double[] correctX = new double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0 };
        double[] correctY = new double[] { 0.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0 };

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testGetManyElementsOutOfRangeSquare()
    {
        HashSet<SuperElement> set = tree.getManySections(-1, -1, -2, -2);

        assertEquals(2, set.size());

        double[] correctX = new double[] { 0.0, 1.0 };
        double[] correctY = new double[] { 0.0, 2.0 };

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testGetManyElementsMiddleSquare()
    {
        HashSet<SuperElement> set = tree.getManySections(5, 5, 7, 7);

        assertEquals(3, set.size());

        double[] correctX = new double[] { 5.0, 6.0, 7.0 };
        double[] correctY = new double[] { 10.0, 12.0, 14.0 };

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testGetManyElementsCornerTopLeft()
    {
        HashSet<SuperElement> set = tree.getManySections(0, 30, -1, 31);

        assertEquals(3, set.size());

        double[] correctX = new double[] { 2.0, 3.0, 4.0 };
        double[] correctY = new double[] { 4.0, 6.0, 8.0 };

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testGetManyElementsCornerTopRight()
    {
        HashSet<SuperElement> set = tree.getManySections(30, 30, 30, 30);

        assertEquals(2, set.size());

        double[] correctX = new double[] { 8.0, 9.0 };
        double[] correctY = new double[] { 16.0, 18.0 };

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testGetManyElementsCornerBottomRight()
    {
        HashSet<SuperElement> set = tree.getManySections(30, 0, 31, -1);

        assertEquals(3, set.size());

        double[] correctX = new double[] { 5.0, 6.0, 7.0 };
        double[] correctY = new double[] { 10.0, 12.0, 14.0 };

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testClear()
    {
        tree.clear();
        HashSet<SuperElement> set = tree.getManySections(-1, -1, 10, 20);

        assertEquals(0, set.size());

        double[] correctX = new double[] {};
        double[] correctY = new double[] {};

        assertHashSetEqualsToPoints(set, correctX, correctY);
    }

    @Test
    public void testEmptyKD()
    {
        tree = new KDTree();
        assertEquals(null, tree.getManySections(0, 0, 0, 0));
        tree.clear();
        assertEquals(null, tree.getManySections(0, 0, 0, 0));
    }

    @Test
    public void testPointer()
    {
        Path2D rectangle = new Path2D.Double();
        SuperElement el = new UnknownWay(rectangle);
        Pointer p = new Pointer(1, 2, el);
        assertEquals(p.getElement(), el);
        Element ele = (Element) p.getElement();
        assertEquals(ele.getShape(), rectangle);
    }

    private void assertHashSetEqualsToPoints(HashSet<SuperElement> set,
        double[] correctX,
        double[] correctY)
    {
        double[] x = new double[correctX.length];
        double[] y = new double[correctY.length];
        int i = 0;
        for (SuperElement ele : set) {
            Element el = (Element) ele;
            Rectangle2D bounds = el.getShape().getBounds2D();
            x[i] = bounds.getWidth();
            y[i] = bounds.getHeight();
            i++;
        }

        Arrays.sort(x);
        Arrays.sort(y);

        assertEquals(true, Arrays.equals(x, correctX));
        assertEquals(true, Arrays.equals(y, correctY));
    }
}
