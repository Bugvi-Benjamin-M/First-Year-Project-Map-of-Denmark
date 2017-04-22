package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 20/04/2017
 */
public class CanvasPopup extends View {

    private JPopupMenu popupMenu;
    private final int DISMISS_DELAY = 2500;
    private Timer dismisstimer;

    public CanvasPopup() {
        popupMenu = new JPopupMenu() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(ThemeHelper.color("canvasPopupBackground"));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        popupMenu.setLayout(new BorderLayout());
    }

    public void addToPopup(JComponent component) {
        popupMenu.add(component);
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void showPopupMenu() {
        popupMenu.setVisible(true);
    }

    public void hidePopupMenu() {
        popupMenu.setVisible(false);
    }

    public void setLocation(int x, int y) {
        popupMenu.setLocation(x, y);
    }

    public boolean isVisible() {
        return popupMenu.isVisible();
    }

    public void setSize(int width, int height) {
        popupMenu.setPopupSize(width, height);
    }

    public void startDismissTimer() {
        if (dismisstimer == null) {
            dismisstimer = new Timer(DISMISS_DELAY, a -> {
                if (a.getSource() == dismisstimer) {
                    dismisstimer.stop();
                    dismisstimer = null;
                    popupMenu.setVisible(false);
                    popupMenu = null;
                }
            });
            dismisstimer.start();
        } else dismisstimer.restart();
    }


    public void stopDismissTimer() {
        if(dismisstimer != null) dismisstimer.stop();
        dismisstimer = null;
    }
}


