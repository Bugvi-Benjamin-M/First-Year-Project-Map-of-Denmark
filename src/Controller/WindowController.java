package Controller;

import Helpers.OSDetector;
import View.PopupWindow;
import View.Window;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.security.Key;

/**
 * Created by Búgvi Magnussen on 14-03-2017.
 */
public final class WindowController extends Controller {

    private static WindowController instance;
    private WindowInteractionHandler handler;

    private WindowController(Window window) {
        super(window);
        addInteractionHandlerToWindow();
    }

    public static WindowController getInstance(Window window) {
        if(instance == null) {
            instance = new WindowController(window);
        }
        return instance;
    }

    private void addInteractionHandlerToWindow() {
        handler = new WindowInteractionHandler((JPanel) window.getFrame().getContentPane());
        window.getFrame().addComponentListener(handler);
        specifyKeyBindings();

    }

    private void specifyKeyBindings() {
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
    }

    public void resetInstance() {
        instance = null;
    }


    private class WindowInteractionHandler extends ComponentAdapter {

        private JPanel content;

        public WindowInteractionHandler(JPanel content) {
            this.content = content;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            System.out.println("Window Resized");
        }

        private void addKeyBinding(int key, int activationKey, AbstractAction event) {
            content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                    put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            content.getActionMap().put(event.toString(), event);

        }
    }
}
