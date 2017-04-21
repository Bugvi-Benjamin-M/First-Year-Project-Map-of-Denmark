package Controller;

import Enums.OSMEnums.ElementType;
import Enums.ZoomLevel;
import Helpers.GlobalValue;
import Model.Elements.Element;
import Model.Elements.Road;
import Model.Model;
import View.MapCanvas;
import View.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Jakob on 06-03-2017.
 */
public final class CanvasController extends Controller implements Observer {

    private static final double ZOOM_FACTOR = 0.9;
    private static final double KEYBOARD_ZOOM_FACTOR = 1.0;
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

    private Point2D lastMousePosition;
    private CanvasInteractionHandler handler;
    private static double zoom_value;
    private HashSet<Element> roads;

    private CanvasController() {
        super();
        model = Model.getInstance();
        model.addObserver(this);
    }

    public static CanvasController getInstance() {
        if(instance == null) {
            instance = new CanvasController();
        }
        return instance;
    }

    public void resizeEvent() {
        mapCanvas.revalidate();
        mapCanvas.repaint();
    }

    public void setupCanvas() {
        mapCanvas = new MapCanvas();
        mapCanvas.setPreferredSize(new Dimension(window.getFrame().getWidth(), window.getFrame().getHeight() - GlobalValue.getToolbarWidth()));
        mapCanvas.setElements(model.getElements());
        //mapCanvas.setCoastlines(model.getCoastlines());
        addInteractionHandlerToCanvas();
    }

    private void addInteractionHandlerToCanvas() {
        handler = new CanvasInteractionHandler(JComponent.WHEN_FOCUSED);
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
        handler.addKeyBinding(KeyEvent.VK_0, Helpers.OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CanvasController.adjustToBounds();
                Model.getInstance().modelHasChanged();
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
        mapCanvas.pan(-model.getMinLongitude(false), -model.getMaxLatitude(false));
        double factor = mapCanvas.getWidth()/(model.getMaxLongitude(false)- model.getMinLongitude(false));
        mapCanvas.zoom(factor);
    }

    public static void adjustToDynamicBounds(){
        ZoomLevel.resetZoomFactor();
        zoom_value = 0;
        double distancetoreach = 0;
        double currentdistance = 1;
        resetBounds();
        double factor = mapCanvas.getWidth()/(model.getMaxLongitude(false)- model.getMinLongitude(false));
        mapCanvas.pan(- model.getMinLongitude(true), -model.getMaxLatitude(true));
        mapCanvas.zoom(factor);
        while(distancetoreach < currentdistance) {
            Rectangle2D rect = mapCanvas.getVisibleRect();
            distancetoreach = model.getMaxLongitude(true) - model.getMinLongitude(true);
            Point2D leftcorner = mapCanvas.toModelCoords(new Point2D.Double(rect.getX(), rect.getY()));
            Point2D rightcorner = mapCanvas.toModelCoords(new Point2D.Double(rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));
            currentdistance = rightcorner.getX() - leftcorner.getX();
            mapCanvas.zoom(Math.pow(ZOOM_FACTOR, -1));
            mapCanvas.repaint();
            changeZoomLevel(-1);
        }
    }

    public static void resetBounds(){
        mapCanvas.resetTransform();
    }

    @Override
    public void update(Observable o, Object arg) {
        mapCanvas.repaint();
    }

    private void mousePressedEvent(MouseEvent event) {
        mapCanvas.grabFocus();
        lastMousePosition = event.getPoint();
    }

    private void mouseClickedEvent(MouseEvent event) {
        mapCanvas.grabFocus();
        Point2D mousePosition = event.getPoint();
        Point2D mouseInModel = mapCanvas.toModelCoords(mousePosition);
        mapCanvas.setCurrentPoint(mouseInModel);
        calculateNearestNeighbour((float) mouseInModel.getX(), (float) mouseInModel.getY());
    }

    private void mouseDraggedEvent(MouseEvent event) {
        mapCanvas.grabFocus();
        Point2D currentMousePosition = event.getPoint();
        double dx = currentMousePosition.getX() - lastMousePosition.getX();
        double dy = currentMousePosition.getY() - lastMousePosition.getY();
        mapCanvas.pan(dx, dy);
        lastMousePosition = currentMousePosition;
    }

    private void mouseWheelMovedEvent(MouseWheelEvent event) {
        mapCanvas.grabFocus();
        double wheel_rotation = event.getPreciseWheelRotation();
        double factor = Math.pow(ZOOM_FACTOR, wheel_rotation);
        Point2D currentMousePosition = event.getPoint();
        double dx = currentMousePosition.getX();
        double dy = currentMousePosition.getY();

        double increase = -wheel_rotation*10;
        if ((zoom_value > -30 || increase > 0) &&
                (zoom_value < 700 || increase < 0)) {
            mapCanvas.pan(-dx, -dy);
            mapCanvas.zoom(factor);
            changeZoomLevel(increase);
            mapCanvas.pan(dx, dy);
        }
    }

    private void keyboardZoomEvent(double keyboardZoomFactor) {
        double dx = mapCanvas.getVisibleRect().getWidth()/2;
        double dy = mapCanvas.getVisibleRect().getHeight()/2;
        double increase = -keyboardZoomFactor*10;

        if ((zoom_value > -30 || increase > 0) &&
                (zoom_value < 700 || increase < 0)) {
            mapCanvas.pan(-dx, -dy);
            mapCanvas.zoom(Math.pow(ZOOM_FACTOR, keyboardZoomFactor));
            changeZoomLevel(increase);
            mapCanvas.pan(dx, dy);
        }
    }

    private static void changeZoomLevel(double zoomFactor) {
        if (zoomFactor != 0.0) {
            zoom_value += zoomFactor;
            GlobalValue.setZoomLevel(zoom_value);
        }
    }

    private void calculateNearestNeighbour(float x, float y){
        roads = model.getElements().get(ElementType.HIGHWAY).getManySections(x - 1f, y - 1f, x + 1f, y + 1f);
        float minDist = 1000;
        Road e = null;
        for(Element element : roads){
            Road r = (Road) element;
            if(r.getShape().distTo(new Point2D.Float(x, y)) < minDist){
                if(!r.getName().equals("")) {
                    e = r;
                    minDist = (float) r.getShape().distTo(new Point2D.Float(x, y));
                }
            }
        }
        if(e != null)  {
            new PopupWindow().infoBox(null, e.getName(), "Nearest Road");
        }
    }

    public void themeHasChanged() {
        mapCanvas.revalidate();
        mapCanvas.repaint();
    }

    public void toggleKeyBindings(boolean status) {
        for(Object key : mapCanvas.getActionMap().keys()) {
            mapCanvas.getActionMap().get(key).setEnabled(status);
        }
    }

    public void toggleAntiAliasing(boolean status) {
        mapCanvas.toggleAntiAliasing(status);
    }

    public MapCanvas getMapCanvas(){
        return mapCanvas;
    }

    public void resetInstance() {
        instance = null;
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

}
