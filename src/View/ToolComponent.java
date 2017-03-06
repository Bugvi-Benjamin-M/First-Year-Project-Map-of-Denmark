package View;

import javax.swing.*;
import java.awt.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class ToolComponent extends JPanel{
    private ImageIcon icon;
    private String tagline = "not found";

    public ToolComponent(String path, String tagline) {
        super();
        if (tagline != null) this.tagline = tagline;
        icon = createImageIcon(path, this.tagline);
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.add(new JLabel(icon));
        this.add(new JLabel(tagline));
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    private ImageIcon createImageIcon(String path,
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
