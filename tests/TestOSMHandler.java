import Controller.CanvasController;
import Controller.InfobarController;
import Controller.ToolbarController;
import Enums.OSMEnums.WayType;
import Helpers.FileHandler;
import OSM.OSMHandler;
import View.Window;
import junit.framework.TestCase;
import Model.*;
import junit.*;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Nik on 13/03/17.
 */
public class TestOSMHandler extends TestCase {

    @Test
    public void testRoads(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
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
    public void testReload(){
        Window window = new Window();
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
    public void testUnknown() {
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int UnknownCount = model.getWayElements().get(WayType.UNKNOWN).size();
        assertEquals(1, UnknownCount);
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

    public void testBounds(){
        Window window = new Window();
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
