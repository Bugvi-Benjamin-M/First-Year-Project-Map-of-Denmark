package View;

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
    private String tagline = "not found";

    /**
     * Constructor for a ToolFeature
     * @param path The path to the icon file
     * @param tagline The tagline beneath the Icon
     */
    ToolFeature(String path, String tagline) {
        super();
        if (tagline != null) this.tagline = tagline;
        icon = createImageIcon(path, this.tagline);
        setupLayout();
        this.setPreferredSize(new Dimension(50,90));
    }

    @Override
    void setupLayout() {
        this.setLayout(new GridLayout(2,1));
        this.add(new JLabel(icon));
        JPanel label = new JPanel();
        label.add(new JLabel(tagline));
        this.add(label);
    }

    static void loadFileChooser() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "OSM files", "osm");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            System.out.println("You chose to open this file: " +
                    file.getName());
            Helpers.File.load(file.getAbsolutePath());
        }
    }
}
