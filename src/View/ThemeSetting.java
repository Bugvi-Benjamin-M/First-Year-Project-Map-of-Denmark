package View;

import javax.swing.*;
import java.awt.*;

/**
 * The ThemeSetting is a visual component from which the user is able to change
 * the the current theme of the program.
 */
public class ThemeSetting extends View {

    private JLabel themeLabel;
    private JComboBox<String> themeList;

    /** Constructor of the ThemeSetting */
    public ThemeSetting()
    {
        setPreferredSize(new Dimension(400, 200));

        themeLabel = new JLabel("Theme:");

        String[] themes = new String[] { "Default", "Night" };
        themeList = new JComboBox<>(themes);
        add(themeLabel);
        add(themeList);
    }

    /**
     * Retrieves the currently selected theme
     */
    public String getSelectedTheme()
    {
        return themeList.getSelectedItem().toString();
    }

    /**
     * Sets the selected theme to the default theme
     */
    public void setSelectedThemeToDefault() { themeList.setSelectedIndex(0); }

    /**
     * Sets the selected theme to a given theme ID
     * @param theme An ID for a theme
     */
    public void setSelectedTheme(String theme)
    {
        themeList.setSelectedItem(theme);
    }
}
