package ControllerPackage;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Helpers.FileHandler;
import Model.Model;
import View.MapCanvas;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestCanvasController {


    @Before
    public void buildUp() {
        Model.getInstance().resetInstance();
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).resetInstance();
        MainWindowController.getInstance().resetInstance();
    }

    @Test
    public void TestSingleton() {
        Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        CanvasController canvasController2 = CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        try {
            FileHandler.loadResource("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(canvasController.equals(canvasController2));
    }

    @Test
    public void testCanvasListener() {
        Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        MapCanvas canvas = canvasController.getMapCanvas();
        assertEquals(1, canvas.getListeners(MouseListener.class).length);
        assertEquals(1, canvas.getListeners(MouseWheelListener.class).length);
        assertEquals(1, canvas.getListeners(MouseMotionListener.class).length);
    }

    @Test
    public void testGetCanvas() {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance(MainWindowController.getInstance().getWindow()).getMapCanvas();
        assertNotNull(canvas);
        assertTrue(canvas instanceof JPanel);
    }

    @Test
    public void testAdjustToBounds() {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance(MainWindowController.getInstance().getWindow()).getMapCanvas();
        canvas.getVisibleRect().contains(2,2); //Todo Check if point is on screen
        //Todo implement properly
        //Need to find a way to test this properly
    }



    @Test
    public void testResetBounds() {
        //Todo implement properly
        //Maybe consider Robot class
        //Transform is always identity, need to find a valid way to test this methof
    }

    @Test
    public void testUpdate() {

        //Todo implement properly
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance(MainWindowController.getInstance().getWindow()).getMapCanvas();
        CanvasController canvasController = CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        canvasController.update(Model.getInstance(), null);
    }

    @Test
    public void testMousePressed() {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance(MainWindowController.getInstance().getWindow()).getMapCanvas();
        MouseListener[] listener = canvas.getMouseListeners();
       // MouseEvent event = new MouseEvent(canvas, )

        //tODO continue work here, create new mouse event. Maybe use robot class
        //listener[0].mousePressed();


    }

    @Test
    public void testMouseDragged() {
        //Todo implement properly
    }

    @Test
    public void testMouseWheelMoved() {
        //Todo implement properly
    }

    @Test
    public void testNumberOfKeyBindings() {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance(MainWindowController.getInstance().getWindow()).getMapCanvas();
        assertEquals(12, canvas.getRegisteredKeyStrokes().length);
    }

    @Test
    public void testActionKeyBindings() {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance(MainWindowController.getInstance().getWindow()).getMapCanvas();
        ActionMap actionMap = canvas.getActionMap();
        InputMap inputMap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        List<String> inputMapKeyValues = new ArrayList<>();
        for(KeyStroke s : inputMap.keys()) {
            inputMapKeyValues.add((String) inputMap.get(s));
        }
        for(String s : inputMapKeyValues) {
            assertTrue(actionMap.get(s).isEnabled());
        }
    }
}
