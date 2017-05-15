package Theme;

import java.awt.*;

/**
 * The NightTheme is supposed to be used during evenings or
 * nights, when too bright colours can disturb the user.
 */
public class NightTheme implements Theme {

    private String name = "Night";

    public Color rail()
    {
        return new Color(0x999999);
    }
    public Color background()
    {
        return new Color(0x242f3e);
    }
    public Color toolbar()
    {
        return new Color(0x3f3f3f);
    }
    public Color icon()
    {
        return new Color(0xFFFFFF);
    }
    public Color boundary()
    {
        return new Color(0xD31BC6);
    }
    public Color border()
    {
        return new Color(0x000000);
    }
    public Color searchfield()
    {
        return new Color(0x323236);
    }
    public Color defaulttext() { return new Color(0xD1D1D1); }
    public Color toolTipBackground() { return new Color(0x555258); }
    public Color toolTipForeground() { return new Color(0xFEFDFF); }
    public Color canvasPopupBackground() { return new Color(0x7562A9); }
    public Color canvasPopupForeground() { return new Color(0xFFFFFF); }
    public Color canvasExtra() {return new Color(255, 255, 255, 40);}
    public Color toolActivated() { return new Color(0x875997); }
    public Color toolHover() {return new Color(0xD1B1E4);}
    public Color scrollBarThumb() {return new Color(0x875997);}
    public Color pointHover() {return new Color(0xAEA9AF);}
    public Color addressMarkerForeground() {return new Color(0x875997);}
    public Color markerBackground() {return new Color(0XFFFFFF);}
    public Color fromMarkerForeground() {return new Color(0x875997);}
    public Color toMarkerForeground() {return new Color(0x875997);}
    public Color inactiveButton() {return new Color(0xA4A6A7);}
    public Color poiButtonsForeground() {return new Color(0xFFFBFB);}
    public Color progressBarBackground() {
        return new Color(0x0C0A0B);
    }
    public Color progressBarForeground() {
        return new Color(0x875997);
    }
    public Color cityName() { return new Color(0xCCC9C7); }
    public Color roadName() { return new Color(0x000000); }
    public Color barName() { return new Color(0xF9FF17); }
    public Color nightClubName() { return new Color(0x00FFFF); }
    public Color fastFoodName() { return new Color(0xFF00F6); }
    public Color hospital() { return new Color(0xA80405); }
    public Color parkingAmenity() { return new Color(0x858585); }
    public Color sportAmenity() { return new Color(0x9ECBA0); }
    public Color railwayStation() { return new Color(0xC2F5FF); }
    public Color airportAmenity() { return new Color(0x949494); }
    public Color university() { return new Color(0x913D3E); }
    public Color poi() { return new Color(0x3EFF00); }
    public Color water()
    {
        return new Color(0x515c6d);
    }
    public Color park() { return new Color(0x044512); }
    public Color forest()
    {
        return new Color(0x033A11);
    }
    public Color grassland()
    {
        return new Color(0x043A0F);
    }
    public Color grass()
    {
        return new Color(0x02370E);
    }
    public Color heath()
    {
        return new Color(0x616F37);
    }
    public Color meadow()
    {
        return new Color(0x033A10);
    }
    public Color farmland() { return new Color(0x616F37); }
    public Color beach()
    {
        return new Color(0x5F5C47);
    }
    public Color building()
    {
        return new Color(0x34445A);
    }
    public Color bridge(){
        return new Color(0x6C6C6C);
    }
    public Color sportspitch(){
        return new Color(0x044512);
    }
    public Color wetland(){
        return new Color(0x34445A);
    }
    public Color commonland(){
        return new Color(0x044512);
    }
    public Color playground(){
        return new Color(0x044512);
    }
    public Color parking(){
        return new Color(0x8B8B8B);
    }
    public Color sportstrack(){ return new Color(0x044512); }

    // Roads
    public Color route() {
        return new Color(234, 93, 255, 129);
    }
    public Color routeBorder(){
        return new Color(135, 32, 137, 129);
    }
    public Color motorway() { return new Color(0xD47487); }
    public Color trunkRoad() { return new Color(0xC06678); }
    public Color primaryRoad() { return new Color(0xAE6202); }
    public Color secondaryRoad() { return new Color(0x998206); }
    public Color tertiaryRoad() { return new Color(0x787878); }
    public Color unclassifiedRoad() { return new Color(0x787878); }
    public Color residentialRoad() { return new Color(0x787878); }
    public Color livingStreet() { return new Color(0x787878); }
    public Color serviceRoad() { return new Color(0x787878); }
    public Color raceway() { return new Color(0x787878); }
    public Color pedestrianStreet() { return new Color(0x787878); }
    public Color track() { return new Color(0x787878); }
    public Color steps() { return new Color(0x787878); }
    public Color footway() { return new Color(0x787878); }
    public Color footwayArea() { return new Color(0x45444E); }
    public Color bridleway() { return new Color(0x787878); }
    public Color cycleway() { return new Color(0x787878); }
    public Color path() { return new Color(0x787878); }
    public Color road() { return new Color(0x787878); }
    public Color airport() { return new Color(0x8D8993); }

    @Override
    public String getName()
    {
        return name;
    }
}
