package Helpers;

import Theme.DefaultTheme;
import Theme.Theme;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;


/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 13/03/2017
 */
public final class ThemeHelper {

    private static Theme currentTheme;
    private static ThemeHelper instance;

    private ThemeHelper() {
        currentTheme = new DefaultTheme();
    }

    public static ThemeHelper getInstance() {
        if (instance == null) {
            instance = new ThemeHelper();
        }
        return instance;
    }

    public static String getCurrentTheme() {
        return currentTheme.getName();
    }

    public static Color color(String input) {
        input.trim().toLowerCase();
        try {
            Method method = currentTheme.getClass().getDeclaredMethod(input, null);
            return (Color) method.invoke(currentTheme, null);
        } catch (Exception e) {
            throw new RuntimeException("Color not found" + input);
        }
    }

    public static void setTheme(String input) {
        String modInput = input.trim().replaceAll("\\s", "") + "Theme";
        try {
            Constructor<?> constructor = Class.forName("Theme." + modInput).getConstructor();
            currentTheme = (Theme) constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Theme not found" + input + "\n" + "modified input: " + modInput);
        }
    }
}
