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
    public Color getBoundaryColor() {
        return new Color(0xFF1DF2);
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(0xede6de);
    }

    @Override
    public Color getToolbarColor() {
        return new Color(0xEEEEEE);
    }

    @Override
    public Color getParkColor() {
        return new Color(0xb4d39a);
    }

    @Override
    public Color getSandColor() {
        return new Color(0xfcf99a);
    }

    @Override
    public Color getHighwayRoadColor(){
        return new Color(0xf15d00);
    }

    @Override
    public Color getPrimaryRoadColor(){
        return new Color(0xD4A23F);
    }

    @Override
    public Color getSecondaryRoadColor(){
        return new Color(0xFCF99A);
    }

    @Override
    public Color getTertiaryRoadColor(){
        return new Color(0xFFFFFF);
    }

}
