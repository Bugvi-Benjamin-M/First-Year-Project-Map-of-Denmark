import KDtree.*;
import org.junit.Before;
import org.junit.Test;
import Model.Elements.*;
import java.awt.*;
import java.awt.geom.*;


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
    public void buildUp(){
        tree = new KDTree();
    }

    @Test
    public void testPointer(){
        Path2D rectangle = new Path2D.Double();
        Element el = new UnknownWay(rectangle);
        Pointer p = new Pointer(1, 2, el);
        assertEquals(p.getElement(), el);
        assertEquals(p.getElement().getShape(), rectangle);
    }
}
