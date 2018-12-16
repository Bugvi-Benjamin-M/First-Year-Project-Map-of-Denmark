package View;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static javax.swing.SpringLayout.*;

/**
 * A TextView is a visual component that contains a collection of labels that
 * are positioned based upon when they were added to the TextView.
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

    /**
     * Creates and adds a new JLabel to the TextView
     * @param key The key of the new JLabel for easy retrieval
     * @param message The initial message of the JLabel
     */
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

    /**
     * Removes a JLabel from the TextView
     * @param key The key of the JLabel to remove
     */
    public void removeJLabel(String key)
    {
        JLabel removed = labels.get(key);
        if (removed != null) {
            labels.remove(key);
            this.remove(removed);
        }
    }

    /**
     * Returns a JLabel based on its key
     */
    public JLabel getJLabel(String key) { return labels.get(key); }

    /**
     * Resets the TextView
     */
    public void reset()
    {
        labels = new HashMap<>();
        lastInserted = null;
    }
}
