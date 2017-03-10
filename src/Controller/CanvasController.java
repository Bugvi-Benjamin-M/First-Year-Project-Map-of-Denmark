package Controller;

import Enums.WayType;
import Helpers.Constant;
import Model.*;
import View.MapCanvas;
import View.Window;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakob on 06-03-2017.
 */
public class CanvasController extends Controller implements Observer {
    private Window window;
    private static MapCanvas mapCanvas;
    private static Model model;
    private static float zoomfactor;

    public CanvasController(Window window) {
        this.window = window;
        model = Model.getInstance();
        model.addObserver(this);
        mapCanvas = new MapCanvas(window.getDimension());
        new CanvasMouseAdapter();
        window.addComponent(BorderLayout.CENTER,mapCanvas);
    }

    public static void adjustToBounds() {
        zoomfactor = mapCanvas.getWidth()/(model.getMaxLongitude()- model.getMinLongitude());
        mapCanvas.pan(-model.getMinLongitude(), -model.getMaxLatitude());
        mapCanvas.zoom(mapCanvas.getWidth()/(model.getMaxLongitude()- model.getMinLongitude()));
    }

    public static void resetBounds(){
        mapCanvas.resetTransform();
    }

    @Override
    public void update(Observable o, Object arg) {
        mapCanvas.resetShapes();

        java.util.List<Shape> roads = new ArrayList<>();
        java.util.List<Element> roadElements = model.getWayElements(WayType.ROAD);
        mapCanvas.addShapes(roads);
        mapCanvas.repaint();
    }

    private class CanvasMouseAdapter extends MouseAdapter {

        private Point2D lastMousePosition;

        public CanvasMouseAdapter() {
            mapCanvas.addMouseListener(this);
            mapCanvas.addMouseMotionListener(this);
            mapCanvas.addMouseWheelListener(this);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            lastMousePosition = e.getPoint();
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            Point2D currentMousePosition = e.getPoint();
            double dx = currentMousePosition.getX() - lastMousePosition.getX();
            double dy = currentMousePosition.getY() - lastMousePosition.getY();
            mapCanvas.pan(dx, dy);
            lastMousePosition = currentMousePosition;
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double factor = Math.pow(Constant.getZOOM_FACTOR(), e.getWheelRotation());
            Point2D currentMousePosition = e.getPoint();
            double dx = currentMousePosition.getX();
            double dy = currentMousePosition.getY();
            mapCanvas.pan(-dx, -dy);
            mapCanvas.zoom(factor);
            mapCanvas.pan(dx, dy);
        }
    }
}
