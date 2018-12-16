import KDtree.*;
import org.junit.Before;
import org.junit.Test;
import Model.Elements.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;

public class TestNodeGenerator {

    @Test
    public void testLinearPlus()
    {
        NodeGenerator gen = new NodeGenerator(10, 2);
        KDTree tree = new KDTree();

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
    public void testLinearMinus()
    {
        NodeGenerator gen = new NodeGenerator(10, 2);
        KDTree tree = new KDTree();

        for (int i = 10; i > 0; i--) {
            gen.addPoint(new Point2D.Float(i, i * 2));
        }

        gen.initialise();
        gen.setupTree(tree);

        for (int i = 10; i > 0; i--) {
            Path2D path = new Path2D.Double();
            path.moveTo(0, 0);
            path.lineTo(i, i * 2);
            Element el = new UnknownWay(path);
            tree.putPointer(new Pointer(i, i * 2, el));
        }
    }

    @Test
    public void testRandom()
    {
        NodeGenerator gen = new NodeGenerator(10, 2);
        KDTree tree = new KDTree();

        double[] x = new double[] { 0.0, 6.0, 4.0, 7.0, 3.0, 6.0, 2.0, 1.0, 5.0, 7.0 };
        double[] y = new double[] { 5.0, 6.0, 2.0, 6.0, 1.0, 7.0, 2.0, 5.0, 1.0, 4.0 };

        for (int i = 0; i < 10; i++) {
            gen.addPoint(new Point2D.Float((float)x[i], (float)y[i]));
        }

        gen.initialise();
        gen.setupTree(tree);

        for (int i = 0; i < 10; i++) {
            Path2D path = new Path2D.Double();
            Element el = new UnknownWay(path);
            tree.putPointer(new Pointer((float)x[i], (float)y[i], el));
        }
    }
}
