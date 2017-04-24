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
public class TestNodeCarrier {

    private CoastlineFileGenerator.NodeCarrier node;
    private long ref;
    private float lon, lat;

    @Before
    public void setUp() throws Exception
    {
        ref = 1;
        lon = 0.0f;
        lat = 0.0f;
        node = new CoastlineFileGenerator.NodeCarrier(ref, lon, lat);
    }

    @After
    public void tearDown() throws Exception
    {
        node = null;
    }

    @Test
    public void testToString() throws Exception
    {
        String output = node.toString();
        assertEquals(output, "<node id=\"" + ref + "\" "
                + "lat=\"" + lat + "\" lon=\"" + lon + "\"/>");
    }
}