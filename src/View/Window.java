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

    private final static String MAIN_TITLE = "OSM Map Viewer v0.2";

    private JFrame window;
    private Dimension dimension;
    private LayoutManager layout;
    private String title;
    private int closeOperation;
    private int extendedState;
    private JFrame relativeTo;

    /** Default constructor for Window objects. At default the program opens with the window maximized.
     * This constructor is used to create the main window of the application.
     *
     */
    public Window() {
        this(MAIN_TITLE, WindowConstants.EXIT_ON_CLOSE, new Dimension(1200, 1000),
                null, JFrame.MAXIMIZED_BOTH, new BorderLayout());
    }

    /**
     * Creates a window with specific attributes
     * @param closeOperation what should happen when the window is closed
     * @param dimension the dimension of the window
     * @param relativeTo the location of the window relative to the given JFrame
     * @param extendedState the size state of the window
     */
    public Window(String title, int closeOperation, Dimension dimension, JFrame relativeTo, int extendedState, LayoutManager layout) {
        this.title = title;
        window = new JFrame(title);
        this.layout = layout;
        window.setLayout(layout);
        this.dimension = dimension;
        window.setPreferredSize(dimension);
        this.closeOperation = closeOperation;
        window.setDefaultCloseOperation(closeOperation);
        window.pack();
        this.extendedState = extendedState;
        window.setExtendedState(extendedState);
        this.relativeTo = relativeTo;
        window.setLocationRelativeTo(relativeTo);
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

    public JFrame getFrame() {
        return window;
    }
}
