package View;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Window {
    private JFrame window;
    private Dimension dimension;
    private final String TITLE = "OSM Map Viewer v0.1";

    public Window() {
        window = new JFrame(TITLE);
        window.setLayout(new BorderLayout());
        dimension = new Dimension(1000,1000);
        window.setPreferredSize(dimension);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        update(true);
    }

    public void addComponent(String layout, View component) {
        window.add(component,layout);
        update(true);
    }

    public Dimension getDimension() {
        return dimension;
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    public void update(boolean visibility) {
        window.pack();
        window.setVisible(visibility);
    }
}
