import Controller.CanvasController;
import Enums.OSMEnums.WayType;
import Enums.RoadType;
import Helpers.FileHandler;
import View.Window;
import junit.framework.TestCase;
import Model.*;
import org.junit.Test;

import java.awt.geom.Path2D;
import java.io.FileNotFoundException;
import javax.swing.*;
import java.awt.*;


public class TestModel extends TestCase {

    @Test
    public void testSingleton() {
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .layout(new BorderLayout())
                            .show();

        Model model = Model.getInstance();
        Model model2 = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(model.equals(model2));

        model.resetInstance();
        model2.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testClear() {
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .show();

        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, model.getWayElements().isEmpty());
        model.clear();
        assertEquals(true, model.getWayElements().get(WayType.ROAD).isEmpty());
        assertEquals(true, model.getWayElements().get(WayType.UNKNOWN).isEmpty());

        model.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testGetBounds() {
        Model model = Model.getInstance();
        assertTrue(model.getMinLatitude() == 0.0f);
        assertTrue(model.getMaxLatitude() == 0.0f);
        assertTrue(model.getMinLongitude() == 0.0f);
        assertTrue(model.getMaxLongitude() == 0.0f);

        model.resetInstance();
    }

    @Test
    public void testSetBounds() {
        Model model = Model.getInstance();
        model.setBounds(2.3f,2.4f,2.5f, 2.6f);
        assertEquals(2.3f, model.getMinLatitude());
        assertEquals(2.4f, model.getMaxLatitude());
        assertEquals(2.5f, model.getMinLongitude());
        assertEquals(2.6f, model.getMaxLongitude());

        model.resetInstance();
    }

    @Test
    public void testGetWayElements() {
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .show();

        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        model.clear();
        assertTrue(model.getWayElements() != null);
        assertEquals(model.getWayElements().size(), WayType.values().length);

        model.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testAddWayElement() {
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .show();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        assertEquals(0, model.getWayElements().get(WayType.ROAD).size());
        Path2D path = new Path2D.Float();
        model.addWayElement(WayType.ROAD, new Road(RoadType.SERVICE, path));
        assertEquals(1, model.getWayElements().get(WayType.ROAD).size());

        model.resetInstance();
        canvasController.resetInstance();
    }

}
