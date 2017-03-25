package Theme;

import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 24/03/2017
 */
public class ColorBlindTheme implements Theme {

    private String name = "Color Blind";

    @Override
    public Color background() {
        return new Color(0xDE2E64);
    }

    @Override
    public Color toolbar() {
        return new Color(0x9BDEC9);
    }

    @Override
    public Color water() {
        return new Color(0x0F0DDE);
    }

    @Override
    public Color park() {
        return new Color(0x13DE00);
    }

    @Override
    public Color sand() {
        return new Color(0xDE721F);
    }

    @Override
    public Color highwayroad() {
        return new Color(0xDE86B9);
    }

    @Override
    public Color primaryroad() {
        return new Color(0xDE244F);
    }

    @Override
    public Color secondaryroad() {
        return new Color(0xDE395D);
    }

    @Override
    public Color tertiaryroad() {
        return new Color(0xDE4279);
    }

    @Override
    public String getName() {
        return name;
    }
}
