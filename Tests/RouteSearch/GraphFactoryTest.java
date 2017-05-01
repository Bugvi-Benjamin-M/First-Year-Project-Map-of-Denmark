package RouteSearch;

import KDtree.KDTree;
import KDtree.Pointer;
import Model.Elements.Road;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 30-04-2017.
 * @project BFST
 */
public class GraphFactoryTest {

    private GraphFactory factory;

    @Before
    public void setUp() throws Exception {
        // TODO: Finish test
        KDTree tree = new KDTree();
        List<Road> roads = new LinkedList<>();
        Road road = new Road(null,"Vej 1");
        road.setMaxSpeed(60);
        // road.setRelation();
        // roads.add();
        factory = new GraphFactory(null);
    }

    @Test
    public void getGraph() throws Exception {

    }

}