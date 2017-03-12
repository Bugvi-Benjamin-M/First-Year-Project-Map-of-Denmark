package View;

import javax.swing.*;
import java.awt.*;

/**
 * Class details:
 * The SearchTool is a unique Tool that consist of a visual searchbar component
 * and is able to manipulate and interact with that searchbar.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017
 */
public class SearchTool extends ToolComponent {

    private ImageIcon searchIcon, directionIcon;

    /**
     * The base Constructor of the SearchTool
     */
    SearchTool(String searchImgPath, String directionsImgPath) {
        super();
        searchIcon = createImageIcon(searchImgPath,"Search button");
        directionIcon = createImageIcon(directionsImgPath,"Directions button");
        setPreferredSize(new Dimension(200,80));
        setupLayout();
    }

    /**
     * Sets up the layout of the Tool Component
     */
    @Override
    void setupLayout() {
        JTextField searchField = new JTextField("search...");
    }
}
