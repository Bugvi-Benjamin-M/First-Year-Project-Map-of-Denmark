import Controller.CanvasController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import Model.Model;
import View.Window;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.FileNotFoundException;


public class TestModel extends TestCase {


    @Test
    public void testSingleton() {
        Window window = new Window();
        Model model = Model.getInstance();
        Model model2 = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(model.equals(model2));
    }

    @Test
    public void testClear() {
        Window window = new Window();
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

    }

    @Test
    public void testSetBounds() {

    }

}