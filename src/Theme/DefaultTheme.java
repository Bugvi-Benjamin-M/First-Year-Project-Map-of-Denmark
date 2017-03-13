package Theme;

import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 13/03/2017
 */
public class DefaultTheme implements Theme {

    @Override
    public Color getWaterColor() {
        return new Color(0x99B3CC);
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(0xede6de);
    }

    @Override
    public Color getParkColor() {
        return new Color(0xb4d39a);
    }

    @Override
    public Color getSandColor() {
        return new Color(0xfcf99a);
    }

}
