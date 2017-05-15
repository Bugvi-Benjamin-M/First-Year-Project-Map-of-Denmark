package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;


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

    public JPopupMenu getPopupMenu() { return popupMenu; }

    public void addTool(ToolComponent tool) { popupMenu.add(tool); }

    public void showPopupMenu() { popupMenu.setVisible(true); }

    public void hidePopupMenu() { popupMenu.setVisible(false); }

    public void setLocation(Point point) { popupMenu.setLocation(point); }

    public boolean isVisible() { return popupMenu.isVisible(); }

    public SpringLayout getLayout() { return layout; }
}
