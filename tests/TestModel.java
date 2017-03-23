import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.OSMEnums.WayType;
import Enums.RoadType;
import org.junit.Test;
import Model.Model;
import Model.Road;

import java.awt.geom.Path2D;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import org.junit.Before;
import org.junit.After;
import Helpers.FileHandler;
import org.junit.Test;
import Model.Model;

import java.io.FileNotFoundException;
import Model.Element;
import Model.UnknownWay;

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
        Path2D path = new Path2D.Float();
        model.addWayElement(WayType.ROAD, new Road(RoadType.SERVICE, path));
        assertEquals(1, model.getWayElements().get(WayType.ROAD).size());
    }

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
    }

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
        model.setBounds(2.3f,2.4f,2.5f, 2.6f);
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
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(model.equals(model2));
    }

    @Test
    public void TestRoadIsElement() {
        Road road = new Road(RoadType.SERVICE, new Path2D.Float());
        UnknownWay unknownWay = new UnknownWay(new Path2D.Float());
        assertTrue(road instanceof Element);
        assertTrue(unknownWay instanceof Element);
    }

    @Test
    public void testConstructorWithoutName() {
        Road road = new Road(RoadType.SERVICE, new Path2D.Float());
        assertTrue(road.getName().equals(""));
    }

    @Test
    public void testGetname() {
        Road road = new Road(RoadType.UNCLASSIFIED, new Path2D.Float(), "Road");
        assertEquals("Road", road.getName());
    }

    @Test
    public void testGetPath() {
        Path2D.Float path = new Path2D.Float();
        path.moveTo(0f, 0f);
        path.lineTo(1f, 1f);
        path.closePath();
        Road road = new Road(RoadType.PRIMARY, path);
        assertEquals(path, road.getPath());
        assertTrue(path.getBounds().equals(
                road.getPath().getBounds()));
    }

    @Test
    public void testGetPath2() {
        Path2D path = new Path2D.Float();
        path.moveTo(0f, 0f);
        path.lineTo(1f, 1f);
        path.closePath();
        UnknownWay unknownWay = new UnknownWay(path);
        assertEquals(path, unknownWay.getPath());
        assertTrue(path.getBounds().equals(
                unknownWay.getPath().getBounds()));
    }

    @Test
    public void testGetRoadType() {
        Road service = new Road(RoadType.SERVICE, new Path2D.Float());
        Road unclassified = new Road(RoadType.UNCLASSIFIED, new Path2D.Float());
        Road highway = new Road(RoadType.HIGHWAY, new Path2D.Float());
        Road tertiary = new Road(RoadType.TERTIARY, new Path2D.Float());
        Road secondary = new Road(RoadType.SECONDARY, new Path2D.Float());
        Road primary = new Road(RoadType.PRIMARY, new Path2D.Float());
        assertEquals(RoadType.SERVICE, service.getRoadType());
        assertEquals(RoadType.UNCLASSIFIED, unclassified.getRoadType());
        assertEquals(RoadType.HIGHWAY, highway.getRoadType());
        assertEquals(RoadType.TERTIARY, tertiary.getRoadType());
        assertEquals(RoadType.SECONDARY, secondary.getRoadType());
        assertEquals(RoadType.PRIMARY, primary.getRoadType());
    }
}
