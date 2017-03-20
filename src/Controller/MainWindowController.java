package Controller;

import View.PopupWindow;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Búgvi Magnussen on 14-03-2017.
 */
public final class MainWindowController extends WindowController {

    private static MainWindowController instance;

    private MainWindowController(Window window) {
        super(window);
    }

    public static WindowController getInstance(Window window) {
        if(instance == null) {
            instance = new MainWindowController(window);
        }
        return instance;
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
    }

    public void resetInstance() {
        instance = null;
    }
}
