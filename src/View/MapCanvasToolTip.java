package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 20/04/2017
 */
public class MapCanvasToolTip extends JToolTip {

    private static final int TOOLTIP_SIZE = 20;
    private static final int TOOLTIP_OFFSET = 10;

    public MapCanvasToolTip() {
        super();
        super.createToolTip();
        setFont(new Font(getFont().getName(), getFont().getStyle(), TOOLTIP_SIZE));
    }

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return new Point((int) event.getPoint().getX()+TOOLTIP_OFFSET, (int) event.getPoint().getY()+TOOLTIP_OFFSET);
    }

}
