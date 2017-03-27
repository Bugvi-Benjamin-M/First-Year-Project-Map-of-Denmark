package ModelPackage.Coastlines;

import Model.Coastlines.CoastlineFileGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 23-03-2017.
 * @project BFST
 */
public class TestWayCarrier {

    private CoastlineFileGenerator.WayCarrier way;
    private long ref;

    @Before
    public void setUp() throws Exception {
        ref = 12345678L;
        way = new CoastlineFileGenerator.WayCarrier(ref);
    }

    @After
    public void tearDown() throws Exception {
        way = null;
    }

    @Test
    public void testToString() throws Exception {
        String emptyWay = way.toString();
        assertEquals(emptyWay,"<way id=\""+ref+"\">\n</way>");
        way.add(new CoastlineFileGenerator.NodeCarrier(1,0,0));
        String nonEmptyWay = way.toString();
        assertNotEquals(nonEmptyWay,emptyWay);
        assertEquals(nonEmptyWay,"<way id=\""+ref+"\">\n<nd ref=\""+1+"\"/>\n</way>");
    }

}