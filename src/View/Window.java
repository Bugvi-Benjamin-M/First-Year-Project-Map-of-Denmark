package View;

import javax.swing.*;
import java.awt.*;
import Helpers.ThemeHelper;

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
        //
    }


    public Window title(String title){
        this.title = title;
        window = new JFrame(title);
        return this;
    }

    public Window closeOperation(int closeOperation){
        this.closeOperation = closeOperation;
        window.setDefaultCloseOperation(closeOperation);
        return this;
    }

    public Window dimension(Dimension dimension){
        this.dimension = dimension;
        window.setPreferredSize(dimension);
        return this;
    }

    public Window layout(LayoutManager layout){
        this.layout = layout;
        window.setLayout(layout);
        window.pack();
        return this;
    }

    public Window relativeTo(JFrame relativeTo){
        this.relativeTo = relativeTo;
        window.setLocationRelativeTo(relativeTo);
        return this;
    }

    public Window extendedState(int extendedState){
        this.extendedState = extendedState;
        window.setExtendedState(extendedState);
        return this;
    }

    public Window show(){
        setVisible(true);
        return this;
    }

    public Window hide() {
        setVisible(false);
        return this;
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
