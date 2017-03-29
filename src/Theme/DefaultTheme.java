package Theme;

import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 13/03/2017
 */
public class DefaultTheme implements Theme {

    private static String name = "Default";

    @Override
    public Color water() {
        return new Color(0x99B3CC);
    }

    @Override
    public Color boundary() {
        return new Color(0xFF1DF2);
    }

    @Override
    public Color background() {
        return new Color(0xede6de);
    }

    @Override
    public Color toolbar() {
        return new Color(0xEEEEEE);
    }

    @Override
    public Color icon() {
        return new Color(0x000000);
    }

    @Override
    public Color park() {
        return new Color(0xb4d39a);
    }

    @Override
    public Color sand() {
        return new Color(0xfcf99a);
    }

    @Override
    public Color highwayroad(){
        return new Color(0xf15d00);
    }

    @Override
    public Color primaryroad(){
        return new Color(0xD4A23F);
    }

    @Override
    public Color secondaryroad(){
        return new Color(0xFCF99A);
    }

    @Override
    public Color tertiaryroad(){
        return new Color(0xFFFFFF);
    }

    @Override
    public String getName() {
        return name;
    }

}
