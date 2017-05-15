package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * The MenuTool is a visual component that consists of a collection of tool
 * functionality's that would otherwise not be able to be shown on the toolbar.
 */
public class MenuTool extends ToolComponent {

    private JPopupMenu popupMenu;
    private SpringLayout layout;

    public MenuTool() { setupLayout(); }

    @Override
    void setupLayout()
    {
        popupMenu = new JPopupMenu() {
            @Override
            public void paintComponent(Graphics g)
            {
                g.setColor(ThemeHelper.color("toolbar"));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        layout = new SpringLayout();
        popupMenu.setLayout(layout);
    }

    /**
     * Returns the popup menu
     */
    public JPopupMenu getPopupMenu() { return popupMenu; }

    /**
     * Adds a tool to the menu
     * @param tool The tool component to be added
     */
    public void addTool(ToolComponent tool) { popupMenu.add(tool); }

    /**
     * Shows the popup menu
     */
    public void showPopupMenu() { popupMenu.setVisible(true); }

    /**
     * Hides the popup menu
     */
    public void hidePopupMenu() { popupMenu.setVisible(false); }

    /**
     * Changes the location of the popupmenu
     * @param point A point on the window
     */
    public void setLocation(Point point) { popupMenu.setLocation(point); }

    /**
     * Returns whether the popup menu is visible or not
     */
    public boolean isVisible() { return popupMenu.isVisible(); }

    /**
     * Retrieves the layout of the menu tool
     */
    public SpringLayout getLayout() { return layout; }
}
