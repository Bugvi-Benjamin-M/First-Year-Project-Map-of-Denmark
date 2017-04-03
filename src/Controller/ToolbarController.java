package Controller;

import Enums.FileType;
import Enums.ToolType;
import Helpers.FileHandler;
import Helpers.OSDetector;
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

    private Toolbar toolbar;
    private static ToolbarController instance;

    private ToolbarController(Window window) {
        super(window);
        toolbar = new Toolbar();
        this.window.addComponent(BorderLayout.PAGE_START, toolbar,true);
        addInteractionHandlersToTools();
    }

    public static ToolbarController getInstance(Window window) {
        if (instance == null) {
            instance = new ToolbarController(window);
        }
        return instance;
    }

    private void addInteractionHandlersToTools() {
        addInteractionHandlerToLoadTool();
        addInteractionHandlerToSaveTool();
        addInteractionHandlerToSettingsTool();
        addInteractionHandlerToToolbar();
    }

    private void addInteractionHandlerToToolbar() {
        new ToolbarInteractionHandler();
    }

    private void addInteractionHandlerToSaveTool() {
        new ToolInteractionHandler(ToolType.SAVE, KeyEvent.VK_S, OSDetector.getActivationKey());
    }

    private void addInteractionHandlerToLoadTool() {
        new ToolInteractionHandler(ToolType.LOAD, KeyEvent.VK_L, OSDetector.getActivationKey());
    }

    private void addInteractionHandlerToSettingsTool() {
        new ToolInteractionHandler(ToolType.SETTINGS, KeyEvent.VK_COMMA, OSDetector.getActivationKey());
    }

    private void toolEvent(ToolType type) {
        switch (type) {
            case LOAD:
                loadEvent();
                break;
            case SAVE:
                saveEvent();
                break;
            case SETTINGS:
                settingsEvent();
                break;
        }
    }

    private void loadEvent() {
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[]{
                new FileNameExtensionFilter("OSM Files", FileType.OSM.toString()),
                new FileNameExtensionFilter("ZIP Files", FileType.ZIP.toString())
        };
        JFileChooser chooser = PopupWindow.fileLoader(false, filters);
        if (chooser != null) {
            FileHandler.fileChooserLoad(chooser.getSelectedFile().toString());
            try {
                FileHandler.fileChooserLoad(chooser.getSelectedFile().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveEvent() {
        PopupWindow.infoBox(null, "You activated save tool","Tool activated");
    }

    private void settingsEvent() {
        if(SettingsWindowController.getInstance() == null) {
            SettingsWindowController.getInstance();
        } else {
            SettingsWindowController.getInstance().showSettingsWindow();
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void resetInstance() {
        instance = null;
    }

    public void themeHasChanged() {
        this.window.removeComponent(toolbar);
        toolbar = null;
        SearchController.getInstance(window).saveCurrentText();
        toolbar = new Toolbar();
        SearchController.getInstance(window).searchToolThemeChangeEvent();
        this.window.addComponent(BorderLayout.PAGE_START, toolbar,true);

        addInteractionHandlersToTools();
    }

    public void toggleKeyBindings(boolean status) {
        for(ToolType type : toolbar.getAllTools().keySet()) {
            if(toolbar.getTool(type).getActionMap().keys() == null) continue;
            for(Object key : toolbar.getTool(type).getActionMap().keys()) {
                toolbar.getTool(type).getActionMap().get(key).setEnabled(status);
            }
        }
    }

    private class ToolInteractionHandler extends MouseAdapter {

        private ToolType type;
        private ToolFeature tool;
        private int keyEvent;
        private int activationKey;

        public ToolInteractionHandler(ToolType type, int keyEvent, int activationKey) {
            this.type = type;
            this.keyEvent = keyEvent;
            this.activationKey = activationKey;
            tool = (ToolFeature) toolbar.getTool(type);
            addMouseListener();
            setKeyShortCuts();
        }

        private void addMouseListener() {
            if (tool != null) {
                tool.addMouseListener(this);
            } else {
                throw new RuntimeException("No such tool found");
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if(tool == null) toolbar.grabFocus();
            else {
                tool.grabFocus();
                toolEvent(type);
            }
        }

        private void setKeyShortCuts() {
            tool.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke(keyEvent, activationKey), type.toString().toLowerCase());
            tool.getActionMap().
                    put(type.toString().toLowerCase(), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    toolEvent(type);
                }
            });
        }

    }

    private class ToolbarInteractionHandler extends MouseAdapter {

        public ToolbarInteractionHandler() {
            addMouseListener();
        }

        private void addMouseListener() {
            toolbar.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            toolbar.grabFocus();
        }
    }

}
