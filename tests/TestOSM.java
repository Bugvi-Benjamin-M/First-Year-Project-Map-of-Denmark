import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Model.Model;
import OSM.OSMHandler;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TestOSM {

    private Model model;
    private CanvasController canvasController;
    private WindowController mainWindowController;
    private OSMHandler OSMHandler;

    @Before
    public void buildUp()
    {
        model = Model.getInstance();
        mainWindowController = MainWindowController.getInstance();
        canvasController = CanvasController.getInstance();
        OSMHandler = OSMHandler.getInstance();

        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
        OSMHandler.resetInstance();
        System.gc();
        model = Model.getInstance();
        mainWindowController = MainWindowController.getInstance();
        canvasController = CanvasController.getInstance();
        OSMHandler = OSMHandler.getInstance();
    }

    @Test
    public void filler()
    {
        assertEquals(true, true);
    }

}
