import Controller.CanvasController;
import Enums.OSMEnums.WayType;
import Enums.RoadType;
import Helpers.FileHandler;
import View.Window;
import junit.framework.TestCase;
import Model.*;
import org.junit.Test;

import java.awt.geom.Path2D;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TestModel extends TestCase {

    @Test
    public void testSingleton() {
        Window window = new Window();
        Model model = Model.getInstance();
        Model model2 = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(model.equals(model2));
        try {
            Method resetModelInstance = Model.class.getDeclaredMethod("resetInstance");
            Method resetCanvasControllerInstance = CanvasController.class.getDeclaredMethod("resetInstance");
            resetModelInstance.setAccessible(true);
            resetCanvasControllerInstance.setAccessible(true);
            resetModelInstance.invoke(model);
            resetModelInstance.invoke(model2);
            resetCanvasControllerInstance.invoke(canvasController);
            resetModelInstance.setAccessible(false);
            resetCanvasControllerInstance.setAccessible(false);
            System.gc();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClear() {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(false, model.getWayElements().isEmpty());
        model.clear();
        assertEquals(true, model.getWayElements().get(WayType.ROAD).isEmpty());
        assertEquals(true, model.getWayElements().get(WayType.UNKNOWN).isEmpty());
        try {
            Method resetModelInstance = Model.class.getDeclaredMethod("resetInstance");
            resetModelInstance.setAccessible(true);
            resetModelInstance.invoke(model);
            Method resetCanvasControllerInstance = CanvasController.class.getDeclaredMethod("resetInstance");
            resetCanvasControllerInstance.setAccessible(true);
            resetCanvasControllerInstance.invoke(canvasController);
            resetModelInstance.setAccessible(false);
            resetCanvasControllerInstance.setAccessible(false);
            System.gc();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetBounds() {
        Model model = Model.getInstance();
        assertTrue(model.getMinLatitude() == 0.0f);
        assertTrue(model.getMaxLatitude() == 0.0f);
        assertTrue(model.getMinLongitude() == 0.0f);
        assertTrue(model.getMaxLongitude() == 0.0f);
        try {
            Method resetModelInstance = Model.class.getDeclaredMethod("resetInstance");
            resetModelInstance.setAccessible(true);
            resetModelInstance.invoke(model);
            resetModelInstance.setAccessible(false);
            System.gc();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetBounds() {
        Model model = Model.getInstance();
        model.setBounds(2.3f,2.4f,2.5f, 2.6f);
        assertEquals(2.3f, model.getMinLatitude());
        assertEquals(2.4f, model.getMaxLatitude());
        assertEquals(2.5f, model.getMinLongitude());
        assertEquals(2.6f, model.getMaxLongitude());
        try {
            Method resetModelInstance = Model.class.getDeclaredMethod("resetInstance");
            resetModelInstance.setAccessible(true);
            resetModelInstance.invoke(model);
            resetModelInstance.setAccessible(false);
            System.gc();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWayElements() {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        model.clear();
        assertTrue(model.getWayElements() != null);
        assertEquals(model.getWayElements().size(), WayType.values().length);
        try {
            Method resetModelInstance = Model.class.getDeclaredMethod("resetInstance");
            Method resetCanvasControllerInstance = CanvasController.class.getDeclaredMethod("resetInstance");
            resetModelInstance.setAccessible(true);
            resetCanvasControllerInstance.setAccessible(true);
            resetModelInstance.invoke(model);
            resetCanvasControllerInstance.invoke(canvasController);
            resetModelInstance.setAccessible(false);
            resetCanvasControllerInstance.setAccessible(false);
            System.gc();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddWayElement() {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        assertEquals(1, model.getWayElements().get(WayType.ROAD).size());
        assertEquals(0, model.getWayElements().get(WayType.UNKNOWN).size());
        Path2D path = new Path2D.Float();
        model.addWayElement(WayType.ROAD, new Road(RoadType.SERVICE, path));
        model.addWayElement(WayType.UNKNOWN, new UnknownWay(path));
        assertEquals(2, model.getWayElements().get(WayType.ROAD).size());
        assertEquals(1, model.getWayElements().get(WayType.UNKNOWN).size());
        try {
            Method resetModelInstance = Model.class.getDeclaredMethod("resetInstance");
            Method resetCanvasControllerInstance = CanvasController.class.getDeclaredMethod("resetInstance");
            resetModelInstance.setAccessible(true);
            resetCanvasControllerInstance.setAccessible(true);
            resetModelInstance.invoke(model);
            resetCanvasControllerInstance.invoke(canvasController);
            resetModelInstance.setAccessible(false);
            resetCanvasControllerInstance.setAccessible(false);
            System.gc();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
