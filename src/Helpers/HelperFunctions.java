package Helpers;

import Controller.CanvasController;
import OSM.OSMWay;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class details:
 * A collection of different mathematical and useful functions
 * used for different purposes in different parts of the program.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 04-04-2017
 */
public class HelperFunctions {

    /**
   * Simplifies and generalizes a path recursively
   * @param points A list of points representing a path (ordered)
   * @param epsilon The distance dimension (greater than zero)
   * @return simplified path
   * @see <a
   * href='https://en.wikipedia.org/wiki/Ramer%E2%80%93Douglas%E2%80%93Peucker_algorithm'>Douglas-Peucker
   * Wikipedia</a>
   */
    public static List<Point2D> pathGeneralization(List<Point2D> points,
        double epsilon)
    {
        // Find the point with the maximum distance
        double dmax = 0;
        int index = 0;
        int end = points.size();
        for (int i = 1; i < end; i++) {
            double d = distanceBetweenPointAndPath(points.get(0), points.get(end - 1),
                points.get(i));
            // System.out.println("d: "+d);
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        List<Point2D> result = new ArrayList<>();

        // if max distance is greater than epsilon, recursively simplify
        if (dmax >= epsilon) {
            // System.out.println("recursive call");
            // Recursive call
            List<Point2D> firstSection = new ArrayList<>(), secondSection = new ArrayList<>();
            for (int i = 1; i <= index; i++) {
                firstSection.add(points.get(i));
            }
            for (int i = index; i < end; i++) {
                secondSection.add(points.get(i));
            }

            firstSection = pathGeneralization(firstSection, epsilon);
            secondSection = pathGeneralization(secondSection, epsilon);

            // Build the result list
            for (int i = 1; i < firstSection.size() - 2; i++) {
                result.add(firstSection.get(i));
            }
            for (int i = 1; i < secondSection.size() - 1; i++) {
                result.add(secondSection.get(i));
            }
        } else {
            // System.out.println("add all");
            for (int i = 1; i < end; i++) {
                result.add(points.get(i));
            }
        }

        return result;
    }

    /**
   * Calculates the perpendicular distance between a point and a line
   * @param pathStart The beginning point of the line
   * @param pathEnd The end point of the line
   * @see <a
   * href='https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line'>Perpendicular
   * distance - Wikipedia</a>
   */
    public static double distanceBetweenPointAndPath(Point2D pathStart,
        Point2D pathEnd,
        Point2D point)
    {
        double lengthOfPath = distanceBetweenTwoPoints(pathStart, pathEnd);
        double numerator = ((pathEnd.getY() - pathStart.getY()) * point.getX()) - ((pathEnd.getX() - pathStart.getX()) * point.getY()) + (pathEnd.getX() * pathStart.getY()) - (pathEnd.getY() * pathStart.getX());
        if (numerator < 0)
            numerator *= -1;
        return numerator / lengthOfPath;
    }

    /**
   * Calculates the distance between two given points using the pythagorean
   * theorem.
   * @see <a
   * href='https://en.wikipedia.org/wiki/Pythagorean_theorem'>Pythagorean
   * Theorem - Wikipedia</a>
   */
    public static double distanceBetweenTwoPoints(Point2D pointA, Point2D pointB)
        throws IllegalArgumentException
    {
        if (pointA == null || pointB == null)
            throw new IllegalArgumentException("Points has to be initialized.");
        double dx = pointB.getX() - pointA.getX();
        double dy = pointB.getY() - pointA.getY();

        // if negative convert to positive
        if (dx < 0)
            dx *= -1;
        if (dy < 0)
            dy *= -1;

        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    /**
     * Returns the size of a polygon based entirely on a ordered collection of points that connect
     * @param points The list of Point2D representing the shape of the polygon
     */
    public static double sizeOfPolygon(List<Point2D> points)
    {
        double dividend = 0;
        int previus = 0, next = 1;
        int n = points.size();
        Point2D previousPoint, nextPoint;
        for (int i = 0; i < n - 1; i++) {
            previousPoint = points.get(previus);
            nextPoint = points.get(next);
            dividend += (previousPoint.getX() * nextPoint.getY() - previousPoint.getY() * nextPoint.getX());
            previus++;
            next++;
        }
        previousPoint = points.get(n - 1);
        nextPoint = points.get(0);
        dividend += (previousPoint.getX() * nextPoint.getY() - previousPoint.getY() * nextPoint.getX());
        return Math.abs(dividend / 2);
    }

    /**
     * Calculates the distance between two points to meters
     * @param v A point on a sphere
     * @param w Another point on a sphere
     */
    public static double distanceInMeters(Point2D v, Point2D w) {
        double R = 6371e3;
        double latitude1 = Math.toRadians(v.getY());
        double latitude2 = Math.toRadians(w.getY());
        double dy = Math.toRadians((v.getY()-w.getY()));
        double dx = Math.toRadians(v.getX()-w.getX());
        double a = Math.sin(dy) * Math.sin(dy/2) +
                Math.cos(latitude1) * Math.cos(latitude2) *
                        Math.sin(dx/2) * Math.sin(dx/2);
        double c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        return R * c;
    }

    /**
     * Calculates the total distance between each consecutive set of points in a collection
     * @param way An ordered collection of points
     */
    public static double distanceInMeters(List<Point2D> way) {
        if (way.size() < 2) throw new IllegalArgumentException("needs at least two points to calculate the distance");
        double length = 0.0;
        for (int i = 1; i < way.size(); i++) {
            length += distanceInMeters(way.get(i - 1), way.get(i));
        }
        return length;
    }

    public static float convertDistanceFromScreenCoordsToModelCoords(int distance) {
        Point2D start = CanvasController.getInstance().getMapCanvas().toModelCoords(new Point2D.Float(0,0));
        Point2D end = CanvasController.getInstance().getMapCanvas().toModelCoords(new Point2D.Float(distance,0));
        return (float) (end.getX() - start.getX());
    }

    private static double dotProduct(Vector a, Vector b) {
        if (a == null || b == null) {
            throw new NullPointerException("Arguments must not be null");
        }
        return (a.x * b.y) + (a.y * b.x);
    }

    public static double angle(OSMWay one, OSMWay other) {
        if (one == null || other == null) {
            throw new NullPointerException("Arguments must not be null");
        } else if (one.size() < 2 || other.size() < 2) {
            throw new IllegalArgumentException("Angle between points is not possible");
        }
        Vector a = new Vector(one.getFromNode(),one.getToNode());
        Vector b = new Vector(other.getFromNode(),other.getToNode());
        return angle(a,b);
    }

    public static double angle(Point2D from1, Point2D to1,Point2D from2,Point2D to2){
        if (from1 == null || from2 == null || to1 == null || to2 == null)
            throw new IllegalArgumentException("No arguments may be null.");
        Vector a = new Vector(from1,to1);
        Vector b = new Vector(from2,to2);
        return angle(a,b);
    }

    private static double angle(Vector a, Vector b) {
        //System.out.println("a "+a.toString()+" - b "+b.toString());
        double distances = a.length() * b.length();
        return (dotProduct(a,b) / distances);
    }

    private static class Vector {
        private double x;
        private double y;

        Vector(Point2D a, Point2D b) {
            x = a.getX()-b.getX();
            y = a.getY()-b.getY();
        }

        double length() {
            return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        }

        @Override
        public String toString() {
            return "("+x+";"+y+")";
        }
    }
}
