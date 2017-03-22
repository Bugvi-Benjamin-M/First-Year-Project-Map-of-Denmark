package OSM;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import Model.Model;
import org.junit.Test;

import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Nik on 13/03/17.
 */
public class TestReload {
    @Test
    public void testReload(){
        WindowController mainWindowController = MainWindowController.getInstance();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        try {
            FileHandler.loadDefault("/testThreeRoads.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(3, RoadCount);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }
}
