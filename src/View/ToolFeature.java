package View;

import Enums.ToolType;
import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Class details:
 * The ToolFeature is a visual component consisting of one icon
 * and one describing tagline depending on the type of the tool.
 */
public class ToolFeature extends ToolComponent {

    private String icon;
    private ToolType type;
    private JLabel label;
    private JLabel iconLabel;
    private Dimension standardSize;

    /**
   * Constructor for a ToolFeature
   * @param icon The string to the icon file
   * @param type The type of this tool
   */
    public ToolFeature(String icon, ToolType type)
    {
        super();
        standardSize = new Dimension(45, 60);
        this.type = type;
        this.icon = icon;
        setupLayout();
        this.setPreferredSize(standardSize);
    }

    /**
   * Sets up the specific layout for any Tool Feature
   */
    @Override
    public void setupLayout()
    {
        label = new JLabel(type.toString());
        label.setOpaque(false);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));

        iconLabel = new JLabel(this.icon);
        iconLabel.setFont(Helpers.FontAwesome.getFontAwesome());
        iconLabel.setOpaque(false);

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setOpaque(false);
        this.add(iconLabel);
        this.add(label);
    }

    /**
     * Retrieves the type of the tool
     */
    public ToolType getType() { return type; }

    /**
     * Updates and sets the theme for this tool
     */
    public void setTheme()
    {
        for (Component component : getComponents())
            component.setForeground(ThemeHelper.color("icon"));
    }

    /**
     * Change the font size of the text
     */
    public void overrideStandardLabelFontSize(int size)
    {
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, size));
    }

    /**
     * Create some space between the label and the icon
     */
    public void createSpaceBetweenLabelAndIcon(int size)
    {
        this.add(Box.createRigidArea(new Dimension(0, size)), 1);
    }

    /**
     * Create some space between the icon and the top of the toolbar
     */
    public void createSpaceBeforeIcon(int size) {
        this.remove(iconLabel);
        iconLabel.setBorder(new EmptyBorder(0, size, 0, 0));
        this.add(iconLabel, 0);
    }
}
