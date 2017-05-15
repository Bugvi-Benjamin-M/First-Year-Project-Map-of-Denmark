package Helpers;

import Controller.CanvasController;
import KDtree.Point;
import Model.Model;
import OSM.OSMHandler;
import OSM.OSMWay;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Class details:
 * A collection of different mathematical and useful functions
 * used for different purposes in different parts of the program.
 *
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
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }

        List<Point2D> result = new ArrayList<>();

        // if max distance is greater than epsilon, recursively simplify
        if (dmax >= epsilon) {
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
     * Quickly generates the distance between two points in meters
     * Note that this function only works on the Map of Denmark
     */
    public static double lazyDistance(Point2D from, Point2D to){
        return Math.sqrt((Math.pow(((from.getX()-to.getX())/62.445),2)) + (Math.pow(((from.getY()-to.getY())/111.096),2)));
    }

    /**
     * Calculates the distance between two points to meters
     * @param v A point on a sphere
     * @param w Another point on a sphere
     */
    public static double distanceInMeters(Point2D v, Point2D w) {
        double R = 6371e3; //Earth radius in meters
        float longfactor = Model.getInstance().getLongitudeFactor();
        double latitude1 = Math.toRadians(-v.getY());
        double latitude2 = Math.toRadians(-w.getY());
        double dy = Math.toRadians((v.getY()-w.getY()));
        double dx = Math.toRadians((v.getX()/longfactor) - (w.getX()/longfactor));
        double a = Math.sin(dy) * Math.sin(dy/2) + Math.cos(latitude1) * Math.cos(latitude2) * Math.sin(dx/2) * Math.sin(dx/2);
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

    /**
     * Convertes nano seconds to a string describing the amount
     */
    public static String convertNanotimeToTime(long loadtime) {
        long loadtimeMilliseconds = loadtime / 1000000;
        return convertMillitimeToTime(loadtimeMilliseconds);
        }

    /**
     * Convertes milli seconds to a string describing the amount
     */
    public static String convertMillitimeToTime(long loadtime) {
        long loadtimeSeconds = loadtime / 1000;
        long loadtimeMinutes = loadtimeSeconds / 60;
        String name = "";
        if (loadtimeMinutes > 0) name += loadtimeMinutes + " m, ";
        return name + (loadtimeSeconds - (loadtimeMinutes * 60)) + " s, " +
                (loadtime - (loadtimeSeconds * 1000)) + " ms";
    }

    /**
     * Convertes nano seconds to a string describing the amount
     */
    public static String simplifyNanoTime(long loadtime) {
        long loadtimeMilliseconds = loadtime / 1000000;
        long loadtimeSeconds = loadtimeMilliseconds / 1000;
        long loadtimeMinutes = loadtimeSeconds / 60;
        String time = "";
        if (loadtimeMinutes > 0) {
            time += loadtimeMinutes + " min";
            long seconds = (loadtimeSeconds - (loadtimeMinutes * 60));
            if (seconds > 0) time += ", "+seconds+" sec";
        } else {
            time += loadtimeSeconds + " sec";
            long miliseconds = (loadtimeMilliseconds - (loadtimeSeconds * 1000));
            if (miliseconds > 0) {
                time += ", "+miliseconds+" ms";
            }
        }
        if (time.equals("")) return "N/A";
        return time;
    }

    /**
     * Compares the direction of two vectors, provided as points.
     */
    public static int direction(Point2D a, Point2D b, Point2D c, Point2D d) {
        Vector ab = new Vector(a,b);
        Vector cd = new Vector(c,d);
        double dot = ab.x*-cd.y + ab.y*cd.x;
        if (dot > 0) {
            return -1;  // to the left
        } else if (dot < 0) {
            return 1;   // to the right
        } else {
            return 0;   // parallel
        }
    }

    /**
     * A mathematical vector described by two points.
     */
    private static class Vector {
        private double x;
        private double y;

        /**
         * Vector constructor
         */
        Vector(Point2D a, Point2D b) {
            x = b.getX()-a.getX();
            y = b.getY()-a.getY();
        }

        /**
         * Length of the vector
         */
        double length() {
            return Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        }

        @Override
        public String toString() {
            return "("+x+";"+y+")";
        }
    }
}
