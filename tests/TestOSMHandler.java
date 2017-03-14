import Controller.CanvasController;
import Controller.InfobarController;
import Controller.ToolbarController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import OSM.OSMHandler;
import View.Window;
import junit.framework.TestCase;
import Model.*;

import java.io.FileNotFoundException;

/**
 * Created by Nik on 13/03/17.
 */
public class TestOSMHandler extends TestCase {

    public void testRoads(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }

        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
    }

    public void testReload(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        try {
            FileHandler.loadDefault("/testThreeRoads.osm");
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            new RuntimeException(e);
        }
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
    }

    public void testUnknown() {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        try {
            FileHandler.loadDefault("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            new RuntimeException(e);
        }
        int UnknownCount = model.getWayElements().get(WayType.UNKNOWN).size();
        assertEquals(1, UnknownCount);
    }

    public void testBounds(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        try {
            FileHandler.loadDefault("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            new RuntimeException(e);
        }
        assertTrue(Math.abs(-55.7272800 - model.getMinLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4692700 * OSMHandler.getInstance().getLongitudeFactor() - model.getMinLongitude()) < 0.0001);
        assertTrue(Math.abs(-55.7286100 - model.getMaxLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4735500 * OSMHandler.getInstance().getLongitudeFactor() - model.getMaxLongitude()) < 0.0001);

    }
}
