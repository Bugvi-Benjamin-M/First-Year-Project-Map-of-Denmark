package Controller;

import Helpers.OSDetector;
import Helpers.Utilities.DebugWindow;
import View.MapCanvas;
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
    private MainWindowController(Window window) {
        super(window);
    }

    public static MainWindowController getInstance() {
        if(instance == null) {
            Window mainWindow = setupWindow();
            instance = new MainWindowController(mainWindow);
        }
        return instance;
    }

    private static Window setupWindow() {
       Window mainWindow = new Window().title(MAIN_TITLE)
                .closeOperation(WindowConstants.EXIT_ON_CLOSE)
                .dimension(new Dimension(1200, 1000))
                .extendedState(JFrame.MAXIMIZED_BOTH)
                .layout(new BorderLayout())
                .relativeTo(null)
                .icon()
                .hide();
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
        window.getFrame().getContentPane().addComponentListener(handler); //THINK THIS THROUGH
    }

    public void resetInstance() {
        instance = null;
    }

    private class MainWindowInteractionHandler extends MainWindowController.WindowInteractionHandler {

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            ToolbarController.getInstance(window).resizeEvent();
            CanvasController.getInstance(window).resizeEvent();
        }
    }
}