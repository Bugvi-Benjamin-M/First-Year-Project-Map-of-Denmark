package ToolListeners;

import Controller.CanvasController;
import Enums.ToolType;
import Helpers.Constant;
import Helpers.FileHandler;
import Model.Model;
import View.PopupWindow;
import View.Toolbar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.MouseEvent;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 10-03-2017.
 * @project BFST
 */
public class LoadToolListener extends ToolListener {

    public LoadToolListener(ToolType type) {
        super(type);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[] {
                new FileNameExtensionFilter("OSM Files", Constant.osmFilter),
                new FileNameExtensionFilter("ZIP Files", Constant.zipFilter)
        };
        JFileChooser chooser = PopupWindow.fileLoader(false, filters);
        if(chooser != null) {
            //Clear all data in model
            Model.getInstance().clear();
            CanvasController.resetBounds();
            //load and add data to model
            FileHandler.load(chooser.getSelectedFile().toString());
            //reset shapelist and add data from model to shapelist
            Model.getInstance().modelHasChanged();
            CanvasController.adjustToBounds();
        }
        Toolbar.toggleWellOnTool(type);
    }
}
