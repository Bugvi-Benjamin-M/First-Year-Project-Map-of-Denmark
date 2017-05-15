package View;

import Helpers.FontAwesome;
import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 05/05/2017
 */
public class JourneyPlannerSearchClearButtons extends View {

    private final float BUTTON_FONT_SIZE = 40f;
    private JLabel searchButton;
    private JLabel clearButton;
    private final String CLEAR_BUTTON_TOOLTIP = "Clear Search!";
    private final String SEARCH_BUTTON_TOOLTIP = "Search!";
    private final int CLEAR_BUTTON_WIDTH = 45;
    private final int SEARCH_BUTTON_WIDTH= 40;
    private final int CLEAR_BUTTON_HEIGHT = 40;
    private final int SEARCH_BUTTON_HEIGHT = 40;
    private final int DISTANCE_BETWEEN_BUTTONS = 85;

    public JourneyPlannerSearchClearButtons() {
        clearButton = new JLabel("\uf12d");
        //clearButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        clearButton.setOpaque(true);
        //clearButton.setPreferredSize(new Dimension(CLEAR_BUTTON_WIDTH, CLEAR_BUTTON_HEIGHT));
        clearButton.setBorder(BorderFactory.createEmptyBorder());
        clearButton.setToolTipText(CLEAR_BUTTON_TOOLTIP);
        searchButton = new JLabel("\uf002");
        //searchButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        searchButton.setOpaque(true);
        //searchButton.setPreferredSize(new Dimension(SEARCH_BUTTON_WIDTH, SEARCH_BUTTON_HEIGHT));
        searchButton.setBorder(BorderFactory.createEmptyBorder());
        searchButton.setToolTipText(SEARCH_BUTTON_TOOLTIP);
        applyTheme();
        applyLargeState();
        //add(clearButton);
        //add(Box.createHorizontalStrut(DISTANCE_BETWEEN_BUTTONS));
        //add(searchButton);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    public void applyLargeState() {
        removeAll();
        clearButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        clearButton.setPreferredSize(new Dimension(CLEAR_BUTTON_WIDTH, CLEAR_BUTTON_HEIGHT));
        searchButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE));
        searchButton.setPreferredSize(new Dimension(SEARCH_BUTTON_WIDTH, SEARCH_BUTTON_HEIGHT));
        add(clearButton);
        add(Box.createHorizontalStrut(DISTANCE_BETWEEN_BUTTONS));
        add(searchButton);
    }

    public void applySmallState() {
        removeAll();
        clearButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE-15));
        clearButton.setPreferredSize(new Dimension(CLEAR_BUTTON_WIDTH-15, CLEAR_BUTTON_HEIGHT-15));
        searchButton.setFont(FontAwesome.getFontAwesome().deriveFont(BUTTON_FONT_SIZE-15));
        searchButton.setPreferredSize(new Dimension(SEARCH_BUTTON_WIDTH-15, SEARCH_BUTTON_HEIGHT-15));
        add(clearButton);
        add(Box.createHorizontalStrut(DISTANCE_BETWEEN_BUTTONS-30));
        add(searchButton);
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        clearButton.setBackground(ThemeHelper.color("toolbar"));
        clearButton.setForeground(ThemeHelper.color("icon"));
        searchButton.setBackground(ThemeHelper.color("toolbar"));
        searchButton.setForeground(ThemeHelper.color("icon"));
    }

    public JLabel getSearchButton() {
        return searchButton;
    }

    public JLabel getClearButton() {
        return clearButton;
    }
}
