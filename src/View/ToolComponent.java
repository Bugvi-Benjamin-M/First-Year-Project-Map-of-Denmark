package View;

import javax.swing.*;
import java.awt.*;

/**
 * Class details:
 *
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @author Oracle Team
 * @version 06-03-2017.
 */
public abstract class ToolComponent extends JPanel {

    private Color defaultColor;
    private boolean activated;

    ToolComponent()
    {
        super();
        defaultColor = Helpers.ThemeHelper.color("icon");
        activated = false;
    }

    abstract void setupLayout();

    public Color getDefaultColor() { return defaultColor; }

    public void toggleActivate(boolean state)
    {
        activated = state;
        paintComponent(getGraphics());
    }

    protected boolean getActivatedStatus() { return activated; }
}
