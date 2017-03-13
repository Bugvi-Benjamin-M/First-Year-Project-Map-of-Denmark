import Controller.CanvasController;
import Controller.InfobarController;
import Controller.ToolbarController;
import Enums.WayType;
import Helpers.FileHandler;
import View.Window;
import junit.framework.TestCase;
import Model.*;

/**
 * Created by Nik on 13/03/17.
 */
public class TestOSMHandler extends TestCase {

    public void testOSMHandler(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);

        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
    }

    public void testOSMHandlerWithReload(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = new CanvasController(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        FileHandler.loadDefault("/defaultosm.osm");
        FileHandler.loadDefault("/test.osm");
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
    }
}
