package View;

import Enums.ToolType;

import javax.swing.*;
import java.awt.*;

/**
 * Class details:
 * The ToolFeature is a visual component consisting of one icon
 * and one describing tagline depending on the type of the tool.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @version 06-03-2017
 */
public class ToolFeature extends ToolComponent {

    private String icon;
    private ToolType type;
    private JLabel label;
    private JLabel iconLabel;

    /**
     * Constructor for a ToolFeature
     * @param path The string to the icon file
     * @param type The type of this tool
     */
    ToolFeature(String icon, ToolType type) {
        super();
        this.type = type;
        this.icon = icon;
        setupLayout();
        this.setPreferredSize(new Dimension(50,90));
    }

    /**
     * Sets up the specific layout for any Tool Feature
     */
    @Override
    void setupLayout() {
        label = new JLabel(type.toString());
        label.setForeground(Helpers.ThemeHelper.color("icon"));
        label.setOpaque(false);

        iconLabel = new JLabel(this.icon);
        iconLabel.setFont(Helpers.FontAwesome.getFontAwesome());
        iconLabel.setForeground(Helpers.ThemeHelper.color("icon"));
        iconLabel.setOpaque(false);

        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.setOpaque(false);
        this.add(iconLabel);
        this.add(label);
    }

    public ToolType getType() {
        return type;
    }
}
