package View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class MapCanvas extends View implements Observer {
    private Observable observable;
    private Dimension dimension;

    public MapCanvas(Dimension dimension) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.BLACK);
        this.dimension = dimension;
        setPreferredSize(this.dimension);
    }

    public void setObserver(Observable observer) {
        observable = observer;
        observer.addObserver(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void update(Observable observer, Object arg) {
        repaint();
    }
}
