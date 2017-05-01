package RouteSearch;

import Enums.TravelType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 01-05-2017.
 * @project BFST
 */
public class GraphTest {

    private Graph graph;

    @Before
    public void setUp() throws Exception {
        graph = new Graph(10);
    }

    @Test
    public void testAddingEdges() {
        graph.addEdge(new Edge(0,1,20,0.5f, TravelType.WALK));
        System.out.println(graph.toString());
    }

    @After
    public void tearDown() throws Exception {
        graph = null;
    }
}