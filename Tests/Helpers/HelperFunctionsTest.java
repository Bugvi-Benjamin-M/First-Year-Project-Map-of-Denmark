package Helpers;

import KDtree.Point;
import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 04-04-2017.
 * @project BFST
 */
public class HelperFunctionsTest {

    private static Point2D A = new Point2D.Double(2,5);
    private static Point2D B = new Point2D.Double(6,1);
    private static Point2D C = new Point2D.Double(8,5);
    private static Point2D D = new Point2D.Double(12,1);
    private static Point2D E = new Point2D.Double(-1,-1);

    @Test
    public void pathGeneralization() throws Exception {
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D.Double(-10,3));
        points.add(new Point2D.Double(-8.96,3.59));
        points.add(new Point2D.Double(-7.91,2.55));
        points.add(new Point2D.Double(-7,3));
        points.add(new Point2D.Double(-5.95,3.36));
        points.add(new Point2D.Double(-5,3));
        points.add(new Point2D.Double(-4.98,2.17));
        points.add(new Point2D.Double(-5.01,1.13));
        points.add(new Point2D.Double(-6,0.67));
        points.add(new Point2D.Double(-7.03,0.75));
        points.add(new Point2D.Double(-8,0.29));
        points.add(new Point2D.Double(-9.04,0.71));
        points.add(new Point2D.Double(-10.02,0.55));

        System.out.println(points.size());
        List<Point2D> result = HelperFunctions.pathGeneralization(points,2);
        System.out.println(result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(points.get(i),result.get(i));
        }
    }

    @Test
    public void distanceBetweenPointAndPath() throws Exception {
        double distance = HelperFunctions.distanceBetweenPointAndPath(A,B,C);
        assertEquals(4.24,distance,0.1);
        distance = HelperFunctions.distanceBetweenPointAndPath(A,B,E);
        assertEquals(6.36,distance,0.1);
    }

    @Test
    public void distanceBetweenTwoPoints() throws Exception {
        double distance = HelperFunctions.distanceBetweenTwoPoints(A,B);
        assertEquals(5.66,distance,0.1);
        distance = HelperFunctions.distanceBetweenTwoPoints(B,D);
        assertEquals(6,distance,0.1);
        distance = HelperFunctions.distanceBetweenTwoPoints(C,A);
        assertEquals(6,distance,0.1);
        distance = HelperFunctions.distanceBetweenTwoPoints(A,E);
        assertEquals(6.71,distance,0.1);
    }

}