package Controller;

import Controller.ToolbarControllers.ToolbarController;
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
    private final int DELAY = 200;
    private javax.swing.Timer waitingTimer;

    private MainWindowController() {
        super(setupWindow());
        SwingUtilities.invokeLater(() -> {
            ToolbarController.getInstance();
            InfobarController.getInstance();
            CanvasController.getInstance();
            CanvasController.adjustToBounds();
            CanvasController.getInstance().getMapCanvas().grabFocus();
        });
    }

    public static MainWindowController getInstance() {
        if(instance == null) {
            instance = new MainWindowController();
        }
        return instance;
    }

    private static Window setupWindow() {
       Window mainWindow = new Window().title(MAIN_TITLE)
                .closeOperation(WindowConstants.EXIT_ON_CLOSE)
                .dimension(new Dimension(1200, 1000))
                .extendedState(JFrame.MAXIMIZED_BOTH)
                .relativeTo(null)
                .layout(new BorderLayout())
                .icon()
                .hide();
        mainWindow.setMinimumWindowSize(new Dimension(650, 500));
        return mainWindow;
    }

    @Override
    protected void specifyKeyBindings() {
        handler.addKeyBinding(KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Toolkit.getDefaultToolkit().beep();
                if(PopupWindow.confirmBox(null, "Do You Wish to Quit OSM Visualiser?",
                        "PLease Confirm!", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    System.exit(0);
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

    private class MainWindowInteractionHandler extends MainWindowController.WindowInteractionHandler {

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            ToolbarController.getInstance().resizeEvent();
            CanvasController.getInstance().resizeEvent();
            if(waitingTimer == null) {
                waitingTimer = new Timer(DELAY, e1 -> {
                    if(e1.getSource()==waitingTimer) {
                        waitingTimer.stop();
                        waitingTimer = null;
                        CanvasController.getInstance().resizeEvent();
                    }
                });
                waitingTimer.start();
            } else waitingTimer.restart();
        }


        @Override
        public void componentMoved(ComponentEvent e) {
            super.componentMoved(e);
            ToolbarController.getInstance().moveEvent();
        }
    }
}