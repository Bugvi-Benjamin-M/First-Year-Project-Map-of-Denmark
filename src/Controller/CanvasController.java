package Controller;

import Model.Coastlines.CoastlineFactory;
import Model.Model;
import Helpers.OSDetector;
import Model.*;
import View.MapCanvas;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class CanvasController extends Controller implements Observer {

    private static final double ZOOM_FACTOR = 0.9;
    private static final double KEYBOARD_ZOOM_FACTOR = 2.0;
    private static final double PAN_FACTOR = 38.5;

    private enum PanType {
        LEFT,
        RIGHT,
        UP,
        DOWN
    }

    private static MapCanvas mapCanvas;
    private static Model model;
    private static CanvasController instance;

    private static CoastlineController coastlineController;

    private Point2D lastMousePosition;
    private CanvasInteractionHandler handler;

    private CanvasController(Window window) {
        super(window);
        model = Model.getInstance();
        model.addObserver(this);

        coastlineController = CoastlineController.getInstance();

        mapCanvas = new MapCanvas(window.getDimension(), theme);
        mapCanvas.setWayElements(model.getWayElements());
        mapCanvas.setCoastlines(coastlineController.getCoastlinePaths());
        addInteractionHandlerToCanvas();
        window.addComponent(BorderLayout.CENTER,mapCanvas);
    }

    public static CanvasController getInstance(Window window) {
        if(instance == null) {
            instance = new CanvasController(window);
        }
        return instance;
    }

    private void addInteractionHandlerToCanvas() {
        handler = new CanvasInteractionHandler(JComponent.WHEN_IN_FOCUSED_WINDOW);
        mapCanvas.addMouseListener(handler);
        mapCanvas.addMouseMotionListener(handler);
        mapCanvas.addMouseWheelListener(handler);
        specifyKeyBindings();
    }

    private void specifyKeyBindings() {
        handler.addKeyBinding(KeyEvent.VK_PLUS, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardZoomEvent(-KEYBOARD_ZOOM_FACTOR);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_MINUS, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardZoomEvent(KEYBOARD_ZOOM_FACTOR);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_UP, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.UP);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_DOWN, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.DOWN);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.LEFT);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.RIGHT);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD6, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.RIGHT);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD2, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.DOWN);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD4, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.LEFT);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD8, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panEvent(PanType.UP);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_ADD, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardZoomEvent(-KEYBOARD_ZOOM_FACTOR);
            }
        });
        handler.addKeyBinding(KeyEvent.VK_SUBTRACT, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keyboardZoomEvent(KEYBOARD_ZOOM_FACTOR);
            }
        });
    }

    private void panEvent(PanType type) {
        double dx = 0.0;
        double dy = 0.0;
        switch (type) {
            case DOWN:
                dx = 0;
                dy = -PAN_FACTOR;
                break;
            case UP:
                dx = 0;
                dy = PAN_FACTOR;
                break;
            case LEFT:
                dx = PAN_FACTOR;
                dy = 0;
                break;
            case RIGHT:
                dx = -PAN_FACTOR;
                dy = 0;
                break;
        }
        mapCanvas.pan(dx, dy);
    }

    public static void adjustToBounds() {
        mapCanvas.pan(-model.getMinLongitude(), -model.getMaxLatitude());
        mapCanvas.zoom(mapCanvas.getWidth()/(model.getMaxLongitude()- model.getMinLongitude()));
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

    private void mouseClickedEvent(MouseEvent event) {
        Point2D mousePosition = event.getPoint();
        Point2D mouseInModel = mapCanvas.toModelCoords(mousePosition);
        System.out.println(mouseInModel.getX() + " " + mouseInModel.getY());
        //ArrayList<Element> elements = Model.getInstance().getBst().getSection(mouseInModel.getX(), mouseInModel.getY());
        //mapCanvas.setCurrentSection(elements);
        mapCanvas.setCurrentPoint(mouseInModel);
        ArrayList<Element> elements = Model.getInstance().getBst().getManySections(mouseInModel.getX(), mouseInModel.getY(), mouseInModel.getX() + 0.3, mouseInModel.getY() + 0.3);
        mapCanvas.setCurrentSection(elements);
        mapCanvas.repaint();
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

        private int specifiedFocus;

        private CanvasInteractionHandler(int specifiedFocus) {
            this.specifiedFocus = specifiedFocus;
        }

        private void addKeyBinding(int key, int activationKey, AbstractAction event) {
            mapCanvas.getInputMap(specifiedFocus).
                    put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            mapCanvas.getActionMap().put(event.toString(), event);
        }


        @Override
        public void mouseClicked(MouseEvent e){
            mouseClickedEvent(e);
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

    public MapCanvas getMapCanvas(){
        return mapCanvas;
    }

    public void resetInstance() {
        instance = null;
    }

}
