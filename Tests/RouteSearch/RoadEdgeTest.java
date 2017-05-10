package RouteSearch;

import Helpers.HelperFunctions;
import Model.Elements.RoadEdge;
import OSM.OSMWay;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

import java.awt.geom.Point2D;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 09-05-2017
 */
public class RoadEdgeTest {

    private RoadEdge from, left, continued, right;
    /*
    @Before
    public void setUp() throws Exception {
        Point2D connection = new Point2D.Float(3,4);
        from = new RoadEdge(new Point2D.Float(2,1),connection,"from road",50);
        left = new RoadEdge(connection,new Point2D.Float(1,6),"left road",50);
        continued = new RoadEdge(connection,new Point2D.Float(5,6),"from road",50);
        right = new RoadEdge(connection,new Point2D.Float(6,5),"right road",50);
    }

    @Test
    public void testAngle() throws Exception {
        // setup angles
        double leftAngle, continueAngle, rightAngle;
        leftAngle = HelperFunctions.angle(from.getEither(),from.getOther(from.getEither()),
                left.getEither(),left.getOther(left.getEither()));
        continueAngle = HelperFunctions.angle(from.getEither(),from.getOther(from.getEither())
                ,continued.getEither(),continued.getOther(continued.getEither()));
        rightAngle = HelperFunctions.angle(from.getEither(),from.getOther(from.getEither())
                ,right.getEither(),right.getOther(right.getEither()));

        // assertions
        System.out.println("From - Left: "+ leftAngle);
        assertEquals(-0.44,leftAngle,0.01);
        System.out.println("From - Continue: "+ continueAngle);
        assertEquals(0.89,continueAngle,0.01);
        System.out.println("From - Right: "+ rightAngle);
        assertEquals(0.99,rightAngle,0.01);
    }

    @Test
    public void testThyself() throws Exception {
        double angle = HelperFunctions.angle(from.getEither(), from.getOther(from.getEither()),
                from.getEither(), from.getOther(from.getEither()));
        System.out.println("From - From: "+angle);
    }

    @Test
    public void testReverse() throws Exception {
        RoadEdge reverse = from.createReverse();
        assertEquals(from.getEither(),reverse.getOther(reverse.getEither()));
        assertEquals(from.getOther(from.getEither()),reverse.getEither());
        assertEquals(0,from.compareTo(reverse));
        double angle = HelperFunctions.angle(from.getEither(),from.getOther(from.getEither()),
                reverse.getEither(),reverse.getOther(reverse.getEither()));
        System.out.println("From - Reverse: "+angle);
    }

    @Test
    public void testDirection() throws Exception {
        int compare = from.compareToRoad(left);
        assertEquals(-1,compare);
        System.out.println(left.getName()+" is left of "+from.getName());
        assertEquals(from.getName(),continued.getName());
        System.out.println(continued.getName()+" is part of "+from.getName());
        compare = from.compareToRoad(right);
        assertEquals(1,compare);
        System.out.println(right.getName()+" is right of "+from.getName());
    }
    */
}
