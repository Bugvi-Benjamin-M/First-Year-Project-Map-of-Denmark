package Controller;

import Enums.ToolType;
import Helpers.Constant;
import Helpers.FileHandler;
import Model.Model;
import ToolListeners.ToolInteractionController;
import View.PopupWindow;
import View.ToolFeature;
import View.Toolbar;
import View.Window;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

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
        addInteractorsToTools();
    }

    private void addInteractorsToTools() {
        addInteractorToLoadTool();
    }

    private void addInteractorToLoadTool() {
        toolbar.addInteractorToTool(ToolType.LOAD, new ToolInteractionController((ToolFeature) toolbar.getTool(ToolType.LOAD)));
    }

    public static void loadEvent() {
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
    }
}
