import Controller.*;
import Enums.ToolType;
import Model.Model;
import View.MapCanvas;
import View.Toolbar;
import junit.framework.TestCase;
import org.junit.Test;

import javax.swing.*;
import java.awt.event.MouseListener;

/**
 * Created by Nik on 13/03/17.
 */
public class TestView extends TestCase {

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


    @Test
    public void testCanvasListener() {
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        MapCanvas canvas = canvasController.getMapCanvas();
        assertEquals(1, canvas.getListeners(MouseListener.class).length);

        model.resetInstance();
        mainWindowController.resetInstance();
        canvasController.resetInstance();
        System.gc();
    }

}
