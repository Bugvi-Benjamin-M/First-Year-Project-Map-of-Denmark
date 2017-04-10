package View;

import javax.swing.*;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 05/04/2017
 */
public class MenuTool extends ToolComponent {

    private JPopupMenu popupMenu;
    private SpringLayout layout;

    public MenuTool() {
        setupLayout();
    }

    @Override
    void setupLayout() {
        popupMenu = new JPopupMenu();
        layout = new SpringLayout();
        popupMenu.setLayout(layout);
    }

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void addTool(ToolComponent tool) {
        popupMenu.add(tool);
    }

    public void showPopupMenu() {
        popupMenu.setVisible(true);
    }

    public void hidePopupMenu() {
        popupMenu.setVisible(false);
    }

    public void setLocation(Point point) {
        popupMenu.setLocation(point);
    }

    public boolean isVisible() {
        return popupMenu.isVisible();
    }

    public SpringLayout getLayout() {
        return layout;
    }

    public void setBackGroundColor(Color color) {
        popupMenu.setBackground(color);
    }

    public void setForeGroundColor(Color color) {
        popupMenu.setForeground(color);
    }

}
