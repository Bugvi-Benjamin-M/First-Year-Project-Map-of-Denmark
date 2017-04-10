package View;

import Exceptions.IncorrectLayoutException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowListener;

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

    private static final String ICON_PATH = "/icon.png";

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

    public Window icon() {
        java.net.URL imgURL = this.getClass().getResource(ICON_PATH);
        if (imgURL != null) {
            window.setIconImage(new ImageIcon(imgURL, "OSM Visualizer").getImage());;
        } else {
            System.err.println("Couldn't find file: " + ICON_PATH);
        }
        return this;
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
        if(layout == null) {
            BoxLayout box = new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS);
            this.layout = box;
            window.setLayout(this.layout);
            //window.pack();
            return this;
        } else {
            this.layout = layout;
            window.setLayout(layout);
            window.pack();
            return this;
        }
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
        window.setVisible(true);
        return this;
    }

    public Window hide() {
        window.setVisible(false);
        return this;
    }

    /**
     * Adds a visual component to the window in a given layout position.
     * Reminder, this method requires the window to have BorderLayout. If this method
     * is called and the layout is not BorderLayout, a RuntimeException will be thrown.
     * @param layout A specific BorderLayout constant
     * @param component A visual component that extends the View class
     * @see BorderLayout
     */
    public void addBorderLayoutComponent(String layout, View component, boolean display) {
        checkLayout("BorderLayout");
        window.add(component,layout);
        if (display) show();
    }

    public void addBoxLayoutComponent(View component, int position) {
        window.add(component, position);
    }

    public void removeComponent(View component) {
        window.remove(component);
    }

    /**
     * Checks that the layout of the window matches the parameter. If not, throws
     * an IncorrectLayout Exception.
     * @param layout the layout to check for
     */
    private void checkLayout(String layout) {
        if(!window.getLayout().toString().contains(layout)) {
            throw new IncorrectLayoutException("Layout of the Window is not " + layout);
        }
    }

    public Dimension getDimension() {
        return dimension;
    }

    public JFrame getFrame() {
        return window;
    }

    public void setMinimumWindowSize(Dimension dimension){
        getFrame().setMinimumSize(dimension);
    }

    public void addWindowAdapter(WindowListener listener) {
        window.addWindowListener(listener);
    }
}
