package Controller;

import Enums.BoundType;
import Enums.OSMEnums.ElementType;
import Enums.ZoomLevel;
import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import Model.Elements.Element;
import Model.Elements.POI;
import Model.Elements.Road;
import Model.Elements.SuperElement;
import Model.Model;
import View.CanvasPopup;
import View.MapCanvas;
import View.PopupWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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
    private static final int POPUP_XOFFSET = 20;
    private static final int POPUP_YOFFSET = 10;
    private CanvasPopup popup;

    private final int POPUP_DELAY = 1100;
    private static final int ANTIALIASING_ZOOM_DELAY = 150;
    private static final int COASTLINES_UPDATE_DELAY = 150;
    private final int ANTIALIASING_RESIZE_DELAY = 150;
    private static final float MINIMUM_ACCEPTED_DISTANCE = 0.09f;
    private Timer toolTipTimer;
    private Timer antiAliasingZoomTimer;
    private static Timer coastLinesUpdateTimer;
    private Timer antiAliasingResizeTimer;


    private enum PanType { LEFT,
        RIGHT,
        UP,
        DOWN }

    private static MapCanvas mapCanvas;
    private static Model model;
    private static CanvasController instance;

    private Point2D lastMousePosition;
    private CanvasInteractionHandler handler;
    private CanvasFocusHandler focusHandler;
    private static double zoom_value;
    private HashSet<Element> roads;

    private Cursor crossCursor;
    private Cursor normalCursor;

    private CanvasController()
    {
        super();
    }

    public static CanvasController getInstance() {
        if (instance == null) {
            instance = new CanvasController();
        }
        return instance;
    }

    public void markLocation(float x, float y){
        mapCanvas.setLocationMarker(new Point2D.Float(x, y));
        mapCanvas.panToPoint(new Point2D.Float(x, y));
    }

    public void resizeEvent()
    {
        mapCanvas.revalidate();
        if (PreferencesController.getInstance().getAntiAliasingSetting()) {
            mapCanvas.toggleAntiAliasing(false);
            repaintCanvas();
        }
        if (PreferencesController.getInstance().getAntiAliasingSetting()) {
            if (antiAliasingResizeTimer == null) {
                antiAliasingResizeTimer = new Timer(ANTIALIASING_RESIZE_DELAY, a -> {
                    if (a.getSource() == antiAliasingResizeTimer) {
                        antiAliasingResizeTimer.stop();
                        antiAliasingResizeTimer = null;
                        mapCanvas.toggleAntiAliasing(true);
                        repaintCanvas();
                    }
                });
                antiAliasingResizeTimer.start();
            } else antiAliasingResizeTimer.restart();
        }
    }

    public void disablePopup()
    {
        if (PreferencesController.getInstance()
                .getCanvasRealTimeInformationSetting()) {
            if (popup != null) {
                if (popup.getPopupMenu() != null)
                    popup.hidePopupMenu();
            }
            popup = null;
        }

    }

    public void setupAsObserver() {
        model = Model.getInstance();
        model.addObserver(this);
    }

    public void setupCanvas() {
        mapCanvas = new MapCanvas();
        mapCanvas.setPreferredSize(new Dimension(
                window.getFrame().getWidth(),
                window.getFrame().getHeight() - GlobalValue.getToolbarHeight()));
        updateCanvasElements();
        mapCanvas.setCoastLines(model.getCoastlines());
        alignCanvasAndModel();
        mapCanvas.toggleAntiAliasing(
                PreferencesController.getInstance().getAntiAliasingSetting());
        repaintCanvas();
        addInteractionHandlerToCanvas();
        addFocusHandlerToCanvas();
        toggleKeyBindings();
        crossCursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
        normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        changeCanvasMouseCursorToNormal();
    }

    public void updateCanvasElements(){
        mapCanvas.setElements(model.getElements());
    }

    public void updateCanvasPOI(){
        mapCanvas.setPOIs(model.getPointsOfInterest());
    }

    private void addInteractionHandlerToCanvas()
    {
        handler = new CanvasInteractionHandler(JComponent.WHEN_FOCUSED);
        mapCanvas.addMouseListener(handler);
        mapCanvas.addMouseMotionListener(handler);
        mapCanvas.addMouseWheelListener(handler);
        specifyKeyBindings();
    }

    private void addFocusHandlerToCanvas()
    {
        focusHandler = new CanvasFocusHandler();
        mapCanvas.addFocusListener(focusHandler);
    }

    private void specifyKeyBindings()
    {
        handler.addKeyBinding(KeyEvent.VK_PLUS, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    keyboardZoomEvent(-KEYBOARD_ZOOM_FACTOR);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_MINUS, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    keyboardZoomEvent(KEYBOARD_ZOOM_FACTOR);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_UP, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.UP);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_DOWN, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.DOWN);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.LEFT);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.RIGHT);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD6, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.RIGHT);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD2, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.DOWN);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD4, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.LEFT);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_NUMPAD8, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    panEvent(PanType.UP);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_ADD, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    keyboardZoomEvent(-KEYBOARD_ZOOM_FACTOR);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_SUBTRACT, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    keyboardZoomEvent(KEYBOARD_ZOOM_FACTOR);
                }
            });
        handler.addKeyBinding(KeyEvent.VK_0, Helpers.OSDetector.getActivationKey(),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    CanvasController.adjustToBounds();
                    Model.getInstance().modelHasChanged();
                }
            });
        handler.addKeyBinding(KeyEvent.VK_L, Helpers.OSDetector.getActivationKey(),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        if(GlobalValue.isAddNewPointActive()){
                            MainWindowController.getInstance().requestPoiModeOff();
                        }else {
                            MainWindowController.getInstance().requestPoiModeOn();
                        }
                    }
                });
    }

    private void poiCreationEvent(Point2D point){
        double x = point.getX();
        double y = point.getY();
        point = new Point2D.Double(x, y);
        Point2D p = mapCanvas.toModelCoords(point);
        String description = PopupWindow.textInputBox(null, "Point of Interest", "Enter description: ");
        if(description != null){
            if(description.length() <= 56) {
                POI poi = new POI((float) p.getX(), (float) p.getY(), description);
                model.addPOI(poi);
                mapCanvas.setPOIs(model.getPointsOfInterest());
                MainWindowController.getInstance().requestAddPOI(poi);
                repaintCanvas();
                MainWindowController.getInstance().requestUpdatePointsOfInterestBar();
                MainWindowController.getInstance().requestPoiModeOff();
            }else{
                PopupWindow.warningBox(null, "The Description has to be no Longer than 56 Characters!");
            }
        } else MainWindowController.getInstance().requestPoiModeOff();
    }

    private void panEvent(PanType type)
    {
        disablePopup();
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
        panEvent(dx, dy);
    }

    private void panEvent(double dx, double dy)
    {
        double width = mapCanvas.getVisibleRect().width;
        double height = mapCanvas.getVisibleRect().height;
        double canvasX = width / 2;
        double canvasY = height / 2;

        mapCanvas.pan(dx, dy);

        Point2D newCenterPoint = mapCanvas.toModelCoords(new Point2D.Double(canvasX, canvasY));

        if (newCenterPoint.getX() < model.getMinLongitude(true)
                || newCenterPoint.getX() > model.getMaxLongitude(true)
                || newCenterPoint.getY() < model.getMaxLatitude(true)
                || newCenterPoint.getY() > model.getMinLatitude(true)) {
            mapCanvas.pan(-dx, -dy);
        }
        repaintCanvas();
    }

    public static void adjustToBounds()
    {
        mapCanvas.pan(-model.getMinLongitude(false), -model.getMaxLatitude(false));
        double factor = mapCanvas.getWidth() / (model.getMaxLongitude(false) - model.getMinLongitude(false));
        mapCanvas.zoom(factor);
        repaintCanvas();
    }

    public static void adjustToDynamicBounds()
    {
        double distancetoreach = 0;
        double currentdistance = 1;
        resetBounds();
        zoom_value = 0.0;
        GlobalValue.setZoomLevel(0.0);
        double factor = mapCanvas.getWidth() / (model.getMaxLongitude(false) - model.getMinLongitude(false));
        mapCanvas.pan(-model.getMinLongitude(true), -model.getMaxLatitude(true));
        mapCanvas.zoom(factor);
        while (distancetoreach < currentdistance) {
            Rectangle2D rect = mapCanvas.getVisibleRect();
            distancetoreach = model.getMaxLongitude(true) - model.getMinLongitude(true);
            Point2D leftcorner = mapCanvas.toModelCoords(new Point2D.Double(rect.getX(), rect.getY()));
            Point2D rightcorner = mapCanvas.toModelCoords(new Point2D.Double(
                rect.getX() + rect.getWidth(), rect.getY() + rect.getHeight()));
            currentdistance = rightcorner.getX() - leftcorner.getX();
            mapCanvas.zoom(Math.pow(ZOOM_FACTOR, -1));
            changeZoomLevel(+10);
        }
        repaintCanvas();
        GlobalValue.setMaxZoom(ZoomLevel.getZoomFactor() - 50);
    }

    public void panToPoint(Point.Float aFloat) {
        mapCanvas.panToPoint(aFloat);
    }

    public static void repaintCanvas() {
        if (mapCanvas != null) {
            quickRepaint();
            if(coastLinesUpdateTimer == null) {
                coastLinesUpdateTimer = new Timer(COASTLINES_UPDATE_DELAY, a -> {
                    if(a.getSource() == coastLinesUpdateTimer) {
                        coastLinesUpdateTimer.stop();
                        coastLinesUpdateTimer = null;
                        mapCanvas.setCoastLines(model.getCoastlines());
                        quickRepaint();
                    }
                });
                coastLinesUpdateTimer.start();
            } else coastLinesUpdateTimer.restart();

        }

    }

    private static void quickRepaint() {
        if(mapCanvas != null) {
            mapCanvas.repaint();
            alignCanvasAndModel();
            }

        }

    private static void alignCanvasAndModel() {
        mapCanvas.setMaxLat(model.getMaxLatitude(false));
        mapCanvas.setMaxLon(model.getMaxLongitude(false));
        mapCanvas.setMinLat(model.getMinLatitude(false));
        mapCanvas.setMinLon(model.getMinLongitude(false));
        mapCanvas.setDynMaxLat(model.getMaxLatitude(true));
        mapCanvas.setDynMaxLon(model.getMaxLongitude(true));
        mapCanvas.setDynMinLat(model.getMinLatitude(true));
        mapCanvas.setDynMinLon(model.getMinLongitude(true));
        model.setCameraBound(BoundType.MIN_LONGITUDE, mapCanvas.getCameraMinLon());
        model.setCameraBound(BoundType.MAX_LONGITUDE, mapCanvas.getCameraMaxLon());
        model.setCameraBound(BoundType.MAX_LATITUDE, mapCanvas.getCameraMaxLat());
        model.setCameraBound(BoundType.MIN_LATITUDE, mapCanvas.getCameraMinLat());
    }

    public static void resetBounds() { if(mapCanvas != null) mapCanvas.resetTransform(); }

    @Override
    public void update(Observable o, Object arg)
    {
        repaintCanvas();
    }

    private void mousePressedEvent(MouseEvent event)
    {
        if(mapCanvas.hasFocus()) {
            lastMousePosition = event.getPoint();
            if (PreferencesController.getInstance().getAntiAliasingSetting()) {
                mapCanvas.toggleAntiAliasing(false);
                repaintCanvas();
            }
        }
    }

    private void mouseClickedEvent(MouseEvent event)
    {
        if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
        MainWindowController.getInstance().requestSearchToolCloseList();
        mapCanvas.grabFocus();
        Point2D mousePosition = event.getPoint();
        Point2D mouseInModel = mapCanvas.toModelCoords(mousePosition);
        mapCanvas.setCurrentPoint(mouseInModel);
        if(GlobalValue.isAddNewPointActive()){
            if(SwingUtilities.isLeftMouseButton(event)) {
                poiCreationEvent(event.getPoint());
            }
            else if(SwingUtilities.isRightMouseButton(event)){
                GlobalValue.setIsAddNewPointActive(false);
                MainWindowController.getInstance().requestPoiModeOff();
            }
        }
        popupActivation(event);
    }

    private void mouseDraggedEvent(MouseEvent event)
    {
        if(mapCanvas.hasFocus()) {
            disablePopup();
            Point2D currentMousePosition = event.getPoint();
            double dx = currentMousePosition.getX() - lastMousePosition.getX();
            double dy = currentMousePosition.getY() - lastMousePosition.getY();
            panEvent(dx, dy);
            lastMousePosition = currentMousePosition;
        }
    }

    private void mouseWheelMovedEvent(MouseWheelEvent event) {
        if (mapCanvas.hasFocus()) {
            disablePopup();
            if (PreferencesController.getInstance().getAntiAliasingSetting()) {
                mapCanvas.toggleAntiAliasing(false);
                repaintCanvas();
            }
            double wheel_rotation = event.getPreciseWheelRotation();
            double factor = Math.pow(ZOOM_FACTOR, wheel_rotation);
            Point2D currentMousePosition = event.getPoint();
            double dx = currentMousePosition.getX();
            double dy = currentMousePosition.getY();

            double increase = -wheel_rotation * 10;
            zoomEvent(dx, dy, increase, factor);
            if (PreferencesController.getInstance().getAntiAliasingSetting()) {
                if (antiAliasingZoomTimer == null) {
                    antiAliasingZoomTimer = new Timer(ANTIALIASING_ZOOM_DELAY, a -> {
                        if (a.getSource() == antiAliasingZoomTimer) {
                            antiAliasingZoomTimer.stop();
                            antiAliasingZoomTimer = null;
                            mapCanvas.toggleAntiAliasing(true);
                            repaintCanvas();
                        }
                    });
                    antiAliasingZoomTimer.start();
                } else antiAliasingZoomTimer.restart();
            }
        }
    }

    private void mouseMovedEvent(MouseEvent e)
    {
        if(GlobalValue.isAddNewPointActive()) changeCanvasMouseCursorToPoint();
        else changeCanvasMouseCursorToNormal();
        popupActivation(e);
        /*if (PreferencesController.getInstance()
                .getCanvasRealTimeInformationSetting()) {
            if (mapCanvas.hasFocus()) {
                disablePopup();
                popup = new CanvasPopup();
                popup.setLocation((int)e.getLocationOnScreen().getX() + POPUP_XOFFSET,
                        (int)e.getLocationOnScreen().getY() + POPUP_YOFFSET);
                /*setPopupContent(e);
                if (popup.getPopupMenu().getComponentCount() == 0) {
                    disablePopup();
                    return;
                }*/
                /*if (toolTipTimer == null) {
                    toolTipTimer = new Timer(POPUP_DELAY, a -> {
                        if (a.getSource() == toolTipTimer) {
                            toolTipTimer.stop();
                            toolTipTimer = null;
                            if (popup != null) {
                                setPopupContent(e);
                                if (popup.getPopupMenu().getComponentCount() == 0) {
                                    disablePopup();
                                    return;
                                }
                                popup.showPopupMenu();
                                popup.startDismissTimer();
                            }
                        }
                    });
                    toolTipTimer.start();
                } else {
                    toolTipTimer.restart();
                    popup.stopDismissTimer();
                }
            }
        }*/
    }

    private void mouseReleasedEvent(MouseEvent e) {
        if(mapCanvas.hasFocus()) {
            if (PreferencesController.getInstance().getAntiAliasingSetting()) {
                mapCanvas.toggleAntiAliasing(true);
                repaintCanvas();
            }
            popupActivation(e);
        }
    }

    private void popupActivation(MouseEvent e) {
        if (PreferencesController.getInstance()
                .getCanvasRealTimeInformationSetting()) {
            if (mapCanvas.hasFocus()) {
                disablePopup();
                popup = new CanvasPopup();
                popup.setLocation((int) e.getLocationOnScreen().getX() + POPUP_XOFFSET,
                        (int) e.getLocationOnScreen().getY() + POPUP_YOFFSET);

                /*setPopupContent(e);
                if (popup.getPopupMenu().getComponentCount() == 0) {
                    disablePopup();
                    return;
                }*/
                if (toolTipTimer == null) {
                    toolTipTimer = new Timer(POPUP_DELAY, a -> {
                        if (a.getSource() == toolTipTimer) {
                            toolTipTimer.stop();
                            toolTipTimer = null;
                            if (popup != null) {
                                setPopupContent(e);
                                if (popup.getPopupMenu().getComponentCount() == 0) {
                                    disablePopup();
                                    return;
                                }
                                popup.showPopupMenu();
                                popup.startDismissTimer();
                            }
                        }
                    });
                    toolTipTimer.start();
                } else {
                    toolTipTimer.restart();
                    popup.stopDismissTimer();
                }
            }
        }
    }

    private void setPopupContent(MouseEvent event)
    {
        Point2D point2D = mapCanvas.toModelCoords(event.getPoint());

        Road road = calculateNearestNeighbour((float)point2D.getX(), (float)point2D.getY());
        if (road == null)
            return;
        String content = road.getName();
        AffineTransform transform = new AffineTransform();
        FontRenderContext context = new FontRenderContext(transform, true, true);
        Font font = new Font("Verdana", Font.PLAIN, 12);
        int textWidth = (int)(font.getStringBounds(content, context).getWidth());
        JLabel label = new JLabel(content);
        label.setForeground(ThemeHelper.color("canvasPopupForeground"));
        label.setBackground(ThemeHelper.color("canvasPopupBackground"));
        label.setSize(textWidth + 15, 25);
        popup.setSize(label.getWidth(), label.getHeight());
        label.setVisible(true);
        popup.addToPopup(label);
    }

    private void mouseExitedEvent(MouseEvent e) {
        disablePopup();
        if(GlobalValue.isAddNewPointActive()) changeCanvasMouseCursorToNormal();
    }

    private void mouseEnteredEvent(MouseEvent e) {
        MainWindowController.getInstance().requestPointsOfInterestBarRepaint();
        MainWindowController.getInstance().requestToolbarRepaint();
        if(!MainWindowController.getInstance().doesSearchToolHaveFocus())mapCanvas.grabFocus();
        if(GlobalValue.isAddNewPointActive()) changeCanvasMouseCursorToPoint();
    }

    private void keyboardZoomEvent(double keyboardZoomFactor)
    {
        disablePopup();
        double dx = mapCanvas.getVisibleRect().getWidth() / 2;
        double dy = mapCanvas.getVisibleRect().getHeight() / 2;
        double increase = -keyboardZoomFactor * 10;

        zoomEvent(dx, dy, increase, Math.pow(ZOOM_FACTOR, keyboardZoomFactor));
    }

    private static void zoomEvent(double dx, double dy, double increase,
        double zoomFactor)
    {
        if ((zoom_value > GlobalValue.getMaxZoom() || increase > 0) && (zoom_value < 700 || increase < 0)) {
            mapCanvas.pan(-dx, -dy);
            mapCanvas.zoom(zoomFactor);
            changeZoomLevel(increase);
            CanvasExtrasController.getInstance().updateDistance();
            mapCanvas.pan(dx, dy);
            repaintCanvas();
        }
    }

    private static void changeZoomLevel(double zoomFactor)
    {
        if (zoomFactor != 0.0) {
            zoom_value += zoomFactor;
            GlobalValue.setZoomLevel(zoom_value);
        }
    }

    public static Road calculateNearestNeighbour(float x, float y) {
        ArrayList<HashSet<SuperElement>> roads = getNearestNeighbourOfAllRoads(x, y);

        float minDist = 1000;
        Road e = null;
        for(HashSet<SuperElement> set : roads){
            for (SuperElement element : set) {
            Road r = (Road) element;
            if (r.getShape() != null && r.getShape().distTo(new Point2D.Float(x, y)) < minDist) {
                if (!r.getName().equals("")) {
                    e = r;
                    minDist = (float) r.getShape().distTo(new Point2D.Float(x, y));
                    }
                }
            }
        }

        if (minDist > MINIMUM_ACCEPTED_DISTANCE)
            return null;
        if (e != null) {
            return e;
        } else
            return null;
    }

    private static ArrayList<HashSet<SuperElement>> getNearestNeighbourOfAllRoads(float x, float y){
        ArrayList<HashSet<SuperElement>> roads = new ArrayList<>();
        roads.add(getNearestNeighbour(ElementType.PRIMARY_ROAD, x, y));
        roads.add(getNearestNeighbour(ElementType.SECONDARY_ROAD, x, y));
        roads.add(getNearestNeighbour(ElementType.MOTORWAY, x, y));
        roads.add(getNearestNeighbour(ElementType.MOTORWAY_LINK, x, y));
        roads.add(getNearestNeighbour(ElementType.TERTIARY_ROAD, x, y));
        roads.add(getNearestNeighbour(ElementType.TERTIARY_ROAD_LINK, x, y));
        roads.add(getNearestNeighbour(ElementType.TRUNK_ROAD, x, y));
        roads.add(getNearestNeighbour(ElementType.TERTIARY_ROAD_LINK, x, y));
        roads.add(getNearestNeighbour(ElementType.UNCLASSIFIED_ROAD, x, y));
        roads.add(getNearestNeighbour(ElementType.RESIDENTIAL_ROAD, x, y));
        roads.add(getNearestNeighbour(ElementType.LIVING_STREET, x, y));
        roads.add(getNearestNeighbour(ElementType.SERVICE_ROAD, x, y));
        roads.add(getNearestNeighbour(ElementType.BUS_GUIDEWAY, x, y));
        roads.add(getNearestNeighbour(ElementType.ESCAPE, x, y));
        roads.add(getNearestNeighbour(ElementType.RACEWAY, x, y));
        roads.add(getNearestNeighbour(ElementType.PEDESTRIAN_STREET, x, y));
        roads.add(getNearestNeighbour(ElementType.TRACK, x, y));
        roads.add(getNearestNeighbour(ElementType.STEPS, x, y));
        roads.add(getNearestNeighbour(ElementType.FOOTWAY, x, y));
        roads.add(getNearestNeighbour(ElementType.BRIDLEWAY, x, y));
        roads.add(getNearestNeighbour(ElementType.CYCLEWAY, x, y));
        roads.add(getNearestNeighbour(ElementType.PATH, x, y));
        roads.add(getNearestNeighbour(ElementType.ROAD, x, y));

        return roads;
    }

    private static HashSet<SuperElement> getNearestNeighbour(ElementType type, float x, float y){
        return model.getElements()
                .get(type)
                .getManySections(x - 1f, y - 1f, x + 1f, y + 1f);
    }

    public void themeHasChanged()
    {
        disablePopup();
        popup = new CanvasPopup();
        mapCanvas.setBackgroundColor();
        mapCanvas.revalidate();
        repaintCanvas();
    }

    public void toggleKeyBindings()
    {
        for (Object key : mapCanvas.getActionMap().keys()) {
            mapCanvas.getActionMap().get(key).setEnabled(
                PreferencesController.getInstance().getKeyBindingsSetting());
        }
    }

    public void toggleAntiAliasing()
    {
        mapCanvas.toggleAntiAliasing(
            PreferencesController.getInstance().getAntiAliasingSetting());
    }

    public void changeCanvasMouseCursorToPoint() {
        if(mapCanvas != null) {
            mapCanvas.setCursor(crossCursor);
        }
    }

    public void changeCanvasMouseCursorToNormal() {
        if(mapCanvas != null) {
            mapCanvas.setCursor(normalCursor);
        }
    }

    public MapCanvas getMapCanvas() { return mapCanvas; }

    public void resetInstance() { instance = null; }

    private class CanvasInteractionHandler extends MouseAdapter {

        private int specifiedFocus;
        private CanvasInteractionHandler(int specifiedFocus)
        {
            this.specifiedFocus = specifiedFocus;
        }

        private void addKeyBinding(int key, int activationKey,
            AbstractAction event)
        {
            mapCanvas.getInputMap(specifiedFocus)
                .put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            mapCanvas.getActionMap().put(event.toString(), event);
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            mouseClickedEvent(e);
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            mousePressedEvent(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseReleasedEvent(e);
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            mouseDraggedEvent(e);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            mouseWheelMovedEvent(e);
        }

        @Override
        public void mouseMoved(MouseEvent e)
        {
            mouseMovedEvent(e);
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            mouseExitedEvent(e);
        }

        @Override
        public void mouseEntered(MouseEvent e) {mouseEnteredEvent(e);}
    }

    private class CanvasFocusHandler extends FocusAdapter {

        @Override
        public void focusLost(FocusEvent e)
        {
            disablePopup();
            mapCanvas.toggleAntiAliasing(PreferencesController.getInstance().getAntiAliasingSetting());
            repaintCanvas();
        }
    }
}
