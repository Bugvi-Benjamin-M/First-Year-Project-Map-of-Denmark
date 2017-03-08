package Controller;

import Model.*;
import View.MapCanvas;
import View.Window;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakob on 06-03-2017.
 */
public class CanvasController extends Controller implements Observer {
    private Window window;
    private MapCanvas mapCanvas;

    public CanvasController(Window window) {
        this.window = window;
        Model.getInstance().addObserver(this);
        mapCanvas = new MapCanvas(window.getDimension());
        window.addComponent(BorderLayout.CENTER,mapCanvas);
    }

    @Override
    public void update(Observable o, Object arg) {
        mapCanvas.resetShapes();
        Model model = (Model)o;
        java.util.List<Shape> roads = model.getRoads();
        mapCanvas.addShapes(roads);
        mapCanvas.repaint();
    }
}
