package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static javax.swing.SpringLayout.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 28-03-2017.
 * @project BFST
 */
public class TextView extends View {

    private Map<String, JLabel> labels;

    private JLabel lastInserted;
    private SpringLayout layout;

    private final int MARGIN_TOP = 30;
    private final int MARGIN_LEFT = 20;

    public TextView()
    {
        layout = new SpringLayout();
        this.setLayout(layout);
        labels = new HashMap<>();
    }

    public void addJLabel(String key, String message)
    {
        JLabel newLabel = new JLabel(message);
        newLabel.setPreferredSize(new Dimension(getWidth(), 40));
        labels.put(key, newLabel);
        layout.putConstraint(WEST, newLabel, MARGIN_LEFT, WEST, this);
        if (lastInserted == null) {
            layout.putConstraint(NORTH, newLabel, 10, NORTH, this);
        } else {
            layout.putConstraint(NORTH, newLabel, MARGIN_TOP, NORTH, lastInserted);
        }
        this.add(newLabel);
        lastInserted = newLabel;
    }

    public void removeJLabel(String key)
    {
        JLabel removed = labels.get(key);
        if (removed != null) {
            labels.remove(key);
            this.remove(removed);
        }
    }

    public JLabel getJLabel(String key) { return labels.get(key); }

    public void reset()
    {
        labels = new HashMap<>();
        lastInserted = null;
    }
}
