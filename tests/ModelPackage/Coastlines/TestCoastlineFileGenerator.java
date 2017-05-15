package ModelPackage.Coastlines;

import Model.Coastlines.CoastlineFileGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import static org.junit.Assert.*;

public class TestCoastlineFileGenerator {

    private static CoastlineFileGenerator generator;

    @Before
    public void setUp()
    {
        generator = CoastlineFileGenerator.getInstance();
    }

    @After
    public void tearDown()
    {
        generator.resetInstance();
    }

    @Test
    public void testSingleton()
    {
        assertNotNull(generator);
        CoastlineFileGenerator referenceTwo = CoastlineFileGenerator.getInstance();
        assertEquals(generator, referenceTwo);
    }
}