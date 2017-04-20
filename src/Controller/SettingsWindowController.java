package Controller;

import Helpers.ThemeHelper;
import Main.Main;
import View.*;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The settings controller class controls the settings window and settings in general. It inherits from WindowController.
 * @author Búgvi Magnussen
 * @version 20/03/2017
 */
public final class SettingsWindowController extends WindowController {

    private static SettingsWindowController instance;

    private ThemeSetting themeSettings;
    private SettingsButtons southButtons;
    private Toggle keyboardKeysToggle;
    private Toggle antiAliasingToggle;
    private Toggle canvasRealTimeInformationToggle;
    private boolean keysActiveStatus;
    private boolean antiAliasingStatus;
    private boolean canvasrealTimeInformationStatus;
    private Settings settings;

    private SettingsWindowController() {
        super();
    }

    /**
     * Returns the singleton instance of this class.
     * @return the singleton object
     */
    public static SettingsWindowController getInstance() {
        if(instance == null) {
            return instance = new SettingsWindowController();
        }
        return instance;
    }


    /**
     * Show the already existing settings window.
     */
    @Override
    public void showWindow() {
        window.relativeTo(null);
        window.show();
    }

    /**
     * Sets up a new settings window. In general, this is only done when the singleton instance of this controller
     * is created.
     * @return the newly created settings window.
     */
    public void setupSettingsWindow() {
        window = new Window().title("Settings")
                .closeOperation(WindowConstants.DISPOSE_ON_CLOSE)
                .dimension(new Dimension(600, 600))
                .extendedState(JFrame.NORMAL)
                .layout(new BorderLayout())
                .relativeTo(null)
                .hide();
        window.setMinimumWindowSize(new Dimension(600, 600));
        createSettings();
        addSettingsToWindow();
        buildSettings();
        addInteractionHandlerToWindow();
        hideWindow();
    }

    private void createSettings() {
        settings = new Settings();
        themeSettings = new ThemeSetting();
        keyboardKeysToggle = new KeyboardKeysToggle();
        southButtons = new SettingsButtons();
        antiAliasingToggle = new AntiAliasingToggle();
        canvasRealTimeInformationToggle = new CanvasRealTimeInformationToggle();
        keysActiveStatus = true;
        antiAliasingStatus = false;
        canvasrealTimeInformationStatus = true;
        CanvasController.getInstance().popupActivated(canvasrealTimeInformationStatus);
    }

    /**
     * adds the individual components to the settings JPanel
     */
    private void buildSettings() {
        settings.addSetting(themeSettings);
        settings.createSpace(new Dimension(0,20));
        settings.addSetting(keyboardKeysToggle);
        settings.createSpace(new Dimension(0,20));
        settings.addSetting(antiAliasingToggle);
        settings.createSpace(new Dimension(0,20));
        settings.addSetting(canvasRealTimeInformationToggle);
        settings.createSpace(new Dimension(0,440));
    }

    /**
     * Specifies the location for the different components in the settings window.
     */
    private void addSettingsToWindow() {
        window.addBorderLayoutComponent(BorderLayout.CENTER, settings,true);
        window.addBorderLayoutComponent(BorderLayout.SOUTH, southButtons,true);
        addActionsToSettingsWindowButtons();
    }

    /**
     * Adds actions to the buttons on the settings window.
     */
    private void addActionsToSettingsWindowButtons() {
        southButtons.addActionToApplyButton(a -> {
            applyButtonActivated();
        });
        southButtons.addActionToDefaultButton(a -> {
            defaultButtonActivated();
        });
    }

    /**
     * Method is called when deafault button is pressed. Returns all settings to default.
     */
    private void defaultButtonActivated() {
        if(!ThemeHelper.getCurrentTheme().equals("Default")) {
            ThemeHelper.setTheme("Default");
            themeSettings.setSelectedThemeToDefault();
            MainWindowController.getInstance().themeHasChanged();
        }
        //Todo refactor to avoid contact with main class
        if(!keysActiveStatus) {
            keysActiveStatus = true;
            Main.notifyKeyToggle(keysActiveStatus);
        }
        if(antiAliasingStatus) {
            antiAliasingStatus = false;
            Main.notifyAntiAliasingToggle(antiAliasingStatus);
        }
        if(!canvasrealTimeInformationStatus) {
            canvasrealTimeInformationStatus = true;
            CanvasController.getInstance().popupActivated(canvasrealTimeInformationStatus);
        }
        setToCurrentSettingsAndClose();
    }

    /**
     * Method is called when the apply button is pressed. Applies all changed settings and makes sure the settings
     * window accurately reflects the current settings.
     */
    private void applyButtonActivated() {
        if(!ThemeHelper.getCurrentTheme().equals(themeSettings.getSelectedTheme())) {
            ThemeHelper.setTheme(themeSettings.getSelectedTheme());
            MainWindowController.getInstance().themeHasChanged();
        }
        //Todo refactor to avoid contact with main class
        if(!keyboardKeysToggle.isToggleSelected()) {
            keysActiveStatus = false;
            Main.notifyKeyToggle(keysActiveStatus);
        } else {
            keysActiveStatus = true;
            Main.notifyKeyToggle(keysActiveStatus);
        }
        if(antiAliasingToggle.isToggleSelected()) {
            antiAliasingStatus = true;
            Main.notifyAntiAliasingToggle(antiAliasingStatus);
        } else {
            antiAliasingStatus = false;
            Main.notifyAntiAliasingToggle(antiAliasingStatus);
        }
        if(canvasRealTimeInformationToggle.isToggleSelected()) {
            canvasrealTimeInformationStatus = true;
            CanvasController.getInstance().popupActivated(canvasrealTimeInformationStatus);
        } else {
            canvasrealTimeInformationStatus = false;
            CanvasController.getInstance().popupActivated(canvasrealTimeInformationStatus);
        }
        setToCurrentSettingsAndClose();
    }

    /**
     * Overrides the superclass specifyKeyBindingsMethod. Adds key bindings to the settings window.
     */
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

    /**
     * Overrides the superclass' method. Builds on the superclass method by adding a window listener to the settings window
     * to add further functionality.
     */
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
        keyboardKeysToggle.setSelectedStatus(keysActiveStatus);
        antiAliasingToggle.setSelectedStatus(antiAliasingStatus);
        window.hide();
    }

    /**
     * resets the singleton instance.
     */
    public void resetInstance() {
        instance = null;
    }
}
