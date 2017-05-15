package Controller;

import Enums.BoundType;
import Enums.OSMEnums.ElementType;
import Enums.TravelType;
import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import Model.Elements.POI;
import Model.Elements.Road;
import Model.Elements.RoadEdge;
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


public final class CanvasController extends Controller  {

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

    private Cursor crossCursor;
    private Cursor normalCursor;

    /**
     * Private constructor, used by getInstance.
     */
    private CanvasController()
    {
        super();
    }

    /**
     * Returns the singleton object to the client.
     * @return the singleton object
     */
    public static CanvasController getInstance() {
        if (instance == null) {
            instance = new CanvasController();
        }
        return instance;
    }

    /**
     * Marks a point on the map canvas.
     * @param address the point to be marked.
     */
    public void markLocation(Point2D.Float address){
        mapCanvas.setLocationMarker(address);
        mapCanvas.panToPoint(address);
    }

    /**
     * Toggles whether the last saved route should be displayed on the map canvas.
     * @param isActive display the last saved route.
     */
    public void toggleRouteVisualization(boolean isActive){
        mapCanvas.toggleRouteVisualization(isActive);
    }

    /**
     * Resets the saved route.
     */
    public void resetRoute(){
        mapCanvas.resetRoute();
    }

    /**
     * Adapts the map canvas to the size of the main window.
     * Manipulates antialiasing to be turned off while the window is being resized.
     */
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

    /**
     * Disables the canvas popup.
     */
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

    /**
     * Setup the map canvas and adds elements to it.
     */
    public void setupCanvas() {
        mapCanvas = new MapCanvas();
        model = Model.getInstance();
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

    /**
     * Updates the map canvas' visual elements.
     */
    public void updateCanvasElements(){
        mapCanvas.setElements(model.getElements());
    }

    /**
     * Updates the map canvas' points of interest.
     */
    public void updateCanvasPOI(){
        mapCanvas.setPOIs(model.getPointsOfInterest());
    }

    /**
     * Updates the to and from points related to route search.
     * @param to the to destination point.
     * @param from the from destination point.
     */
    public void updateToAndFrom(Point2D.Float to, Point2D.Float from){
        mapCanvas.setToMarker(to);
        mapCanvas.setFromMarker(from);
        panToPoint(from);
    }

    /**
     * Adds an InteractionHandler to canvas. The interaction handler is a MouseAdapter
     * that handles mouse input as well as keybindings.
     */
    private void addInteractionHandlerToCanvas()
    {
        handler = new CanvasInteractionHandler(JComponent.WHEN_FOCUSED);
        mapCanvas.addMouseListener(handler);
        mapCanvas.addMouseMotionListener(handler);
        mapCanvas.addMouseWheelListener(handler);
        specifyKeyBindings();
    }

    /**
     * Adds a FocusHandler to the map canvas. The FocusHandler, is a FocusAdapter. It
     * handles focus events related to the map canvas.
     */
    private void addFocusHandlerToCanvas() {
        focusHandler = new CanvasFocusHandler();
        mapCanvas.addFocusListener(focusHandler);
    }

    /**
     * Resets the location point on the map canvas that was added as
     * part of the last address search.
     */
    public void canvasResetLocationMarker(){
        mapCanvas.resetLocationMarker();
    }

    /**
     * Add a route to the map canvas.
     * @param path the route to be added to the map canvas.
     */
    public void canvasSetRoute(Iterable<RoadEdge> path) {
        mapCanvas.setRoute(path);
    }

    /**
     * Specify all keybindings for the map canvas.
     */
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
                    adjustToBounds();
                    repaintCanvas();
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

    /**
     * Adds a new point of interest to the map canvas.
     * @param point the point to be added.
     */
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

    /**
     * Pans the map canvas in the direction given by the pantype argument. This method
     * is meant to be used as part of keyboard panning.
     * @param type the direction to pan.
     */
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

    /**
     * Pans the canvas in the direction given by the parameters. This method is meant
     * to be used as part of mouse panning.
     * @param dx the difference in x-coordinates.
     * @param dy the difference in y coordinates.
     */
    private void panEvent(double dx, double dy)
    {
        double width = mapCanvas.getVisibleRect().width;
        double height = mapCanvas.getVisibleRect().height;
        double canvasX = width / 2;
        double canvasY = height / 2;

        mapCanvas.pan(dx, dy);

        Point2D newCenterPoint = mapCanvas.toModelCoords(new Point2D.Double(canvasX, canvasY));

        if (newCenterPoint.getX() < model.getMinLongitude(false)
                || newCenterPoint.getX() > model.getMaxLongitude(false)
                || newCenterPoint.getY() < model.getMaxLatitude(false)
                || newCenterPoint.getY() > model.getMinLatitude(false)) {
            mapCanvas.pan(-dx, -dy);
        }
        repaintCanvas();
    }

    /**
     * Adjusts the bounds of the map canvas to the Denmark wide map.
     */
    public static void adjustToBounds()
    {
        mapCanvas.pan(-model.getMinLongitude(false), -model.getMaxLatitude(false));
        double factor = mapCanvas.getWidth() / (model.getMaxLongitude(false) - model.getMinLongitude(false));
        mapCanvas.zoom(factor);
        repaintCanvas();
    }

    /**
     * Adjusts the bounds of the map canvas to the new bounds in a loaded file.
     */
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
    }

    /**
     * Pans the map canvas to a given point.
     * @param aFloat the point to be panned to.
     */
    public void panToPoint(Point.Float aFloat) {
        mapCanvas.panToPoint(aFloat);
    }


    /**
     * Repaints the map canvas. Utilises a timer, to optimise when
     * coastline elements are updated.
     * Updates all variables in map canvas and alligns it with the model.
     */
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

    /**
     * Repaints the canvas without updating coastlines.
     */
    private static void quickRepaint() {
        if(mapCanvas != null) {
            mapCanvas.repaint();
            alignCanvasAndModel();
            }

        }

    /**
     * Aligns the map canvas with the model.
     * Updates all latitude and longitude values in the map canvas.
     * Updates the camera bounds in the model.
     */
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

    /**
     * Resets the bounds on the map canvas.
     */
    public static void resetBounds() { if(mapCanvas != null) mapCanvas.resetTransform(); }

    /**
     * Handles mouse pressed events. Transfers focus to the map canvas
     * and repaints the map canvas.
     * Manipulates anti-aliasing in such a way, that it is turned off
     * while the mouse key is pressed down.
     * @param event the mouse pressed event.
     */
    private void mousePressedEvent(MouseEvent event)
    {
        mapCanvas.grabFocus();
            lastMousePosition = event.getPoint();
            if (PreferencesController.getInstance().getAntiAliasingSetting()) {
                mapCanvas.toggleAntiAliasing(false);
                repaintCanvas();
            }
    }

    /**
     * Handles mouse clicked events.
     * Closes the Menu tool popup.
     * Closes all search lists that are visible in the application.
     * Transfers focus to the map canvas.
     * Sets the current mouse position as current point.
     * If add new POI mode is active, adds the point the map canvas.
     * Cancels add new POI mode if right button is clicked.
     * Activates the map canvas popup.
     * @param event the mouse clicked event.
     */
    private void mouseClickedEvent(MouseEvent event)
    {
        if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
        MainWindowController.getInstance().requestSearchToolCloseList();
        MainWindowController.getInstance().requestJourneyPlannerCloseSearchLists();
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

    /**
     * Handles mouse dragged events.
     * If the map canvas does not have focus, the event is ignored.
     * Disables the map canvas popup.
     * Registers the mouse position as current point.
     * Initiates panning, calculating the distance to pan based on the last mouse position and current
     * mouse position.
     * @param event the mouse dragged event.
     */
    private void mouseDraggedEvent(MouseEvent event)
    {
        if(mapCanvas.hasFocus()) {
            disablePopup();
            Point2D currentMousePosition = event.getPoint();
            if(lastMousePosition != null) {
                double dx = currentMousePosition.getX() - lastMousePosition.getX();
                double dy = currentMousePosition.getY() - lastMousePosition.getY();
                panEvent(dx, dy);
            }
            lastMousePosition = currentMousePosition;
        }
    }

    /**
     * Handles mouse wheel moved events.
     * If the map canvas does not have focus the event is ignored.
     * Disables the map canvas popup.
     * Manipulates anti-antialiasing in such a way, that it is turned off
     * while zooming is in progress.
     * Uses the mouse wheel rotation to zoom in on the map canvas.
     * Repaints the map canvas for each zoom increment.
     * @param event the mouse wheel moved event.
     */
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

    /**
     * Handles mouse moved events.
     * If add new POI mode is active, the cursor is changed to crosshair.
     * otherwise it is changed to normal cursor.
     * Activates the map canvas popup.
     * @param e the mouse moved event.
     */
    private void mouseMovedEvent(MouseEvent e)
    {
        if(GlobalValue.isAddNewPointActive()) changeCanvasMouseCursorToPoint();
        else changeCanvasMouseCursorToNormal();
        popupActivation(e);
    }

    /**
     * Handles the mouse released event.
     * if the map canvas does not have focus, this event is ignored.
     * Activates anti-aliasing if anti-aliasing setting is selected.
     * Activates the map canvas popup.
     * @param e the mouse released event.
     */
    private void mouseReleasedEvent(MouseEvent e) {
        if(mapCanvas.hasFocus()) {
            if (PreferencesController.getInstance().getAntiAliasingSetting()) {
                mapCanvas.toggleAntiAliasing(true);
                repaintCanvas();
            }
            popupActivation(e);
        }
    }

    /**
     * Activates the map canvas popup.
     * The location of the popup is calculated based on the location of
     * the mouse.
     * Displays the popup after a short delay.
     * if the application is in loading mode, the popup is simply disabled.
     * @param e the mouse event that triggered this method call.
     */
    private void popupActivation(MouseEvent e) {
        if(GlobalValue.isLoading()) {
            disablePopup();
            return;
        }
        if (PreferencesController.getInstance()
                .getCanvasRealTimeInformationSetting()) {
            if (mapCanvas.hasFocus()) {
                disablePopup();
                popup = new CanvasPopup();
                popup.setLocation((int) e.getLocationOnScreen().getX() + POPUP_XOFFSET,
                        (int) e.getLocationOnScreen().getY() + POPUP_YOFFSET);
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

    /**
     * Determines the map canvas popup content.
     * Uses the mouse event given as parameter to determine the location of the popup
     * and to calculate the nearest road name. The nearest road name is then added
     * to the popup.
     * Sets the colors of the popup.
     * @param event the mouse event that triggered the popup activation.
     */
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

    /**
     * Handles mouse exited events.
     * Disables the map canvas popup and changes the cursor to normal if add
     * new POI mode is active.
     * @param e the mouse exited event.
     */
    private void mouseExitedEvent(MouseEvent e) {
        disablePopup();
        if(GlobalValue.isAddNewPointActive()) changeCanvasMouseCursorToNormal();
    }

    /**
     * Handles mouse entered events.
     * Repaints the Points of Interest bar.
     * Repaints the toolbar.
     * Transfers focus to the map canvas if no searchbar popup lists are visible.
     * changes the cursor to crosshair if add new POI mode is active.
     * @param e the mouse entered event.
     */
    private void mouseEnteredEvent(MouseEvent e) {
        MainWindowController.getInstance().requestPointsOfInterestBarRepaint();
        MainWindowController.getInstance().requestToolbarRepaint();
        if(!MainWindowController.getInstance().doesSearchToolHaveFocus() && !MainWindowController.getInstance().doesJourneyPlannerSearchHaveFocus()) mapCanvas.grabFocus();

        if(GlobalValue.isAddNewPointActive()) changeCanvasMouseCursorToPoint();
    }

    /**
     * Initiates zoom on the map based on keyboard input.
     * Uses the visible rectangle of the map canvas to determine the zoom
     * point.
     * @param keyboardZoomFactor the factor that determines the the visual zoom change and the factor to which the current zoom level gets
     *                           adjusted to.
     */
    private void keyboardZoomEvent(double keyboardZoomFactor)
    {
        disablePopup();
        double dx = mapCanvas.getVisibleRect().getWidth() / 2;
        double dy = mapCanvas.getVisibleRect().getHeight() / 2;
        double increase = -keyboardZoomFactor * 10;

        zoomEvent(dx, dy, increase, Math.pow(ZOOM_FACTOR, keyboardZoomFactor));
    }

    /**
     * Zooms the map canvas in relation to the given coordinates and zoom factor.
     * Adjust the zoom level.
     * @param dx the x-coordinate of the zoom point.
     * @param dy the y-coordinate of the zoom point.
     * @param increase the factor by which the current zoom level gets adjusted to.
     * @param zoomFactor the factor that determines the the visual zoom change
     */
    private static void zoomEvent(double dx, double dy, double increase,
        double zoomFactor)
    {
        if ((zoom_value > -50 || increase > 0) && (zoom_value < 700 || increase < 0)) {
            mapCanvas.pan(-dx, -dy);
            mapCanvas.zoom(zoomFactor);
            changeZoomLevel(increase);
            CanvasExtrasController.getInstance().updateDistance();
            mapCanvas.pan(dx, dy);
            repaintCanvas();
        }
    }

    /**
     * Adjusts the zoom level by the given parameter and sets the global zoom level
     * value.
     * @param zoomFactor the factor by which the zoom level is adjusted.
     */
    private static void changeZoomLevel(double zoomFactor)
    {
        if (zoomFactor != 0.0) {
            zoom_value += zoomFactor;
            GlobalValue.setZoomLevel(zoom_value);
        }
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
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

    public static Road calculateNearestNeighbour(float x, float y, TravelType type) {
        ArrayList<HashSet<SuperElement>> roads = getNearestNeighbourOfAllRoads(x, y);

        float minDist = 1000;
        Road e = null;
        for(HashSet<SuperElement> set : roads){
            for (SuperElement element : set) {
            Road r = (Road) element;
            if (r.getShape() != null && r.getShape().distTo(new Point2D.Float(x, y)) < minDist) {
                if(type == TravelType.VEHICLE && !r.isTravelByCarAllowed()){
                    continue;
                }
                if(type == TravelType.BICYCLE && !r.isTravelByBikeAllowed()){
                    continue;
                }
                if(type == TravelType.WALK && !r.isTravelByFootAllowed()){
                    continue;
                }


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

    /**
     * Returns a collection containing all road objects.
     * @param x the x value of the point performing a nearest neighbour search on.
     * @param y the y value of the point performing a nearest neighbour search on.
     * @return a collection of all roads in the data set.
     */
    private static ArrayList<HashSet<SuperElement>> getNearestNeighbourOfAllRoads(float x, float y){
        ArrayList<HashSet<SuperElement>> roads = new ArrayList<>();

        roads.add(getNearestNeighbour(ElementType.HIGHWAY, x, y));
        return roads;
    }

    /**
     * Returns element objects within a given rectangle.
     * @param type The element type to
     * @param x the x value of the point performing a nearest neighbour search on.
     * @param y the y value of the point performing a nearest neighbour search on.
     * @return
     */
    private static HashSet<SuperElement> getNearestNeighbour(ElementType type, float x, float y){
        return model.getElements()
                .get(type)
                .getManySections(x - 1f, y - 1f, x + 1f, y + 1f);
    }

    /**
     * Disables the map canvas popup.
     * Changes the colors to the current theme on the map canvas.
     * Repaints the map canvas.
     */
    public void themeHasChanged()
    {
        disablePopup();
        popup = new CanvasPopup();
        mapCanvas.setBackgroundColor();
        mapCanvas.revalidate();
        repaintCanvas();
    }

    /**
     * Toggles keybindings on the map canvas based on the
     * current user setting.
     */
    public void toggleKeyBindings()
    {
        for (Object key : mapCanvas.getActionMap().keys()) {
            mapCanvas.getActionMap().get(key).setEnabled(
                PreferencesController.getInstance().getKeyBindingsSetting());
        }
    }

    /**
     * Toggles anti-aliasing on the map canvas based on the current
     * user setting.
     */
    public void toggleAntiAliasing()
    {
        mapCanvas.toggleAntiAliasing(
            PreferencesController.getInstance().getAntiAliasingSetting());
    }

    /**
     * Changes the map canvas cursor to crosshair cursor.
     * If the map canvas is null, the method call is ignored.
     */
    public void changeCanvasMouseCursorToPoint() {
        if(mapCanvas != null) {
            mapCanvas.setCursor(crossCursor);
        }
    }

    /**
     * Changes the map canvas cursor to normal cursor.
     * If the map canvas is null, the method call is ignored.
     */
    public void changeCanvasMouseCursorToNormal() {
        if(mapCanvas != null) {
            mapCanvas.setCursor(normalCursor);
        }
    }

    /**
     * Returns the map canvas to the client.
     * @return the map canvas.
     */
    public MapCanvas getMapCanvas() { return mapCanvas; }

    /**
     * Resets the singleton instance.
     * This method is designed for testing purposes.
     */
    public void resetInstance() { instance = null; }

    /**
     * The CanvasInteractionHandler registers all mouse inputs and assigns
     * keybindings to the map canvas. The CanvasInteractionHandler is a MouseAdapter.
     */
    private class CanvasInteractionHandler extends MouseAdapter {

        private int specifiedFocus;

        /**
         * Creates the CanvasInteractionHandler. The given parameter specifies focus requirement for the
         * keybindings and when they can be activated.
         * @param specifiedFocus
         */
        private CanvasInteractionHandler(int specifiedFocus)
        {
            this.specifiedFocus = specifiedFocus;
        }

        /**
         * Adds a keybinding to the map canvas, based on the specifiedFocus.
         * @param key they main keybinding
         * @param activationKey the key to be pressed in order to use main key binding.
         * @param event the action event to be triggered by the keybinding.
         */
        private void addKeyBinding(int key, int activationKey,
            AbstractAction event)
        {
            mapCanvas.getInputMap(specifiedFocus)
                .put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            mapCanvas.getActionMap().put(event.toString(), event);
        }

        /**
         * Registers a mouse clicked event and notifies the CanvasController.
         * @param e the mouse clicked event.
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            mouseClickedEvent(e);
        }

        /**
         * Registers a mouse pressed event and notifies the CanvasController.
         * @param e the mouse pressed event.
         */
        @Override
        public void mousePressed(MouseEvent e)
        {
            mousePressedEvent(e);
        }

        /**
         * Registers a mouse released event and notifies the CanvasController.
         * @param e the mouse released event.
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            mouseReleasedEvent(e);
        }

        /**
         * Registers a mouse dragged event and notifies the CanvasController.
         * @param e the mouse dragged event.
         */
        @Override
        public void mouseDragged(MouseEvent e)
        {
            mouseDraggedEvent(e);
        }

        /**
         * Registers a mouse wheel moved event and notifies the CanvasController.
         * @param e the mouse wheel moved event.
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
            mouseWheelMovedEvent(e);
        }

        /**
         * Registers a mouse moved event and notifies the CanvasController.
         * @param e the mouse moved event.
         */
        @Override
        public void mouseMoved(MouseEvent e)
        {
            mouseMovedEvent(e);
        }

        /**
         * Registers a mouse exited event and notifies the CanvasController.
         * @param e the mouse exited event.
         */
        @Override
        public void mouseExited(MouseEvent e)
        {
            mouseExitedEvent(e);
        }

        /**
         * Registers a mouse entered event and notifies the CanvasController.
         * @param e the mouse entered event.
         */
        @Override
        public void mouseEntered(MouseEvent e) {mouseEnteredEvent(e);}
    }

    /**
     * The CanvasFocusHandler, is a FocusAdapter that handles focus events
     * that relate to the map canvas.
     */
    private class CanvasFocusHandler extends FocusAdapter {

        /**
         * Disables the map canvas popup.
         * Activates anti-aliasing if it is selected by the user.
         * Repaints the map canvas.
         * @param e the focus lost event
         */
        @Override
        public void focusLost(FocusEvent e)
        {
            disablePopup();
            mapCanvas.toggleAntiAliasing(PreferencesController.getInstance().getAntiAliasingSetting());
            repaintCanvas();
        }
    }
}
