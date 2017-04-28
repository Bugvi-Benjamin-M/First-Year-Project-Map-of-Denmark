package Controller;

import Controller.ToolbarControllers.ToolbarController;
import Enums.ToolbarType;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
import Model.Model;
import View.PopupWindow;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Búgvi Magnussen on 14-03-2017.
 */
public final class MainWindowController extends WindowController {

    private static final String MAIN_TITLE = "OSM Map Viewer v0.4";
    private static MainWindowController instance;
    private JLayeredPane layeredPane;

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
        window.setMinimumWindowSize(new Dimension((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1.9), Toolkit.getDefaultToolkit().getScreenSize().height));
        ThemeHelper.setTheme(PreferencesController.getInstance().getThemeSetting());
        setupToolbar();
        setupCanvas();
        setupInformationBar();
        setupLayeredPane();
        addInteractionHandlerToWindow();
        setToolTipTheme();
        toggleKeyBindings();
        hideWindow();
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
        layeredPane.add(PointsOfInterestController.getInstance().getInformationBar(),
                new Integer(1));
        layeredPane.add(CanvasController.getInstance().getMapCanvas(),
            new Integer(1));
        layeredPane.add(ToolbarController.getInstance().getToolbar(),
            new Integer(2));
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

    private void setupInformationBar()
    {
        PointsOfInterestController.getInstance().specifyWindow(window);
        PointsOfInterestController.getInstance().setupInformationBar();
    }

    public void activatePointsOfInterestInformationBar() {
        CanvasController.getInstance().getMapCanvas().setBounds(400, 0, window.getFrame().getWidth(), window.getFrame().getHeight());
        PointsOfInterestController.getInstance().getInformationBar().setBounds(0, 0, 400, window.getFrame().getHeight());
        CanvasController.repaintCanvas();
        PointsOfInterestController.getInstance().getInformationBar().revalidate();

    }

    public void deactivatePointsOfInterestInformationBar() {
        PointsOfInterestController.getInstance().getInformationBar().setBounds(0,0,0,window.getFrame().getHeight());
        CanvasController.getInstance().getMapCanvas().setBounds(0,0, window.getFrame().getWidth(), window.getFrame().getHeight());
        CanvasController.repaintCanvas();
    }

    public void transferFocusToMapCanvas()
    {
        CanvasController.getInstance().getMapCanvas().grabFocus();
    }

    public void themeHasChanged()
    {
        ToolbarController.getInstance().themeHasChanged();
        CanvasController.getInstance().themeHasChanged();
        PointsOfInterestController.getInstance().themeHasChanged();
        setToolTipTheme();
        transferFocusToMapCanvas();
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
        CanvasController.getInstance().getMapCanvas().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    }

    private class MainWindowInteractionHandler
        extends MainWindowController.WindowInteractionHandler {

        @Override
        public void componentResized(ComponentEvent e)
        {
            super.componentResized(e);
            adjustBounds();
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
        }
    }
}
