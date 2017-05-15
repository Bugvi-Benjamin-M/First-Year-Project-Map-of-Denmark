package View;

import Helpers.ThemeHelper;

import javax.swing.*;


public class JourneyPlannerBar extends View {

    private SpringLayout layout;

    public JourneyPlannerBar() {
        applyTheme();
        layout = new SpringLayout();
        setLayout(layout);
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
    }

}
