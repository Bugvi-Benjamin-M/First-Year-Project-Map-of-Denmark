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
    private final int DELAY = 200;
    private javax.swing.Timer waitingTimer;
    private JLayeredPane layeredPane;
    private JPanel panel;
    private JPanel panel2;

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
        setupLayeredPane();
        addToolbarToMainWindow();
        addCanvasToMainWindow();
        addInfobarToMainWindow();
        addInteractionHandlerToWindow();
        CanvasController.adjustToBounds();
        window.getFrame().pack();
        hideWindow();
    }

    private void setupLayeredPane() {
        layeredPane = new JLayeredPane();
        window.getFrame().add(layeredPane, BorderLayout.CENTER);
        layeredPane.setBounds(new Rectangle(window.getFrame().getWidth(), window.getFrame().getHeight()));

        panel = new JPanel(new BorderLayout());
        panel.setBounds(0,GlobalValue.getToolbarWidth(),window.getFrame().getWidth(), window.getFrame().getHeight());
        panel.setBackground(Color.BLUE);
        panel.setOpaque(true);
        layeredPane.setLayer(panel, 0);
        panel2 = new JPanel(new BorderLayout());
        panel2.setBackground(Color.GREEN);
        panel2.setBounds(0,0,window.getFrame().getWidth(), GlobalValue.getToolbarWidth());
        panel2.setOpaque(true);
        layeredPane.setLayer(panel2, 1);
        layeredPane.add(panel, new Integer(0), 0);
        layeredPane.add(panel2, new Integer(1), 0);
    }

    private void addToolbarToMainWindow() {
        ToolbarController.getInstance().specifyWindow(window);
        ToolbarController.getInstance().setupToolbar(ToolbarType.LARGE);
        panel2.add(ToolbarController.getInstance().getToolbar(), BorderLayout.CENTER);
        //window.addBorderLayoutComponent(BorderLayout.NORTH, ToolbarController.getInstance().getToolbar(), true);
        //ToolbarController.getInstance().getToolbar().setVisible(true);
    }

    private void addCanvasToMainWindow() {
        CanvasController.getInstance().specifyWindow(window);
        CanvasController.getInstance().setupCanvas();
        panel.add(CanvasController.getInstance().getMapCanvas(), BorderLayout.CENTER);
        //window.addBorderLayoutComponent(BorderLayout.CENTER, CanvasController.getInstance().getMapCanvas(), true);
        //CanvasController.getInstance().getMapCanvas().setVisible(true);
    }

    private void addInfobarToMainWindow() {
        InfobarController.getInstance().specifyWindow(window);
        InfobarController.getInstance().setupInfobar();
        //window.addBorderLayoutComponent(BorderLayout.LINE_START, InfobarController.getInstance().getInfobar(), true);
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

    private class MainWindowInteractionHandler extends MainWindowController.WindowInteractionHandler {

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            layeredPane.setBounds(0,0,window.getFrame().getWidth(), window.getFrame().getHeight());
            panel.setBounds(0,GlobalValue.getToolbarWidth(),window.getFrame().getWidth(), window.getFrame().getHeight());
            panel2.setBounds(0,0,window.getFrame().getWidth(), GlobalValue.getToolbarWidth());
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