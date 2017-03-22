package Model;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import org.junit.Test;

import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestClear {


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
}
