package Helpers;

import Theme.DefaultTheme;
import Theme.Theme;

import java.awt.*;


/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 13/03/2017
 */
public class ThemeHelper {

    public static Theme getTheme() {
        if(false) { //TODO create setting for theme
            return null;
        }
        return new DefaultTheme();
    }

    public static Color getColor(String color){
        return null;
        //TODO: Get color from getTheme by Reflection
    }
}
