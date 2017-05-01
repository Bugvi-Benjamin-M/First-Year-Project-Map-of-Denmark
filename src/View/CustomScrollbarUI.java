package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.plaf.metal.MetalScrollBarUI;
import javax.swing.plaf.metal.MetalScrollButton;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 27/04/2017
 */
public class CustomScrollbarUI extends MetalScrollBarUI {


    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(ThemeHelper.color("toolbar"));
        g.fill3DRect((int)trackBounds.getX(), (int)trackBounds.getY(), (int)trackBounds.getWidth(), (int)trackBounds.getHeight(), false);

    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        g.setColor(ThemeHelper.color("scrollBarThumb"));
        g.fill3DRect((int)thumbBounds.getX(), (int) thumbBounds.getY(), (int) thumbBounds.getWidth(), (int)thumbBounds.getHeight(), true);
    }

    @Override
    protected JButton createDecreaseButton( int orientation )
    {
        decreaseButton = new MetalScrollButton( 0, 0, false ) {

            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }
        };




        return decreaseButton;
    }

    @Override
    protected JButton createIncreaseButton( int orientation )
    {
        increaseButton =  new MetalScrollButton( 0, 0, false ) {

            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 0;
            }
        };
        return increaseButton;
    }


}
