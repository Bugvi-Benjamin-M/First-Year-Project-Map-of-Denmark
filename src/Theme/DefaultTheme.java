package Theme;

import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 13/03/2017
 */
public class DefaultTheme implements Theme {

    private String name = "Default";

    public Color rail()
    {
        return new Color(0x999999);
    }
    public Color boundary()
    {
        return new Color(0xFF1DF2);
    }
    public Color background()
    {
        return new Color(0xede6de);
    }
    public Color toolbar()
    {
        return new Color(0xEEEEEE);
    }
    public Color icon()
    {
        return new Color(0x000000);
    }
    public Color border()
    {
        return new Color(0x000000);
    }
    public Color searchfield()
    {
        return new Color(0xFFF9FC);
    }
    public Color defaulttext() { return new Color(0xA0A39F); }
    public Color toolTipBackground() { return new Color(0xB9B7B3); }
    public Color toolTipForeground() { return new Color(0x050505); }
    public Color canvasPopupBackground() { return new Color(0xDED0A9); }
    public Color canvasPopupForeground() { return new Color(0x000000); }
    public Color canvasExtra() {return new Color(255, 255, 255, 40);}

    public Color toolActivated() { return new Color(0x4471C2); }
    public Color toolHover() {return new Color(0x83B1F1);}

    public Color scrollBarThumb() {return new Color(0x789AE0);}

    public Color pointHover() {return new Color(0xAEA9AF);}

    public Color poiButtonsForeground() {return new Color(0x000000);}

    public Color progressBarBackground() {
        return new Color(0x8E8A8C);
    }

    public Color progressBarForeground() {
        return new Color(0x4471C2);
    }

    public Color cityName() { return new Color(0x000000); }
    public Color roadName() { return new Color(0x000000); }
    public Color barName() { return new Color(0x000000); }
    public Color nightClubName() { return new Color(0x000000); }
    public Color fastFoodName() { return new Color(0x000000); }
    public Color hospital() { return new Color(0xD30408); }
    public Color placeOfWorship() { return new Color(0x000000); }
    public Color parkingAmenity() { return new Color(0x0A00FF); }
    public Color sportAmenity() { return new Color(0xFF7D00); }

    public Color water()
    {
        return new Color(0x6993D1);
    }
    public Color park()
    {
        return new Color(0xC0E48F);
    }
    public Color forest()
    {
        return new Color(0x9DCA8A);
    }
    public Color grassland()
    {
        return new Color(0xC0E48F);
    }
    public Color grass()
    {
        return new Color(0xC0E48F);
    }
    public Color heath()
    {
        return new Color(0xCCE478);
    }
    public Color meadow()
    {
        return new Color(0xC0E48F);
    }
    public Color farmland() { return new Color(0xCCE478); }
    public Color beach()
    {
        return new Color(0xfcf99a);
    }
    public Color building()
    {
        return new Color(0x959795);
    }
    public Color bridge(){ return new Color(0xD5D4E3); }
    public Color sportspitch(){
        return new Color(0x90E49E);
    }
    public Color wetland(){
        return new Color(0x55C76B);
    }
    public Color commonland(){
        return new Color(0xC0E48F);
    }
    public Color playground(){
        return new Color(0xCFE48A);
    }
    public Color parking(){ return new Color(0xD5D4E3); }
    public Color sportstrack(){
        return new Color(0x90E49E);
    }
    public Color railwayStation() { return new Color(0x00CEE4); }
    public Color airportAmenity() { return new Color(0x000000); }
    public Color university() { return new Color(0xF37700); }
    public Color poi() { return new Color(0xE40006); }

    // Roads:
    public Color route() {return new Color(0x9E00FF);}
    public Color motorway() { return new Color(0xE27B90); }
    public Color trunkRoad() { return new Color(0xE27B90); }
    public Color primaryRoad() { return new Color(0xF18C03); }
    public Color secondaryRoad() { return new Color(0xF1D108); }
    public Color tertiaryRoad() { return new Color(0xFFFFFF); }
    public Color unclassifiedRoad() { return new Color(0xFFFFFF); }
    public Color residentialRoad() { return new Color(0xFFFFFF); }
    public Color livingStreet() { return new Color(0xFFFFFF); }
    public Color serviceRoad() { return new Color(0xFFFFFF); }
    public Color busGuideway() { return new Color(0xFFFFFF); }
    public Color escape() { return new Color(0xFFFFFF); }
    public Color raceway() { return new Color(0xFFFFFF); }
    public Color pedestrianStreet() { return new Color(0xF2F2F2); }
    public Color track() { return new Color(0xFFFFFF); }
    public Color steps() { return new Color(0x000000); }
    public Color footway() { return new Color(0x000000); }
    public Color footwayArea() { return new Color(0xD5D4E3); }
    public Color bridleway() { return new Color(0x000000); }
    public Color cycleway() { return new Color(0x000000); }
    public Color path() { return new Color(0x000000); }
    public Color road() { return new Color(0x9F99F1); }
    public Color airport() { return new Color(0x8D8993); }

    @Override
    public String getName()
    {
        return name;
    }
}
