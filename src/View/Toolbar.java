package View;

import javax.swing.*;
import javax.swing.border.Border;
import javax.tools.Tool;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class Toolbar extends View {
    private List<ToolComponent> tools;

    public Toolbar() {
        tools = new ToolFactory().setupToolbar();
        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        ToolComponent tool = tools.get(0);
        layout.putConstraint(SpringLayout.WEST, tool,
                5,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, tool,
                5,
                SpringLayout.NORTH, this);
        this.add(tool);
        for (int i = 1; i < tools.size(); i++) {
            ToolComponent next = tools.get(i);
            layout.putConstraint(SpringLayout.WEST, next,
                    5,
                    SpringLayout.EAST, tool);
            layout.putConstraint(SpringLayout.NORTH, next,
                    5,
                    SpringLayout.NORTH, this);
            tool = next;
            this.add(tool);
        }
        this.setPreferredSize(new Dimension(500,100));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    private class ToolFactory {
        private List<ToolComponent> setupToolbar() {
            List<ToolComponent> tools = new ArrayList<>();
            tools.add(new ToolComponent("/load.png","Load"));
            tools.add(new ToolComponent("/save.png","Save"));
            return tools;
        }
    }
}
