package Controller;

import Enums.ToolType;
import Helpers.FileHandler;
import Helpers.GlobalConstant;
import Model.Model;
import View.PopupWindow;
import View.ToolFeature;
import View.Toolbar;
import View.Window;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        this.window.addComponent(BorderLayout.PAGE_START, toolbar);
        addInteractorsToTools();
    }

    public static ToolbarController getInstance(Window window) {
        if (instance == null) {
            instance = new ToolbarController(window);
        }
        return instance;
    }

    private void addInteractorsToTools() {
        addInteractorToLoadTool();
    }

    private void addInteractorToLoadTool() {
        toolbar.addInteractorToTool(ToolType.LOAD, new ToolInteractor(ToolType.LOAD));
    }

    public static void loadEvent() {
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[]{
                new FileNameExtensionFilter("OSM Files", GlobalConstant.osmFilter),
                new FileNameExtensionFilter("ZIP Files", GlobalConstant.zipFilter)
        };
        JFileChooser chooser = PopupWindow.fileLoader(false, filters);
        if (chooser != null) {
            Model.getInstance().clear();
            CanvasController.resetBounds();
            FileHandler.load(chooser.getSelectedFile().toString());
            Model.getInstance().modelHasChanged();
            CanvasController.adjustToBounds();
        }
    }


   public class ToolInteractor extends MouseAdapter {

        private ToolType type;
        private ToolFeature tool;

        public ToolInteractor(ToolType type) {
            this.type = type;
            tool = (ToolFeature) toolbar.getTool(type);
            setKeyShortCuts();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            switch (type) {
                case LOAD:
                    loadEvent();
                    break;
            }
        }

        private void loadEvent() {
            Toolbar.toggleWellOnTool(type);
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
}
