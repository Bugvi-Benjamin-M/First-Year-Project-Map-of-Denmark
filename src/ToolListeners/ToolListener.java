package ToolListeners;

import Enums.ToolType;
import View.Toolbar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 10-03-2017.
 * @project BFST
 */
public abstract class ToolListener extends MouseAdapter {

    protected ToolType type;

    public ToolListener(ToolType type) {
        this.type = type;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        Toolbar.toggleWellOnTool(type);
    }

    /*
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        Toolbar.toggleHoverOnTool(type);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        Toolbar.toggleHoverOnTool(type);
    }
    */
}
