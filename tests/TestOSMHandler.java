import Controller.CanvasController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import Model.Model;
import OSM.OSMHandler;
import View.Window;
import org.junit.Test;

import java.awt.*;
import java.io.FileNotFoundException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Nik on 13/03/17.
 */
public class TestOSMHandler {

    @Test
    public void testRoads(){
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .layout(new BorderLayout())
                            .show();

        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
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
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .layout(new BorderLayout())
                            .show();

        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testThreeRoads.osm");
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);

        model.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testUnknown() {
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .layout(new BorderLayout())
                            .show();

        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int UnknownCount = model.getWayElements().get(WayType.UNKNOWN).size();
        assertEquals(1, UnknownCount);

        model.resetInstance();
        canvasController.resetInstance();
    }

    public void testBounds(){
        Window window = new Window().title("TESTING")
                            .dimension(new Dimension(1, 1))
                            .layout(new BorderLayout())
                            .show();

        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);;
        try {
            FileHandler.loadDefault("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Math.abs(-55.7272800 - model.getMinLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4692700 * OSMHandler.getInstance().getLongitudeFactor() - model.getMinLongitude()) < 0.0001);
        assertTrue(Math.abs(-55.7286100 - model.getMaxLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4735500 * OSMHandler.getInstance().getLongitudeFactor() - model.getMaxLongitude()) < 0.0001);

        model.resetInstance();
        canvasController.resetInstance();
    }
}
