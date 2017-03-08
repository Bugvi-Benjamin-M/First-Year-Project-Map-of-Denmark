package View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.*;
import java.util.List;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class MapCanvas extends View {
    private Dimension dimension;
    private JLabel widthLabel, heightLabel;
    private java.util.List<Shape> shapes;

    public MapCanvas(Dimension dimension) {
        super();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(Color.BLACK);
        this.dimension = dimension;
        setPreferredSize(this.dimension);
        addComponentListener();
        widthLabel = new JLabel(""+getWidth());
        heightLabel = new JLabel(""+getHeight());
        this.add(widthLabel);
        this.add(heightLabel);
        shapes = new ArrayList<>();
    }

    public void resetShapes(){
        shapes.clear();
    }

    public void addShapes(java.util.List<Shape> shapes){
        this.shapes.addAll(shapes);
    }

    private void addComponentListener() {
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                widthLabel.setText(""+getWidth());
                heightLabel.setText(""+getHeight());
            }

            @Override
            public void componentMoved(ComponentEvent e) {

            }

            @Override
            public void componentShown(ComponentEvent e) {

            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        for(Shape shape : shapes) {
            g2D.setColor(Color.WHITE);
            g2D.setStroke(new BasicStroke(10.1f));
            g2D.draw(shape);
        }
    }
}
