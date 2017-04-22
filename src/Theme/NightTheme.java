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
    public Color rail() {
        return new Color(0x999999);
    }

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
        return new Color(0xD31BC6);
    }

    @Override
    public Color border() {
        return new Color(0x000000);
    }

    @Override
    public Color searchfield() {
        return new Color(0x323236);
    }

    public Color defaulttext() {
        return new Color(0x8A8FBF);
    }

    public Color toolTipBackground() {return new Color(0x555258);}

    public Color toolTipForeground() {return new Color(0xFEFDFF);}

    public Color canvasPopupBackground() {
        return new Color(0x7562A9);
    }

    public Color canvasPopupForeground() {
        return new Color(0xFFFFFF);
    }

    public Color cityName() { return new Color(0xCCC9C7); }
    public Color roadName() {
        return new Color(0x000000);
    }
    public Color barName(){
        return new Color(0xF9FF17);
    }
    public Color nightClubName(){
        return new Color(0xF9FF17);
    }
    public Color fastFoodName(){
        return new Color(0x000000);
    }
    public Color iceCreamName(){ return new Color(0x000000); }

    @Override
    public Color water() {
        return new Color(0x515c6d);
    }

    @Override
    public Color park() {
        return new Color(0x044512);
    }

    @Override
    public Color forest(){
        return new Color(0x033A11);
    }

    @Override
    public Color grassland(){
        return new Color(0x043A0F);
    }

    @Override
    public Color grass() {
        return new Color(0x02370E);
    }

    @Override
    public Color heath() {
        return new Color(0x616F37);
    }

    @Override
    public Color meadow() {
        return new Color(0x033A10);
    }

    @Override
    public Color farmland() {
        return new Color(0x616F37);
    }

    @Override
    public Color sand() {
        return new Color(0x5F5C47);
    }

    @Override
    public Color building(){
        return new Color(0x34445A);
    }

    //Roads
    public Color motorway(){
        return new Color(0xD47487);
    }
    public Color trunkRoad(){
        return new Color(0xC06678);
    }
    public Color primaryRoad(){
        return new Color(0xAE6202);
    }
    public Color secondaryRoad(){
        return new Color(0x998206);
    }
    public Color tertiaryRoad(){
        return new Color(0x787878);
    }
    public Color unclassifiedRoad(){
        return new Color(0x787878);
    }
    public Color residentialRoad(){
        return new Color(0x787878);
    }
    public Color livingStreet(){
        return new Color(0x787878);
    }
    public Color serviceRoad(){
        return new Color(0x787878);
    }
    public Color busGuideway(){
        return new Color(0x787878);
    }
    public Color escape(){
        return new Color(0x787878);
    }
    public Color raceway(){
        return new Color(0x787878);
    }
    public Color pedestrianStreet(){
        return new Color(0x787878);
    }
    public Color track(){
        return new Color(0x787878);
    }
    public Color steps(){
        return new Color(0x787878);
    }
    public Color footway(){
        return new Color(0x787878);
    }
    public Color footwayArea(){
        return new Color(0x45444E);
    }
    public Color bridleway(){
        return new Color(0x787878);
    }
    public Color cycleway(){
        return new Color(0x787878);
    }
    public Color path(){
        return new Color(0x787878);
    }
    public Color road(){
        return new Color(0x787878);
    }
    public Color roadBorder(){
        return  new Color(0000000);
    }

    @Override
    public String getName() {
        return name;
    }
}
