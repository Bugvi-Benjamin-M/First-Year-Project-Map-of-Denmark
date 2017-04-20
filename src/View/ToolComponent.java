package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

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
        defaultColor = Helpers.ThemeHelper.color("icon");
    }

    abstract void setupLayout();

    public Color getDefaultColor() {
        return defaultColor;
    }


    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return new Point((int) event.getPoint().getX()+20,(int) event.getPoint().getY()+20);
    }
}
