package ModelPackage.Model;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Helpers.FileHandler;
import org.junit.Test;
import Model.Model;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestSingleton {

    @Test
    public void testSingleton() {
        Model model = Model.getInstance();
        Model model2 = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(model.equals(model2));

        model.resetInstance();
        model2.resetInstance();
        mainWindowController.resetInstance();
        canvasController.resetInstance();
    }

}
