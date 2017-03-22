package ModelPackage.Model;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.OSMEnums.WayType;
import org.junit.Test;
import Model.Model;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestGetWayElements {

    @Test
    public void testGetWayElements() {

        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        model.clear();
        assertTrue(model.getWayElements() != null);
        assertEquals(model.getWayElements().size(), WayType.values().length);

        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }
}
