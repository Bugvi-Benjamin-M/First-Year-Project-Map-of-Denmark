package View;

import javax.swing.*;
import java.util.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 28-03-2017.
 * @project BFST
 */
public class TextView extends View {

    private Map<String, JLabel> labels;

    public TextView() {
        labels = new HashMap<>();
    }

    public void addJLabel(String key, String message) {
        JLabel newLabel = new JLabel(message);
        labels.put(key,newLabel);
        this.add(newLabel);
    }

    public void removeJLabel(String key) {
        JLabel removed = labels.get(key);
        if (removed != null) {
            labels.remove(key);
            this.remove(removed);
        }
    }

    public JLabel getJLabel(String key) {
        return labels.get(key);
    }

}
