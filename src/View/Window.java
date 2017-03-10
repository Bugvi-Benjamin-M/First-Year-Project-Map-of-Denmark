package View;

import javax.swing.*;
import java.awt.*;

/**
 * Class details:
 * The Window class is the primary visual presentation of the program and
 * contains all visual elements directly or indirectly.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @version 10-03-2017.
 */
public class Window {

    private final static String TITLE = "OSM Map Viewer v0.1";

    private JFrame window;
    private Dimension dimension;

    /** Constructor for Window objects. At default the program opens with the window maximized. */
    public Window() {
        window = new JFrame(TITLE);

        window.setLayout(new BorderLayout());
        dimension = new Dimension(1200,1000);
        window.setPreferredSize(dimension);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Adds a visual component to the window in a given layout position
     * @param layout A specific BorderLayout constant
     * @param component A visual component that extends the View class
     * @see BorderLayout
     */
    public void addComponent(String layout, View component) {
        window.add(component,layout);
        setVisible(true);
    }

    /**
     * Sets the visibility of the entire window
     */
    public void setVisible(boolean visibility) {
        window.setVisible(visibility);
    }

    public Dimension getDimension() {
        return dimension;
    }
}
