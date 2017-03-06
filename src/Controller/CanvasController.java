package Controller;

import View.MapCanvas;
import View.Window;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakob on 06-03-2017.
 */
public class CanvasController extends Controller {
    private Window window;
    private MapCanvas mapCanvas;

    public CanvasController(Window window) {
        this.window = window;
        mapCanvas = new MapCanvas(window.getDimension());
        window.addComponent(BorderLayout.CENTER,mapCanvas);
    }

    public void setObserver(Observable observer) {
        mapCanvas.setObserver(observer);
    }
}
