package View;

import javax.swing.*;
import java.awt.*;


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

    public String getSelectedTheme()
    {
        return themeList.getSelectedItem().toString();
    }

    public void setSelectedThemeToDefault() { themeList.setSelectedIndex(0); }

    public void setSelectedTheme(String theme)
    {
        themeList.setSelectedItem(theme);
    }
}
