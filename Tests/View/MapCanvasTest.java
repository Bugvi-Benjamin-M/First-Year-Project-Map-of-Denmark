package View;

import Helpers.ThemeHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by Jakob on 01-05-2017.
 */
public class MapCanvasTest {

    MapCanvas mapCanvas;

    @Before
    public void setUp() throws Exception {
        ThemeHelper.setTheme("Default");
        mapCanvas = new MapCanvas();
    }

    @After
    public void tearDown() throws Exception {
        mapCanvas = null;
    }

    @Test
    public void setBackgroundColor() throws Exception {
        mapCanvas.setBackground(Color.BLACK);
        assertTrue(mapCanvas.getBackground().equals(Color.BLACK));

        mapCanvas.setBackground(Color.BLUE);
        assertFalse(mapCanvas.getBackground().equals(Color.RED));
    }

    @Test
    public void toggleAntiAliasing() throws Exception {
        JFrame frame = new JFrame();
        frame.setPreferredSize(new Dimension(100,100));
        frame.add(mapCanvas);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //Test the default value of the RenderingHints
        Graphics2D g = (Graphics2D) mapCanvas.getGraphics();
        assertTrue(g.getRenderingHint(RenderingHints.KEY_ANTIALIASING).equals(RenderingHints.VALUE_ANTIALIAS_OFF));

        /*
         * Test the default value of the field antiAliasing, which is set by the method
         * public void toggleAntiAliasing(boolean isAntiAliasing)
         * Before the method have been invoked.
         */
        Field field = mapCanvas.getClass().getDeclaredField("antiAliasing");
        field.setAccessible(true);
        field.get(mapCanvas);
        assertTrue(field.get(mapCanvas).equals(false));

        //Testing the value of the field antiAliasing after the toggleAntiAliasing-method have been invoked.
        mapCanvas.toggleAntiAliasing(true);
        field = mapCanvas.getClass().getDeclaredField("antiAliasing");
        field.setAccessible(true);
        field.get(mapCanvas);
        assertTrue(field.get(mapCanvas).equals(true));
    }

    @Test
    public void setCoastLines() throws Exception {
        //A visual test may be best.
    }

    @Test
    public void getCameraMaxLon() throws Exception {

    }

    @Test
    public void getCameraMinLon() throws Exception {

    }

    @Test
    public void getCameraMaxLat() throws Exception {

    }

    @Test
    public void getCameraMinLat() throws Exception {

    }

    @Test
    public void setMaxLon() throws Exception {

    }

    @Test
    public void setMinLon() throws Exception {

    }

    @Test
    public void setMaxLat() throws Exception {

    }

    @Test
    public void setMinLat() throws Exception {

    }

    @Test
    public void setDynMaxLon() throws Exception {

    }

    @Test
    public void setDynMinLon() throws Exception {

    }

    @Test
    public void setDynMaxLat() throws Exception {

    }

    @Test
    public void setDynMinLat() throws Exception {

    }

    @Test
    public void paintComponent() throws Exception {

    }

    @Test
    public void zoom() throws Exception {

    }

    @Test
    public void resetTransform() throws Exception {

    }

    @Test
    public void pan() throws Exception {

    }

    @Test
    public void toModelCoords() throws Exception {

    }

    @Test
    public void setElements() throws Exception {

    }

    @Test
    public void setCurrentSection() throws Exception {

    }

    @Test
    public void setCurrentPoint() throws Exception {

    }

    @Test
    public void setLocationMarker() throws Exception {

    }

    @Test
    public void panToPoint() throws Exception {

    }

    @Test
    public void setPOIs() throws Exception {

    }

}