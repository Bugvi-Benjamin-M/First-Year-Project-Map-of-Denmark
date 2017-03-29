package View;

import javax.swing.*;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 29/03/2017
 */
public class Settings extends View {

    private KeyboardKeysToggle keyboardKeysToggle;

    private ThemeSetting themeSetting;

    public Settings() {
        keyboardKeysToggle = new KeyboardKeysToggle();
        themeSetting = new ThemeSetting();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addSettings();
    }

    private void addSettings() {
        add(themeSetting);
        add(Box.createRigidArea(new Dimension(0,20)));
        add(keyboardKeysToggle);
        add(Box.createRigidArea(new Dimension(0, 480)));
    }

    public KeyboardKeysToggle getKeyboardKeysToggle() {
        return keyboardKeysToggle;
    }

    public ThemeSetting getThemeSetting() {
        return themeSetting;
    }

}
