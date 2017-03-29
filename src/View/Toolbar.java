package View;

import Enums.ToolType;
import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static javax.swing.SpringLayout.*;

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

    private final int MARGIN_SMALL_LEFT = 20;
    private final int MARGIN_LARGE_LEFT = 80;
    private final int MARGIN_SMALL_RIGHT = -20;
    private final int MARGIN_LARGE_RIGHT = -80;
    private final int MARGIN_TOP = 5;

    /**
     * Constructor for the Toolbar
     */
    public Toolbar() {
        tools = new ToolFactory().setupToolbar();

        layout = new SpringLayout();
        this.setLayout(layout);

        setupTools();

        this.setPreferredSize(new Dimension(500,100));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackGroundColor();
    }

    public void setBackGroundColor() {
        setBackground(ThemeHelper.color("toolbar"));
    }

    /**
     * Creates and adds all the tools to the toolbar
     */
    private void setupTools() {
        addSaveTool(addLoadTool());
        addSettingsTool();
    }

    /**
     * Creates the LoadTool and set up the position of the tool
     * @return loadTool reference to be used for positioning
     */
    private ToolComponent addLoadTool() {
        ToolComponent tool = tools.get(ToolType.LOAD);
        layout.putConstraint(WEST, tool,
                MARGIN_SMALL_LEFT,
                WEST, this);
        layout.putConstraint(NORTH, tool,
                MARGIN_TOP,
                NORTH, this);
        this.add(tool);
        return tool;
    }

    /**
     * Creates the SaveTool, positions it and adds it to the toolbar
     * @param tool the reference to the tool so that this tool can
     *             be positioned to the right of that tool
     */
    private ToolComponent addSaveTool(ToolComponent tool) {
        ToolComponent next = tools.get(ToolType.SAVE);
        layout.putConstraint(WEST, next,
                MARGIN_SMALL_LEFT,
                EAST, tool);
        layout.putConstraint(NORTH, next,
                MARGIN_TOP,
                NORTH, this);
        tool = next;
        this.add( tool);
        return tool;
    }

    /**
     * Creates the settings tool, positions it and adds it to the toolbar
     *
     */
    private ToolComponent addSettingsTool() {
        ToolComponent tool = tools.get(ToolType.SETTINGS);
        layout.putConstraint(EAST, tool,
                MARGIN_SMALL_RIGHT,
                EAST, this);
        layout.putConstraint(NORTH, tool,
                MARGIN_TOP,
                NORTH, this);
        this.add( tool);
        return tool;
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
            return tools;
        }
    }
}
