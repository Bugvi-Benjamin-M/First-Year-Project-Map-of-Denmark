package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 25/03/2017
 */
public class SettingsSouthButtons extends View {

    private JButton apply;
    private JButton defaultSet;
    private List<JButton> buttons;

    public SettingsSouthButtons() {
        buttons = new ArrayList<>();
        setPreferredSize(new Dimension(400, 50));
        createButtons();
        addButtons();
    }

    private void createButtons() {
        buttons.add(apply = new JButton("Apply Changes"));
        buttons.add(defaultSet = new JButton("Default Settings"));
    }

    public void addActionToApplyButton(ActionListener e) {

        apply.addActionListener(e);

    }

    public void addActionToDefaultButton(ActionListener e) {

        defaultSet.addActionListener(e);

    }

    private void addButtons() {
        for (JButton button : buttons) {
            add(button);
        }
    }



}
