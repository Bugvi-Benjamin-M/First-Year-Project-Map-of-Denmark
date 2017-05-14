package Controller;

import Controller.ToolbarControllers.ToolbarController;
import Enums.ToolType;
import Enums.ToolbarType;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import Model.Elements.POI;
import Model.Elements.Road;
import Model.Elements.RoadEdge;
import Model.Model;
import View.DistanceScalerView;
import View.PopupWindow;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

/**
 * Created by Búgvi Magnussen on 14-03-2017.
 * This class is the controller for the main window of the application.
 * It deals with setting up the main window and all the components in it.
 * The MainWindowController also acts as a connection between other controllers.
 */
public final class MainWindowController extends WindowController {

    private static final String MAIN_TITLE = "OSM Map Viewer v1.0";
    private final int FROM_RESIZE_EVENT_TO_MINIMUMWIDTH = 70;
    private static MainWindowController instance;
    private JLayeredPane layeredPane;
    private Timer inSlideTimer;
    private final int SLIDE_DELAY = 10;
    private final int PIXELS_TO_MOVE = 10;

    private boolean isSliding;

    /**
     * private constructor, called by getInstance.
     */
    private MainWindowController() { super(); }

    /**
     * Returns the singleton instance of the MainWindowController.
     * @return the singleton
     */
    public static MainWindowController getInstance()
    {
        if (instance == null) {
            instance = new MainWindowController();
        }
        return instance;
    }

    /**
     * Sets up the main window of the application.
     */
    public void setupMainWindow()
    {
        window = new Window()
                     .title(MAIN_TITLE)
                     .closeOperation(WindowConstants.EXIT_ON_CLOSE)
                     .dimension(new Dimension(1200, 1000))
                     .extendedState(JFrame.MAXIMIZED_BOTH)
                     .relativeTo(null)
                     .layout(new BorderLayout())
                     .icon()
                     .hide();
        //Todo, optimise window height minimum
        window.setMinimumWindowSize(new Dimension(ToolbarController.getSmallLargeEventWidth()-FROM_RESIZE_EVENT_TO_MINIMUMWIDTH,(int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight()) /1.3)));
        setupToolbar();
        setupCanvas();
        setupCanvasExtras();
        setupPointsOfInterestBar();
        setupJourneyPlannerBar();
        setupLayeredPane();
        addInteractionHandlerToWindow();
        setToolTipTheme();
        toggleKeyBindings();
        hideWindow();

        isSliding = false;
    }

    /**
     * Sets the theme for tooltips application wide.
     */
    private void setToolTipTheme()
    {
        UIManager.put("ToolTip.background", ThemeHelper.color("toolTipBackground"));
        UIManager.put("ToolTip.foreground", ThemeHelper.color("toolTipForeground"));
    }

    /**
     * Sets up the JLayeredPane.
     */
    private void setupLayeredPane()
    {
        layeredPane = new JLayeredPane();
        window.getFrame().add(layeredPane, BorderLayout.CENTER);
        adjustBounds();
        ToolbarController.getInstance().getToolbar().setOpaque(true);
        CanvasController.getInstance().getMapCanvas().setOpaque(true);
        PointsOfInterestController.getInstance().getInformationBar().setOpaque(true);
        JourneyPlannerBarController.getInstance().getInformationBar().setOpaque(true);
        CanvasExtrasController.getInstance().getDistanceScaller().setOpaque(true);
        layeredPane.add(CanvasController.getInstance().getMapCanvas(),
                new Integer(1));
        layeredPane.add(CanvasExtrasController.getInstance().getDistanceScaller(),
                new Integer(6));
        layeredPane.add(PointsOfInterestController.getInstance().getInformationBar(),
                new Integer(3));
        layeredPane.add(JourneyPlannerBarController.getInstance().getInformationBar(),
                new Integer(4));
        layeredPane.add(ToolbarController.getInstance().getToolbar(),
            new Integer(5));
    }

    /**
     * Adjusts all components bounds in the JLayeredPane.
     */
    private void adjustBounds()
    {
        layeredPane.setBounds(new Rectangle(window.getFrame().getWidth(),
            window.getFrame().getHeight()));
        ToolbarController.getInstance().getToolbar().setBounds(
            0, 0, window.getFrame().getWidth(), GlobalValue.getToolbarHeight());
        CanvasController.getInstance().getMapCanvas().setBounds(
            0, 0, window.getFrame().getWidth(), window.getFrame().getHeight());
        PointsOfInterestController.getInstance().getInformationBar().setBounds(
            0, 0, 0, window.getFrame().getHeight());
        if(!JourneyPlannerBarController.getInstance().isLargeJourneyPlannerVisible() &&
                !JourneyPlannerBarController.getInstance().isSmallJourneyPlannerVisible() &&
                !PointsOfInterestController.getInstance().isLargePOIVisible() &&
                !PointsOfInterestController.getInstance().isSmallPOIVisible())
            JourneyPlannerBarController.getInstance().getInformationBar().setBounds(
                0, 0, 0, window.getFrame().getHeight());
        DistanceScalerView distance = CanvasExtrasController.getInstance()
                .getDistanceScaller();
        if(!JourneyPlannerBarController.getInstance().isSmallJourneyPlannerVisible() && !PointsOfInterestController.getInstance().isSmallPOIVisible()) {
            distance.setBounds(window.getFrame().getWidth() - 180,
                    window.getFrame().getHeight() - 120, 120,
                    40);
        } else {
            distance.setBounds(window.getFrame().getWidth() - 180,
                    window.getFrame().getHeight() - (105 + GlobalValue.getSmallInformationBarHeight()), 120,
                    40);
        }
    }

    /**
     * Sets up the toolbar.
     */
    private void setupToolbar()
    {
        ToolbarController.getInstance().specifyWindow(window);
        ToolbarController.getInstance().setupToolbar(ToolbarType.LARGE);
    }

    /**
     * Sets up the canvas.
     */
    private void setupCanvas()
    {
        CanvasController.getInstance().specifyWindow(window);
        CanvasController.getInstance().setupCanvas();
    }

    /**
     * Sets up the canvas extra component, the distance scaler.
     */
    private void setupCanvasExtras(){
        CanvasExtrasController.getInstance().specifyWindow(window);
        CanvasExtrasController.getInstance().setupExtras();
    }

    /**
     * Sets up the Points of Interest bar.
     */
    private void setupPointsOfInterestBar()
    {
        PointsOfInterestController.getInstance().specifyWindow(window);
        PointsOfInterestController.getInstance().setupInformationBar();
        PointsOfInterestController.getInstance().setupBasePointsOfInterestBar();
    }

    /**
     * Sets up the JourneyPlanner.
     */
    private void setupJourneyPlannerBar() {
        JourneyPlannerBarController.getInstance().specifyWindow(window);
        JourneyPlannerBarController.getInstance().setupInformationBar();
        JourneyPlannerBarController.getInstance().setupBaseJourneyPlannerBar();
    }

    /**
     * Activates the large version of the Points of Interest bar. The Points of Interest bar will appear
     * with a slide in animation.
     */
    public void activateLargePointsOfInterestInformationBar() {
        if(inSlideTimer == null) {
            PointsOfInterestController.getInstance().clearPointsOfInterestBar();
            int boundsTo = GlobalValue.getLargeInformationBarWidth();
            final int[] boundsNow = {0};
            isSliding = true;
            inSlideTimer = new Timer(SLIDE_DELAY, ae -> {
                if (boundsTo > boundsNow[0]) {
                    boundsNow[0] = boundsNow[0] + PIXELS_TO_MOVE;
                    PointsOfInterestController.getInstance().getInformationBar().setBounds(0, 0, boundsNow[0], window.getFrame().getHeight());
                    PointsOfInterestController.getInstance().getInformationBar().revalidate();
                    PointsOfInterestController.getInstance().getInformationBar().repaint();
                } else {
                    inSlideTimer.stop();
                    inSlideTimer = null;
                    isSliding = false;
                }
            });
            inSlideTimer.start();
            PointsOfInterestController.getInstance().setupLargePointsOfInterestBar();
        }
    }
    /**
     * Activates the small version of the Points of Interest bar. The Points of Interest bar will appear
     * with a slide in animation.
     */
    public void activateSmallPointsOfInterestInformationBar() {
        if(inSlideTimer == null) {
            PointsOfInterestController.getInstance().clearPointsOfInterestBar();
            int boundsTo = GlobalValue.getSmallInformationBarHeight();
            final int[] boundsNow = {0};
            isSliding = true;
            inSlideTimer = new Timer(SLIDE_DELAY, ae -> {
                if (boundsTo > boundsNow[0]) {
                    boundsNow[0] = boundsNow[0] + PIXELS_TO_MOVE;
                    PointsOfInterestController.getInstance().getInformationBar().setBounds(0, window.getFrame().getHeight() - boundsNow[0], window.getFrame().getWidth(), window.getFrame().getHeight());
                    PointsOfInterestController.getInstance().getInformationBar().revalidate();
                    PointsOfInterestController.getInstance().getInformationBar().repaint();
                } else {
                    inSlideTimer.stop();
                    inSlideTimer = null;
                    isSliding = false;
                }
            });
            inSlideTimer.start();
            PointsOfInterestController.getInstance().setupSmallPointsOfInterestBar();
            CanvasExtrasController.getInstance().getDistanceScaller().setLocation(CanvasExtrasController.getInstance().getDistanceScaller().getX(), CanvasExtrasController.getInstance().getDistanceScaller().getY() - GlobalValue.getSmallInformationBarHeight() + 15);
        }
    }

    /**
     * Deactivates and removes the large version of the Points of Interest bar.
     * Will block deactivation if an animation is already in progress.
     */
    public void deactivateLargePointsOfInterestInformationBar() {
        if(!isSliding) {
            PointsOfInterestController.getInstance().getInformationBar().setBounds(0, 0, 0, window.getFrame().getHeight());
            PointsOfInterestController.getInstance().clearPointsOfInterestBar();
            CanvasController.repaintCanvas();
        }
    }

    /**
     * Deactivates and removes the small version of the Points of Interest bar.
     * Will block deactivation if an animation is already in progress.
     */
    public void deactivateSmallPointsOfInterestInformationBar() {
        if(!isSliding) {
            PointsOfInterestController.getInstance().getInformationBar().setBounds(0, window.getFrame().getHeight(), window.getFrame().getWidth(), window.getFrame().getHeight());
            PointsOfInterestController.getInstance().clearPointsOfInterestBar();
            CanvasExtrasController.getInstance().getDistanceScaller().setLocation(CanvasExtrasController.getInstance().getDistanceScaller().getX(),
                    CanvasExtrasController.getInstance().getDistanceScaller().getY() + GlobalValue.getSmallInformationBarHeight() - 15);
            CanvasController.repaintCanvas();
        }
    }

    /**
     * Activates the Larger version of the JourneyPlanner. The JourneyPlanner will appear with a slide in
     * animation.
     */
    public void activateLargeJourneyPlannerInformationBar() {
        if(inSlideTimer == null) {
            JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
            int boundsTo = GlobalValue.getLargeInformationBarWidth();
            final int[] boundsNow = {0};
            isSliding = true;
            inSlideTimer = new Timer(SLIDE_DELAY, ae -> {
                if (boundsTo > boundsNow[0]) {
                    boundsNow[0] = boundsNow[0] + PIXELS_TO_MOVE;
                    JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0, 0, boundsNow[0], window.getFrame().getHeight());
                    JourneyPlannerBarController.getInstance().getInformationBar().revalidate();
                    JourneyPlannerBarController.getInstance().getInformationBar().repaint();
                } else {
                    inSlideTimer.stop();
                    inSlideTimer = null;
                    isSliding = false;
                }
            });
            inSlideTimer.start();
            JourneyPlannerBarController.getInstance().setupLargeJourneyPlannerBar();
        }
    }

    /**
     * Activates the small version of the JourneyPlanner. The JourneyPlanner will appear
     * with a slide in animation.
     */
    public void activateSmallJourneyPlannerInformationBar() {
        if (!isSliding) {
            JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
            int boundsTo = GlobalValue.getSmallInformationBarHeight();
            final int[] boundsNow = {0};
            isSliding = true;
            inSlideTimer = new Timer(SLIDE_DELAY, ae -> {
                if (boundsTo > boundsNow[0]) {
                    boundsNow[0] = boundsNow[0] + PIXELS_TO_MOVE;
                    JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0, window.getFrame().getHeight() - boundsNow[0], window.getFrame().getWidth(), window.getFrame().getHeight());
                    JourneyPlannerBarController.getInstance().getInformationBar().revalidate();
                    JourneyPlannerBarController.getInstance().getInformationBar().repaint();
                } else {
                    inSlideTimer.stop();
                    inSlideTimer = null;
                    isSliding = false;
                }
            });
            inSlideTimer.start();
            JourneyPlannerBarController.getInstance().setupSmallJourneyPlannerBar();
            CanvasExtrasController.getInstance().getDistanceScaller().setLocation(CanvasExtrasController.getInstance().getDistanceScaller().getX(), CanvasExtrasController.getInstance().getDistanceScaller().getY() - GlobalValue.getSmallInformationBarHeight() + 15);
        }
    }

    /**
     * This method replaces the large version of the JourneyPlanner with the small version.
     */
    private void swapFromLargeJourneyPlannerToSmall() {
        JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
        JourneyPlannerBarController.getInstance().setupSmallJourneyPlannerBar();
        JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0, window.getFrame().getHeight() - GlobalValue.getSmallInformationBarHeight(), window.getFrame().getWidth(), window.getFrame().getHeight());
        JourneyPlannerBarController.getInstance().getInformationBar().revalidate();
        JourneyPlannerBarController.getInstance().getInformationBar().repaint();
    }

    /**
     * This method replaces the small version of the JourneyPlanner with the large version.
     */
    private void swapFromSmallJourneyPlannerToLarge() {
        JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
        JourneyPlannerBarController.getInstance().setupLargeJourneyPlannerBar();
        JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0,0,GlobalValue.getLargeInformationBarWidth(), window.getFrame().getHeight());
        JourneyPlannerBarController.getInstance().getInformationBar().revalidate();
        JourneyPlannerBarController.getInstance().getInformationBar().repaint();
    }

    /**
     * Deactivates and removes the large version of the JourneyPlanner.
     * Will block deactivation if an animation is already in progress.
     */
    public void deactivateLargeJourneyPlannerInformationBar() {
        if(!isSliding) {
            JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0, 0, 0, window.getFrame().getHeight());
            JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
            CanvasController.repaintCanvas();
        }
    }

    /**
     * Deactivates and removes the small version of the JourneyPlanner.
     * Will block deactivation if an animation is already in progress.
     */
    public void deactivateSmallJourneyPlannerInformationBar() {
        if(!isSliding) {
            JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0, 0, 0, window.getFrame().getHeight());
            JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
            CanvasExtrasController.getInstance().getDistanceScaller().setLocation(CanvasExtrasController.getInstance().getDistanceScaller().getX(),
                    CanvasExtrasController.getInstance().getDistanceScaller().getY() + GlobalValue.getSmallInformationBarHeight() - 15);
            CanvasController.repaintCanvas();
        }
    }

    /**
     * This method replaces the large version of the POIinformationbar with the small version.
     */
    private void swapFromLargePOIToSmall() {
        PointsOfInterestController.getInstance().clearPointsOfInterestBar();
        PointsOfInterestController.getInstance().setupSmallPointsOfInterestBar();
        PointsOfInterestController.getInstance().getInformationBar().setBounds(0, window.getFrame().getHeight() - GlobalValue.getSmallInformationBarHeight(), window.getFrame().getWidth(), window.getFrame().getHeight());
        PointsOfInterestController.getInstance().getInformationBar().revalidate();
        PointsOfInterestController.getInstance().getInformationBar().repaint();
        if(GlobalValue.isAddNewPointActive()) CanvasController.getInstance().changeCanvasMouseCursorToPoint();
        else CanvasController.getInstance().changeCanvasMouseCursorToNormal();
    }

    /**
     * This method replaces the small version of the POIinformationbar with the large version.
     */
    private void swapFromSmallPOIToLarge() {
        PointsOfInterestController.getInstance().clearPointsOfInterestBar();
        PointsOfInterestController.getInstance().setupLargePointsOfInterestBar();
        PointsOfInterestController.getInstance().getInformationBar().setBounds(0,0,GlobalValue.getLargeInformationBarWidth(), window.getFrame().getHeight());
        PointsOfInterestController.getInstance().getInformationBar().revalidate();
        PointsOfInterestController.getInstance().getInformationBar().repaint();
        if(GlobalValue.isAddNewPointActive()) CanvasController.getInstance().changeCanvasMouseCursorToPoint();
        else CanvasController.getInstance().changeCanvasMouseCursorToNormal();
    }

    /**
     * Transfers focus to the map canvas.
     * note: to be used to decrease coupling.
     */
    public void transferFocusToMapCanvas()
    {
        CanvasController.getInstance().getMapCanvas().grabFocus();
    }

    /**
     * Notify relevant parties of theme change.
     */
    public void themeHasChanged()
    {
        ToolbarController.getInstance().themeHasChanged();
        CanvasController.getInstance().themeHasChanged();
        CanvasExtrasController.getInstance().themeHasChanged();
        PointsOfInterestController.getInstance().themeHasChanged();
        JourneyPlannerBarController.getInstance().themeHasChanged();
        setToolTipTheme();
        setProgressBarTheme();
    }

    /**
     * Lets a client know if a sliding animation is in progress.
     * @return is an animation in progress
     */
    public boolean isSliding() {
        return isSliding;
    }


    /**
     * Specifies key bindings for the main window.
     */
    @Override
    protected void specifyKeyBindings()
    {
        handler.addKeyBinding(
            KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (ToolbarController.getInstance().doesSearchbarHaveFocus())
                        return;
                    else {
                        Toolkit.getDefaultToolkit().beep();
                        if (PopupWindow.confirmBox(
                                null, "Do You Wish to Quit OSM Visualiser?",
                                "PLease Confirm!",
                                JOptionPane.YES_NO_OPTION)
                            == JOptionPane.YES_OPTION) {
                            System.exit(0);
                        }
                    }
                }
            });
        handler.addKeyBinding(KeyEvent.VK_D, OSDetector.getActivationKey(),
            new AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e)
                {
                    DebugWindow.getInstance().show();
                }
            });
    }

    /**
     * Adds an interaction handler to the window. This interaction handler deals with resizing and other events that
     * relate to the main window. The interation handler is a component adapter.
     * @Override this method also adds a window adapter to deal with window focus.
     */
    @Override
    protected void addInteractionHandlerToWindow()
    {
        super.addInteractionHandlerToWindow();
        MainWindowInteractionHandler handler = new MainWindowInteractionHandler();
        window.getFrame().addComponentListener(handler);

        window.getFrame().addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowLostFocus(WindowEvent e)
            {
                CanvasController.getInstance().disablePopup();
                ToolbarController.getInstance().setLoadingScreenAlwaysOnTopStatus(false);
                if(ToolbarController.getInstance().isMenuToolPopupVisible()) {
                    ToolbarController.getInstance().requestHideMenuToolPopup();
                    ToolbarController.getInstance().getToolbar().getTool(ToolType.MENU).toggleActivate(false);
                }

            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                ToolbarController.getInstance().setLoadingScreenAlwaysOnTopStatus(true);
            }
        });
    }

    /**
     * Toggles key bindings across the application.
     */
    public void setKeyToggle()
    {
        CanvasController.getInstance().toggleKeyBindings();
        ToolbarController.getInstance().toggleKeyBindings();
        SettingsWindowController.getInstance().toggleKeyBindings();
        MainWindowController.getInstance().toggleKeyBindings();
    }

    /**
     * Resets the instance.
     * Note: is only intended for testing purposes.
     */
    public void resetInstance() { instance = null; }

    /**
     * Repaint the map canvas.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasRepaint()
    {
        CanvasController.repaintCanvas();
    }


    /**
     * Reset the elements in map canvas
     * note: to be used to decrease coupling.
     */
    public void requestCanvasResetElements() {
        CanvasController.getInstance().getMapCanvas().setElements(Model.getInstance().getElements());
    }

    /**
     * Transfers focus to the informationBar, either Points of interest information bar or JourneyPlanner
     * information bar, depending on which of these is visible.
     * note: to be used to decrease coupling.
     */
    public void transferFocusToInformationBar() {
        PointsOfInterestController.getInstance().getInformationBar().grabFocus();
    }

    /**
     * Change the canvas cursor to a crosshair.
     * note: to be used to decrease coupling.
     */
    public void changeCanvasMouseCursorToPoint() {
        CanvasController.getInstance().changeCanvasMouseCursorToPoint();
    }

    /**
     * Repaints either Points of interest information bar or JourneyPlanner
     * information bar, depending on which of these is visible.
     * note: to be used to decrease coupling.
     */
    public void requestPointsOfInterestBarRepaint() {
        PointsOfInterestController.getInstance().repaintPointsOfInterestBar();
    }

    /**
     * Change the canvas cursor to a normal.
     * note: to be used to decrease coupling.
     */
    public void changeCanvasMouseCursorToNormal() {
        CanvasController.getInstance().changeCanvasMouseCursorToNormal();
    }

    /**
     * Adjust map canvas to data bounds,
     * note: not dynamic, only on startup.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasAdjustToBounds() {
        CanvasController.adjustToBounds();
    }

    /**
     * Adjust map canvas to data bounds,
     * note: dynamic, to be used every time a file is loaded.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasAdjustToDynamicBounds() {
        CanvasController.adjustToDynamicBounds();
    }

    /**
     * Repaint the toolbar.
     * note: to be used to decrease coupling.
     */
    public void requestToolbarRepaint() {
        ToolbarController.getInstance().repaintToolbar();
    }

    /**
     * Close the address parser popup list.
     * note: to be used to decrease coupling.
     */
    public void requestSearchToolCloseList() {
        ToolbarController.getInstance().requestSearchToolHideList();
    }

    /**
     * Lets the client know if the search fields in the JourneyPlanner have focus.
     * note: to be used to decrease coupling.
     * @return do the JourneyPlanner search fields have focus
     */
    public boolean doesJourneyPlannerSearchHaveFocus() {
        return JourneyPlannerBarController.getInstance().isASearchListOpen();
    }

    /**
     * Close the JourneyPlanner search popup lists.
     * note: to be used to decrease coupling.
     */
    public void requestJourneyPlannerCloseSearchLists() {
        JourneyPlannerBarController.getInstance().closeSearchLists();
    }

    /**
     * Sets the theme of progressBars across the application.
     */
    public void setProgressBarTheme() {
        UIManager.put("ProgressBar.border", BorderFactory.createLineBorder(ThemeHelper.color("border")));
        UIManager.put("ProgressBar.background", ThemeHelper.color("progressBarBackground"));
        UIManager.put("ProgressBar.foreground", ThemeHelper.color("progressBarForeground"));
    }

    /**
     * Lets the client know whether the menu tool popup is visible
     * @return is the menu tool popup visible.
     * note: to be used to decrease coupling.
     */
    public boolean isMenuToolPopupVisible() {
        return ToolbarController.getInstance().isMenuToolPopupVisible();
    }

    /**
     * Closes the menu tool popup
     * note: to be used to decrease coupling.
     */
    public void requestMenuToolHidePopup() {
        ToolbarController.getInstance().requestHideMenuToolPopup();
    }

    /**
     * Lets the client know the address parser has focus
     * @return does the address parser have focus
     * note: to be used to decrease coupling.
     */
    public boolean doesSearchToolHaveFocus() {
        return ToolbarController.getInstance().doesSearchbarHaveFocus();
    }

    /**
     * Resets the location marker on the map canvas.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasResetLocationMarker(){
        CanvasController.getInstance().canvasResetLocationMarker();
    }

    /**
     * Updates the canvas points of interest.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasUpdatePOI() {
        CanvasController.getInstance().updateCanvasPOI();
    }

    /**
     * Toggles whether the current route should be displayed.
     * @param isActive
     * note: to be used to decrease coupling.
     */
    public void requestCanvasToggleRouteVisualization(boolean isActive){
        CanvasController.getInstance().toggleRouteVisualization(isActive);
    }

    /**
     * Sets the to and from points in a route search on the map canvas.
     * @param to the to point
     * @param from the from point
     * note: to be used to decrease coupling.
     */
    public void requestCanvasUpateToAndFrom(Point2D.Float to, Point2D.Float from){
        CanvasController.getInstance().updateToAndFrom(to, from);
    }

    /**
     * Resets the route on the map canvas
     * note: to be used to decrease coupling.
     */
    public void requestCanvasResetRoute(){
        CanvasController.getInstance().resetRoute();
    }

    /**
     * Updates the searched address point on the map canvas.
     * @param address the new point
     * note: to be used to decrease coupling.
     */
    public void requestCanvasUpdateAddressMarker(Point2D.Float address){
        CanvasController.getInstance().markLocation(address);
    }

    /**
     * Pans to the given point on the map canvas.
     * @param aFloat the point to be panned to.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasPanToPoint(Point.Float aFloat) {
        CanvasController.getInstance().panToPoint(aFloat);
    }

    /**
     * Toggles the new POI mode off.
     * note: to be used to decrease coupling.
     */
    public void requestPoiModeOff() {
        PointsOfInterestController.getInstance().poiModeOff();
    }

    /**
     * Toggles the new POI mode on.
     * note: to be used to decrease coupling.
     */
    public void requestPoiModeOn() {
        PointsOfInterestController.getInstance().poiModeOn();
    }

    /**
     * Add a point of interest to the map canvas.
     * @param poi the point to be added.
     * note: to be used to decrease coupling.
     */
    public void requestAddPOI(POI poi) {
        PointsOfInterestController.getInstance().addPOI(poi);
    }

    /**
     * Updates the points of interest bar such that all current points of interest are visible.
     * note: to be used to decrease coupling.
     */
    public void requestUpdatePointsOfInterestBar() {
        PointsOfInterestController.getInstance().updatePointsOfInterestBar();
    }

    /**
     * Sets a route on the map canvas.
     * @param path the route to be added to the map canvas.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasSetRoute(Iterable<RoadEdge> path) {
        CanvasController.getInstance().canvasSetRoute(path);
    }

    /**
     * Calculates nearest road to a given point.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * note: to be used to decrease coupling.
     */
    public Road requestCalculateNearestNeighbour(float x, float y) {
        return CanvasController.calculateNearestNeighbour(x,y);
    }

    /**
     * The interaction handler that deals with key shortcuts and various other events, such as component resize,
     * component hidden, and component shown.
     */
    private class MainWindowInteractionHandler extends MainWindowController.WindowInteractionHandler {
        /**
         * Called every time the main window is resized. Adjusts bounds of components and notifies all relevant parties
         * to deal with the size of the window.
         * @param e the resize event
         */
        @Override
        public void componentResized(ComponentEvent e)
        {
            super.componentResized(e);
            adjustBounds();
            /*if(ToolbarController.getInstance().isPoiToolActive()) {
                ToolbarController.getInstance().getToolbar().getTool(ToolType.POI).toggleActivate(false);
                ToolbarController.getInstance().setIsPoiToolActive(false);
                if(ToolbarController.getInstance().getType() == ToolbarType.SMALL) deactivateSmallPointsOfInterestInformationBar();
                else if(ToolbarController.getInstance().getType() == ToolbarType.LARGE) deactivateLargePointsOfInterestInformationBar();
            } else if(ToolbarController.getInstance().isJourneyPlannerToolActive()) {
                ToolbarController.getInstance().getToolbar().getTool(ToolType.ROUTES).toggleActivate(false);
                ToolbarController.getInstance().setIsJourneyPlannerToolActive(false);
                if(ToolbarController.getInstance().getType() == ToolbarType.LARGE) deactivateLargeJourneyPlannerInformationBar();
                else if(ToolbarController.getInstance().getType() == ToolbarType.SMALL) deactivateSmallJourneyPlannerInformationBar();
            }*/
            JourneyPlannerBarController.getInstance().resizeEvent();
            PointsOfInterestController.getInstance().resizeEvent();
            ToolbarController.getInstance().resizeEvent();
            CanvasController.getInstance().resizeEvent();
            if(JourneyPlannerBarController.getInstance().isLargeJourneyPlannerVisible() && window.getFrame().getWidth() < ToolbarController.getSmallLargeEventWidth()) {
                swapFromLargeJourneyPlannerToSmall();
            } else if(JourneyPlannerBarController.getInstance().isSmallJourneyPlannerVisible() && window.getFrame().getWidth() >= ToolbarController.getSmallLargeEventWidth()) {
                swapFromSmallJourneyPlannerToLarge();
            }
            if(PointsOfInterestController.getInstance().isLargePOIVisible() && window.getFrame().getWidth() < ToolbarController.getSmallLargeEventWidth()) {
                swapFromLargePOIToSmall();
            } else if(PointsOfInterestController.getInstance().isSmallPOIVisible() && window.getFrame().getWidth() >= ToolbarController.getSmallLargeEventWidth()) {
                swapFromSmallPOIToLarge();
            }
            CanvasController.getInstance().disablePopup();
        }

        /**
         * Called every time the main window is moved. Notifies relevant parties of the moved event.
         * @param e the move event.
         */
        @Override
        public void componentMoved(ComponentEvent e)
        {
            super.componentMoved(e);
            ToolbarController.getInstance().moveEvent();
            CanvasController.getInstance().disablePopup();
        }

        /**
         * Called every time the main window is hidden. Notifies relevant parties of the hide event.
         * @param e the hide event
         */
        @Override
        public void componentHidden(ComponentEvent e)
        {
            super.componentHidden(e);
            CanvasController.getInstance().disablePopup();
            ToolbarController.getInstance().setLoadingScreenAlwaysOnTopStatus(false);
            if(ToolbarController.getInstance().isMenuToolPopupVisible()) {
                ToolbarController.getInstance().requestHideMenuToolPopup();
                ToolbarController.getInstance().getToolbar().getTool(ToolType.MENU).toggleActivate(false);
            }
        }

        /**
         * Called every time the main window is shown. Notifies relevant parties of the shown event.
         * @param e the shown event.
         */
        @Override
        public void componentShown(ComponentEvent e) {
            ToolbarController.getInstance().setLoadingScreenAlwaysOnTopStatus(true);
        }
    }
}
