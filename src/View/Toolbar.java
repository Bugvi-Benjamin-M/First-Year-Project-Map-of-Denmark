package View;

import Enums.ToolType;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final int MARGIN_TOP = 20;

    /**
     * Constructor for the Toolbar
     */
    public Toolbar() {
        tools = new ToolFactory().setupToolbar();

        layout = new SpringLayout();
        this.setLayout(layout);

        setupTools();

        this.setPreferredSize(new Dimension(500,120));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * Adds a given MouseListener to a specified Tool
     * @param toolType The type of the Tool, unique
     * @see ToolType
     */
    public void addMouseListenerToTool(ToolType toolType, MouseListener listener){
        ToolComponent tool = tools.get(toolType);
        if(tool != null){
            tool.addMouseListener(listener);
        } else {
            throw new RuntimeException("No such tool found.");
        }
    }

    public static void toggleHoverOnTool(ToolType type) {
        ToolComponent tool = tools.get(type);
        tool.toggleHover();
    }

    /**
     * Creates and adds all the tools to the toolbar
     */
    private void setupTools() {
        ToolComponent tool = addLoadTool();
        addSaveTool(tool);
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
    private void addSaveTool(ToolComponent tool) {
        ToolComponent next = tools.get(ToolType.SAVE);
        layout.putConstraint(WEST, next,
                MARGIN_SMALL_LEFT,
                EAST, tool);
        layout.putConstraint(NORTH, next,
                MARGIN_TOP,
                NORTH, this);
        tool = next;
        this.add( tool);
    }

    /** ToolFactory creates the collection of visual components representing the tools */
    private class ToolFactory {
        private Map<ToolType, ToolComponent> setupToolbar() {
            Map<ToolType, ToolComponent> tools = new HashMap<>();

            tools.put(ToolType.LOAD, new ToolFeature("/load.png",ToolType.LOAD));
            tools.put(ToolType.SAVE, new ToolFeature("/save.png",ToolType.SAVE));
            return tools;
        }
    }
}
