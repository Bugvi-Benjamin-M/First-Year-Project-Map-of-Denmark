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
import Enums.ToolType;
import java.awt.event.MouseListener;
import javax.swing.*;
import View.*;
import Controller.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestView {

    @Before
    public void buildUp(){
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        ToolbarController toolbarController = ToolbarController.getInstance(mainWindowController.getWindow());
        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
        toolbarController.resetInstance();
    }

    @Test
    public void testToolListeners(){
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        ToolbarController toolbarController = ToolbarController.getInstance(mainWindowController.getWindow());
        Toolbar toolbar = toolbarController.getToolbar();
        JPanel loadtool = toolbar.getTool(ToolType.LOAD);
        assertEquals(1, loadtool.getListeners(MouseListener.class).length);
        JPanel savetool = toolbar.getTool(ToolType.SAVE);
        assertEquals(1, savetool.getListeners(MouseListener.class).length);
        JPanel settingsTool = toolbar.getTool(ToolType.SETTINGS);
        assertEquals(1, settingsTool.getListeners(MouseListener.class).length);

    }

    @Test
    public void testCanvasListener() {
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        MapCanvas canvas = canvasController.getMapCanvas();
        assertEquals(1, canvas.getListeners(MouseListener.class).length);

    }
}
