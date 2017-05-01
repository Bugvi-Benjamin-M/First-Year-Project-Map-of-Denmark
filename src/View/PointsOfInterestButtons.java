package View;

import Helpers.FontAwesome;
import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 27/04/2017
 */
public class PointsOfInterestButtons extends View {

    private final float BUTTON_FONT_SIZE = 40f;
    private final int BUTTON_WIDTH = 40;
    private final int BUTTON_HEIGHT = 45;
    private final int SPACE_BETWEEN_BUTTONS = 18;
    private final String NEW_BUTTON_TOOLTIP = "Add a New Point to the Map";
    private final String DELETEALL_BUTTON_TOOLTIP = "Delete all Points";

    private JLabel newPointButton;
    private JLabel deleteAllButton;


    public PointsOfInterestButtons() {
        newPointButton = new JLabel("\uf124");
        deleteAllButton = new JLabel("\uf1f8");
        newPointButton.setOpaque(true);
        deleteAllButton.setOpaque(true);
        newPointButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        newPointButton.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
        deleteAllButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        deleteAllButton.setPreferredSize(new Dimension(BUTTON_WIDTH,BUTTON_HEIGHT));
        newPointButton.setToolTipText(NEW_BUTTON_TOOLTIP);
        deleteAllButton.setToolTipText(DELETEALL_BUTTON_TOOLTIP);
        newPointButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        deleteAllButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        add(newPointButton);
        add(Box.createHorizontalStrut(SPACE_BETWEEN_BUTTONS));
        add(deleteAllButton);
        applyTheme();
    }


    public void applyTheme() {
        this.setBackground(ThemeHelper.color("toolbar"));
        newPointButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        deleteAllButton.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        newPointButton.setBackground(ThemeHelper.color("toolbar"));
        deleteAllButton.setBackground(ThemeHelper.color("toolbar"));
        newPointButton.setForeground(ThemeHelper.color("poiButtonsForeground"));
        deleteAllButton.setForeground(ThemeHelper.color("poiButtonsForeground"));
    }

    public JLabel getNewPointButton() {
        return newPointButton;
    }

    public JLabel getDeleteAllButton() {
        return deleteAllButton;
    }
}
