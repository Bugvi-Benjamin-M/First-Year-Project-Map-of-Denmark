package View;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Model.Model;
import org.junit.Test;

import java.awt.event.MouseListener;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestCanvasListener {

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
