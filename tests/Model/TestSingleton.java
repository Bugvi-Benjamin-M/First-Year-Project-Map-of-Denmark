package Model;

import Controller.CanvasController;
import Helpers.FileHandler;
import View.Window;
import org.junit.Test;

import java.awt.*;
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
        Window window = new Window().title("TESTING")
                .dimension(new Dimension(1, 1))
                .layout(new BorderLayout())
                .show();

        Model model = Model.getInstance();
        Model model2 = Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(window);
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(model.equals(model2));

        model.resetInstance();
        model2.resetInstance();
        canvasController.resetInstance();
    }

}
