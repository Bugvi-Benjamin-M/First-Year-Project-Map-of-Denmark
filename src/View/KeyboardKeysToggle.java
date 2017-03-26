package View;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Búgvi Magnussen on 26-03-2017.
 */
public class KeyboardKeysToggle extends View {

    private JCheckBox toggleBox;
    private JLabel toggleLabel;

    public KeyboardKeysToggle() {
        setPreferredSize(new Dimension(400, 50));
        toggleLabel = new JLabel("Toggle Keyboard Shortcuts:");
        toggleBox = new JCheckBox();
        add(toggleLabel);
        add(toggleBox);
        toggleBox.setSelected(true);
    }

    public boolean isToggleSelected() {
        return toggleBox.isSelected();
    }

    public void setSelectedStatus(boolean isSelected) {
        toggleBox.setSelected(isSelected);
    }


}
