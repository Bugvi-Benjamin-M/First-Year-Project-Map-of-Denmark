package Helpers;

import java.awt.*;
import java.io.InputStream;

public class FontAwesome{


    public static Font getFontAwesome(){
        try {
            InputStream is = FontAwesome.class.getResourceAsStream("/fa.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(Font.PLAIN, 40f);
            return font;
        } catch (Exception e) {
            throw new RuntimeException("FontAwesome could not be loaded.");
        }
    }

}
