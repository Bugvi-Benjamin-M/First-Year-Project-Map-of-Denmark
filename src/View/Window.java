package View;

import Helpers.Constant;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Window {
    private JFrame window;
    private Dimension dimension;

    public Window() {
        window = new JFrame(Constant.getTITLE());
        window.setLayout(new BorderLayout());
        dimension = new Dimension(1200,1000);
        window.setPreferredSize(dimension);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setLocationRelativeTo(null);
        setVisible(true);
    }

    public void addComponent(String layout, View component) {
        window.add(component,layout);
        setVisible(true);
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

    public void setVisible(boolean visibility) {
        window.setVisible(visibility);
    }

    public void pack() {
        window.pack();
    }
}
