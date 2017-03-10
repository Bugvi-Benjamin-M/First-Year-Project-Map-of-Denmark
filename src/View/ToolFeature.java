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

    private ImageIcon icon;
    private ToolType type;
    private JPanel label;
    private JPanel iconLabel;

    /**
     * Constructor for a ToolFeature
     * @param path The path to the icon file
     * @param type The type of this tool
     */
    ToolFeature(String path, ToolType type) {
        super();
        this.type = type;
        icon = createImageIcon(path, this.type.toString());
        setupLayout();
        this.setPreferredSize(new Dimension(50,90));
    }

    /**
     * Sets up the specific layout for any Tool Feature
     */
    @Override
    void setupLayout() {
        iconLabel = new JPanel();
        iconLabel.add(new JLabel(icon));
        label = new JPanel();
        label.add(new JLabel(type.toString()));
        this.setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
        this.add(iconLabel);
        this.add(label);
    }

    @Override
    public void toggleWell() {
        if(!isWellActivated()) {
            label.setBackground(getHoverColor());
            iconLabel.setBackground(getHoverColor());
        } else {
            label.setBackground(getDefaultColor());
            iconLabel.setBackground(getDefaultColor());
        }
        super.toggleWell();
    }

    public ToolType getType() {
        return type;
    }
}
