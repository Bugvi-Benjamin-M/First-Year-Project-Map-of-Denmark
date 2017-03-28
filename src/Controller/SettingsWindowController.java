package Controller;

import Enums.ToolType;
import Helpers.ThemeHelper;
import Helpers.Utilities.DebugWindow;
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
    private boolean keysActive;

    private SettingsWindowController(Window window) {
        super(window);
        setupSettingsWindowSpecifics();
        keysActive = true;
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
            applyButtonActivated();
        });
        southButtons.addActionToDefaultButton(a -> {
            defaultButtonActivated();
        });
        southButtons.addActionToOpenDebugButton(a -> {
            openDebugActivated();
        });
    }

    private void defaultButtonActivated() {
        if(!ThemeHelper.getCurrentTheme().equals("Default")) {
            ThemeHelper.setTheme("Default");
            themeSettings.setSelectedThemeToDefault();
            Main.notifyThemeChange();
        }
        if(!keyboardKeysToggle.isToggleSelected()) {
            keyboardKeysToggle.setSelectedStatus(true);
            keysActive = true;
            Main.notifyKeyToggle(true);
        }
        setToCurrentSettingsAndClose();
    }

    private void applyButtonActivated() {
        if(!ThemeHelper.getCurrentTheme().equals(themeSettings.getSelectedTheme())) {
            ThemeHelper.setTheme(themeSettings.getSelectedTheme());
            Main.notifyThemeChange();
        }
        if(!keyboardKeysToggle.isToggleSelected()) {
            toggleKeyBindings(false);
            keysActive = false;
        } else {
            toggleKeyBindings(true);
            keysActive = true;
        }
        setToCurrentSettingsAndClose();
    }

    private void openDebugActivated() {
        DebugWindow.getInstance().show();
    }

    @Override
    protected void specifyKeyBindings() {
        handler.addKeyBinding(KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setToCurrentSettingsAndClose();
            }
        });
        handler.addKeyBinding(KeyEvent.VK_ENTER, KeyEvent.VK_UNDEFINED, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyButtonActivated();
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
                setToCurrentSettingsAndClose();
            }
        });
    }


    /**
     * Sets the settings to the current settings. This method makes sure that the current settings are
     * displayed properly the next time the settings window is opened.
     *
     */
    private void setToCurrentSettingsAndClose() {
        themeSettings.setSelectedTheme(ThemeHelper.getCurrentTheme());
        keyboardKeysToggle.setSelectedStatus(keysActive);
        window.hide();
        ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
    }

    /**
     * resets the singleton instance.
     */
    public void resetInstance() {
        instance = null;
    }
}
