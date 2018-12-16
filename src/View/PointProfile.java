package View;

import Helpers.FontAwesome;
import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * The PointProfile is a visual component that displays information about a point
 * of interest or user-specified place
 */
public class PointProfile extends View {

    private final int PROFILE_HEIGHT = 90;
    private final int PROFILE_WIDTH = 290;
    private final int DESCRIPTION_FONT = 15;
    private final int MAX_CHARACTERS = 28;
    private final int DESCRIPTION_LABEL_SMALL_OFFSET = -10;
    private final int DESCRIPTION_LABEL_LARGE_OFFSET = -20;
    private JLabel label;
    private SpringLayout layout;
    private boolean isDoubleLine;
    private JLabel deleteButton;

    private String description;

    private float x;
    private float y;

    /**
     * Creates a new point profile
     * @param description Description for the POI
     * @param x The x-coordinate for the POI
     * @param y The y-coordinate for the POI
     */
    public PointProfile(String description, float x, float y) {
        this.x = x;
        this.y = y;
        this.description = description;
        isDoubleLine = false;
        if(this.description.length() > MAX_CHARACTERS) {
            this.description = "<html>" + description.substring(0,MAX_CHARACTERS) + "<br>" + description.substring(MAX_CHARACTERS, description.length()) + "</html>";
            isDoubleLine = true;
        }
        layout = new SpringLayout();
        setLayout(layout);
        setBorder(BorderFactory.createLineBorder(ThemeHelper.color("border")));
        setPreferredSize(new Dimension(PROFILE_WIDTH, PROFILE_HEIGHT));
        setMaximumSize(new Dimension(PROFILE_WIDTH, PROFILE_HEIGHT));
        setMinimumSize(new Dimension(PROFILE_WIDTH, PROFILE_HEIGHT));
        setupDescriptionLabel();
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, DESCRIPTION_LABEL_SMALL_OFFSET, SpringLayout.HORIZONTAL_CENTER, this);
        if(isDoubleLine) layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, DESCRIPTION_LABEL_SMALL_OFFSET, SpringLayout.VERTICAL_CENTER, this);
        else layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, DESCRIPTION_LABEL_LARGE_OFFSET, SpringLayout.VERTICAL_CENTER, this);
        setupDeleteButton();
        layout.putConstraint(SpringLayout.EAST, deleteButton, -25, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, deleteButton, -15, SpringLayout.SOUTH, this);
        add(label);
        add(deleteButton);
        applyTheme();
    }

    /**
     * Updates and applies the current theme to the point profile
     */
    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        label.setBackground(ThemeHelper.color("toolbar"));
        label.setForeground(ThemeHelper.color("icon"));
        deleteButton.setBackground(ThemeHelper.color("toolbar"));
        deleteButton.setForeground(ThemeHelper.color("icon"));
    }

    /**
     * Sets up the delete point button
     */
    private void setupDeleteButton() {
        deleteButton = new JLabel("\uf00d");
        deleteButton.setPreferredSize(new Dimension(25,20));
        deleteButton.setFont(FontAwesome.getFontAwesome().deriveFont(25f));
        deleteButton.setOpaque(true);
    }

    /**
     * Sets up the description label
     */
    private void setupDescriptionLabel() {
        label = new JLabel(description);
        label.setFont(new Font(label.getFont().getFontName(), label.getFont().getStyle(), DESCRIPTION_FONT));
    }

    /**
     * Retrieve the delete button
     */
    public JLabel getDeleteButton() {
        return deleteButton;
    }

    /**
     * Retrieves the x-coordinate of the POI
     */
    public float getPOIX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the POI
     */
    public float getPOIY() {
        return y;
    }

    /**
     * Returns the description label
     */
    public JLabel getDescription() {
        return label;
    }
}
