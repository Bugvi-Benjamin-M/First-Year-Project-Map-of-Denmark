package ControllerPackage;

import Model.Model;
import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.ToolbarControllers.ToolbarController;
import Controller.WindowController;
import Enums.ToolType;
import View.Toolbar;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseListener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 23/03/2017
 */
public class TestToolbarController {

    @Test
    public void test() {
        assertEquals(true, true);
    }

    @Before
    public void buildUp() {
        Model.getInstance().resetInstance();
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).resetInstance();
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).resetInstance();
        MainWindowController.getInstance().resetInstance();
    }

    @Test
    public void testToolListeners(){
        Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController.getInstance(mainWindowController.getWindow());
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
    public void testSingleton() {
        Model.getInstance();
        CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        ToolbarController toolbarController = ToolbarController.getInstance(MainWindowController.getInstance().getWindow());
        ToolbarController toolbarController2 = ToolbarController.getInstance(MainWindowController.getInstance().getWindow());
        assertTrue(toolbarController.equals(toolbarController2));
    }




}
