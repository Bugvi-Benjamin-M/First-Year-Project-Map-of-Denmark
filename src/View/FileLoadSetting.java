package View;

import Helpers.FontAwesome;

import javax.swing.*;
import java.awt.*;

/**
 * The FileLoadSetting is visual component where from the user is able to
 * specify which file the program should start up with when loading.
 */
public class FileLoadSetting extends View {

    private JLabel label;
    private JButton chooseButton;
    private JButton defaultButton;
    private JTextField fileField;
    private Dimension defaultSize;

    public FileLoadSetting() {
        chooseButton = new JButton("\uf15b");
        chooseButton.setFont(FontAwesome.getFontAwesome().deriveFont(14f));
        chooseButton.setPreferredSize(new Dimension(60,35));
        defaultButton = new JButton("\uf0e2");
        defaultButton.setFont(FontAwesome.getFontAwesome().deriveFont(14f));
        defaultButton.setPreferredSize(new Dimension(60,35));
        label = new JLabel("Load on Startup:");
        fileField = new JTextField();
        fileField.setPreferredSize(new Dimension(300, 40));
        fileField.setEnabled(false);
        setPreferredSize(defaultSize = new Dimension(500, 40));
        add(label);
        add(fileField);
        add(chooseButton);
        add(defaultButton);
        chooseButton.setToolTipText("Choose a File to Load on Startup");
        defaultButton.setToolTipText("Set Startup File to Default");
    }

    /**
     * Retrieves the chooser button
     */
    public JButton getChooseButton() {
        return chooseButton;
    }

    /**
     * Changes the text hint in the path field
     */
    public void setTextField(String text) {
        fileField.setText(text);
    }

    /**
     * Returns the default button
     */
    public JButton getDefaultButton() {
        return defaultButton;
    }


}
