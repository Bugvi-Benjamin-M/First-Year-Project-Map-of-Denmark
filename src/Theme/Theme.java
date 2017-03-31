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

    public abstract Color water();
    public abstract Color park();
    public abstract Color sand();

    public abstract Color highwayroad();
    public abstract Color primaryroad();
    public abstract Color secondaryroad();
    public abstract Color tertiaryroad();

    public abstract String getName();
}
