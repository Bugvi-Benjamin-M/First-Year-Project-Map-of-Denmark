package ModelPackage.Coastlines;

import Model.Coastlines.CoastlineFileGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

import static org.junit.Assert.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 23-03-2017.
 * @project BFST
 */
public class TestCoastlineFileGenerator {

    private static CoastlineFileGenerator generator;

    @Before
    public void setUp() {
        generator = CoastlineFileGenerator.getInstance();
    }

    @After
    public void tearDown() {
        generator.resetInstance();
    }

    @Test
    public void testSingleton() {
        assertNotNull(generator);
        CoastlineFileGenerator referenceTwo = CoastlineFileGenerator.getInstance();
        assertEquals(generator,referenceTwo);
    }

    @Test
    public void testAddElement() {
        // generator.startElement(null,null,"", new AttributesImpl());
        // generator.endElement();
    }

    /* ------------ EMPTY TEST METHODS (ALWAYS EMPTY) ------------ */
    @Test
    public void testSetDocumentLocator() {}

    @Test
    public void testStartDocument() {}

    @Test
    public void testEndDocument() {}

    @Test
    public void testStartPrefixMapping() {}

    @Test
    public void testEndPrefixMapping() {}

    @Test
    public void testCharacters() {}

    @Test
    public void testIgnorableWhitespace() {}

    @Test
    public void testProcessingInstruction() {}

    @Test
    public void testSkippedEntity() {}

}