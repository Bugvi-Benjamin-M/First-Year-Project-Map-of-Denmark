package Helpers.Utilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FPSCounterTest {

    private FPSCounter counter;

    @Before
    public void setUp()
    {
        counter = new FPSCounter();
        counter.start();
    }

    @Test
    public void testThread()
    {
        System.out.println("Before assert: " + counter.getFPS());
        assertEquals(counter.getFPS(), 0.0, 0.001);
        try {
            counter.interrupt();
            counter.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("After interrupt: " + counter.getFPS());
        assertNotEquals(counter.getFPS(), 0.0, 0.00000000001);
        System.out.println("End: " + counter.getFPS());
    }

    @Test
    public void testRunning()
    {
        assertTrue(counter.isAlive());
        // note that stopping a thread is unsafe
    }

    @After
    public void tearDown()
    {
        counter.stopThread();
    }
}