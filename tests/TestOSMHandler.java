import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import Model.Model;
import OSM.OSMHandler;
import org.junit.Test;

import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nik on 13/03/17.
 */
public class TestOSMHandler {

    @Test
    public void testRoads(){

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        try {
            FileHandler.loadDefault("/testRoad.osm");
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);

        model.resetInstance();
        canvasController.resetInstance();
    }

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

        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testUnknown() {

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        try {
            FileHandler.loadDefault("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int UnknownCount = model.getWayElements().get(WayType.UNKNOWN).size();
        assertEquals(1, UnknownCount);


        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testBounds(){

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());;
        try {
            FileHandler.loadDefault("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Math.abs(-55.7272800 - model.getMinLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4692700 * OSMHandler.getInstance().getLongitudeFactor() - model.getMinLongitude()) < 0.0001);
        assertTrue(Math.abs(-55.7286100 - model.getMaxLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4735500 * OSMHandler.getInstance().getLongitudeFactor() - model.getMaxLongitude()) < 0.0001);


        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }
}
