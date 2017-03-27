package Controller;

import Enums.ToolType;
import Helpers.ThemeHelper;
import Main.Main;
import View.KeyboardKeysToggle;
import View.SettingsSouthButtons;
import View.ThemeSetting;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 20/03/2017
 */
public final class SettingsWindowController extends WindowController {

    private static SettingsWindowController instance;
    private ThemeSetting themeSettings;
    private SettingsSouthButtons southButtons;
    private KeyboardKeysToggle keyboardKeysToggle;

    private SettingsWindowController(Window window) {
        super(window);
        setupSettingsWindowSpecifics();
    }

    public static SettingsWindowController getInstance() {
        if(instance == null) {
            return instance = new SettingsWindowController(setupWindow());
        }
        window.relativeTo(null);
        window.show();
        return instance;
    }

    private static Window setupWindow() {
        Window settings = new Window().title("Settings")
                .closeOperation(WindowConstants.DISPOSE_ON_CLOSE)
                .dimension(new Dimension(600, 600))
                .extendedState(JFrame.NORMAL)
                .layout(new BorderLayout())
                .relativeTo(null)
                .show();
        settings.setMinimumWindowSize(new Dimension(600, 600));
        return settings;
    }

    private void setupSettingsWindowSpecifics() {
        themeSettings = new ThemeSetting();
        window.addComponent(BorderLayout.NORTH, themeSettings);
        southButtons = new SettingsSouthButtons();
        window.addComponent(BorderLayout.SOUTH, southButtons);
        keyboardKeysToggle = new KeyboardKeysToggle();
        window.addComponent(BorderLayout.CENTER, keyboardKeysToggle);
        addActionsToSettingsWindowButtons();
    }

    private void addActionsToSettingsWindowButtons() {
        southButtons.addActionToApplyButton(a -> {
            if(!ThemeHelper.getCurrentTheme().equals(themeSettings.getSelectedTheme())) {
                ThemeHelper.setTheme(themeSettings.getSelectedTheme());
                Main.notifyThemeChange();
            }
            if(!keyboardKeysToggle.isToggleSelected()) {
                toggleKeyBindings(false);
            } else {
                toggleKeyBindings(true);
            }
            ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
            window.hide();
        });
        southButtons.addActionToDefaultButton(a -> {
            if(!ThemeHelper.getCurrentTheme().equals("Default")) {
                ThemeHelper.setTheme("Default");
                themeSettings.setSelectedThemeToDefault();
                Main.notifyThemeChange();
            }
            if(!keyboardKeysToggle.isToggleSelected()) {
                keyboardKeysToggle.setSelectedStatus(true);
                Main.notifyKeyToggle(true);
            }

            ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
            window.hide();
        });
    }

    @Override
    protected void specifyKeyBindings() {
        handler.addKeyBinding(KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                themeSettings.setSelectedTheme(ThemeHelper.getCurrentTheme());
                window.hide();
                ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
            }
        });
    }
    @Override
    protected void addInteractionHandlerToWindow() {
        super.addInteractionHandlerToWindow();
        window.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
                themeSettings.setSelectedTheme(ThemeHelper.getCurrentTheme());
                //Todo check if keys are enabled and change the toggle box
                //Todo make sure settings are consistent with the current system settings, even if user just closes window
            }
        });
    }

    public void resetInstance() {
        instance = null;
    }
}
