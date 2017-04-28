package View;

import Helpers.FontAwesome;
import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 28/04/2017
 */
public class PointProfile extends View {

    private final int PROFILE_HEIGHT = 90;
    private final int PROFILE_WIDHT = 290;
    private JLabel label;
    private SpringLayout layout;
    private boolean isDoubleLine;
    private JLabel deleteButton;


    public PointProfile(String description) {
        isDoubleLine = false;
        if(description.length() > 28) {
            description = "<html>" + description.substring(0,28) + "<br>" + description.substring(28, description.length()) + "</html>";
            isDoubleLine = true;
        }
        layout = new SpringLayout();
        setLayout(layout);
        setBorder(BorderFactory.createLineBorder(ThemeHelper.color("border")));
        setPreferredSize(new Dimension(PROFILE_WIDHT, PROFILE_HEIGHT));
        setMaximumSize(new Dimension(PROFILE_WIDHT, PROFILE_HEIGHT));
        setMinimumSize(new Dimension(PROFILE_WIDHT, PROFILE_HEIGHT));
        label = new JLabel(description);
        label.setFont(new Font(label.getFont().getFontName(), label.getFont().getStyle(), 15));
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, -10, SpringLayout.HORIZONTAL_CENTER, this);
        if(isDoubleLine) layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, -10, SpringLayout.VERTICAL_CENTER, this);
        else layout.putConstraint(SpringLayout.VERTICAL_CENTER, label, -20, SpringLayout.VERTICAL_CENTER, this);
        setupDeleteButton();
        layout.putConstraint(SpringLayout.EAST, deleteButton, -25, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, deleteButton, -15, SpringLayout.SOUTH, this);
        add(label);
        add(deleteButton);
        applyTheme();
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        label.setBackground(ThemeHelper.color("toolbar"));
        label.setForeground(ThemeHelper.color("icon"));
        deleteButton.setBackground(ThemeHelper.color("toolbar"));
        deleteButton.setForeground(ThemeHelper.color("icon"));
    }

    private void setupDeleteButton() {
        deleteButton = new JLabel("\uf00d");
        deleteButton.setPreferredSize(new Dimension(25,20));
        deleteButton.setFont(FontAwesome.getFontAwesome().deriveFont(25f));
        deleteButton.setOpaque(true);
    }

    public JLabel getDeleteButton() {
        return deleteButton;
    }
}
