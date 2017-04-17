package Controller.ToolbarControllers;

import Controller.Controller;
import Controller.MainWindowController;
import Controller.SettingsWindowController;
import Enums.FileType;
import Enums.ToolType;
import Enums.ToolbarType;
import Helpers.FileHandler;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import View.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;

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

    private ToolbarType type;

    private final int MARGIN_SMALL_LEFT = 20;
    private final int MARGIN_SMALL_RIGHT = -40;
    private final int MARGIN_SMALLEST_LEFT = 10;
    private final int MARGIN_SMALLEST_RIGHT = -10;
    private final int MARGIN_TOP = 20;

    private ToolbarController() {
        super();
        SearchToolController.getInstance();
        MenuToolController.getInstance();
    }

    public static ToolbarController getInstance() {
        if (instance == null) {
            instance = new ToolbarController();
        }
        return instance;
    }

    public void setupToolbar(ToolbarType type) {
        toolbar = new Toolbar();
        toolbarLayout = toolbar.getLayout();
        toolbar.setPreferredSize(new Dimension(window.getFrame().getWidth(), GlobalValue.getToolbarWidth()));
        this.type = type;
        switch (type) {
            case LARGE:
                setupLargeToolbar();
                break;
            case SMALL:
                setupSmallToolbar();
                break;
        }
        SearchToolController.getInstance().specifyWindow(window);
        MenuToolController.getInstance().specifyWindow(window);
        SearchToolController.getInstance().setupSearchTool();
        MenuToolController.getInstance().setupMenuTool();
        addInteractionHandlersToTools();
    }

    public void setupLargeToolbar() {
        removeAllComponentsFromToolbar();
        addSaveToolToLargeToolbar(addLoadToolToLargeToolbar());
        addSettingsToolToLargeToolbar();
        addSearchButtonToolToLargeToolbar(addSearchToolToLargeToolbar());
        type = ToolbarType.LARGE;
    }

    public void setupSmallToolbar() {
        removeAllComponentsFromToolbar();
        addMenuToolToSmallToolbar();
        addSearchToolToSmallToolbar(addSearchButtonToolToSmallToolbar());
        type = ToolbarType.SMALL;
    }

    public void resizeEvent() {
        if(type == ToolbarType.LARGE && MainWindowController.getInstance().getWindow().getFrame().getWidth() < SMALL_LARGE_EVENT_WIDTH) {
            setupSmallToolbar();
            return;
        }
        if(type == ToolbarType.SMALL && MainWindowController.getInstance().getWindow().getFrame().getWidth() >= SMALL_LARGE_EVENT_WIDTH) {
            MenuToolController.getInstance().hidePopupMenu();
            setupLargeToolbar();
            return;
        }
        if(type == ToolbarType.LARGE) searchToolResizeEvent();
        else MenuToolController.getInstance().windowResizedEvent();
    }

    public void moveEvent() {
        if(type == ToolbarType.SMALL) MenuToolController.getInstance().windowMovedEvent();
    }

    private void removeAllComponentsFromToolbar() {
        if(toolbar.getComponents().length == 0) return;
        for(Component component : toolbar.getComponents()) {
            toolbar.remove(component);
        }
    }

    private ToolComponent addSearchButtonToolToSmallToolbar() {
        ToolComponent button = toolbar.getTool(ToolType.SEARCHBUTTON);
        toolbarLayout.putConstraint(EAST, button,
                MARGIN_SMALL_RIGHT,
                EAST, toolbar);
        putNorthConstraints(button);
        toolbar.add(button);
        return button;
    }


    private ToolComponent addMenuToolToSmallToolbar() {
        ToolComponent menu = toolbar.getTool(ToolType.MENU);
        toolbarLayout.putConstraint(WEST, menu,
                MARGIN_SMALL_LEFT,
                WEST, toolbar);
        putNorthConstraints(menu);
        toolbar.add(menu);
        MenuToolController.getInstance().setupLayoutForMenuTool();
        return menu;
    }

    private ToolComponent addSearchToolToSmallToolbar(ToolComponent tool) {
        SearchToolController.getInstance().searchToolFixedSizeEvent();
        ToolComponent search = toolbar.getTool(ToolType.SEARCHBAR);
        toolbarLayout.putConstraint(EAST, search,
                MARGIN_SMALLEST_RIGHT,
                WEST, tool);
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

    private ToolComponent addSearchButtonToolToLargeToolbar(ToolComponent tool) {
        ToolComponent searchButton = toolbar.getTool(ToolType.SEARCHBUTTON);
        toolbarLayout.putConstraint(WEST, searchButton,
                MARGIN_SMALLEST_LEFT,
                EAST, tool);
        putNorthConstraints(searchButton);
        tool = searchButton;
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
        ToolComponent search = toolbar.getTool(ToolType.SEARCHBAR);
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
        SearchToolController.getInstance().searchToolResizeEvent();
    }

    private void addInteractionHandlersToTools() {
        addInteractionHandlerToLoadTool();
        addInteractionHandlerToSaveTool();
        addInteractionHandlerToSettingsTool();
        addInteractionHandlerToMenuTool();
        addInterHandlerToSearchButtonTool();
        addInteractionHandlerToToolbar();
    }

    private void addInteractionHandlerToMenuTool() {
        new ToolInteractionHandler(ToolType.MENU, KeyEvent.VK_M, OSDetector.getActivationKey());
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

    private void addInterHandlerToSearchButtonTool() {
        new ToolInteractionHandler(ToolType.SEARCHBUTTON, KeyEvent.VK_ENTER, KeyEvent.VK_UNDEFINED);
    }

    private void addInteractionHandlerToSettingsTool() {
        new ToolInteractionHandler(ToolType.SETTINGS, KeyEvent.VK_COMMA, OSDetector.getActivationKey());
    }

    protected void toolEvent(ToolType type) {
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
            case MENU:
                menuEvent();
                break;
            case SEARCHBUTTON:
                searchButtonEvent();
                break;
        }
    }

    private void searchButtonEvent() {
        SearchToolController.getInstance().searchActivatedEvent();
    }

    private void menuEvent() {
        MenuToolController.getInstance().menuToolActivated();
    }

    private void loadEvent() {
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[]{
                new FileNameExtensionFilter("OSM Files", FileType.OSM.toString()),
                new FileNameExtensionFilter("ZIP Files", FileType.ZIP.toString()),
                new FileNameExtensionFilter("BIN Files", FileType.BIN.toString())
        };
        JFileChooser chooser = PopupWindow.fileLoader(false, filters);
        if (chooser != null) {
            try {
                FileHandler.fileChooserLoad(chooser.getSelectedFile().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveEvent() {
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[]{
                new FileNameExtensionFilter("BIN Files", FileType.BIN.toString())
        };
        JFileChooser chooser = PopupWindow.fileSaver(false, filters);
        if (chooser != null) {
            try {
                FileHandler.fileChooserSave(chooser.getSelectedFile().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void settingsEvent() {
            SettingsWindowController.getInstance().showWindow();
    }

    public boolean doesSearchbarHaveFocus() {
        return SearchToolController.getInstance().doesSearchbarHaveFocus();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void resetInstance() {
        instance = null;
    }

    public void themeHasChanged() {
        SearchToolController.getInstance().themeHasChanged();
        toolbar.applyTheme();
    }

    public void toggleKeyBindings(boolean status) {
        for (ToolType type : toolbar.getAllTools().keySet()) {
            if (toolbar.getTool(type).getActionMap().keys() == null) continue;
            for (Object key : toolbar.getTool(type).getActionMap().keys()) {
                toolbar.getTool(type).getActionMap().get(key).setEnabled(status);
            }
        }
    }

    public void transferFocusToCanvas() {
        MainWindowController.getInstance().transferFocusToMapCanvas();
    }

    public void requestCanvasRepaint() {
        MainWindowController.getInstance().requestCanvasRepaint();
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
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            toolbar.grabFocus();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            toolbar.grabFocus();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            toolbar.grabFocus();
        }
    }

}
