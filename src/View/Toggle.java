package View;

import javax.swing.*;
import java.awt.*;

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

    public boolean isToggleSelected() { return toggleBox.isSelected(); }

    public void setSelectedStatus(boolean isSelected)
    {
        toggleBox.setSelected(isSelected);
    }
}
