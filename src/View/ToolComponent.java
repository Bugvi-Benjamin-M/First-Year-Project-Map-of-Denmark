package View;

import javax.swing.*;
import javax.swing.border.Border;
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

    ToolComponent() {
        super();
        defaultColor = Helpers.ThemeHelper.color("toolbar");
    }

    abstract void setupLayout();

    Color getDefaultColor() {
        return defaultColor;
    }
}
