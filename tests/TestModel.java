import Controller.CanvasController;
import Helpers.FileHandler;
import View.Window;
import junit.framework.TestCase;
import Model.*;
import org.junit.Test;

import java.io.FileNotFoundException;


public class TestModel extends TestCase {

    private Model model;
    private Model model2;
    private CanvasController canvasController;
    private Window window;

    public TestModel() {
        window = new Window();
        model = Model.getInstance();
        model2 = Model.getInstance();
        canvasController = CanvasController.getInstance(window);

        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSingleton() {
        assertTrue(model.equals(model2));
    }

    @Test
    public void testClear() {

        assertFalse(model.getWayElements().isEmpty());
        model.clear();
        assertTrue(model.getWayElements().isEmpty());

    }

}