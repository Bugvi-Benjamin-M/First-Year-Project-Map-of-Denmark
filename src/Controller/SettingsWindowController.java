package Controller;

import Enums.ToolType;
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
    private static ThemeSetting themeSettings;
    private static SettingsSouthButtons southButtons;

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
                .dimension(new Dimension(500, 500))
                .extendedState(JFrame.NORMAL)
                .layout(new BorderLayout())
                .relativeTo(null)
                .show();
        return settings;
    }

    private void setupSettingsWindowSpecifics() {
        themeSettings = new ThemeSetting();
        window.addComponent(BorderLayout.NORTH, themeSettings);
        southButtons = new SettingsSouthButtons();
        window.addComponent(BorderLayout.SOUTH, southButtons);
        addActionsToSettingsWindowButtons();
    }

    private void addActionsToSettingsWindowButtons() {
        southButtons.addActionToApplyButton(a -> {
            if(!themeHelper.getCurrentTheme().equals(themeSettings.getSelectedTheme())) {
                themeHelper.setTheme(themeSettings.getSelectedTheme());
                notifyThemeChange();
            }
            ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
            window.hide();
        });
        southButtons.addActionToDefaultButton(a -> {
            ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
            window.hide();
        });
    }

    private void notifyThemeChange() {
        CanvasController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        MainWindowController.getInstance().themeHasChanged();
        InfobarController.getInstance(MainWindowController.getInstance().getWindow()).themeHasChanged();
        SettingsWindowController.getInstance().themeHasChanged();
        //Todo, figure out how to notify of theme change
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
    @Override
    protected void addInteractionHandlerToWindow() {
        super.addInteractionHandlerToWindow();
        window.getFrame().addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                ToolbarController.getInstance(window).getToolbar().toggleWellOnTool(ToolType.SETTINGS);
            }
        });
    }

    public void resetInstance() {
        instance = null;
    }
}
