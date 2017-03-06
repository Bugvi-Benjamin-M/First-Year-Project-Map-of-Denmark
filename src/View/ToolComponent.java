package View;

import javax.swing.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
abstract class ToolComponent extends JPanel {

    abstract void setupLayout();

    /** Returns an ImageIcon, or null if the path was invalid. */
    ImageIcon createImageIcon(String path,
                              String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
