package Controller;

import Controller.ToolbarControllers.ToolbarController;
import Enums.ToolbarType;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import Helpers.Utilities.DebugWindow;
import View.PopupWindow;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Búgvi Magnussen on 14-03-2017.
 */
public final class MainWindowController extends WindowController {

    private static final String MAIN_TITLE = "OSM Map Viewer v0.3";
    private static MainWindowController instance;
    private JLayeredPane layeredPane;


    private MainWindowController() {
        super();
    }

    public static MainWindowController getInstance() {
        if(instance == null) {
            instance = new MainWindowController();
        }
        return instance;
    }

    public void setupMainWindow() {
       window = new Window().title(MAIN_TITLE)
                .closeOperation(WindowConstants.EXIT_ON_CLOSE)
                .dimension(new Dimension(1200, 1000))
                .extendedState(JFrame.MAXIMIZED_BOTH)
                .relativeTo(null)
                .layout(new BorderLayout())
                .icon()
                .hide();
        window.setMinimumWindowSize(new Dimension(650, 500));
        setupToolbar();
        setupCanvas();
        setupInfobar();
        setupLayeredPane();
        addInteractionHandlerToWindow();
        CanvasController.adjustToBounds();
        hideWindow();
    }

    private void setupLayeredPane() {
        layeredPane = new JLayeredPane();
        window.getFrame().add(layeredPane, BorderLayout.CENTER);
        adjustBounds();
        ToolbarController.getInstance().getToolbar().setOpaque(true);
        CanvasController.getInstance().getMapCanvas().setOpaque(true);
        layeredPane.add(CanvasController.getInstance().getMapCanvas(), new Integer(1));
        layeredPane.add(ToolbarController.getInstance().getToolbar(), new Integer(2));
    }

    private void adjustBounds() {
        layeredPane.setBounds(new Rectangle(window.getFrame().getWidth(), window.getFrame().getHeight()));
        ToolbarController.getInstance().getToolbar().setBounds(0,0,window.getFrame().getWidth(), GlobalValue.getToolbarWidth());
        CanvasController.getInstance().getMapCanvas().setBounds(0,0,window.getFrame().getWidth(), window.getFrame().getHeight());
    }

    private void setupToolbar() {
        ToolbarController.getInstance().specifyWindow(window);
        ToolbarController.getInstance().setupToolbar(ToolbarType.LARGE);
    }

    private void setupCanvas() {
        CanvasController.getInstance().specifyWindow(window);
        CanvasController.getInstance().setupCanvas();
    }

    private void setupInfobar() {
        InfobarController.getInstance().specifyWindow(window);
        InfobarController.getInstance().setupInfobar();
    }

    public void transferFocusToMapCanvas() {
        CanvasController.getInstance().getMapCanvas().grabFocus();
    }

    public void themeHasChanged() {
        //layeredPane.remove(ToolbarController.getInstance().getToolbar());
        ToolbarController.getInstance().themeHasChanged();
        CanvasController.getInstance().themeHasChanged();
        transferFocusToMapCanvas();
        //ToolbarController.getInstance().getToolbar().setOpaque(true);
        //adjustBounds();
        //layeredPane.add(ToolbarController.getInstance().getToolbar(), new Integer(2));
    }

    @Override
    protected void specifyKeyBindings() {
        handler.addKeyBinding(KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ToolbarController.getInstance().doesSearchbarHaveFocus()) return;
                else {
                    Toolkit.getDefaultToolkit().beep();
                    if (PopupWindow.confirmBox(null, "Do You Wish to Quit OSM Visualiser?",
                            "PLease Confirm!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            }
        });
        handler.addKeyBinding(KeyEvent.VK_D, OSDetector.getActivationKey(), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DebugWindow.getInstance().show();
            }
        });
    }
    @Override
    protected void addInteractionHandlerToWindow() {
        super.addInteractionHandlerToWindow();
        MainWindowInteractionHandler handler = new MainWindowInteractionHandler();
        window.getFrame().addComponentListener(handler);
    }

    public void resetInstance() {
        instance = null;
    }

    public void requestCanvasRepaint() {
        CanvasController.getInstance().getMapCanvas().repaint();
    }

    private class MainWindowInteractionHandler extends MainWindowController.WindowInteractionHandler {

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            adjustBounds();
            ToolbarController.getInstance().resizeEvent();
            CanvasController.getInstance().resizeEvent();
        }


        @Override
        public void componentMoved(ComponentEvent e) {
            super.componentMoved(e);
            ToolbarController.getInstance().moveEvent();
        }
    }
}