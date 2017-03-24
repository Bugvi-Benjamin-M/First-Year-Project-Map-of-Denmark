package Controller;

import Enums.ToolType;
import View.ThemeSetting;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 20/03/2017
 */
public final class SettingsWindowController extends WindowController {

    private static SettingsWindowController instance;

    private SettingsWindowController(Window window) {
        super(window);
    }

    public static SettingsWindowController getInstance() {
        if(instance == null) {
            Window settings = new Window().title("Settings")
                    .closeOperation(WindowConstants.DISPOSE_ON_CLOSE)
                    .dimension(new Dimension(500, 500))
                    .extendedState(JFrame.NORMAL)
                    .layout(new BorderLayout())
                    .relativeTo(null)
                    .show();
            settings.addComponent(BorderLayout.NORTH, new ThemeSetting());
            return instance = new SettingsWindowController(settings);
        }
        window.relativeTo(null);
        window.show();
        return instance;
    }

    @Override
    protected void specifyKeyBindings() {
        handler.addKeyBinding(KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                window.hide();
                ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
            }
        });
    }

    public void resetInstance() {
        instance = null;
    }


}
