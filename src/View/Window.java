package View;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jakob on 06-03-2017.
 */
public class Window {
    private JFrame window;

    public Window() {
        window = new JFrame();
        window.setLayout(new BorderLayout());
        window.setPreferredSize(new Dimension(1000,1000));
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        update(true);
    }

    public void addComponent(String layout, View component) {
        window.add(component,layout);
        update(true);
    }

    public void addComponent(String layout, Component component) {
        window.add(component,layout);
        update(true);
    }

    public void update(boolean visibility) {
        window.pack();
        window.setVisible(visibility);
    }
}
