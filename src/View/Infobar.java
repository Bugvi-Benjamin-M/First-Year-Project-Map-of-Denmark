package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class details:
 * The Infobar is a visual component that either displays information about
 * a specific route, about a specific element or about a specific search.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @version 06-03-2017
 */
public class Infobar extends View {


    private JLabel widthLabel, heightLabel;

    /** Constructor of the Infobar */
    public Infobar() {
        setBackground(Color.blue);
        setPreferredSize(new Dimension(400,500));
        widthLabel = new JLabel(""+getWidth());
        heightLabel = new JLabel(""+getHeight());
        this.add(widthLabel);
        this.add(heightLabel);
        addComponentListener();
    }

    /**
     * Toggles the visibility of the infobar (default false)
     */
    public void toggleVisibility() {
        setVisible(!isVisible());
    }

    /** Adds an ComponentListener */
    private void addComponentListener() {
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                widthLabel.setText(""+getWidth());
                heightLabel.setText(""+getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }
}
