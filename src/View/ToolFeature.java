package View;

import Enums.ToolType;
import Helpers.ScreenScaler;
import Helpers.ThemeHelper;

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
     * @param icon The string to the icon file
     * @param type The type of this tool
     */
    public ToolFeature(String icon, ToolType type) {
        super();
        this.type = type;
        this.icon = icon;
        setupLayout();
        this.setPreferredSize(new Dimension(60 * ScreenScaler.getWidthScaleFactor(),58 * ScreenScaler.getHeightScaleFactor()));
    }

    /**
     * Sets up the specific layout for any Tool Feature
     */
    @Override
    public void setupLayout() {
        label = new JLabel(type.toString());
        label.setOpaque(false);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14 * ScreenScaler.getWidthScaleFactor()));

        iconLabel = new JLabel(this.icon);
        iconLabel.setFont(Helpers.FontAwesome.getFontAwesome());
        iconLabel.setOpaque(false);


        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.setOpaque(false);
        this.add(iconLabel);
        this.add(label);
    }

    public ToolType getType() {
        return type;
    }

    public void setTheme() {
        label.setForeground(ThemeHelper.color("icon"));
        iconLabel.setForeground(ThemeHelper.color("icon"));
    }
}
