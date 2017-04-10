package View;

import Enums.ToolType;
import Helpers.GlobalValue;
import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import Controller.MainWindowController;

/**
 * Class details:
 * The Toolbar is a visual component consisting of a collection of tools.
 *
 * @author Andreas Blanke, blan@itu.dk
 * @author Niclas Hedam, nhed@itu.dk
 * @version 06-03-2017
 */
public class Toolbar extends View {

    private static Map<ToolType, ToolComponent> tools;
    private SpringLayout layout;

    /**
     * Constructor for the Toolbar
     */
    public Toolbar() {
        tools = new ToolFactory().setupToolbar();
        layout = new SpringLayout();
        setLayout(layout);

        int width = MainWindowController.getInstance().getWindow().getFrame().getBounds().width;
        setPreferredSize(new Dimension(width, 100));
        setBorder(BorderFactory.createLineBorder(ThemeHelper.color("border")));
        setBackGroundColor();
    }

    public void setBackGroundColor() {
        setBackground(ThemeHelper.color("toolbar"));
    }

    public SpringLayout getLayout() {
        return layout;
    }

    /**
     * Returns the ToolComponent of the type given as parameter
     * @param type of the tool to be returned
     * @return a ToolFeature
     */
    public ToolComponent getTool(ToolType type) {
        return tools.get(type);
    }

    public Map<ToolType, ToolComponent> getAllTools() {
        return tools;
    }

    /** ToolFactory creates the collection of visual components representing the tools */
    private class ToolFactory {
        private Map<ToolType, ToolComponent> setupToolbar() {
            Map<ToolType, ToolComponent> tools = new HashMap<>();

            tools.put(ToolType.LOAD, new ToolFeature("\uf115",ToolType.LOAD));
            tools.put(ToolType.SAVE, new ToolFeature("\uf0c7",ToolType.SAVE));
            tools.put(ToolType.SETTINGS, new ToolFeature("\uf085",ToolType.SETTINGS));
            tools.put(ToolType.MENU, new ToolFeature("\uf0c9", ToolType.MENU));
            tools.put(ToolType.SEARCHBAR, new SearchTool(GlobalValue.getSearchFieldLargeSize()));
            tools.put(ToolType.SEARCHBUTTON, new ToolFeature("\uf002", ToolType.SEARCHBUTTON));
            return tools;
        }
    }
}
