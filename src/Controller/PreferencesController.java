package Controller;

import Helpers.DefaultSettings;
import Helpers.GlobalValue;
import Helpers.ThemeHelper;

import java.util.prefs.*;

/**
 * This class stores controls application wide settings.
 * @Warning this class will generate a warning on Windows. This is due to a bug
 * in the JDK. Please ignore.
 */
public final class PreferencesController {

    private static PreferencesController instance;
    private Preferences preferences;

    /**
     * Private constructor, called by getInstance.
     */
    private PreferencesController()
    {
        super();
        // Ignore warnings that occur due to JDK bug
    }

    /**
     * Returns the singleton object of this class to the client.
     * @return the singleton object
     */
    public static PreferencesController getInstance()
    {
        if (instance == null) {
            instance = new PreferencesController();
        }
        return instance;
    }

    /**
     * Sets up the base preferences.
     * Applies the theme.
     */
    public void setupPreferences()
    {
        preferences = Preferences.userNodeForPackage(this.getClass());
        ThemeHelper.setTheme(PreferencesController.getInstance().getThemeSetting());
    }

    /**
     * Returns the stored anti-aliasing setting to the client.
     * @return is anti-aliasing enabled.
     */
    public boolean getAntiAliasingSetting()
    {
        return preferences.getBoolean("AntiAliasing",
            DefaultSettings.ANTI_ALIASING);
    }

    /**
     * Returns the stored keybindings toggle setting to the client.
     * @return are keybindings enabled.
     */
    public boolean getKeyBindingsSetting()
    {
        return preferences.getBoolean("KeyBindings",
            DefaultSettings.TOGGLE_KEY_BINDINGS);
    }

    /**
     * Returns the stored show nearest road name setting to the client.
     * @return is show nearest road name enabled.
     */
    public boolean getShowNearestRoadNameSetting()
    {
        return preferences.getBoolean("CanvasRealTimeInformation",
            DefaultSettings.CANVAS_REALTIME_INFORMATION);
    }

    /**
     * Returns the stored use fastest route setting to the client.
     * @return is use fastest route enabled.
     */
    public boolean getUseFastestRouteSetting() {
        return preferences.getBoolean("UseFastestRoute",DefaultSettings.USE_FASTEST_ROUTE);
    }

    /**
     * Returns the stored theme setting to the client.
     * @return the preferred theme.
     */
    public String getThemeSetting()
    {
        return preferences.get("Theme", DefaultSettings.THEME);
    }

    /**
     * Returns the stored startup file name setting to the client.
     * @return the name of the preferred startup file.
     */
    public String getStartupFileNameSetting() {
        return preferences.get("StartupFileName", DefaultSettings.DEFAULT_FILE_NAME);
    }

    /**
     * Returns the stored startup file path setting to the client.
     * @return the path of the preferred startup file.
     */
    public String getStartupFilePathSetting() {
        return preferences.get("StartupFilePath", GlobalValue.DEFAULT_BIN_RESOURCE);
    }

    /**
     * Set and store the preferred anti-aliasing setting.
     * @param setting anti-aliasing enabled.
     */
    public void setAntiAliasingSetting(boolean setting)
    {
        preferences.putBoolean("AntiAliasing", setting);
    }

    /**
     * Set and store the preferred keybinding toggle setting.
     * @param setting keybindings enabled.
     */
    public void setKeyBindingsSetting(boolean setting)
    {
        preferences.putBoolean("KeyBindings", setting);
    }

    /**
     * Set and store the preferred show nearest road name setting.
     * @param setting show nearest road name enabled.
     */
    public void setShowNearestRoadNameSetting(boolean setting)
    {
        preferences.putBoolean("CanvasRealTimeInformation", setting);
    }

    /**
     * Set and store the preferred use fastest route setting.
     * @param setting use fasted route enabled.
     */
    public void setUseFastestRouteSetting(boolean setting) {
        preferences.putBoolean("UseFastestRoute",setting);
    }

    /**
     * Set and store the preferred theme setting.
     * @param setting the preferred theme.
     */
    public void setThemeSetting(String setting)
    {
        preferences.put("Theme", setting);
    }

    /**
     * Set and store the preferred startup file name setting.
     * @param setting the preferred startup file name.
     */
    public void setStartupFileNameSetting(String setting) {
        preferences.put("StartupFileName", setting);
    }

    /**
     * Set and store the preferred startup file path setting.
     * @param setting the preferred startup file path.
     */
    public void setStartupFilePathSetting(String setting) {
        preferences.put("StartupFilePath", setting);
    }

    /**
     * Returns all settings values to default settings and stores them.
     */
    public void setToDefaultSettings()
    {
        preferences.putBoolean("AntiAliasing", DefaultSettings.ANTI_ALIASING);
        preferences.putBoolean("CanvasRealTimeInformation", DefaultSettings.CANVAS_REALTIME_INFORMATION);
        preferences.putBoolean("KeyBindings", DefaultSettings.TOGGLE_KEY_BINDINGS);
        preferences.putBoolean("UseFastestRoute",DefaultSettings.USE_FASTEST_ROUTE);
        preferences.put("Theme", DefaultSettings.THEME);
        preferences.put("StartupFileName", DefaultSettings.DEFAULT_FILE_NAME);
        preferences.put("StartupFilePath", GlobalValue.DEFAULT_BIN_RESOURCE);
    }
}
