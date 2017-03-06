package View;

import javax.swing.*;
import java.awt.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class SearchTool extends ToolComponent {

    private ImageIcon searchIcon, directionIcon;

    SearchTool(String searchImgPath, String directionsImgPath) {
        super();
        searchIcon = createImageIcon(searchImgPath,"Search button");
        directionIcon = createImageIcon(directionsImgPath,"Directions button");
        setPreferredSize(new Dimension(200,80));
        setupLayout();
    }

    @Override
    void setupLayout() {
        JTextField searchField = new JTextField("search...");
    }
}
