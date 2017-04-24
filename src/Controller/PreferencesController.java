package Controller;

import Helpers.DefaultSettings;

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
        // Ignore warnings that occur due to JDK bug.
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

    public String getThemeSetting()
    {
        return preferences.get("Theme", DefaultSettings.THEME);
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

    public void setThemeSetting(String setting)
    {
        preferences.put("Theme", setting);
    }

    public void setToDefaultSettings()
    {
        preferences.putBoolean("AntiAliasing", DefaultSettings.ANTI_ALIASING);
        preferences.putBoolean("CanvasRealTimeInformation",
            DefaultSettings.CANVAS_REALTIME_INFORMATION);
        preferences.putBoolean("KeyBindings", DefaultSettings.TOGGLE_KEY_BINDINGS);
        preferences.put("Theme", DefaultSettings.THEME);
    }
}
