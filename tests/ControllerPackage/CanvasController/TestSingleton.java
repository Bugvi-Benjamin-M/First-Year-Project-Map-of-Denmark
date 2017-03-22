package ControllerPackage.CanvasController;

import Controller.CanvasController;
import Controller.MainWindowController;
import Helpers.FileHandler;
import Model.Model;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertTrue;

/**
 * Created by Búgvi Magnussen on 22-03-2017.
 */
public class TestSingleton {

    @Test
    public void TestSingleton() {
        Model.getInstance();
        CanvasController canvasController = CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        CanvasController canvasController2 = CanvasController.getInstance(MainWindowController.getInstance().getWindow());
        try {
            FileHandler.loadDefault("/testRoad.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertTrue(canvasController.equals(canvasController2));

        Model.getInstance().resetInstance();
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).resetInstance();
    }


}
