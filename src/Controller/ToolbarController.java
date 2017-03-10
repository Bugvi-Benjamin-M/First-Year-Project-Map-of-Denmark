package Controller;

import Enums.ToolType;
import Helpers.Constant;
import Helpers.FileHandler;
import Model.Model;
import View.PopupWindow;
import View.Toolbar;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class ToolbarController extends Controller {
    private Window window;
    private Toolbar toolbar;

    public ToolbarController(Window window) {
        toolbar = new Toolbar();
        this.window = window;
        this.window.addComponent(BorderLayout.PAGE_START,toolbar);
        addMouseListenersToTools();
    }

    private void addMouseListenersToTools() {
        addMouseListenerToLoadTool();
    }
    private void addMouseListenerToLoadTool() {
        toolbar.addMouseListenerToTool(ToolType.LOAD, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser chooser = PopupWindow.fileLoader(false, Constant.getFileNameExtensionFilters());
                if(chooser != null) {
                    Model.getInstance().clear();
                    FileHandler.load(chooser.getSelectedFile().toString());
                    CanvasController.adjustToBounds();
                }

            }
        });
    }

}
