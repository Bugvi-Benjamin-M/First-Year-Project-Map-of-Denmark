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
    public Color border() {
        return new Color(0x000000);
    }

    @Override
    public Color searchfield() {
        return new Color(0xFFF9FC);
    }

    @Override
    public Color defaulttext() {
        return new Color(0xA0A39F);
    }

    public Color cityName() { return new Color(0x000000); }

    @Override
    public Color water() {
        return new Color(0x91BEFF);
    }

    @Override
    public Color park() {
        return new Color(0xC0E48F);
    }

    @Override
    public Color sand(){
        return new Color(0xfcf99a);
    }
    @Override
    public Color building(){
        return new Color(0x959795);
    }

    //Roads:
    public Color motorway() {
        return new Color(0xF100C8);
    }
    public Color trunkRoad() {
        return new Color(0xF13400);
    }
    public Color primaryRoad(){
        return new Color(0xF18C03);
    }
    public Color secondaryRoad(){
        return new Color(0xF1D108);
    }
    public Color tertiaryRoad(){
        return new Color(0xE4F136);
    }
    public Color unclassifiedRoad(){
        return new Color(0xFFFFFF);
    }
    public Color residentialRoad(){
        return new Color(0xFFFFFF);
    }
    public Color livingStreet(){
        return new Color(0xFFFFFF);
    }
    public Color serviceRoad(){
        return new Color(0xFFFFFF);
    }
    public Color busGuideway(){
        return new Color(0xFFFFFF);
    }
    public Color escape(){
        return new Color(0x9F99F1);
    }
    public Color raceway(){
        return new Color(0x9F99F1);
    }
    public Color pedestrianStreet(){
        return new Color(0xFFFFFF);
    }
    public Color track(){
        return new Color(0xF17205);
    }
    public Color steps(){
        return new Color(0xF10000);
    }
    public Color footway(){
        return new Color(0xF10000);
    }
    public Color bridleway(){
        return new Color(0x9F99F1);
    }
    public Color cycleway(){
        return new Color(0x040BF1);
    }
    public Color path(){
        return new Color(0x9F99F1);
    }
    public Color road(){
        return new Color(0x9F99F1);
    }


    @Override
    public String getName() {
        return name;
    }

}
