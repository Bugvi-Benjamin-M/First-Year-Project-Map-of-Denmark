package Controller.ToolbarControllers;

import Controller.Controller;
import Controller.MainWindowController;
import Controller.PreferencesController;
import Controller.SettingsWindowController;
import Enums.FileType;
import Enums.ToolType;
import Enums.ToolbarType;
import Exceptions.FileWasNotFoundException;
import Helpers.DefaultSettings;
import Helpers.FileHandler;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import View.PopupWindow;
import View.ToolComponent;
import View.ToolFeature;
import View.Toolbar;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;

import static javax.swing.SpringLayout.*;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public final class ToolbarController extends Controller {

    private static final int SMALL_LARGE_EVENT_WIDTH = (int) (0.133036*Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 814.714);

    private Toolbar toolbar;
    private SpringLayout toolbarLayout;
    private static ToolbarController instance;
    private boolean poiToolActive;

    private ToolbarType type;

    private final int MARGIN_SMALL_LEFT = 20;
    private final int MARGIN_MEDIUM_LEFT = 35;
    private final int MARGIN_SMALL_RIGHT = -20;
    private final int MARGIN_SMALLEST_LEFT = 10;
    private final int MARGIN_SMALLEST_RIGHT = -10;
    private final int MARGIN_LARGE_RIGHT = -60;
    private final int MARGIN_TOP = 20;
    private final int LOADING_SCREEN_OFFSET = 10;
    private JWindow loadWindow;

    private ToolbarController()
    {
        super();
        SearchToolController.getInstance();
        MenuToolController.getInstance();
    }

    public static ToolbarController getInstance()
    {
        if (instance == null) {
            instance = new ToolbarController();
        }
        return instance;
    }

    public void setupToolbar(ToolbarType type)
    {
        toolbar = new Toolbar();
        poiToolActive = false;
        toolbarLayout = toolbar.getLayout();
        toolbar.setPreferredSize(new Dimension(window.getFrame().getWidth(),
            GlobalValue.getToolbarHeight()));
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
        setToolTips();
        customisePOITool();
        customiseSearchButtonTool();
        customiseSettingsTool();
        customiseRoutesTool();
        toggleKeyBindings();
    }

    public void setupLargeToolbar()
    {
        SetToLargeToolbarToolTipSetting();
        removeAllComponentsFromToolbar();
        addRoutesToolToLargeToolbar(addPOIToolToLargeToolbar(
            addSaveToolToLargeToolbar(addLoadToolToLargeToolbar())));
        addSettingsToolToLargeToolbar();
        addSearchButtonToolToLargeToolbar(addSearchToolToLargeToolbar());
        type = ToolbarType.LARGE;
    }


    public void setupSmallToolbar()
    {
        setToSmallToolbarToolTipSetting();
        removeAllComponentsFromToolbar();
        addMenuToolToSmallToolbar();
        addSearchToolToSmallToolbar(addSearchButtonToolToSmallToolbar());
        MenuToolController.getInstance().setupLayoutForMenuTool();
        type = ToolbarType.SMALL;
    }

    private void setToSmallToolbarToolTipSetting() {
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.LOAD));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.SAVE));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.POI));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.ROUTES));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.SETTINGS));
    }

    private void SetToLargeToolbarToolTipSetting() {
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.LOAD));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.SAVE));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.POI));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.ROUTES));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.SETTINGS));
    }

    private void removeActivationFromTools()
    {
        if (toolbar.getComponents().length == 0)
            return;
        for (Component component : toolbar.getComponents()) {
            if (component instanceof ToolFeature) {
                ((ToolFeature)component).toggleActivate(false);
            }
        }
    }

    public ToolbarType getType() { return type; }

    private void setToolTips()
    {
        for (ToolType tool : toolbar.getAllTools().keySet()) {
            switch (tool) {
            case LOAD:
                toolbar.getTool(tool).setToolTipText("Load a chosen file");
                break;
            case SAVE:
                toolbar.getTool(tool).setToolTipText(
                    "Save the current state of the map");
                break;
            case SEARCHBAR:
                SearchToolController.getInstance().setToolTip();
                break;
            case SEARCHBUTTON:
                toolbar.getTool(tool).setToolTipText("Initialise search");
                break;
            case SETTINGS:
                toolbar.getTool(tool).setToolTipText("Open settings window");
                break;
            case MENU:
                toolbar.getTool(tool).setToolTipText("Access tools");
                break;
            case POI:
                toolbar.getTool(tool).setToolTipText("Manage Points of Interest");
                break;
            case ROUTES:
                toolbar.getTool(tool).setToolTipText("Journey Planner");
                break;
            }
        }
    }

    public void resizeEvent()
    {
        if (type == ToolbarType.LARGE && MainWindowController.getInstance().getWindow().getFrame().getWidth() < SMALL_LARGE_EVENT_WIDTH) {
            removeActivationFromTools();
            setupSmallToolbar();
            return;
        }
        if (type == ToolbarType.SMALL && MainWindowController.getInstance().getWindow().getFrame().getWidth() >= SMALL_LARGE_EVENT_WIDTH) {
            MenuToolController.getInstance().hidePopupMenu();
            removeActivationFromTools();
            setupLargeToolbar();
            return;
        }
        if (type == ToolbarType.LARGE)
            searchToolResizeEvent();
        else MenuToolController.getInstance().windowResizedEvent();
        calculateLoadingScreenPosition();
    }

    public void moveEvent()
    {
        if (type == ToolbarType.SMALL)
            MenuToolController.getInstance().windowMovedEvent();
        calculateLoadingScreenPosition();
    }

    private void calculateLoadingScreenPosition() {
        if(loadWindow != null) {
            loadWindow.setLocation(toolbar.getLocationOnScreen().x + LOADING_SCREEN_OFFSET, toolbar.getLocationOnScreen().y + toolbar.getHeight() + LOADING_SCREEN_OFFSET);
        }
    }

    private void removeAllComponentsFromToolbar()
    {
        if (toolbar.getComponents().length == 0)
            return;
        for (Component component : toolbar.getComponents()) {
            toolbar.remove(component);
        }
    }

    private void customisePOITool()
    {
        ToolFeature poiFeature = (ToolFeature)toolbar.getTool(ToolType.POI);
        poiFeature.overrideStandardLabelFontSize(13);
        poiFeature.createSpaceBeforeIcon(9);
        poiFeature.createSpaceBetweenLabelAndIcon(1);
    }

    private void customiseSearchButtonTool()
    {
        ToolFeature searchButtonFeature = (ToolFeature)toolbar.getTool(ToolType.SEARCHBUTTON);
        searchButtonFeature.overrideStandardLabelFontSize(13);
        searchButtonFeature.createSpaceBetweenLabelAndIcon(4);
    }

    private void customiseSettingsTool()
    {
        ToolFeature settingsFeature = (ToolFeature)toolbar.getTool(ToolType.SETTINGS);
        settingsFeature.overrideStandardLabelFontSize(12);
        settingsFeature.createSpaceBetweenLabelAndIcon(6);
    }

    private void customiseRoutesTool() {
        ToolFeature routesFeature = (ToolFeature) toolbar.getTool(ToolType.ROUTES);
        routesFeature.overrideStandardLabelFontSize(13);
        routesFeature.createSpaceBetweenLabelAndIcon(1);
    }

    private void addRoutesToolToLargeToolbar(ToolComponent tool) {
        ToolComponent routes = toolbar.getTool(ToolType.ROUTES);
        toolbarLayout.putConstraint(WEST, routes, MARGIN_MEDIUM_LEFT, EAST, tool);
        putNorthConstraints(routes);
        tool = routes;
        toolbar.add(tool);
    }

    private ToolComponent addPOIToolToLargeToolbar(ToolComponent tool)
    {
        ToolComponent poi = toolbar.getTool(ToolType.POI);
        toolbarLayout.putConstraint(WEST, poi, MARGIN_MEDIUM_LEFT, EAST, tool);
        putNorthConstraints(poi);
        tool = poi;
        toolbar.add(tool);
        return tool;
    }

    private ToolComponent addSearchButtonToolToSmallToolbar()
    {
        ToolComponent button = toolbar.getTool(ToolType.SEARCHBUTTON);
        toolbarLayout.putConstraint(EAST, button, MARGIN_SMALL_RIGHT, EAST,
            toolbar);
        putNorthConstraints(button);
        toolbar.add(button);
        return button;
    }

    private ToolComponent addMenuToolToSmallToolbar()
    {
        ToolComponent menu = toolbar.getTool(ToolType.MENU);
        toolbarLayout.putConstraint(WEST, menu, MARGIN_SMALL_LEFT, WEST, toolbar);
        putNorthConstraints(menu);
        toolbar.add(menu);
        return menu;
    }

    private ToolComponent addSearchToolToSmallToolbar(ToolComponent tool)
    {
        SearchToolController.getInstance().searchToolFixedSizeEvent();
        ToolComponent search = toolbar.getTool(ToolType.SEARCHBAR);
        toolbarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, search, 0 , SpringLayout.HORIZONTAL_CENTER, toolbar);
        toolbarLayout.putConstraint(SpringLayout.VERTICAL_CENTER, search, 0,
            SpringLayout.VERTICAL_CENTER, toolbar);
        toolbar.add(search);
        return search;
    }

    private ToolComponent addLoadToolToLargeToolbar()
    {
        ToolComponent load = toolbar.getTool(ToolType.LOAD);
        toolbarLayout.putConstraint(WEST, load, MARGIN_SMALL_LEFT, WEST, toolbar);
        putNorthConstraints(load);
        toolbar.add(load);
        return load;
    }

    private ToolComponent addSaveToolToLargeToolbar(ToolComponent tool)
    {
        ToolComponent save = toolbar.getTool(ToolType.SAVE);
        toolbarLayout.putConstraint(WEST, save, MARGIN_MEDIUM_LEFT, EAST, tool);
        putNorthConstraints(save);
        tool = save;
        toolbar.add(tool);
        return tool;
    }

    private ToolComponent addSearchButtonToolToLargeToolbar(ToolComponent tool)
    {
        ToolComponent searchButton = toolbar.getTool(ToolType.SEARCHBUTTON);
        toolbarLayout.putConstraint(WEST, searchButton, MARGIN_SMALLEST_LEFT, EAST,
            tool);
        putNorthConstraints(searchButton);
        tool = searchButton;
        toolbar.add(tool);
        return tool;
    }

    private ToolComponent addSettingsToolToLargeToolbar()
    {
        ToolComponent settings = toolbar.getTool(ToolType.SETTINGS);
        toolbarLayout.putConstraint(EAST, settings, MARGIN_SMALL_RIGHT, EAST,
            toolbar);
        putNorthConstraints(settings);
        toolbar.add(settings);
        return settings;
    }

    private ToolComponent addSearchToolToLargeToolbar()
    {
        ToolComponent search = toolbar.getTool(ToolType.SEARCHBAR);
        toolbarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, search, 0,
            SpringLayout.HORIZONTAL_CENTER, toolbar);
        toolbarLayout.putConstraint(SpringLayout.VERTICAL_CENTER, search, 0,
            SpringLayout.VERTICAL_CENTER, toolbar);
        toolbar.add(search);
        return search;
    }

    private void putNorthConstraints(ToolComponent tool)
    {
        toolbarLayout.putConstraint(NORTH, tool, MARGIN_TOP, NORTH, toolbar);
    }

    public void searchToolResizeEvent()
    {
        SearchToolController.getInstance().searchToolResizeEvent();
    }

    private void addInteractionHandlersToTools()
    {
        addInteractionHandlerToLoadTool();
        addInteractionHandlerToSaveTool();
        addInteractionHandlerToSettingsTool();
        addInteractionHandlerToMenuTool();
        addInterHandlerToSearchButtonTool();
        addInteractionHandlerToPOITool();
        addInteractionHandlerToRoutesTool();
        addInteractionHandlerToToolbar();
    }

    private void addInteractionHandlerToPOITool() {
        new ToolInteractionHandler(ToolType.POI, KeyEvent.VK_P,
                OSDetector.getActivationKey());
    }

    private void addInteractionHandlerToMenuTool()
    {
        new ToolInteractionHandler(ToolType.MENU, KeyEvent.VK_M,
            OSDetector.getActivationKey());
    }

    private void addInteractionHandlerToRoutesTool() {
        new ToolInteractionHandler(ToolType.ROUTES, KeyEvent.VK_R,
                OSDetector.getActivationKey());
    }

    private void addInteractionHandlerToToolbar()
    {
        new ToolbarInteractionHandler();
    }

    private void addInteractionHandlerToSaveTool()
    {
        new ToolInteractionHandler(ToolType.SAVE, KeyEvent.VK_S,
            OSDetector.getActivationKey());
    }

    private void addInteractionHandlerToLoadTool()
    {
        new ToolInteractionHandler(ToolType.LOAD, KeyEvent.VK_L,
            OSDetector.getActivationKey());
    }

    private void addInterHandlerToSearchButtonTool()
    {
        new ToolInteractionHandler(ToolType.SEARCHBUTTON, KeyEvent.VK_ENTER,
            KeyEvent.VK_UNDEFINED);
    }

    private void addInteractionHandlerToSettingsTool()
    {
        new ToolInteractionHandler(ToolType.SETTINGS, KeyEvent.VK_COMMA,
            OSDetector.getActivationKey());
    }

    protected void toolEvent(ToolType type)
    {
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
        case POI:
            poiToolActivatedEvent();
            break;
        case ROUTES:
            routesToolActivatedEvent();
            break;
        }
    }

    public void setIsPoiToolActive(boolean status) {
        poiToolActive = status;
    }

    public boolean isPoiToolActive() {
        return poiToolActive;
    }

    private void searchButtonEvent()
    {
        SearchToolController.getInstance().searchActivatedEvent();
    }

    private void poiToolActivatedEvent() {
        if(!poiToolActive) {
            toolbar.getTool(ToolType.POI).toggleActivate(true);
            if(type == ToolbarType.LARGE) MainWindowController.getInstance().activateLargePointsOfInterestInformationBar();
            else if(type == ToolbarType.SMALL) MainWindowController.getInstance().activateSmallPointsOfInterestInformationBar();
            MainWindowController.getInstance().transferFocusToInformationBar();
            poiToolActive = true;
        } else {
            toolbar.getTool(ToolType.POI).toggleActivate(false);
            if(type == ToolbarType.LARGE) MainWindowController.getInstance().deactivateLargePointsOfInterestInformationBar();
            else if(type == ToolbarType.SMALL) MainWindowController.getInstance().deactivateSmallPointsOfInterestInformationBar();
            MainWindowController.getInstance().transferFocusToMapCanvas();
            poiToolActive = false;
        }

    }

    private void routesToolActivatedEvent() {
        toolbar.getTool(ToolType.ROUTES).toggleActivate(true);
        PopupWindow.infoBox(null, "Routes tool activated", "Temporary popup");
        toolbar.getTool(ToolType.ROUTES).toggleActivate(false);
    }

    private void menuEvent()
    {
        toolbar.getTool(ToolType.MENU).toggleActivate(true);
        MenuToolController.getInstance().menuToolActivated();
    }

    private void loadEvent()
    {
        if (type == ToolbarType.LARGE)
            toolbar.getTool(ToolType.LOAD).toggleActivate(true);
        Object[] options = new Object[] { "Load default", "Select file" };
        int selected = PopupWindow.confirmBox(
            null, "Do you want to load the default "
                + "file or select your own file to load from?",
            "Load file options", options, options[1]);
        switch (selected) {
        case JOptionPane.YES_OPTION:
            loadDefaultFile();
            break;
        case JOptionPane.NO_OPTION:
            loadNewFile();
            break;
        case JOptionPane.CLOSED_OPTION:
            break;
        }
        if (type == ToolbarType.LARGE)
            toolbar.getTool(ToolType.LOAD).toggleActivate(false);
    }

    private void loadDefaultFile() {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                loadWindow = PopupWindow.LoadingScreen("Loading Default File!");
                calculateLoadingScreenPosition();

                if (PreferencesController.getInstance().getStartupFileNameSetting().equals(DefaultSettings.DEFAULT_FILE_NAME)) {
                    FileHandler.loadDefaultResource(false);
                } else {
                    try {
                        FileHandler.fileChooserLoad(PreferencesController.getInstance().getStartupFilePathSetting());
                    } catch (FileNotFoundException | FileWasNotFoundException e) {
                        PopupWindow.infoBox(null, "Could Not Find Preferred Default File: " +
                                PreferencesController.getInstance().getStartupFileNameSetting() + ".\n" +
                                "Loading Danmark.bin.", "File Not Found");
                        FileHandler.loadDefaultResource(false);
                    }
                }
                return "Done";
            }
            @Override
            protected void done() {
                MainWindowController.getInstance().requestCanvasResetElements();
                MainWindowController.getInstance().requestCanvasAdjustToDynamicBounds();
                MainWindowController.getInstance().requestCanvasUpdatePOI();
                GlobalValue.setMaxZoom(GlobalValue.MAX_ZOOM_DECREASE);
                MainWindowController.getInstance().requestCanvasRepaint();
                loadWindow.setVisible(false);
                loadWindow = null;
            }
        };

        worker.execute();
    }

    private void loadNewFile()
    {
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[] {
            new FileNameExtensionFilter("OSM Files", FileType.OSM.toString()),
            new FileNameExtensionFilter("ZIP Files", FileType.ZIP.toString()),
            new FileNameExtensionFilter("BIN Files", FileType.BIN.toString())
        };
        JFileChooser chooser = PopupWindow.fileLoader(false, filters);
        if (chooser != null) {
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    loadWindow = PopupWindow.LoadingScreen("Loading: " + chooser.getSelectedFile().getName());
                    calculateLoadingScreenPosition();
                    try {
                        FileHandler.fileChooserLoad(chooser.getSelectedFile().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "Done";
                }
                @Override
                protected void done() {
                    MainWindowController.getInstance().requestCanvasAdjustToDynamicBounds();
                    MainWindowController.getInstance().requestCanvasResetElements();
                    MainWindowController.getInstance().requestCanvasUpdatePOI();

                    MainWindowController.getInstance().requestCanvasRepaint();
                    loadWindow.setVisible(false);
                    loadWindow = null;
                }
            };
            worker.execute();
        }
    }

    private void saveEvent()
    {
        if (type == ToolbarType.LARGE)
            toolbar.getTool(ToolType.SAVE).toggleActivate(true);
        FileNameExtensionFilter[] filters = new FileNameExtensionFilter[] {
            new FileNameExtensionFilter("BIN Files", FileType.BIN.toString())
        };
        JFileChooser chooser = PopupWindow.fileSaver(false, filters);
        if (chooser != null) {
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    loadWindow = PopupWindow.LoadingScreen("Saving File: " + chooser.getSelectedFile().getName());
                    calculateLoadingScreenPosition();
                    try {
                        FileHandler.fileChooserSave(chooser.getSelectedFile().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "Done";
                }

                @Override
                protected void done() {
                    loadWindow.setVisible(false);
                    loadWindow = null;
                    if (type == ToolbarType.LARGE)
                        toolbar.getTool(ToolType.SAVE).toggleActivate(false);
                }
            };
            worker.execute();
        } else if (type == ToolbarType.LARGE) toolbar.getTool(ToolType.SAVE).toggleActivate(false);
    }


    private void settingsEvent()
    {
        toolbar.getTool(ToolType.SETTINGS).toggleActivate(true);
        SettingsWindowController.getInstance().showWindow();
    }

    public boolean doesSearchbarHaveFocus()
    {
        return SearchToolController.getInstance().doesSearchbarHaveFocus();
    }

    public Toolbar getToolbar() { return toolbar; }

    public void setLoadingScreenAlwaysOnTopStatus(boolean status) {
        if(loadWindow != null) {
            loadWindow.setAlwaysOnTop(status);
        }
    }

    public void resetInstance() { instance = null; }

    public void themeHasChanged()
    {
        SearchToolController.getInstance().themeHasChanged();
        toolbar.applyTheme();
    }

    public void toggleKeyBindings()
    {
        for (ToolType type : toolbar.getAllTools().keySet()) {
            if (toolbar.getTool(type).getActionMap().keys() == null)
                continue;
            for (Object key : toolbar.getTool(type).getActionMap().keys()) {
                toolbar.getTool(type).getActionMap().get(key).setEnabled(
                    PreferencesController.getInstance().getKeyBindingsSetting());
            }
        }
    }

    public void requestSearchToolHideList() {
        SearchToolController.getInstance().closeSearchToolList();
    }

    private void mouseEnteredTool(ToolType type, MouseEvent e) {
        toolbar.getTool(type).toggleHover(true);
    }

    private void mouseExitedTool(ToolType type, MouseEvent e) {
        if(toolbar.getTool(type).getActivatedStatus()) {
            toolbar.getTool(type).toggleHover(false);
            toolbar.getTool(type).toggleActivate(true);
        } else {
            toolbar.getTool(type).toggleHover(false);
        }
    }

    public void transferFocusToCanvas()
    {
        MainWindowController.getInstance().transferFocusToMapCanvas();
    }

    public void requestCanvasRepaint()
    {
        MainWindowController.getInstance().requestCanvasRepaint();
    }

    public void repaintToolbar() {
        toolbar.revalidate();
        toolbar.repaint();
    }

    public boolean isMenuToolPopupVisible() {
        return MenuToolController.getInstance().isPopupVisible();
    }

    public void requestHideMenuToolPopup() {
        MenuToolController.getInstance().hidePopupMenu();
    }

    private class ToolInteractionHandler extends MouseAdapter {

        private ToolType toolType;
        private ToolFeature tool;
        private int keyEvent;
        private int activationKey;

        public ToolInteractionHandler(ToolType toolType, int keyEvent,
            int activationKey)
        {
            this.toolType = toolType;
            this.keyEvent = keyEvent;
            this.activationKey = activationKey;
            tool = (ToolFeature)toolbar.getTool(toolType);
            addMouseListener();
            setKeyShortCuts();
        }

        private void addMouseListener()
        {
            if (tool != null) {
                tool.addMouseListener(this);
            } else {
                throw new RuntimeException("No such tool found");
            }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            super.mouseClicked(e);
            if (tool == null)
                toolbar.grabFocus();
            else {
                tool.grabFocus();
                if(type == ToolbarType.SMALL) {
                    if (toolType != ToolType.MENU) {
                        if (isMenuToolPopupVisible()) requestHideMenuToolPopup();
                        if (toolType != ToolType.POI) tool.setTheme();
                    }
                }
                toolEvent(toolType);
            }
            requestSearchToolHideList();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            if(tool != null) {
                mouseEnteredTool(toolType, e);
                if(!doesSearchbarHaveFocus()) tool.grabFocus();
            }


        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            if(tool != null) mouseExitedTool(toolType, e);
        }

        private void setKeyShortCuts()
        {
            tool.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(keyEvent, activationKey),
                    toolType.toString().toLowerCase());
            tool.getActionMap().put(toolType.toString().toLowerCase(),
                new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        toolEvent(toolType);
                    }
                });
        }
    }

    private class ToolbarInteractionHandler extends MouseAdapter {

        public ToolbarInteractionHandler() { addMouseListener(); }

        private void addMouseListener() { toolbar.addMouseListener(this); }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            super.mouseClicked(e);
            if(isMenuToolPopupVisible()) requestHideMenuToolPopup();
            toolbar.grabFocus();
            requestSearchToolHideList();
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            super.mousePressed(e);
            if(!doesSearchbarHaveFocus()) toolbar.grabFocus();
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            super.mouseReleased(e);
            if(!doesSearchbarHaveFocus()) toolbar.grabFocus();
        }

        @Override
        public void mouseDragged(MouseEvent e)
        {
            super.mouseDragged(e);
            if(!doesSearchbarHaveFocus()) toolbar.grabFocus();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            if(!doesSearchbarHaveFocus()) toolbar.grabFocus();
        }
    }
}
