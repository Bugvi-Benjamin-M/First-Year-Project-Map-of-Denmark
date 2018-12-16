package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * A ToolComponent is an abstract tool that can be used in a toolbar
 */
public abstract class ToolComponent extends JPanel {

    private Color defaultColor;
    private boolean activated;

    ToolComponent()
    {
        super();
        defaultColor = Helpers.ThemeHelper.color("icon");
        activated = false;
    }

    /**
     * Sets up the specific layout for any Tool Component
     */
    abstract void setupLayout();

    /**
     * Activate or deactivates the tool
     */
    public void toggleActivate(boolean state)
    {
        activated = state;
        for(Component component : this.getComponents()) {
            if(getActivatedStatus()) component.setForeground(ThemeHelper.color("toolActivated"));
            else component.setForeground(ThemeHelper.color("icon"));
        }
    }

    /**
     * Activates or deactiviates hovering over the tool
     */
    public void toggleHover(boolean hover) {
        for(Component component : this.getComponents()) {
            if(hover) component.setForeground(ThemeHelper.color("toolHover"));
            else component.setForeground(ThemeHelper.color("icon"));
        }
    }

    /**
     * Returns whether this tool is activated
     */
    public boolean getActivatedStatus() { return activated; }
}
