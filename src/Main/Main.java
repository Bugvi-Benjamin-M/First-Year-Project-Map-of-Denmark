package Main;

import Controller.*;
import Helpers.FileHandler;
import Model.Model;

import java.io.FileNotFoundException;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Main {


    public static void main(String[] args) {

        Model.getInstance();

        WindowController windowController = MainWindowController.getInstance();

        CanvasController.getInstance(windowController.getWindow());
        ToolbarController.getInstance(windowController.getWindow());
        InfobarController.getInstance(windowController.getWindow());
        try {
            FileHandler.loadDefault("/defaultosm.osm");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
