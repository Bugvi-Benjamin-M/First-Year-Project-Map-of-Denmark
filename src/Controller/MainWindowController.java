package Controller;

import Controller.ToolbarControllers.ToolbarController;
import Enums.ToolType;
import Enums.ToolbarType;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import Model.Elements.POI;
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

    private MainWindowController() { super(); }

    public static MainWindowController getInstance()
    {
        if (instance == null) {
            instance = new MainWindowController();
        }
        return instance;
    }

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

    private void setToolTipTheme()
    {
        UIManager.put("ToolTip.background", ThemeHelper.color("toolTipBackground"));
        UIManager.put("ToolTip.foreground", ThemeHelper.color("toolTipForeground"));
    }

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
        JourneyPlannerBarController.getInstance().getInformationBar().setBounds(
                0, 0, 0, window.getFrame().getHeight());
        DistanceScalerView distance = CanvasExtrasController.getInstance()
                .getDistanceScaller();
        distance.setBounds(window.getFrame().getWidth()-180,
                window.getFrame().getHeight()-120, 120,
                40);
    }

    private void setupToolbar()
    {
        ToolbarController.getInstance().specifyWindow(window);
        ToolbarController.getInstance().setupToolbar(ToolbarType.LARGE);
    }

    private void setupCanvas()
    {
        CanvasController.getInstance().specifyWindow(window);
        CanvasController.getInstance().setupCanvas();
    }

    private void setupCanvasExtras(){
        CanvasExtrasController.getInstance().specifyWindow(window);
        CanvasExtrasController.getInstance().setupExtras();
    }

    private void setupPointsOfInterestBar()
    {
        PointsOfInterestController.getInstance().specifyWindow(window);
        PointsOfInterestController.getInstance().setupInformationBar();
        PointsOfInterestController.getInstance().setupBasePointsOfInterestBar();
    }

    private void setupJourneyPlannerBar() {
        JourneyPlannerBarController.getInstance().specifyWindow(window);
        JourneyPlannerBarController.getInstance().setupInformationBar();
        JourneyPlannerBarController.getInstance().setupBaseJourneyPlannerBar();
    }

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

    public void deactivateLargePointsOfInterestInformationBar() {
        if(!isSliding) {
            PointsOfInterestController.getInstance().getInformationBar().setBounds(0, 0, 0, window.getFrame().getHeight());
            PointsOfInterestController.getInstance().clearPointsOfInterestBar();
            CanvasController.repaintCanvas();
        }
    }

    public void deactivateSmallPointsOfInterestInformationBar() {
        if(!isSliding) {
            PointsOfInterestController.getInstance().getInformationBar().setBounds(0, window.getFrame().getHeight(), window.getFrame().getWidth(), window.getFrame().getHeight());
            PointsOfInterestController.getInstance().clearPointsOfInterestBar();
            CanvasExtrasController.getInstance().getDistanceScaller().setLocation(CanvasExtrasController.getInstance().getDistanceScaller().getX(),
                    CanvasExtrasController.getInstance().getDistanceScaller().getY() + GlobalValue.getSmallInformationBarHeight() - 15);
            CanvasController.repaintCanvas();
        }
    }

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

    public void deactivateLargeJourneyPlannerInformationBar() {
        if(!isSliding) {
            JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0, 0, 0, window.getFrame().getHeight());
            JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
            CanvasController.repaintCanvas();
        }
    }


    public void deactivateSmallJourneyPlannerInformationBar() {
        if(!isSliding) {
            JourneyPlannerBarController.getInstance().getInformationBar().setBounds(0, 0, 0, window.getFrame().getHeight());
            JourneyPlannerBarController.getInstance().clearJourneyPlannerBar();
            CanvasExtrasController.getInstance().getDistanceScaller().setLocation(CanvasExtrasController.getInstance().getDistanceScaller().getX(),
                    CanvasExtrasController.getInstance().getDistanceScaller().getY() + GlobalValue.getSmallInformationBarHeight() - 15);
            CanvasController.repaintCanvas();
        }
    }

    public void transferFocusToMapCanvas()
    {
        CanvasController.getInstance().getMapCanvas().grabFocus();
    }

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

    public boolean isSliding() {
        return isSliding;
    }

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

    public void setKeyToggle()
    {
        CanvasController.getInstance().toggleKeyBindings();
        ToolbarController.getInstance().toggleKeyBindings();
        SettingsWindowController.getInstance().toggleKeyBindings();
        MainWindowController.getInstance().toggleKeyBindings();
    }

    public void resetInstance() { instance = null; }

    public void requestCanvasRepaint()
    {
        CanvasController.repaintCanvas();
    }

    public void requestCanvasResetElements() {
        CanvasController.getInstance().getMapCanvas().setElements(Model.getInstance().getElements());
    }

    public void transferFocusToInformationBar() {
        PointsOfInterestController.getInstance().getInformationBar().grabFocus();
    }

    public void changeCanvasMouseCursorToPoint() {
        CanvasController.getInstance().changeCanvasMouseCursorToPoint();
    }

    public void requestPointsOfInterestBarRepaint() {
        PointsOfInterestController.getInstance().repaintPointsOfInterestBar();
    }

    public void changeCanvasMouseCursorToNormal() {
        CanvasController.getInstance().changeCanvasMouseCursorToNormal();
    }

    public void requestCanvasAdjustToBounds() {
        CanvasController.adjustToBounds();
    }

    public void requestCanvasAdjustToDynamicBounds() {
        CanvasController.adjustToDynamicBounds();
    }

    public void requestToolbarRepaint() {
        ToolbarController.getInstance().repaintToolbar();
    }

    public void requestSearchToolCloseList() {
        ToolbarController.getInstance().requestSearchToolHideList();
    }

    public boolean doesJourneyPlannerSearchHaveFocus() {
        return JourneyPlannerBarController.getInstance().isASearchListOpen();
    }

    public void requestJourneyPlannerCloseSearchLists() {
        JourneyPlannerBarController.getInstance().closeSearchLists();
    }

    public void setProgressBarTheme() {
        UIManager.put("ProgressBar.border", BorderFactory.createLineBorder(ThemeHelper.color("border")));
        UIManager.put("ProgressBar.background", ThemeHelper.color("progressBarBackground"));
        UIManager.put("ProgressBar.foreground", ThemeHelper.color("progressBarForeground"));
    }

    public boolean isMenuToolPopupVisible() {
        return ToolbarController.getInstance().isMenuToolPopupVisible();
    }

    public void requestMenuToolHidePopup() {
        ToolbarController.getInstance().requestHideMenuToolPopup();
    }

    public boolean doesSearchToolHaveFocus() {
        return ToolbarController.getInstance().doesSearchbarHaveFocus();
    }

    public void requestCanvasUpdatePOI() {
        CanvasController.getInstance().updateCanvasPOI();
    }

    public void requestCanvasToggleRouteVisualization(boolean isActive){
        CanvasController.getInstance().toggleRouteVisualization(isActive);
    }

    public void requestCanvasUpateToAndFrom(Point2D.Float to, Point2D.Float from){
        CanvasController.getInstance().updateToAndFrom(to, from);
    }

    public void requestCanvasResetToAndFrom(){
        CanvasController.getInstance().resetToAndFrom();
    }

    public void requestCanvasUpdateAddressMarker(Point2D.Float address){
        CanvasController.getInstance().markLocation(address);
    }

    public void requestCanvasPanToPoint(Point.Float aFloat) {
        CanvasController.getInstance().panToPoint(aFloat);
    }

    public void requestPoiModeOff() {
        PointsOfInterestController.getInstance().poiModeOff();
    }

    public void requestPoiModeOn() {
        PointsOfInterestController.getInstance().poiModeOn();
    }

    public void requestAddPOI(POI poi) {
        PointsOfInterestController.getInstance().addPOI(poi);
    }

    public void requestUpdatePointsOfInterestBar() {
        PointsOfInterestController.getInstance().updatePointsOfInterestBar();
    }

    private class MainWindowInteractionHandler
        extends MainWindowController.WindowInteractionHandler {

        @Override
        public void componentResized(ComponentEvent e)
        {
            super.componentResized(e);
            adjustBounds();
            if(ToolbarController.getInstance().isPoiToolActive()) {
                ToolbarController.getInstance().getToolbar().getTool(ToolType.POI).toggleActivate(false);
                ToolbarController.getInstance().setIsPoiToolActive(false);
                if(ToolbarController.getInstance().getType() == ToolbarType.SMALL) deactivateSmallPointsOfInterestInformationBar();
                else if(ToolbarController.getInstance().getType() == ToolbarType.LARGE) deactivateLargePointsOfInterestInformationBar();
            } else if(ToolbarController.getInstance().isJourneyPlannerToolActive()) {
                ToolbarController.getInstance().getToolbar().getTool(ToolType.ROUTES).toggleActivate(false);
                ToolbarController.getInstance().setIsJourneyPlannerToolActive(false);
                if(ToolbarController.getInstance().getType() == ToolbarType.LARGE) deactivateLargeJourneyPlannerInformationBar();
                else if(ToolbarController.getInstance().getType() == ToolbarType.SMALL) deactivateSmallJourneyPlannerInformationBar();
            }

            ToolbarController.getInstance().resizeEvent();
            CanvasController.getInstance().resizeEvent();
            CanvasController.getInstance().disablePopup();
        }

        @Override
        public void componentMoved(ComponentEvent e)
        {
            super.componentMoved(e);
            ToolbarController.getInstance().moveEvent();
            CanvasController.getInstance().disablePopup();
        }

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

        @Override
        public void componentShown(ComponentEvent e) {
            ToolbarController.getInstance().setLoadingScreenAlwaysOnTopStatus(true);
        }
    }
}
