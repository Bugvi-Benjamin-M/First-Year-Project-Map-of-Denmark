package Theme;

import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 24/03/2017
 */
public class NightTheme implements Theme {

    private String name = "Night";

    @Override
    public Color background() {
        return new Color(0x242f3e);
    }

    @Override
    public Color toolbar() {
        return new Color(0x3f3f3f);
    }

    @Override
    public Color icon() {
        return new Color(0xFFFFFF);
    }

    @Override
    public Color boundary() {
        return new Color(0xFF1DF2);
    }

    @Override
    public Color border() {
        return new Color(0x000000);
    }

    @Override
    public Color searchfield() {
        return new Color(0x323236);
    }

    @Override
    public Color defaulttext() {
        return new Color(0xA4ADDE);
    }

    @Override
    public Color water() {
        return new Color(0x515c6d);
    }

    @Override
    public Color park() {
        return new Color(0x263c3f);
    }

    @Override
    public Color sand() {
        return new Color(0x2f3948);
    }

    //Roads
    public Color motorway(){
        return new Color(0x1f2835);
    }
    public Color trunkRoad(){
        return new Color(0x1f2835);
    }
    public Color primaryRoad(){
        return new Color(0x1f2835);
    }
    public Color secondaryRoad(){
        return new Color(0x1f2835);
    }
    public Color tertiaryRoad(){
        return new Color(0x1f2835);
    }
    public Color unclassifiedRoad(){
        return new Color(0x1f2835);
    }
    public Color residentialRoad(){
        return new Color(0x1f2835);
    }
    public Color livingStreet(){
        return new Color(0x1f2835);
    }
    public Color serviceRoad(){
        return new Color(0x1f2835);
    }
    public Color busGuideway(){
        return new Color(0x1f2835);
    }
    public Color escape(){
        return new Color(0x1f2835);
    }
    public Color raceway(){
        return new Color(0x1f2835);
    }
    public Color pedestrianStreet(){
        return new Color(0x1f2835);
    }
    public Color track(){
        return new Color(0x1f2835);
    }
    public Color steps(){
        return new Color(0x1f2835);
    }
    public Color footway(){
        return new Color(0x1f2835);
    }
    public Color bridleway(){
        return new Color(0x1f2835);
    }
    public Color cycleway(){
        return new Color(0x1f2835);
    }
    public Color path(){
        return new Color(0x1f2835);
    }
    public Color road(){
        return new Color(0x1f2835);
    }

    @Override
    public String getName() {
        return name;
    }
}
