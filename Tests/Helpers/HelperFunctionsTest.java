package Helpers;

import org.junit.Test;

import java.awt.geom.Point2D;
import java.util.*;

import static org.junit.Assert.*;

public class HelperFunctionsTest {

    private static Point2D A = new Point2D.Double(2, 5);
    private static Point2D B = new Point2D.Double(6, 1);
    private static Point2D C = new Point2D.Double(8, 5);
    private static Point2D D = new Point2D.Double(12, 1);
    private static Point2D E = new Point2D.Double(-1, -1);

    @Test
    public void pathGeneralizationEpsilonTest() throws Exception
    {
        List<Point2D> points = new ArrayList<>();

        int maxValue = 100000;
        for (int i = 0; i < maxValue; i++) {
            double x = Math.random();
            double y = Math.random();
            points.add(new Point2D.Double(x, y));
        }

        System.out.println(points.size());
        HashMap<Integer, Double> values = new HashMap<>();
        for (double i = 1; i > 0; i -= 0.01) {
            List<Point2D> result = HelperFunctions.pathGeneralization(points, i);
            values.put(result.size(), i);
        }

        List<String> results = new ArrayList<>();
        for (Integer size : values.keySet()) {
            String res = "" + size + " " + values.get(size);
            double compared = ((double)size / (double)maxValue);
            res += " " + compared;
            results.add(res);
        }
        Collections.sort(results);
        results.forEach(System.out::println);
    }

    @Test
    public void pathGeneralizationTest() throws Exception {
        List<Point2D> way = new ArrayList<>();
        way.add(A);
        way.add(B);
        way.add(C);
        way.add(D);
        way.add(B);
        way.add(E);
        way.add(A);
        List<Point2D> result = HelperFunctions.pathGeneralization(
                way,1.0);
        assertEquals(6,result.size());
        assertEquals(B,result.get(0));
        assertEquals(C,result.get(1));
        assertEquals(D,result.get(2));
        assertEquals(B,result.get(3));
        assertEquals(E,result.get(4));
        assertEquals(A,result.get(5));
        result = HelperFunctions.pathGeneralization(result,0.0000001);
        assertEquals(0,result.size());
    }

    @Test
    public void distanceBetweenPointAndPath() throws Exception
    {
        double distance = HelperFunctions.distanceBetweenPointAndPath(A, B, C);
        assertEquals(4.24, distance, 0.1);
        distance = HelperFunctions.distanceBetweenPointAndPath(A, B, E);
        assertEquals(6.36, distance, 0.1);
        distance = HelperFunctions.distanceBetweenPointAndPath(B, D, C);
        assertEquals(4, distance, 0.1);
        distance = HelperFunctions.distanceBetweenPointAndPath(A, E, B);
        assertEquals(5.37, distance, 0.1);
    }

    @Test
    public void distanceBetweenTwoPoints() throws Exception
    {
        double distance = HelperFunctions.distanceBetweenTwoPoints(A, B);
        assertEquals(5.66, distance, 0.1);
        distance = HelperFunctions.distanceBetweenTwoPoints(B, D);
        assertEquals(6, distance, 0.1);
        distance = HelperFunctions.distanceBetweenTwoPoints(C, A);
        assertEquals(6, distance, 0.1);
        distance = HelperFunctions.distanceBetweenTwoPoints(A, E);
        assertEquals(6.71, distance, 0.1);
    }

    @Test
    public void distanceInMeters() throws Exception {
        Point2D point1 = new Point2D.Double(12.59257,55.65868);
        Point2D point2 = new Point2D.Double(12.58867,55.65914);
        double distance = HelperFunctions.distanceInMeters(point1,point2);
        System.out.println("distance: "+distance);
        assertEquals(72,distance,1);
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D.Double(12.6771518,55.6310739));
        points.add(new Point2D.Double(12.6782027,55.6306729));
        points.add(new Point2D.Double(12.6793972,55.630198));
        points.add(new Point2D.Double(12.6812215,55.6294461));
        points.add(new Point2D.Double(12.7293449,55.6097865));
        distance = HelperFunctions.distanceInMeters(points);
        System.out.println("distance: "+distance);
        assertEquals(3347,distance,1);
    }

    @Test
    public void directionTest() throws Exception {
        int direction = HelperFunctions.direction(A,B,A,C);
        System.out.println("AB-AC: "+direction);
        assertEquals(1,direction);
        direction = HelperFunctions.direction(A,B,A,D);
        assertEquals(1,direction);
        System.out.println("AB-AD: "+direction);
        direction = HelperFunctions.direction(A,B,A,E);
        assertEquals(-1,direction);
        System.out.println("AB-AE: "+direction);
        direction = HelperFunctions.direction(A,C,A,D);
        assertEquals(-1,direction);
        System.out.println("AC-AD: "+direction);
        direction = HelperFunctions.direction(A,C,A,B);
        assertEquals(-1,direction);
        System.out.println("AC-AB: "+direction);
    }
}