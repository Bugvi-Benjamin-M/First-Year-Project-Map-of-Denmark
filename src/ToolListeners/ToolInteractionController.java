package ToolListeners;

import Controller.ToolbarController;
import Enums.ToolType;
import View.ToolFeature;
import View.Toolbar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 10-03-2017.
 * @project BFST
 */
public class ToolInteractionController extends MouseAdapter {

    private ToolType type;
    private ToolFeature tool;

    public ToolInteractionController(ToolFeature tool) {
        type = tool.getType();
        this.tool = tool;
        setKeyShortCuts();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        Toolbar.toggleWellOnTool(type);

        switch (type) {
            case LOAD:
                loadEvent();
                break;
        }
    }

    private void loadEvent() {
        ToolbarController.loadEvent();
        Toolbar.toggleWellOnTool(type);
    }

    private void setKeyShortCuts() {
        switch (type) {
            case LOAD:
            tool.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK), "load");
            tool.getActionMap().put("load", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadEvent();
                }
            });
                break;
        }
    }

}
