package Helpers;

import java.awt.*;
import java.io.InputStream;

public class FontAwesome {

    public static Font fontAwesome;
    public static Font getFontAwesome()
    {
        if(fontAwesome != null){
            return fontAwesome;
        }

        try {
            InputStream is = FontAwesome.class.getResourceAsStream("/fa.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            fontAwesome = font.deriveFont(Font.PLAIN, 38f);
            return fontAwesome;
        } catch (Exception e) {
            throw new RuntimeException("FontAwesome could not be loaded.");
        }
    }
}
