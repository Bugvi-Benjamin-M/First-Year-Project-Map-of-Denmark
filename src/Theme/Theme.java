package Theme;

import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 13/03/2017
 */
public interface Theme {

    public abstract Color background();
    public abstract Color toolbar();
    public abstract Color icon();
    public abstract Color boundary();
    public abstract Color border();
    public abstract Color searchfield();
    public abstract Color defaulttext();

    public abstract Color toolTipBackground();
    public abstract Color toolTipForeground();

    public abstract Color canvasPopupBackground();
    public abstract Color canvasPopupForeground();
    public abstract Color canvasExtra();

    public abstract Color toolActivated();
    public abstract Color toolHover();

    public abstract Color scrollBarThumb();

    public abstract Color pointHover();

    public abstract Color poiButtonsForeground();

    public abstract Color progressBarBackground();
    public abstract Color progressBarForeground();

    public abstract Color water();
    public abstract Color park();
    public abstract Color forest();
    public abstract Color grassland();
    public abstract Color grass();
    public abstract Color heath();
    public abstract Color farmland();
    public abstract Color meadow();
    public abstract Color beach();
    public abstract Color building();
    public abstract Color bridge();
    public abstract Color sportspitch();
    public abstract Color wetland();
    public abstract Color commonland();
    public abstract Color playground();
    public abstract Color parking();
    public abstract Color sportstrack();

    // Roads
    public abstract Color route();
    public abstract Color motorway();
    public abstract Color trunkRoad();
    public abstract Color primaryRoad();
    public abstract Color secondaryRoad();
    public abstract Color tertiaryRoad();
    public abstract Color unclassifiedRoad();
    public abstract Color residentialRoad();
    public abstract Color livingStreet();
    public abstract Color serviceRoad();
    public abstract Color busGuideway();
    public abstract Color escape();
    public abstract Color raceway();
    public abstract Color pedestrianStreet();
    public abstract Color track();
    public abstract Color steps();
    public abstract Color footway();
    public abstract Color footwayArea();
    public abstract Color bridleway();
    public abstract Color cycleway();
    public abstract Color path();
    public abstract Color road();
    public abstract Color airport();

    public abstract Color rail();

    public abstract Color cityName();
    public abstract Color roadName();
    public abstract Color barName();
    public abstract Color nightClubName();
    public abstract Color fastFoodName();

    public abstract Color hospital();
    public abstract Color placeOfWorship();
    public abstract Color parkingAmenity();
    public abstract Color sportAmenity();
    public abstract Color railwayStation();
    public abstract Color airportAmenity();
    public abstract Color university();
    public abstract Color poi();

    public abstract String getName();
}
