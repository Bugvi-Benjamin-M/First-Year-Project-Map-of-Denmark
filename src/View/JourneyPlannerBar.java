package View;

import Helpers.ThemeHelper;

import javax.swing.*;

/**
 * The JourneyPlannerBar is a retractable visual component that consists
 * of components used when trying to find a route between two roads
 */
public class JourneyPlannerBar extends View {

    private SpringLayout layout;

    public JourneyPlannerBar() {
        applyTheme();
        layout = new SpringLayout();
        setLayout(layout);
    }

    /**
     * Updates and applies the currently selected theme to the component
     */
    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
    }

}
