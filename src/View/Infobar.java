package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class Infobar extends View {


    private JLabel widthLabel, heightLabel;

    public Infobar() {
        setBackground(Color.blue);
        setPreferredSize(new Dimension(400,500));
        widthLabel = new JLabel(""+getWidth());
        heightLabel = new JLabel(""+getHeight());
        this.add(widthLabel);
        this.add(heightLabel);
        addComponentListener();
    }

    public void toggleVisibility() {
        setVisible(!isVisible());
    }

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
