package Controller;

import Controller.ToolbarControllers.ToolbarController;
import Enums.FileType;
import Enums.ToolType;
import Enums.ToolbarType;
import Helpers.DefaultSettings;
import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import View.*;
import View.Window;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The settings controller class controls the settings window and settings in
 * general. It inherits from WindowController.
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
    private FileLoadSetting fileLoadSetting;
    private Settings settings;
    private String chosenFilePath;
    private String chosenFileName;

    private SettingsWindowController() { super(); }

    /**
   * Returns the singleton instance of this class.
   * @return the singleton object
   */
    public static SettingsWindowController getInstance()
    {
        if (instance == null) {
            return instance = new SettingsWindowController();
        }
        return instance;
    }

    /**
   * Show the already existing settings window.
   */
    @Override
    public void showWindow()
    {
        MainWindowController.getInstance().getWindow().getFrame().setEnabled(false);
        MainWindowController.getInstance().getWindow().getFrame().setFocusable(false);
        window.relativeTo(null);
        window.show();
        window.getFrame().setAlwaysOnTop(true);

    }

    /**
   * Sets up a new settings window. In general, this is only done when the
   * singleton instance of this controller
   * is created.
   * @return the newly created settings window.
   */
    public void setupSettingsWindow()
    {
        window = new Window()
                     .title("Settings")
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
        toggleKeyBindings();
        hideWindow();
    }

    private void createSettings()
    {
        settings = new Settings();
        themeSettings = new ThemeSetting();
        keyboardKeysToggle = new KeyboardKeysToggle();
        southButtons = new SettingsButtons();
        antiAliasingToggle = new AntiAliasingToggle();
        canvasRealTimeInformationToggle = new CanvasRealTimeInformationToggle();
        fileLoadSetting = new FileLoadSetting();
        setToCurrentSettings();
    }

    private void setToCurrentSettings()
    {
        antiAliasingToggle.setSelectedStatus(
            PreferencesController.getInstance().getAntiAliasingSetting());
        canvasRealTimeInformationToggle.setSelectedStatus(
            PreferencesController.getInstance()
                .getCanvasRealTimeInformationSetting());
        keyboardKeysToggle.setSelectedStatus(
            PreferencesController.getInstance().getKeyBindingsSetting());
        themeSettings.setSelectedTheme(
            PreferencesController.getInstance().getThemeSetting());
        fileLoadSetting.setTextField(PreferencesController.getInstance().getStartupFileNameSetting());
    }

    /**
   * adds the individual components to the settings JPanel
   */
    private void buildSettings()
    {
        settings.createSpace(new Dimension(0, 20));
        settings.addSetting(themeSettings);
        settings.createSpace(new Dimension(0, 20));
        settings.addSetting(keyboardKeysToggle);
        settings.createSpace(new Dimension(0, 20));
        settings.addSetting(antiAliasingToggle);
        settings.createSpace(new Dimension(0, 20));
        settings.addSetting(canvasRealTimeInformationToggle);
        settings.createSpace(new Dimension(0, 20));
        settings.addSetting(fileLoadSetting);
        settings.createSpace(new Dimension(0, 400));
    }

    /**
   * Specifies the location for the different components in the settings window.
   */
    private void addSettingsToWindow()
    {
        window.addBorderLayoutComponent(BorderLayout.CENTER, settings, true);
        window.addBorderLayoutComponent(BorderLayout.SOUTH, southButtons, true);
        addActionsToSettingsWindowButtons();
    }

    /**
   * Adds actions to the buttons on the settings window.
   */
    private void addActionsToSettingsWindowButtons()
    {
        southButtons.addActionToApplyButton(a -> { applyButtonActivated(); });
        southButtons.addActionToDefaultButton(a -> { defaultButtonActivated(); });
        fileLoadSetting.getChooseButton().addActionListener(a -> {
            fileButtonActivated();
        });
        fileLoadSetting.getDefaultButton().addActionListener(a -> {
            defaultFileButtonActivated();
        });
    }

    private void defaultFileButtonActivated() {
        chosenFilePath = GlobalValue.DEFAULT_BIN_RESOURCE;
        chosenFileName = DefaultSettings.DEFAULT_FILE_NAME;
        fileLoadSetting.setTextField(DefaultSettings.DEFAULT_FILE_NAME);
    }

    private void fileButtonActivated() {
        window.getFrame().setAlwaysOnTop(false);
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[] {
                new FileNameExtensionFilter("OSM Files", FileType.OSM.toString()),
                new FileNameExtensionFilter("ZIP Files", FileType.ZIP.toString()),
                new FileNameExtensionFilter("BIN Files", FileType.BIN.toString())
        };
        JFileChooser chooser = PopupWindow.fileLoader(false, filters);

        if (chooser != null) {
            chosenFileName = chooser.getSelectedFile().getName();
            chosenFilePath = chooser.getSelectedFile().getAbsolutePath();
            fileLoadSetting.setTextField(chooser.getSelectedFile().getName());
        }
        window.getFrame().setAlwaysOnTop(true);
    }

    /**
   * Method is called when default button is pressed. Returns all settings to
   * default.
   */
    private void defaultButtonActivated()
    {
        PreferencesController.getInstance().setToDefaultSettings();
        ThemeHelper.setTheme(PreferencesController.getInstance().getThemeSetting());
        MainWindowController.getInstance().themeHasChanged();
        MainWindowController.getInstance().setKeyToggle();
        CanvasController.getInstance().toggleAntiAliasing();
        PreferencesController.getInstance().setStartupFileNameSetting(DefaultSettings.DEFAULT_FILE_NAME);
        PreferencesController.getInstance().setStartupFilePathSetting(GlobalValue.DEFAULT_BIN_RESOURCE);
        setToCurrentSettings();
        hideWindow();
    }

    /**
   * Method is called when the apply button is pressed. Applies all changed
   * settings and makes sure the settings
   * window accurately reflects the current settings.
   */
    private void applyButtonActivated()
    {
        PreferencesController.getInstance().setThemeSetting(
            themeSettings.getSelectedTheme());
        PreferencesController.getInstance().setCanvasRealTimeInformationSetting(
            canvasRealTimeInformationToggle.isToggleSelected());
        PreferencesController.getInstance().setKeyBindingsSetting(
            keyboardKeysToggle.isToggleSelected());
        PreferencesController.getInstance().setAntiAliasingSetting(
            antiAliasingToggle.isToggleSelected());
        ThemeHelper.setTheme(PreferencesController.getInstance().getThemeSetting());
        MainWindowController.getInstance().themeHasChanged();
        MainWindowController.getInstance().setKeyToggle();
        CanvasController.getInstance().toggleAntiAliasing();
        PreferencesController.getInstance().setStartupFilePathSetting(chosenFilePath);
        PreferencesController.getInstance().setStartupFileNameSetting(chosenFileName);
        setToCurrentSettings();
        hideWindow();
    }

    /**
   * Overrides the superclass specifyKeyBindingsMethod. Adds key bindings to the
   * settings window.
   */
    @Override
    protected void specifyKeyBindings()
    {
        handler.addKeyBinding(KeyEvent.VK_ESCAPE, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    setToCurrentSettings();
                    hideWindow();
                }
            });
        handler.addKeyBinding(KeyEvent.VK_ENTER, KeyEvent.VK_UNDEFINED,
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    applyButtonActivated();
                }
            });
    }

    /**
   * Overrides the superclass' method. Builds on the superclass method by adding
   * a window listener to the settings window
   * to add further functionality.
   */
    @Override
    protected void addInteractionHandlerToWindow()
    {
        super.addInteractionHandlerToWindow();
        window.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                setToCurrentSettings();
                hideWindow();
            }
        });
    }

    @Override
    public void hideWindow()
    {
        MainWindowController.getInstance().getWindow().getFrame().setEnabled(true);
        MainWindowController.getInstance().getWindow().getFrame().setFocusable(
            true);
        window.getFrame().setAlwaysOnTop(true);
        window.hide();
        if (ToolbarController.getInstance().getType() == ToolbarType.LARGE)
            ToolbarController.getInstance()
                .getToolbar()
                .getTool(ToolType.SETTINGS)
                .toggleActivate(false);
        MainWindowController.getInstance().transferFocusToMapCanvas();
    }

    /**
   * resets the singleton instance.
   */
    public void resetInstance() { instance = null; }
}
