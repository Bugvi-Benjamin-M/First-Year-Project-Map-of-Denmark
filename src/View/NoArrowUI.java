package View;

import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.*;
import java.awt.*;

public class NoArrowUI extends BasicComboBoxUI {

    public static NoArrowUI createUI(JComponent c) {
        return new NoArrowUI();
    }

    @Override
    protected JButton createArrowButton() {
        return null;
    }
}
