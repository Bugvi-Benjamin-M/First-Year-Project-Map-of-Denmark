package View;

import Enums.ToolType;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @version 06-03-2017
 * @project BFST
 */
public class ToolFeature extends ToolComponent {

    private ImageIcon icon;
    private ToolType type;

    /**
     * Constructor for a ToolFeature
     * @param path The path to the icon file
     * @param type The tagline beneath the Icon
     */
    ToolFeature(String path, ToolType type) {
        super();
        this.type = type;
        icon = createImageIcon(path, this.type.toString());
        setupLayout();
        this.setPreferredSize(new Dimension(50,90));
    }

    @Override
    void setupLayout() {
        this.setLayout(new GridLayout(2,1));
        this.add(new JLabel(icon));
        JPanel label = new JPanel();
        label.add(new JLabel(type.toString()));
        this.add(label);
    }

}
