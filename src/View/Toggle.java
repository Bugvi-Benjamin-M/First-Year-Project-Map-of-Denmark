package View;

import javax.swing.*;
import java.awt.*;

/**
 * A visual component consisting of a checkbox and a descriptive label
 */
public abstract class Toggle extends View {

    protected JCheckBox toggleBox;
    protected JLabel toggleLabel;
    protected Dimension dimension;

    public Toggle(String description)
    {
        setPreferredSize(dimension = new Dimension(400, 40));
        toggleBox = new JCheckBox();
        toggleLabel = new JLabel(description);
        add(toggleLabel);
        add(toggleBox);
    }

    /**
     * Returns whether the checkbox is selected or not
     */
    public boolean isToggleSelected() { return toggleBox.isSelected(); }

    /**
     * Changes the status of the checkbox from either selected or not selected
     */
    public void setSelectedStatus(boolean isSelected)
    {
        toggleBox.setSelected(isSelected);
    }
}
