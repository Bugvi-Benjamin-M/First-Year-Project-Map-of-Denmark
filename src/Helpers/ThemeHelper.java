package Helpers;

import Theme.*;

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
}
