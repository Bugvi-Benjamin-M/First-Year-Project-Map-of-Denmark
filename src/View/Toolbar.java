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
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class Toolbar extends View {
    private Map<ToolType, ToolComponent> tools;

    private final int MARGIN_SMALL_LEFT = 20;
    private final int MARGIN_LARGE_LEFT = 80;
    private final int MARGIN_TOP = 20;

    public Toolbar() {
        tools = new ToolFactory().setupToolbar();

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);

        addTools(layout);

        this.setPreferredSize(new Dimension(500,120));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void addMouseListenerToTool(ToolType toolType, MouseListener listener){
        ToolComponent tool = tools.get(toolType);
        if(tool != null){
            tool.addMouseListener(listener);
        } else {
            throw new RuntimeException("No such tool found.");
        }
    }

    private void addTools(SpringLayout layout) {
        ToolComponent tool = addLoadTool(layout);
        addSaveTool(layout, tool);
    }

    private ToolComponent addLoadTool(SpringLayout layout) {
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

    private void addSaveTool(SpringLayout layout, ToolComponent tool) {
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

    private class ToolFactory {
        private Map<ToolType, ToolComponent> setupToolbar() {
            Map<ToolType, ToolComponent> tools = new HashMap<>();

            tools.put(ToolType.LOAD, new ToolFeature("/load.png",ToolType.LOAD));
            tools.put(ToolType.SAVE, new ToolFeature("/save.png",ToolType.SAVE));
            return tools;
        }
    }

}
