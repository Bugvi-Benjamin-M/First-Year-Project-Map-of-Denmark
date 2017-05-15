package Helpers;

import Theme.Theme;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


public final class ThemeHelper {

    private static Theme currentTheme;

    /**
     * Returns the current shown theme
     */
    public static String getCurrentTheme() { return currentTheme.getName(); }

    /**
     * Returns the applicate color based on a search query.
     * The color is determined by the current theme in the Theme folder.
     */
    public static Color color(String input)
    {
        try {
            Method method = currentTheme.getClass().getDeclaredMethod(input, null);
            return (Color)method.invoke(currentTheme, null);
        } catch (Exception e) {
            throw new RuntimeException("Color not found " + input);
        }
    }

    /**
     * Sets the current theme, based on a theme name.
     */
    public static void setTheme(String input)
    {
        String modInput = input.trim().replaceAll("\\s", "") + "Theme";
        try {
            Constructor<?> constructor = Class.forName("Theme." + modInput).getConstructor();
            currentTheme = (Theme)constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Theme not found" + input + "\n"
                + "modified input: " + modInput);
        }
    }
}
