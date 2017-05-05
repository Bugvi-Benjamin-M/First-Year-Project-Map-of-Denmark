package View;

import Helpers.FontAwesome;
import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 04/05/2017
 */
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

    public JourneyPlannerTransportTypeButtons() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),"Means of Transportation:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("Verdana", Font.PLAIN, TITLE_FONT), ThemeHelper.color("icon")));
        onFootButton = new JLabel("\uf183");
        onFootButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        onFootButton.setOpaque(true);
        onFootButton.setPreferredSize(new Dimension(ONFOOT_BUTTON_WIDTH, BUTTON_HEIGHT));
        onFootButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        onFootButton.setToolTipText(ON_FOOT_TOOLTIP);
        bicycleButton = new JLabel("\uf206");
        bicycleButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        bicycleButton.setOpaque(true);
        bicycleButton.setPreferredSize(new Dimension(BICYCLE_BUTTON_WIDTH, BUTTON_HEIGHT));
        bicycleButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        bicycleButton.setToolTipText(BICYCLE_TOOLTIP);
        carButton = new JLabel("\uf1b9");
        carButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        carButton.setOpaque(true);
        carButton.setPreferredSize(new Dimension(CAR_BUTTON_WIDTH, BUTTON_HEIGHT));
        carButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        carButton.setToolTipText(CAR_TOOLTIP);
        applyTheme();
        add(onFootButton);
        add(Box.createHorizontalStrut(SPACE_BETWEEN_BUTTONS));
        add(bicycleButton);
        add(Box.createHorizontalStrut(SPACE_BETWEEN_BUTTONS));
        add(carButton);
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
        setBorder(BorderFactory.createTitledBorder(null,"Means of Transportation:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font("Verdana", Font.PLAIN, TITLE_FONT), ThemeHelper.color("icon")));
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


}
