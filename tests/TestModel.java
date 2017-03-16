import Controller.CanvasController;
import Controller.ToolbarController;
import Helpers.FileHandler;
import View.Toolbar;
import View.Window;
import junit.framework.TestCase;
import Model.*;
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
        assertFalse(model.getWayElements().isEmpty());
        model.clear();
        assertTrue(model.getWayElements().isEmpty());

    }

}