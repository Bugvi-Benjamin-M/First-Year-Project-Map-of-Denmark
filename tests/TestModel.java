import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.OSMEnums.WayType;
import Enums.RoadType;
import Helpers.FileHandler;
import Model.Model;
import Model.Road;
import View.Window;
import org.junit.Test;

import java.awt.*;
import java.awt.geom.Path2D;
import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;


public class TestModel {

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

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, model.getWayElements().isEmpty());
        model.clear();
        assertEquals(true, model.getWayElements().get(WayType.ROAD).isEmpty());
        assertEquals(true, model.getWayElements().get(WayType.UNKNOWN).isEmpty());

        mainWindowController.resetInstance();
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

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        model.clear();
        assertTrue(model.getWayElements() != null);
        assertEquals(model.getWayElements().size(), WayType.values().length);

        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testAddWayElement() {
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        assertEquals(0, model.getWayElements().get(WayType.ROAD).size());
        Path2D path = new Path2D.Float();
        model.addWayElement(WayType.ROAD, new Road(RoadType.SERVICE, path));
        assertEquals(1, model.getWayElements().get(WayType.ROAD).size());


        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }

}
