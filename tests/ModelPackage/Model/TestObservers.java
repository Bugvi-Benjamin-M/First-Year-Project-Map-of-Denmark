package ModelPackage.Model;

import Controller.CanvasController;
import Controller.MainWindowController;
import Model.Model;
import org.junit.Test;

import java.util.Calendar;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestObservers {

    @Test
    public void testObservers() {
        Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        assertEquals(1, Model.getInstance().countObservers());
        Model.getInstance().resetInstance();
        canvasController.resetInstance();
        MainWindowController.getInstance().resetInstance();
    }
}
