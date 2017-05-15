package View;

import Helpers.FontAwesome;

import javax.swing.*;
import java.awt.*;


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

    public JButton getChooseButton() {
        return chooseButton;
    }

    public void setTextField(String text) {
        fileField.setText(text);
    }

    public JButton getDefaultButton() {
        return defaultButton;
    }


}
