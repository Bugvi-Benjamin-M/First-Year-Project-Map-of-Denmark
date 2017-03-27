package ModelPackage;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.BoundType;
import Enums.OSMEnums.WayType;
import Enums.RoadType;
import Model.Model;
import Model.Road;
import OSM.OSMWay;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestModel {

    @Before
    public void buildUp(){
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }

    @Test
    public void testAddWayElement() {
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        assertEquals(0, model.getWayElements().get(WayType.ROAD).size());
        OSMWay path = new OSMWay();
        model.addWayElement(WayType.ROAD, new Road(RoadType.SERVICE, path));
        assertEquals(1, model.getWayElements().get(WayType.ROAD).size());
    }

    /*@Test
    public void testClear() {

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        try {
            FileHandler.loadResource("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, model.getWayElements().isEmpty());
        model.clear();
        assertEquals(true, model.getWayElements().get(WayType.ROAD).isEmpty());
        assertEquals(true, model.getWayElements().get(WayType.UNKNOWN).isEmpty());
    }*/

    @Test
    public void testGetBounds() {
        Model model = Model.getInstance();
        assertTrue(model.getMinLatitude() == 0.0f);
        assertTrue(model.getMaxLatitude() == 0.0f);
        assertTrue(model.getMinLongitude() == 0.0f);
        assertTrue(model.getMaxLongitude() == 0.0f);
    }

    @Test
    public void testGetWayElements() {

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        model.clear();
        assertTrue(model.getWayElements() != null);
        assertEquals(model.getWayElements().size(), WayType.values().length);
    }

    @Test
    public void testObservers() {
        Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        assertEquals(1, Model.getInstance().countObservers());
    }

    @Test
    public void testSetBounds() {
        Model model = Model.getInstance();
        float minlat = 2.3f, maxlat = 2.4f, minlon = 2.5f, maxlon = 2.6f;
        model.setBound(BoundType.MIN_LATITUDE,minlat);
        model.setBound(BoundType.MAX_LATITUDE,maxlat);
        model.setBound(BoundType.MIN_LONGITUDE,minlon);
        model.setBound(BoundType.MAX_LONGITUDE,maxlon);
        assertEquals(2.3f, model.getMinLatitude());
        assertEquals(2.4f, model.getMaxLatitude());
        assertEquals(2.5f, model.getMinLongitude());
        assertEquals(2.6f, model.getMaxLongitude());
    }

    @Test
    public void testSingleton() {
        Model model = Model.getInstance();
        Model model2 = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        assertTrue(model.equals(model2));
    }
}
