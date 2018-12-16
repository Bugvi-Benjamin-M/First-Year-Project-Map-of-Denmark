package View;

import Helpers.FontAwesome;
import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

/**
 * The JourneyPlannerSearchClearButtons is a visual component that consists
 * of a clear button and a search button.
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
        clearButton.setOpaque(true);
        clearButton.setBorder(BorderFactory.createEmptyBorder());
        clearButton.setToolTipText(CLEAR_BUTTON_TOOLTIP);
        searchButton = new JLabel("\uf002");
        searchButton.setOpaque(true);
        searchButton.setBorder(BorderFactory.createEmptyBorder());
        searchButton.setToolTipText(SEARCH_BUTTON_TOOLTIP);
        applyTheme();
        applyLargeState();
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
    }

    /**
     * Applies the large state to the component, e.g. resizing to accommodate
     * the larger width with more room on the screen
     */
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

    /**
     * Applies the small state to the component, e.g. resize such that there
     * is space for the component on a smaller screen width
     */
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

    /**
     * Updates and applies the currently selected theme to the component
     */
    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        clearButton.setBackground(ThemeHelper.color("toolbar"));
        clearButton.setForeground(ThemeHelper.color("icon"));
        searchButton.setBackground(ThemeHelper.color("toolbar"));
        searchButton.setForeground(ThemeHelper.color("icon"));
    }

    /**
     * Retrieve the search button
     */
    public JLabel getSearchButton() {
        return searchButton;
    }

    /**
     * Retrieve the clear button
     */
    public JLabel getClearButton() {
        return clearButton;
    }
}
