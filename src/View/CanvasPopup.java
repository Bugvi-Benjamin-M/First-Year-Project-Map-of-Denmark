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


    public CanvasPopup() {
        popupMenu = new JPopupMenu() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(ThemeHelper.color("canvasPopupBackground"));
                g.fillRect(0,0, getWidth(), getHeight());
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
        popupMenu.setLocation(x,y);
    }

    public boolean isVisible() {
        return popupMenu.isVisible();
    }

    public void setSize(int width, int height) {
        popupMenu.setPopupSize(width, height);
    }
}
