package Controller;

import Helpers.DefaultSettings;
import Helpers.GlobalValue;
import Helpers.ThemeHelper;

import java.util.prefs.*;

/**
 * Created by Búgvi Magnussen on 22-04-2017.
 *
 * This class stores controls application wide settings.
 * @Warning this class will generate a warning on Windows. This is due to a bug
 * in the JDK. Please ignore.
 */

public final class PreferencesController {

    private static PreferencesController instance;
    private Preferences preferences;

    /**
   * This class stores controls application wide settings.
   * @Warning this class will generate a warning on Windows. This is due to a
   * bug in the JDK. Code has been added
   * to make sure the warnings are suppressed.
   *
   */
    private PreferencesController()
    {
        super();
        // Ignore warnings that occur due to JDK bug
    }

    public static PreferencesController getInstance()
    {
        if (instance == null) {
            instance = new PreferencesController();
        }
        return instance;
    }

    public void setupPreferences()
    {
        preferences = Preferences.userNodeForPackage(this.getClass());
        ThemeHelper.setTheme(PreferencesController.getInstance().getThemeSetting());
        GlobalValue.setFastestRoute(PreferencesController.getInstance().getUseFastestRouteSetting());
    }

    public boolean getAntiAliasingSetting()
    {
        return preferences.getBoolean("AntiAliasing",
            DefaultSettings.ANTI_ALIASING);
    }

    public boolean getKeyBindingsSetting()
    {
        return preferences.getBoolean("KeyBindings",
            DefaultSettings.TOGGLE_KEY_BINDINGS);
    }

    public boolean getCanvasRealTimeInformationSetting()
    {
        return preferences.getBoolean("CanvasRealTimeInformation",
            DefaultSettings.CANVAS_REALTIME_INFORMATION);
    }

    public boolean getUseFastestRouteSetting() {
        return preferences.getBoolean("UseFastestRoute",DefaultSettings.USE_FASTEST_ROUTE);
    }

    public String getThemeSetting()
    {
        return preferences.get("Theme", DefaultSettings.THEME);
    }

    public String getStartupFileNameSetting() {
        return preferences.get("StartupFileName", DefaultSettings.DEFAULT_FILE_NAME);
    }

    public String getStartupFilePathSetting() {
        return preferences.get("StartupFilePath", GlobalValue.DEFAULT_BIN_RESOURCE);
    }

    public void setAntiAliasingSetting(boolean setting)
    {
        preferences.putBoolean("AntiAliasing", setting);
    }

    public void setKeyBindingsSetting(boolean setting)
    {
        preferences.putBoolean("KeyBindings", setting);
    }

    public void setCanvasRealTimeInformationSetting(boolean setting)
    {
        preferences.putBoolean("CanvasRealTimeInformation", setting);
    }

    public void setUseFastestRouteSetting(boolean setting) {
        preferences.putBoolean("UseFastestRoute",setting);
    }

    public void setThemeSetting(String setting)
    {
        preferences.put("Theme", setting);
    }

    public void setStartupFileNameSetting(String setting) {
        preferences.put("StartupFileName", setting);
    }

    public void setStartupFilePathSetting(String setting) {
        preferences.put("StartupFilePath", setting);
    }

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
