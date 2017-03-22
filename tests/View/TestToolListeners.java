package View;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.ToolbarController;
import Controller.WindowController;
import Enums.ToolType;
import Model.Model;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseListener;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestToolListeners {

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

        model.resetInstance();
        canvasController.resetInstance();
        toolbarController.resetInstance();
        mainWindowController.resetInstance();
        System.gc();
    }
}
