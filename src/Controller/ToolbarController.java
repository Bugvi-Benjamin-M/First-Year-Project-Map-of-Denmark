package Controller;

import Enums.ToolType;
import Helpers.Constant;
import Helpers.FileHandler;
import Model.Model;
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
public final class ToolbarController extends Controller {
    private Window window;
    private Toolbar toolbar;
    private static ToolbarController instance;

    private ToolbarController(Window window) {
        toolbar = new Toolbar();
        this.window = window;
        this.window.addComponent(BorderLayout.PAGE_START,toolbar);
        addInteractorsToTools();
    }

    public static ToolbarController getInstance(Window window) {
        if(instance == null) {
            instance = new ToolbarController(window);
        }
        return instance;
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
            Model.getInstance().clear();
            CanvasController.resetBounds();
            FileHandler.load(chooser.getSelectedFile().toString());
            Model.getInstance().modelHasChanged();
            CanvasController.adjustToBounds();
        }
    }
}
