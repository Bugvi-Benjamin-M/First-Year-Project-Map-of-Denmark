package Controller;

import Helpers.OSDetector;
import Model.Model;
import View.MapCanvas;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class CanvasController extends Controller implements Observer {

    private static final double ZOOM_FACTOR = 0.9;
    private static final double KEYBOARD_ZOOM_IN_FACTOR = -1.3;
    private static final double KEYBOARD_ZOOM_OUT_FACTOR = 1.3;

    private static MapCanvas mapCanvas;
    private static Model model;
    private static CanvasController instance;
    private Point2D lastMousePosition;
    private CanvasInteractionHandler handler;

    private CanvasController(Window window) {
        super(window);
        model = Model.getInstance();
        model.addObserver(this);
        mapCanvas = new MapCanvas(window.getDimension(), theme);
        mapCanvas.setWayElements(model.getWayElements());
        addCanvasInteractionHandler();
        window.addComponent(BorderLayout.CENTER,mapCanvas);
    }

    public static CanvasController getInstance(Window window) {
        if(instance == null) {
            instance = new CanvasController(window);
        }
        return instance;
    }

    private void addCanvasInteractionHandler() {
        handler = new CanvasInteractionHandler();
        specifyKeyBindings();
    }

    private void specifyKeyBindings() {
        handler.addKeyBinding(KeyEvent.VK_PLUS, OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardZoomEvent(KEYBOARD_ZOOM_IN_FACTOR);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_MINUS, OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardZoomEvent(KEYBOARD_ZOOM_OUT_FACTOR);
            }
        });
    }

    public static void adjustToBounds() {
        mapCanvas.pan(-model.getMinLongitude(), -model.getMaxLatitude());
        mapCanvas.zoom(mapCanvas.getWidth()/(model.getMaxLongitude()- model.getMinLongitude()));
    }

    /**
     * This method returns the mapCanvas used by the MapCanvasController. It is only used for
     * testing purposes.
     * @return the mapCanvas instance
     *
     */
    public MapCanvas getMapCanvas(){
        return mapCanvas;
    }

    public static void resetBounds(){
        mapCanvas.resetTransform();
    }

    @Override
    public void update(Observable o, Object arg) {

        mapCanvas.repaint();
    }

    private void mousePressedEvent(MouseEvent event) {
        lastMousePosition = event.getPoint();
    }

    private void mouseDraggedEvent(MouseEvent event) {
        Point2D currentMousePosition = event.getPoint();
        double dx = currentMousePosition.getX() - lastMousePosition.getX();
        double dy = currentMousePosition.getY() - lastMousePosition.getY();
        mapCanvas.pan(dx, dy);
        lastMousePosition = currentMousePosition;
    }

    private void mouseWheelMovedEvent(MouseWheelEvent event) {
        double factor = Math.pow(ZOOM_FACTOR, event.getWheelRotation());
        Point2D currentMousePosition = event.getPoint();
        double dx = currentMousePosition.getX();
        double dy = currentMousePosition.getY();
        mapCanvas.pan(-dx, -dy);
        mapCanvas.zoom(factor);
        mapCanvas.pan(dx, dy);
    }

    private void keyboardZoomEvent(double keyboardZoomFactor) {
        double dx = mapCanvas.getVisibleRect().getWidth()/2;
        double dy = mapCanvas.getVisibleRect().getHeight()/2;
            mapCanvas.pan(-dx, -dy);
            mapCanvas.zoom(Math.pow(ZOOM_FACTOR, keyboardZoomFactor));
            mapCanvas.pan(dx, dy);
    }

    private class CanvasInteractionHandler extends MouseAdapter {

        private CanvasInteractionHandler() {
            mapCanvas.addMouseListener(this);
            mapCanvas.addMouseMotionListener(this);
            mapCanvas.addMouseWheelListener(this);
        }

        private void addKeyBinding(int key, int activationKey, AbstractAction event) {
            mapCanvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                    put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            mapCanvas.getActionMap().put(event.toString(), event);
        }


        @Override
        public void mousePressed(MouseEvent e) {
            mousePressedEvent(e);
        }


        @Override
        public void mouseDragged(MouseEvent e) {
            mouseDraggedEvent(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            mouseWheelMovedEvent(e);
        }
    }
}
