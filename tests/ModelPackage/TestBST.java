package ModelPackage;

import Model.BST;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.geom.Point2D;


/**
 * Created by Jakob on 22-03-2017.
 */
public class TestBST {
    BST bst;

    @Before
    public void setUp() throws Exception {
        bst = new BST();
        bst.addPoint(new Point2D.Double(-1, 2));
        bst.addPoint(new Point2D.Double(4, 3));
        bst.addPoint(new Point2D.Double(0, -2));
        bst.addPoint(new Point2D.Double(-3, 1));
        bst.addPoint(new Point2D.Double(-4, -4));
    }

    @After
    public void tearDown() throws Exception {
        //
    }

    @Test
    public void testGetManySections(){
        //TODO: should this work?
        //assertEquals(5, bst.getManySections(-10.0, -10.0, 10.0, 10.0).size());
    }

}
