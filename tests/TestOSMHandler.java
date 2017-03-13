import Controller.CanvasController;
import Controller.InfobarController;
import Controller.ToolbarController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import View.Window;
import junit.framework.TestCase;
import Model.*;

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
        FileHandler.loadDefault("/testRoad.osm");

        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
    }

    public void testReload(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        FileHandler.loadDefault("/testThreeRoads.osm");
        FileHandler.loadDefault("/testRoad.osm");
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
    }

    public void testUnknown(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        FileHandler.loadDefault("/testUnknown.osm");
        int UnknownCount = model.getWayElements().get(WayType.UNKNOWN).size();
        assertEquals(1, UnknownCount);
    }
}
