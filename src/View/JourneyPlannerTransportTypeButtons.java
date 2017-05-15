package View;

import Helpers.FontAwesome;
import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;


public class JourneyPlannerTransportTypeButtons extends View {


    private JLabel onFootButton;
    private JLabel bicycleButton;
    private JLabel carButton;
    private final float BUTTON_FONT_SIZE = 40f;

    private final int BICYCLE_BUTTON_WIDTH = 60;
    private final int ONFOOT_BUTTON_WIDTH = 35;
    private final int CAR_BUTTON_WIDTH = 55;
    private final int BUTTON_HEIGHT = 45;
    private final int SPACE_BETWEEN_BUTTONS = 18;
    private final int TITLE_FONT = 15;
    private final String ON_FOOT_TOOLTIP = "Walking!";
    private final String BICYCLE_TOOLTIP = "Cycling!";
    private final String CAR_TOOLTIP = "Driving!";

    private final int SMALL_BICYCLE_BUTTON_WIDHT = 28;
    private final int SMALL_ONFOOT_BUTTON_WIDTH = 15;
    private final int SMALL_CAR_BUTTON_WIDTH = 26;
    private final int SMALL_BUTTON_HEIGHT = 26;
    private final float SMALL_BUTTON_FONT_SIZE = 20f;
    private final int SMALL_SPACE_BETWEEN_BUTTONS = 8;
    private final int SMALL_TITLE_FONT = 10;

    private boolean isLargeState;

    public JourneyPlannerTransportTypeButtons() {
        onFootButton = new JLabel("\uf183");
        onFootButton.setOpaque(true);
        onFootButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        onFootButton.setToolTipText(ON_FOOT_TOOLTIP);
        bicycleButton = new JLabel("\uf206");
        bicycleButton.setOpaque(true);
        bicycleButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        bicycleButton.setToolTipText(BICYCLE_TOOLTIP);
        carButton = new JLabel("\uf1b9");
        carButton.setOpaque(true);
        carButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        carButton.setToolTipText(CAR_TOOLTIP);
        applyTheme();
        applyLargeState();
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        onFootButton.setBackground(ThemeHelper.color("toolbar"));
        onFootButton.setForeground(ThemeHelper.color("icon"));
        onFootButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        bicycleButton.setBackground(ThemeHelper.color("toolbar"));
        bicycleButton.setForeground(ThemeHelper.color("icon"));
        bicycleButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        carButton.setBackground(ThemeHelper.color("toolbar"));
        carButton.setForeground(ThemeHelper.color("icon"));
        carButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        if(isLargeState) setBorder(BorderFactory.createTitledBorder(null,"Means of Transportation:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("Verdana", Font.PLAIN, TITLE_FONT), ThemeHelper.color("icon")));
        else setBorder(BorderFactory.createTitledBorder(null,"Means of Transportation:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("Verdana", Font.PLAIN, SMALL_TITLE_FONT), ThemeHelper.color("icon")));
    }

    public JLabel getOnFootButton() {
        return onFootButton;
    }

    public JLabel getBicycleButton() {
        return bicycleButton;
    }

    public JLabel getCarButton() {
        return carButton;
    }

    public void applySmallerState() {
        isLargeState = false;
        removeAll();
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),"Means of Transportation:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("Verdana", Font.PLAIN, SMALL_TITLE_FONT), ThemeHelper.color("icon")));
        onFootButton.setPreferredSize(new Dimension(SMALL_ONFOOT_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT));
        onFootButton.setFont(FontAwesome.getFontAwesome().deriveFont(SMALL_BUTTON_FONT_SIZE));
        bicycleButton.setPreferredSize(new Dimension(SMALL_BICYCLE_BUTTON_WIDHT, SMALL_BUTTON_HEIGHT));
        bicycleButton.setFont(FontAwesome.getFontAwesome().deriveFont(SMALL_BUTTON_FONT_SIZE));
        carButton.setPreferredSize(new Dimension(SMALL_CAR_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT));
        carButton.setFont(FontAwesome.getFontAwesome().deriveFont(SMALL_BUTTON_FONT_SIZE));
        add(onFootButton);
        add(Box.createHorizontalStrut(SMALL_SPACE_BETWEEN_BUTTONS));
        add(bicycleButton);
        add(Box.createHorizontalStrut(SMALL_SPACE_BETWEEN_BUTTONS));
        add(carButton);
    }

    public void applyLargeState() {
        isLargeState = true;
        removeAll();
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),"Means of Transportation:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("Verdana", Font.PLAIN, TITLE_FONT), ThemeHelper.color("icon")));
        onFootButton.setPreferredSize(new Dimension(ONFOOT_BUTTON_WIDTH, BUTTON_HEIGHT));
        onFootButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        bicycleButton.setPreferredSize(new Dimension(BICYCLE_BUTTON_WIDTH, BUTTON_HEIGHT));
        bicycleButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        carButton.setPreferredSize(new Dimension(CAR_BUTTON_WIDTH, BUTTON_HEIGHT));
        carButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        add(onFootButton);
        add(Box.createHorizontalStrut(SPACE_BETWEEN_BUTTONS));
        add(bicycleButton);
        add(Box.createHorizontalStrut(SPACE_BETWEEN_BUTTONS));
        add(carButton);
    }
}
