package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SpringLayout.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class Toolbar extends View {
    private List<ToolComponent> tools;

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

    private void addTools(SpringLayout layout) {
        ToolComponent tool = addLoadTool(layout);
        addSaveTool(layout, tool);
    }

    private ToolComponent addLoadTool(SpringLayout layout) {
        ToolComponent tool = tools.get(0);
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
        ToolComponent next = tools.get(1);
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
        private List<ToolComponent> setupToolbar() {
            List<ToolComponent> tools = new ArrayList<>();

            tools.add(setupLoadTool());

            tools.add(new ToolFeature("/save.png","Save"));
            return tools;
        }
    }

    private ToolComponent setupLoadTool() {
        ToolComponent loadTool = new ToolFeature("/load.png","Load");
        loadTool.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                ToolFeature.loadFileChooser();
            }
        });
        return loadTool;
    }
}
