package View;

import Model.Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class MapCanvas extends View {
    private Dimension dimension;
    private java.util.List<Shape> shapes;
    private AffineTransform transform;

    public MapCanvas(Dimension dimension) {
        super();
        transform = new AffineTransform();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.dimension = dimension;
        setPreferredSize(this.dimension);
        addComponentListener();
        shapes = new ArrayList<>();
        shapes.add(new Rectangle(10, 10, 100, 100));
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
        g2D.setTransform(transform);
        g2D.setColor(Color.BLACK);
        g2D.setStroke(new BasicStroke(0.2f));
        for(Shape shape : shapes) {

            g2D.draw(shape);
            System.out.println(shape.getBounds());
        }
        Path2D boundary = new Path2D.Double();
        boundary.moveTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMinLatitude());
        boundary.lineTo(Model.getInstance().getMaxLongitude(), Model.getInstance().getMinLatitude());
        boundary.lineTo(Model.getInstance().getMaxLongitude(), Model.getInstance().getMaxLatitude());
        boundary.lineTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMaxLatitude());
        boundary.lineTo(Model.getInstance().getMinLongitude(), Model.getInstance().getMinLatitude());
        g2D.draw(boundary);
    }

    public void zoom(double factor) {
        transform.preConcatenate(AffineTransform.getScaleInstance(factor, factor));
        repaint();
    }

    public void pan(double dx, double dy) {
        transform.preConcatenate(AffineTransform.getTranslateInstance(dx, dy));
        repaint();
    }
}
