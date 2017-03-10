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
abstract class ToolComponent extends JPanel {

    private boolean isWellActivated;
    private Color hoverColor;
    private Color defaultColor;

    ToolComponent() {
        defaultColor = new Color(238,238,238);
        hoverColor = new Color(202, 202, 202);
        isWellActivated = false;
    }

    abstract void setupLayout();

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @see <a href="https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html">
     *     Dublicated code from this tutorial made by the Oracle Team</a>
     */
    ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /** Toggles hovering on component */
    public void toggleWell() {
        isWellActivated = !isWellActivated;
    }

    boolean isWellActivated() {
        return isWellActivated;
    }

    Color getHoverColor() {
        return hoverColor;
    }

    Color getDefaultColor() {
        return defaultColor;
    }
}
