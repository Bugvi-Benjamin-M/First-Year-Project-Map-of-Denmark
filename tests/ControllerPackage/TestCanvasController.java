package ControllerPackage;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Model.Model;
import View.MapCanvas;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestCanvasController {

    @Before
    public void buildUp()
    {
        Model.getInstance().resetInstance();
        CanvasController.getInstance().resetInstance();
        MainWindowController.getInstance().resetInstance();
    }

    @Test
    public void TestSingleton()
    {
        Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance();
        CanvasController canvasController2 = CanvasController.getInstance();
        assertTrue(canvasController.equals(canvasController2));
    }

    @Test
    public void testCanvasListener()
    {
        Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance();
        MapCanvas canvas = canvasController.getMapCanvas();
        assertEquals(1, canvas.getListeners(MouseListener.class).length);
        assertEquals(1, canvas.getListeners(MouseWheelListener.class).length);
        assertEquals(1, canvas.getListeners(MouseMotionListener.class).length);
    }

    @Test
    public void testGetCanvas()
    {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance().getMapCanvas();
        assertNotNull(canvas);
        assertTrue(canvas instanceof JPanel);
    }

    @Test
    public void testAdjustToBounds()
    {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance().getMapCanvas();
        canvas.getVisibleRect().contains(2, 2);
    }

    @Test
    public void testNumberOfKeyBindings()
    {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance().getMapCanvas();
        assertEquals(12, canvas.getRegisteredKeyStrokes().length);
    }

    @Test
    public void testActionKeyBindings()
    {
        Model.getInstance();
        MapCanvas canvas = CanvasController.getInstance().getMapCanvas();
        ActionMap actionMap = canvas.getActionMap();
        InputMap inputMap = canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        List<String> inputMapKeyValues = new ArrayList<>();
        for (KeyStroke s : inputMap.keys()) {
            inputMapKeyValues.add((String)inputMap.get(s));
        }
        for (String s : inputMapKeyValues) {
            assertTrue(actionMap.get(s).isEnabled());
        }
    }
}
