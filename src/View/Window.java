package View;

import Exceptions.IncorrectLayoutException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;

/**
 * Class details:
 * The Window class is the primary visual presentation of the program and
 * contains all visual elements directly or indirectly.
 */
public class Window {

    private static final String ICON_PATH = "/icon.png";

    private JFrame window;
    private Dimension dimension;
    private LayoutManager layout;
    private String title;
    private int closeOperation;
    private int extendedState;
    private JFrame relativeTo;

    public Window() {
    }

    /**
     * Set icon and return window
     */
    public Window icon() {
        java.net.URL imgURL = this.getClass().getResource(ICON_PATH);
        if (imgURL != null) {
            window.setIconImage(new ImageIcon(imgURL, "OSM Visualizer").getImage());
            ;
        } else {
            System.err.println("Couldn't find file: " + ICON_PATH);
        }
        return this;
    }

    /**
     * Set title and return window
     */
    public Window title(String title) {
        this.title = title;
        window = new JFrame(title);
        return this;
    }

    /**
     * Set close operation and return window
     */
    public Window closeOperation(int closeOperation) {
        this.closeOperation = closeOperation;
        window.setDefaultCloseOperation(closeOperation);
        return this;
    }

    /**
     * Set dimension and return window
     */
    public Window dimension(Dimension dimension) {
        this.dimension = dimension;
        window.setPreferredSize(dimension);
        return this;
    }

    /**
     * Set layout manager and return window
     */
    public Window layout(LayoutManager layout) {
        this.layout = layout;
        window.setLayout(layout);
        window.pack();
        return this;
    }

    /**
     * Set relative to frame position and return window
     */
    public Window relativeTo(JFrame relativeTo) {
        this.relativeTo = relativeTo;
        window.setLocationRelativeTo(relativeTo);
        return this;
    }

    /**
     * Set extended state and return window
     */
    public Window extendedState(int extendedState) {
        this.extendedState = extendedState;
        window.setExtendedState(extendedState);
        return this;
    }

    /**
     * Show window and return window
     */
    public Window show() {
        window.setVisible(true);
        return this;
    }

    /**
     * Hide window and return window
     */
    public Window hide() {
        window.setVisible(false);
        return this;
    }

    /**
   * Adds a visual component to the window in a given layout position.
   * Reminder, this method requires the window to have BorderLayout. If this
   * method
   * is called and the layout is not BorderLayout, a RuntimeException will be
   * thrown.
   * @param layout A specific BorderLayout constant
   * @param component A visual component that extends the View class
   * @see BorderLayout
   */
    public void addBorderLayoutComponent(String layout, View component,
        boolean display)
    {
        checkLayout("BorderLayout");
        window.add(component, layout);
        if (display)
            show();
    }

    /**
     * Remove component from window
     * @param component The component to remove
     */
    public void removeComponent(View component) { window.remove(component); }

    /**
   * Checks that the layout of the window matches the parameter. If not, throws
   * an IncorrectLayout Exception.
   * @param layout the layout to check for
   */
    private void checkLayout(String layout)
    {
        if (!window.getLayout().toString().contains(layout)) {
            throw new IncorrectLayoutException("Layout of the Window is not " + layout);
        }
    }

    /**
     * Retrieves current dimension
     */
    public Dimension getDimension() { return dimension; }

    /**
     * Retrieves the frame of the window
     */
    public JFrame getFrame() { return window; }

    /**
     * Sets the minimum window size to a dimension
     */
    public void setMinimumWindowSize(Dimension dimension)
    {
        getFrame().setMinimumSize(dimension);
    }

    /**
     * Adds a window listener to the window that can
     * respond to window interaction
     */
    public void addWindowAdapter(WindowListener listener)
    {
        window.addWindowListener(listener);
    }
}
