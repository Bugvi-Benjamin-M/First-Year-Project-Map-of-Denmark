package View;

import javax.swing.*;
import java.awt.*;


public class Settings extends View {

    public Settings() { setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); }

    public void addSetting(View view) { add(view); }

    public void createSpace(Dimension dimension)
    {
        add(Box.createRigidArea(dimension));
    }
}
