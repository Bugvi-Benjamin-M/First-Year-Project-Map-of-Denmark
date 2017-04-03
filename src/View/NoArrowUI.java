package View;

import Helpers.ThemeHelper;

import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import javax.swing.*;
import java.awt.*;

public class NoArrowUI extends BasicComboBoxUI {

    public static NoArrowUI createUI(JComponent c) {
        return new NoArrowUI();
    }

    @Override
    protected JButton createArrowButton() {
        JButton button = new JButton();
        button.setEnabled(false);
        button.setBorderPainted( false );
        button.setFocusPainted( false );
        button.setBackground(ThemeHelper.color("toolbar"));
        return button;
    }
}
