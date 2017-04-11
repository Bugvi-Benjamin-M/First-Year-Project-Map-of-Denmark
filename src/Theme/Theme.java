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

    public abstract Color water();
    public abstract Color park();
    public abstract Color sand();
    public abstract Color building();

    //Roads
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
    public abstract Color bridleway();
    public abstract Color cycleway();
    public abstract Color path();
    public abstract Color road();

    public abstract Color cityName();


    public abstract String getName();
}
