package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The SettingsButtons is a collection of different buttons to be used for
 * the settings window
 */
public class SettingsButtons extends View {

    private JButton apply;
    private JButton defaultSet;
    private List<JButton> buttons;

    public SettingsButtons()
    {
        buttons = new ArrayList<>();
        setPreferredSize(new Dimension(400, 50));
        createButtons();
        addButtons();
    }

    private void createButtons()
    {
        buttons.add(apply = new JButton("Apply Changes"));
        buttons.add(defaultSet = new JButton("Default Settings"));
    }

    /**
     * Applies an action listener to the apply button
     */
    public void addActionToApplyButton(ActionListener e)
    {
        apply.addActionListener(e);
    }

    /**
     * Applies an action listener to the default button
     */
    public void addActionToDefaultButton(ActionListener e)
    {

        defaultSet.addActionListener(e);
    }

    /**
     * Adds all buttons to the visual component
     */
    private void addButtons()
    {
        for (JButton button : buttons) {
            add(button);
        }
    }
}
