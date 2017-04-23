package View;

import Enums.ToolType;
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
        this.setPreferredSize(new Dimension(45,60));
    }

    /**
     * Sets up the specific layout for any Tool Feature
     */
    @Override
    public void setupLayout() {
        label = new JLabel(type.toString());
        label.setOpaque(false);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 14));

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
        for(Component component : getComponents()) component.setForeground(ThemeHelper.color("icon"));
    }

    public void overrideStandardLabelFontSize(int size) {
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, size));
    }

    public void createSpaceBetweenLabelAndIcon(int size) {
        this.add(Box.createRigidArea(new Dimension(0, size)), 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!getActivatedStatus()) g.setColor(ThemeHelper.color("toolbar"));
        else g.setColor(ThemeHelper.color("toolActivated"));
        g.fillRect(0,0, getWidth(), getHeight());
        label.repaint();
        iconLabel.repaint();
    }
}
