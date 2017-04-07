package Helpers;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 04-04-2017.
 * @project BFST
 */
public class HelperFunctions {

    /**
     * Simplifies and generalizes a path recursively
     * @param points A list of points representing a path (ordered)
     * @param epsilon The distance dimension (greater than zero)
     * @return simplified path
     * @see <a href='https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm'>Douglas-Peucker Wikipedia</a>
     */
    public static List<Point2D> pathGeneralization(List<Point2D> points, double epsilon) {
        // Find the point with the maximum distance
        double dmax = 0;
        int index = 0;
        int end = points.size();
        for (int i = 1; i < end - 1; i++) {
            double d = distanceBetweenPointAndPath(points.get(0),points.get(end-1),points.get(i));
            System.out.println("d: "+d);
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        List<Point2D> result = new ArrayList<>();

        // if max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            System.out.println("recursive call");
            // Recursive call
            List<Point2D> firstSection = new ArrayList<>(), secondSection = new ArrayList<>();
            for (int i = 0; i < index; i++) {
                firstSection.add(points.get(i));
            }
            for (int i = index; i < end; i++) {
                secondSection.add(points.get(i));
            }
            firstSection = pathGeneralization(firstSection,epsilon);
            secondSection = pathGeneralization(secondSection,epsilon);

            // Build the result list
            result.addAll(firstSection);
            result.addAll(secondSection);
        } else {
            System.out.println("add all");
            result.addAll(points);
        }

        return result;
    }

    /**
     * Calculates the perpendicular distance between a point and a line
     * @param pathStart The beginning point of the line
     * @param pathEnd The end point of the line
     * @see <a href='https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line'>Perpendicular distance - Wikipedia</a>
     */
    public static double distanceBetweenPointAndPath(Point2D pathStart, Point2D pathEnd, Point2D point) {
        double lengthOfPath = distanceBetweenTwoPoints(pathStart,pathEnd);
        double numerator = ((pathEnd.getY()-pathStart.getY())*point.getX())
                - ((pathEnd.getX() - pathStart.getX())*point.getY())
                + (pathEnd.getX()*pathStart.getY()) - (pathEnd.getY()*pathStart.getX());
        if (numerator < 0) numerator *= -1;
        return numerator / lengthOfPath;
    }

    /**
     * Calculates the distance between two given points using the pythagorean theorem.
     * @see <a href='https://en.wikipedia.org/wiki/Pythagorean_theorem'>Pythagorean Theorem - Wikipedia</a>
     */
    public static double distanceBetweenTwoPoints(Point2D pointA, Point2D pointB) throws IllegalArgumentException {
        if (pointA == null || pointB == null) throw new IllegalArgumentException("Points has to be initialized.");
        double dx = pointB.getX() - pointA.getX();
        double dy = pointB.getY() - pointA.getY();

        // if negative convert to positive
        if (dx < 0) dx *= -1;
        if (dy < 0) dy *= -1;

        return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
    }
}
