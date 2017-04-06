import KDtree.*;
import org.junit.Before;
import org.junit.Test;
import Model.Elements.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.HashSet;



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
        NodeGenerator gen = new NodeGenerator(10, 2);
        tree = new KDTree();

        for(int i = 0; i < 10; i++){
            gen.addPoint(new Point2D.Float(i, i * 2));
        }

        gen.initialise();
        gen.setupTree(tree);

        for(int i = 0; i < 10; i++){
            Path2D rectangle = new Path2D.Double();
            Element el = new UnknownWay(rectangle);
            tree.putPointer(new Pointer(i, i * 2, el));
        }
    }

    @Test
    public void testGetManyElements(){
        HashSet<Element> set = tree.getManyElements(-1, -1, 3, 3);
        assertEquals(2, set.size());
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
