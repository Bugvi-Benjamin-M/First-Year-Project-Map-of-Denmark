package Controller;

import Helpers.DefaultSettings;
import sun.util.logging.PlatformLogger;

import java.util.prefs.*;

/**
 * Created by Búgvi Magnussen on 22-04-2017.
 *
 * This class stores controls application wide settings.
 * @Warning this class will generate a warning on Windows. This is due to a bug in the JDK. Please ignore.
 */

public final class PreferencesController {

    private static PreferencesController instance;
    private Preferences preferences;

    /**
     * This class stores controls application wide settings.
     * @Warning this class will generate a warning on Windows. This is due to a bug in the JDK. Code has been added
     * to make sure the warnings are suppressed.
     *
     */
    private PreferencesController() {
        super();
        //Ignore warnings that occur due to JDK bug.
        PlatformLogger.getLogger("java.util.prefs").setLevel(PlatformLogger.Level.OFF);
    }

    public static PreferencesController getInstance() {
        if(instance == null) {
            instance = new PreferencesController();
        }
        return instance;
    }

    public void setupPreferences() {
        preferences = Preferences.userNodeForPackage(this.getClass());
    }

    public boolean getAntiAliasingSetting() {
        return preferences.getBoolean("AntiAliasing", DefaultSettings.antiAliasing);
    }

    public boolean getKeyBindingsSetting() {
        return preferences.getBoolean("KeyBindings", DefaultSettings.toggleKeyBindings);
    }

    public boolean getCanvasRealTimeInformationSetting() {
        return preferences.getBoolean("CanvasRealTimeInformation", DefaultSettings.canvasRealTimeInformation);
    }

    public String getThemeSetting() {
        return preferences.get("Theme", DefaultSettings.Theme);
    }

    public void setAntiAliasingSetting(boolean setting) {
        preferences.putBoolean("AntiAliasing", setting);
    }

    public void setKeyBindingsSetting(boolean setting) {
        preferences.putBoolean("KeyBindings", setting);
    }

    public void setCanvasRealTimeInformationSetting(boolean setting) {
        preferences.putBoolean("CanvasRealTimeInformation", setting);
    }

    public void setThemeSetting(String setting) {
        preferences.put("Theme", setting);
    }

    public void setToDefaultSettings() {
        preferences.putBoolean("AntiAliasing", DefaultSettings.antiAliasing);
        preferences.putBoolean("CanvasRealTimeInformation", DefaultSettings.canvasRealTimeInformation);
        preferences.putBoolean("KeyBindings", DefaultSettings.toggleKeyBindings);
        preferences.put("Theme", DefaultSettings.Theme);
    }

}

