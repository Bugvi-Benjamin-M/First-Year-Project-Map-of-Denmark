import Controller.CanvasController;
import Controller.InfobarController;
import Controller.ToolbarController;
import Enums.ToolType;
import Helpers.FileHandler;
import Model.Model;
import View.MapCanvas;
import View.ToolFeature;
import View.Toolbar;
import View.Window;
import junit.framework.TestCase;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;

/**
 * Created by Nik on 13/03/17.
 */
public class TestView extends TestCase {

    public void testListeners(){
        Window window = new Window();
        Model model = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        ToolbarController toolbarController = ToolbarController.getInstance(window);
        InfobarController infobarController = new InfobarController(window);
        try {
            Method method = CanvasController.class.getDeclaredMethod("getMapCanvas", new Class[] {});
            method.setAccessible(true);
            MapCanvas canvas = (MapCanvas) method.invoke(canvasController, new Object[]{});
            assertEquals(1, canvas.getListeners(MouseListener.class).length);
            Toolbar toolbar = toolbarController.getToolbar();
            assertEquals(1, canvas.getListeners(MouseListener.class).length);

            JPanel loadtool = toolbar.getTool(ToolType.LOAD);
            assertEquals(1, loadtool.getListeners(MouseListener.class).length);

            JPanel savetool = toolbar.getTool(ToolType.SAVE);
            assertEquals(1, savetool.getListeners(MouseListener.class).length);
            method.setAccessible(false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
