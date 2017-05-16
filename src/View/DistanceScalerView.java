package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * The DistanceScalerView is a visual component representing the how much approx.
 * 100 pixels corresponds to in according to the actual distance on the map.
 */
public class DistanceScalerView extends View {

    private final int PANEL_WIDTH = 120;
    private final int PANEL_HEIGHT = 40;
    private final int TEXT_WIDTH = 100;
    private final int TEXT_HEIGHT = 20;
    private JLabel distance;
    private SpringLayout layout;

    public DistanceScalerView() {
        super();
        distance = new JLabel();
        distance.setHorizontalAlignment(SwingConstants.CENTER);
        distance.setFont(new Font(getFont().getName(),getFont().getStyle(),22));
        distance.setOpaque(false);
        distance.setMinimumSize(new Dimension(TEXT_WIDTH,TEXT_HEIGHT));
        distance.setPreferredSize(new Dimension(TEXT_WIDTH,TEXT_HEIGHT));
        layout = new SpringLayout();
        this.setLayout(layout);
        this.setPreferredSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setMinimumSize(new Dimension(PANEL_WIDTH,PANEL_HEIGHT));
        this.setFocusable(false);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, distance,
                0,SpringLayout.HORIZONTAL_CENTER,this);
        layout.putConstraint(SpringLayout.NORTH, distance,
                0, SpringLayout.NORTH,this);
        this.add(distance);
        themeChanged();
        setBorder(BorderFactory.createLineBorder(ThemeHelper.color("border")));
    }

    /**
     * Updates and applies the currently selected theme to the component
     */
    public void themeChanged() {
        distance.setForeground(ThemeHelper.color("icon"));
        this.setBackground(ThemeHelper.color("canvasPopupBackground"));
    }

    /**
     * Paints the component with the distance marker
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(ThemeHelper.color("icon"));
        g2D.drawLine(10,PANEL_HEIGHT-15,110,PANEL_HEIGHT-15);
        g2D.drawLine(10,15,10,PANEL_HEIGHT-5);
        g2D.drawLine(110,15,110,PANEL_HEIGHT-5);
    }

    /**
     * Sets the distance on the scaler
     * @param distance The distance between two points on the map
     */
    public void setDistance(double distance) {
        String text, ending;
        if (distance < 1000) {
            text = String.valueOf((int) Math.round(distance));
            ending = " m";
        } else {
            text = String.valueOf((int) Math.round(distance/1000));
            ending = " km";
        }
        this.distance.setText("<html><body style='text-align: center;'>"+
                text+ending+"</body></html>");
    }
}
