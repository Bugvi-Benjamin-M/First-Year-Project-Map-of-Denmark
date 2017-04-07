package Controller;

import Enums.FileType;
import Enums.ToolType;
import Helpers.FileHandler;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import View.*;
import View.Window;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.SpringLayout.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public final class ToolbarController extends Controller {

    private static final int SMALL_LARGE_EVENT_WIDTH = 700;

    private Toolbar toolbar;
    private SpringLayout toolbarLayout;
    private static ToolbarController instance;
    private enum ToolbarType {
        LARGE,
        SMALL
    }
    private ToolbarType type;

    private final int MARGIN_SMALL_LEFT = 20;
    private final int MARGIN_SMALL_RIGHT = -20;
    private final int MARGIN_TOP = 15;

    private ToolbarController(Window window) {
        super(window);
        toolbar = new Toolbar();
        toolbarLayout = toolbar.getLayout();
        this.window.addComponent(BorderLayout.NORTH, toolbar,true);
        type = ToolbarType.LARGE;
        setupLargeToolbar();
        addInteractionHandlersToTools();
    }

    public static ToolbarController getInstance(Window window) {
        if (instance == null) {
            instance = new ToolbarController(window);
        }
        return instance;
    }

    private void setupLargeToolbar() {
        removeAllComponentsFromToolbar();
        addSaveToolToLargeToolbar(addLoadToolToLargeToolbar());
        addSettingsToolToLargeToolbar();
        addSearchToolToLargeToolbar();
        type = ToolbarType.LARGE;
    }

    private void setupSmallToolbar() {
        removeAllComponentsFromToolbar();
        addActionToolToSmallToolbar();
        addSearchToolToSmallToolbar();
        //Todo continue work with smallToolbar
        type = ToolbarType.SMALL;
    }

    public void resizeEvent() {
        if(type == ToolbarType.LARGE && MainWindowController.getInstance().getWindow().getFrame().getWidth() < SMALL_LARGE_EVENT_WIDTH) {
            SearchController.getInstance(window).saveCurrentText();
            setupSmallToolbar();
            return;
        }
        if(type == ToolbarType.SMALL && MainWindowController.getInstance().getWindow().getFrame().getWidth() >= SMALL_LARGE_EVENT_WIDTH) {
            SearchController.getInstance(window).saveCurrentText();
            setupLargeToolbar();
            return;
        }
        if(type == ToolbarType.LARGE) searchToolResizeEvent();
    }

    private void removeAllComponentsFromToolbar() {
        if(toolbar.getComponents().length == 0) return;
        for(Component component : toolbar.getComponents()) {
            toolbar.remove(component);
        }
    }

    private ToolComponent addActionToolToSmallToolbar() {
        ToolComponent actions = toolbar.getTool(ToolType.Menu);
        toolbarLayout.putConstraint(WEST, actions,
                MARGIN_SMALL_LEFT,
                WEST, toolbar);
        putNorthConstraints(actions);
        toolbar.add(actions);
        return actions;
    }

    private ToolComponent addSearchToolToSmallToolbar() {
        toolbar.getAllTools().remove(ToolType.SEARCH);
        toolbar.getAllTools().put(ToolType.SEARCH, new SearchTool(GlobalValue.getSearchFieldSmallSize()));
        SearchController.getInstance(window).searchToolReplacedEvent();
        ToolComponent search = toolbar.getTool(ToolType.SEARCH);
        toolbarLayout.putConstraint(EAST, search,
                MARGIN_SMALL_RIGHT,
                EAST, toolbar);
        toolbarLayout.putConstraint(SpringLayout.VERTICAL_CENTER, search, 0, SpringLayout.VERTICAL_CENTER, toolbar);
        toolbar.add(search);
        return search;
    }

    private ToolComponent addLoadToolToLargeToolbar() {
        ToolComponent load = toolbar.getTool(ToolType.LOAD);
        toolbarLayout.putConstraint(WEST, load,
                MARGIN_SMALL_LEFT,
                WEST, toolbar);
        putNorthConstraints(load);
        toolbar.add(load);
        return load;
    }

    private ToolComponent addSaveToolToLargeToolbar(ToolComponent tool) {
        ToolComponent save = toolbar.getTool(ToolType.SAVE);
        toolbarLayout.putConstraint(WEST, save,
                MARGIN_SMALL_LEFT,
                EAST, tool);
        putNorthConstraints(save);
        tool = save;
        toolbar.add(tool);
        return tool;
    }

    private ToolComponent addSettingsToolToLargeToolbar() {
        ToolComponent settings = toolbar.getTool(ToolType.SETTINGS);
        toolbarLayout.putConstraint(EAST, settings,
                MARGIN_SMALL_RIGHT,
                EAST, toolbar);
        putNorthConstraints(settings);
        toolbar.add(settings);
        return settings;
    }

    private ToolComponent addSearchToolToLargeToolbar() {
        ToolComponent search = toolbar.getTool(ToolType.SEARCH);
        toolbarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, search, 0, SpringLayout.HORIZONTAL_CENTER, toolbar);
        toolbarLayout.putConstraint(SpringLayout.VERTICAL_CENTER, search, 0, SpringLayout.VERTICAL_CENTER, toolbar);
        toolbar.add(search);
        return search;
    }

    private void putNorthConstraints(ToolComponent tool) {
        toolbarLayout.putConstraint(NORTH, tool,
                MARGIN_TOP,
                NORTH, toolbar);
    }

    public void searchToolResizeEvent() {
        SearchController.getInstance(window).saveCurrentText();
        rebuildSearchTool();
    }

    private void rebuildSearchTool() {
        toolbar.remove(toolbar.getTool(ToolType.SEARCH));
        toolbar.getAllTools().remove(ToolType.SEARCH);
        toolbar.revalidate();
        toolbar.repaint();
        toolbar.getAllTools().put(ToolType.SEARCH, new SearchTool(GlobalValue.getSearchFieldLargeSize()));
        addSearchToolToLargeToolbar();
        SearchController.getInstance(window).searchToolResizeEvent();
        toolbar.revalidate();
        toolbar.repaint();
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
        SearchController.getInstance(window).saveCurrentText();
        resetToolbar();
        SearchController.getInstance(window).searchToolReplacedEvent();
        window.addComponent(BorderLayout.PAGE_START, toolbar,true);
        if(type == ToolbarType.LARGE) setupLargeToolbar();
        else if(type == ToolbarType.SMALL) setupSmallToolbar();
    }

    private void resetToolbar() {
        window.removeComponent(toolbar);
        toolbar = null;
        toolbar = new Toolbar();
        toolbarLayout = toolbar.getLayout();
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

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            toolbar.grabFocus();
        }
    }
}
