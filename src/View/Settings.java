package View;

import javax.swing.*;
import java.awt.*;

/**
 * Settings is a visual component that is able to contain all the different
 * settings options and toggles.
 */
public class Settings extends View {

    public Settings() { setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); }

    /**
     * Adds a setting to the component
     */
    public void addSetting(View view) { add(view); }

    /**
     * Creates some space around the visual component
     */
    public void createSpace(Dimension dimension)
    {
        add(Box.createRigidArea(dimension));
    }
}
