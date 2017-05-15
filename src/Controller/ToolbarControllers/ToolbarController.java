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
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;

import static javax.swing.SpringLayout.*;

/**
 * This class controls the toolbar of the application, the tools in the toolbar,
 * and the events to be triggered when the tools are activated.
 */
public final class ToolbarController extends Controller {


    private static final int SMALL_LARGE_EVENT_WIDTH = (int) (0.133036*Toolkit.getDefaultToolkit().getScreenSize().getWidth() + 814.714);

    private Toolbar toolbar;
    private SpringLayout toolbarLayout;
    private static ToolbarController instance;
    private boolean poiToolActive;
    private boolean journeyPlannerToolActive;


    private ToolbarType type;

    private final int MARGIN_SMALL_LEFT = 20;
    private final int MARGIN_MEDIUM_LEFT = 35;
    private final int MARGIN_SMALL_RIGHT = -20;
    private final int MARGIN_SMALLEST_LEFT = 10;
    private final int MARGIN_TOP = 20;
    private final int LOADING_SCREEN_OFFSET = 10;
    private JWindow loadWindow;

    /**
     * Private constructor, called by getInstance.
     * Creates the SearchToolController and the MenuToolController.
     */
    private ToolbarController()
    {
        super();
        SearchToolController.getInstance();
        MenuToolController.getInstance();
    }

    /**
     * Returns the singleton instance of the ToolbarController.
     * @return the singleton
     */
    public static ToolbarController getInstance()
    {
        if (instance == null) {
            instance = new ToolbarController();
        }
        return instance;
    }

    /**
     * Lets the client know the width of the main window when
     * the resize event of the toolbar is triggered.
     * @return the width necessary to trigger resize event of the toolbar.
     */
    public static int getSmallLargeEventWidth() {
        return SMALL_LARGE_EVENT_WIDTH;
    }

    /**
     * Sets up the toolbar.
     * @param type Large or small toolbar.
     */
    public void setupToolbar(ToolbarType type)
    {
        toolbar = new Toolbar();
        poiToolActive = false;
        journeyPlannerToolActive = false;
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

    /**
     * Set up the large version of the toolbar and adds tools.
     */
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

    /**
     * Set up the small version of the toolbar and adds tools.
     */
    public void setupSmallToolbar()
    {
        setToSmallToolbarToolTipSetting();
        removeAllComponentsFromToolbar();
        addMenuToolToSmallToolbar();
        addSearchToolToSmallToolbar(addSearchButtonToolToSmallToolbar());
        MenuToolController.getInstance().setupLayoutForMenuTool();
        type = ToolbarType.SMALL;
    }

    /**
     * Adapt all tools' tooltips to the small toolbar by removing tooltips of the tools that are
     * not visible.
     */
    private void setToSmallToolbarToolTipSetting() {
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.LOAD));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.SAVE));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.POI));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.ROUTES));
        ToolTipManager.sharedInstance().unregisterComponent(toolbar.getTool(ToolType.SETTINGS));
    }

    /**
     * Adapt all tools' tooltips to the small toolbar by adding tooltips to the tools that
     * are visible.
     */
    private void SetToLargeToolbarToolTipSetting() {
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.LOAD));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.SAVE));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.POI));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.ROUTES));
        ToolTipManager.sharedInstance().registerComponent(toolbar.getTool(ToolType.SETTINGS));
    }

    /**
     * Sets all tools to non-activated status.
     * Changes the color of the tools that are active to inactive color.
     */
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

    /**
     * Lets the client know which type the toolbar is, large or small.
     * @return the type of the toolbar.
     */
    public ToolbarType getType() { return type; }

    /**
     * Sets the tooltips of the tools in the toolbar.
     */
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
                SearchToolController.getInstance().setToolTip("Search");
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

    /**
     * Adapts the toolbar to a new window size, triggering different resize event if necessary.
     */
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
        if(journeyPlannerToolActive) toolbar.getTool(ToolType.ROUTES).toggleActivate(true);
        else if(poiToolActive) toolbar.getTool(ToolType.POI).toggleActivate(true);
    }

    /**
     * Recalculates the position of the menu tool popup and loading screen, in case
     * the main window is moved by the user.
     */
    public void moveEvent()
    {
        if (type == ToolbarType.SMALL)
            MenuToolController.getInstance().windowMovedEvent();
        calculateLoadingScreenPosition();
    }

    /**
     * Calculates the position of the loading screen.
     */
    private void calculateLoadingScreenPosition() {
        if(loadWindow != null) {
            loadWindow.setLocation(toolbar.getLocationOnScreen().x + LOADING_SCREEN_OFFSET, toolbar.getLocationOnScreen().y + toolbar.getHeight() + LOADING_SCREEN_OFFSET);
        }
    }

    /**
     * Removes all components from the toolbar.
     */
    private void removeAllComponentsFromToolbar()
    {
        if (toolbar.getComponents().length == 0)
            return;
        for (Component component : toolbar.getComponents()) {
            toolbar.remove(component);
        }
    }

    /**
     * Modifies the look of the POI tool, to make sure it matches the
     * other tools better.
     */
    private void customisePOITool()
    {
        ToolFeature poiFeature = (ToolFeature)toolbar.getTool(ToolType.POI);
        poiFeature.overrideStandardLabelFontSize(13);
        poiFeature.createSpaceBeforeIcon(9);
        poiFeature.createSpaceBetweenLabelAndIcon(1);
    }

    /**
     * Modifies the look of the SearchButton tool, to make sure it matches the
     * other tools better.
     */
    private void customiseSearchButtonTool()
    {
        ToolFeature searchButtonFeature = (ToolFeature)toolbar.getTool(ToolType.SEARCHBUTTON);
        searchButtonFeature.overrideStandardLabelFontSize(13);
        searchButtonFeature.createSpaceBetweenLabelAndIcon(4);
    }

    /**
     * Modifies the look of the Settins tool, to make sure it matches the
     * other tools better.
     */
    private void customiseSettingsTool()
    {
        ToolFeature settingsFeature = (ToolFeature)toolbar.getTool(ToolType.SETTINGS);
        settingsFeature.overrideStandardLabelFontSize(12);
        settingsFeature.createSpaceBetweenLabelAndIcon(6);
    }

    /**
     * Modifies the look of the Routes tool, to make sure it matches the
     * other tools better.
     */
    private void customiseRoutesTool() {
        ToolFeature routesFeature = (ToolFeature) toolbar.getTool(ToolType.ROUTES);
        routesFeature.overrideStandardLabelFontSize(13);
        routesFeature.createSpaceBetweenLabelAndIcon(1);
    }

    /**
     * Adds the Routes tool to the large version of the toolbar.
     * @param tool to use for relative positioning.
     */
    private void addRoutesToolToLargeToolbar(ToolComponent tool) {
        ToolComponent routes = toolbar.getTool(ToolType.ROUTES);
        toolbarLayout.putConstraint(WEST, routes, MARGIN_MEDIUM_LEFT, EAST, tool);
        putNorthConstraints(routes);
        tool = routes;
        toolbar.add(tool);
    }

    /**
     * Adds the PointsOfInterest tool to the large version of the toolbar.
     * @param tool to use for relative positioning.
     * @return the tool that was added.
     */
    private ToolComponent addPOIToolToLargeToolbar(ToolComponent tool)
    {
        ToolComponent poi = toolbar.getTool(ToolType.POI);
        toolbarLayout.putConstraint(WEST, poi, MARGIN_MEDIUM_LEFT, EAST, tool);
        putNorthConstraints(poi);
        tool = poi;
        toolbar.add(tool);
        return tool;
    }

    /**
     * Adds the SearchButton tool to the small version of the toolbar.
     * @return the tool that was added.
     */
    private ToolComponent addSearchButtonToolToSmallToolbar()
    {
        ToolComponent button = toolbar.getTool(ToolType.SEARCHBUTTON);
        toolbarLayout.putConstraint(EAST, button, MARGIN_SMALL_RIGHT, EAST,
            toolbar);
        putNorthConstraints(button);
        toolbar.add(button);
        return button;
    }

    /**
     * Adds the Menu tool to the small version of the toolbar.
     * @return the tool that was added.
     */
    private ToolComponent addMenuToolToSmallToolbar()
    {
        ToolComponent menu = toolbar.getTool(ToolType.MENU);
        toolbarLayout.putConstraint(WEST, menu, MARGIN_SMALL_LEFT, WEST, toolbar);
        putNorthConstraints(menu);
        toolbar.add(menu);
        return menu;
    }

    /**
     * Adds the SearchBar tool to the small version toolbar.
     * @param tool to be used for relative positioning.
     * @return the tool that was added.
     */
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

    /**
     * Adds the Load tool to the large version toolbar.
     * @return the tool that was added.
     */
    private ToolComponent addLoadToolToLargeToolbar()
    {
        ToolComponent load = toolbar.getTool(ToolType.LOAD);
        toolbarLayout.putConstraint(WEST, load, MARGIN_SMALL_LEFT, WEST, toolbar);
        putNorthConstraints(load);
        toolbar.add(load);
        return load;
    }

    /**
     * Adds the Save tool to the large version toolbar.
     * @param tool to be used for relative positioning.
     * @return the tool that was added.
     */
    private ToolComponent addSaveToolToLargeToolbar(ToolComponent tool)
    {
        ToolComponent save = toolbar.getTool(ToolType.SAVE);
        toolbarLayout.putConstraint(WEST, save, MARGIN_MEDIUM_LEFT, EAST, tool);
        putNorthConstraints(save);
        tool = save;
        toolbar.add(tool);
        return tool;
    }

    /**
     * Add the SearchButton tool to the large version toolbar.
     * @param tool to be used for relative positioning.
     * @return the tool that was added.
     */
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

    /**
     * Adds the Settings tool to the large version toolbar.
     * @return the tool that was added.
     */
    private ToolComponent addSettingsToolToLargeToolbar()
    {
        ToolComponent settings = toolbar.getTool(ToolType.SETTINGS);
        toolbarLayout.putConstraint(EAST, settings, MARGIN_SMALL_RIGHT, EAST,
            toolbar);
        putNorthConstraints(settings);
        toolbar.add(settings);
        return settings;
    }

    /**
     * Adds the SearchBar tool to the large version of the toolbar.
     * @return the tool that was added.
     */
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

    /**
     * Puts north constraints on a given tool.
     * @param tool to be given north constraints.
     */
    private void putNorthConstraints(ToolComponent tool)
    {
        toolbarLayout.putConstraint(NORTH, tool, MARGIN_TOP, NORTH, toolbar);
    }

    /**
     * Adapts the SearchBar to the size of the main window.
     */
    public void searchToolResizeEvent()
    {
        SearchToolController.getInstance().searchToolResizeEvent();
    }

    /**
     * Adds interaction handlers to the tools in the toolbar. The interaction handler
     * deals with mouse and keyboard inputs.
     */
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

    /**
     * Adds an interaction handler to the Points of Interest tool. The interaction handler
     * deals with mouse and keyboard inputs.
     */
    private void addInteractionHandlerToPOITool() {
        new ToolInteractionHandler(ToolType.POI, KeyEvent.VK_P,
                OSDetector.getActivationKey());
    }

    /**
     * Adds an interaction handler to Menu tool. The interaction handler
     * deals with mouse and keyboard inputs.
     */
    private void addInteractionHandlerToMenuTool()
    {
        new ToolInteractionHandler(ToolType.MENU, KeyEvent.VK_M,
            OSDetector.getActivationKey());
    }

    /**
     * Adds an interaction handler to the Routes tool. The interaction handler
     * deals with mouse and keyboard inputs.
     */
    private void addInteractionHandlerToRoutesTool() {
        new ToolInteractionHandler(ToolType.ROUTES, KeyEvent.VK_R,
                OSDetector.getActivationKey());
    }

    /**
     * Adds a toolbar interaction handler to the toolbar.
     * The toolbar interaction handler deals with mouse inputs.
     */
    private void addInteractionHandlerToToolbar()
    {
        new ToolbarInteractionHandler();
    }

    /**
     * Adds an interaction handler to the Save tool. The interaction handler
     * deals with mouse and keyboard inputs.
     */
    private void addInteractionHandlerToSaveTool()
    {
        new ToolInteractionHandler(ToolType.SAVE, KeyEvent.VK_S,
            OSDetector.getActivationKey());
    }

    /**
     * Adds an interaction handler to the Load tool. The interaction handler
     * deals with mouse and keyboard inputs.
     */
    private void addInteractionHandlerToLoadTool()
    {
        new ToolInteractionHandler(ToolType.LOAD, KeyEvent.VK_L,
            OSDetector.getActivationKey());
    }


    /**
     * Adds an interaction handler to the SearchButton tool. The interaction handler
     * deals with mouse and keyboard inputs.
     */
    private void addInterHandlerToSearchButtonTool()
    {
        new ToolInteractionHandler(ToolType.SEARCHBUTTON, KeyEvent.VK_ENTER,
            KeyEvent.VK_UNDEFINED);
    }

    /**
     * Adds an interaction handler to the Settings tool. The interaction handler
     * deals with mouse and keyboard inputs.
     */
    private void addInteractionHandlerToSettingsTool()
    {
        new ToolInteractionHandler(ToolType.SETTINGS, KeyEvent.VK_COMMA,
            OSDetector.getActivationKey());
    }

    /**
     * Triggers an event that is matched with the given parameter.
     * @param type of the tool that was activated.
     */
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

    /**
     * Triggers the SearchButton tool event. Gives focus to the searchbar to allow
     * input.
     */
    private void searchButtonEvent()
    {
       Point2D.Float point = SearchToolController.getInstance().searchActivatedEvent();
       MainWindowController.getInstance().requestCanvasUpdateAddressMarker(point);
    }

    /**
     * Triggers the Points of Interest tool event. Opens the Points of Interest bar.
     */
    private void poiToolActivatedEvent() {
        if(!MainWindowController.getInstance().isSliding()) {
            if(journeyPlannerToolActive) {
                if(type == ToolbarType.LARGE) MainWindowController.getInstance().deactivateLargeJourneyPlannerInformationBar();
                else MainWindowController.getInstance().deactivateSmallJourneyPlannerInformationBar();
                journeyPlannerToolActive = false;
                toolbar.getTool(ToolType.ROUTES).toggleActivate(false);
            }
            if (!poiToolActive) {
                toolbar.getTool(ToolType.POI).toggleActivate(true);
                if (type == ToolbarType.LARGE)
                    MainWindowController.getInstance().activateLargePointsOfInterestInformationBar();
                else if (type == ToolbarType.SMALL)
                    MainWindowController.getInstance().activateSmallPointsOfInterestInformationBar();
                poiToolActive = true;
            } else {
                toolbar.getTool(ToolType.POI).toggleActivate(false);
                if (type == ToolbarType.LARGE)
                    MainWindowController.getInstance().deactivateLargePointsOfInterestInformationBar();
                else if (type == ToolbarType.SMALL)
                    MainWindowController.getInstance().deactivateSmallPointsOfInterestInformationBar();
                MainWindowController.getInstance().transferFocusToMapCanvas();
                poiToolActive = false;
            }
        }
    }

    /**
     * Triggers the Routes tool event. Opens the Journey Planner bar.
     */
    private void routesToolActivatedEvent() {
        if(!MainWindowController.getInstance().isSliding()) {
            if (poiToolActive) {
                if(type == ToolbarType.LARGE) MainWindowController.getInstance().deactivateLargePointsOfInterestInformationBar();
                else MainWindowController.getInstance().deactivateSmallPointsOfInterestInformationBar();
                poiToolActive = false;
                toolbar.getTool(ToolType.POI).toggleActivate(false);
            }
            if (!journeyPlannerToolActive) {
                toolbar.getTool(ToolType.ROUTES).toggleActivate(true);
                if (type == ToolbarType.LARGE)
                    MainWindowController.getInstance().activateLargeJourneyPlannerInformationBar();
                else if (type == ToolbarType.SMALL)
                    MainWindowController.getInstance().activateSmallJourneyPlannerInformationBar();
                journeyPlannerToolActive = true;

            } else {
                toolbar.getTool(ToolType.ROUTES).toggleActivate(false);
                if (type == ToolbarType.LARGE)
                    MainWindowController.getInstance().deactivateLargeJourneyPlannerInformationBar();
                else if (type == ToolbarType.SMALL)
                    MainWindowController.getInstance().deactivateSmallJourneyPlannerInformationBar();
                journeyPlannerToolActive = false;
            }
        }
    }

    /**
     * Triggers the Menu tool event. Opens or closes the Menu tool popup.
     */
    private void menuEvent()
    {
        toolbar.getTool(ToolType.MENU).toggleActivate(true);
        MenuToolController.getInstance().menuToolActivated();
    }

    /**
     * Triggers the Load tool event. Initiates a filechooser, and loads the selected file.
     */
    private void loadEvent()
    {
        if(GlobalValue.isLoading()) {
            PopupWindow.infoBox(null, "Please Wait for the Current Load Process to Complete Before Loading a New File!", "File Loading in Progress");
            return;
        }
        if(GlobalValue.isSaving()) {
            PopupWindow.infoBox(null, "Please Wait for the Current Save Process to Complete Before Loading a File!", "File Saving in Progress");
            return;
        }
        if (type == ToolbarType.LARGE)
            toolbar.getTool(ToolType.LOAD).toggleActivate(true);
        Object[] options = new Object[] { "Load Default", "Select File" };
        int selected = PopupWindow.confirmBox(
            null, "Do you Want to Load the Default "
                + "File or Select your own File to Load From?",
            "Load File Options", options, options[1]);
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

    /**
     * Loads the selected default file. The method uses a SwingWorker to
     * handle the task in the background, letting the user interact with the
     * GUI while the operation is underway.
     */
    private void loadDefaultFile() {
        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                GlobalValue.setIsLoading(true);
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
                //GlobalValue.setMaxZoom(GlobalValue.MAX_ZOOM_DECREASE);
                MainWindowController.getInstance().requestCanvasRepaint();
                loadWindow.setVisible(false);
                loadWindow = null;
                GlobalValue.setIsLoading(false);
            }
        };

        worker.execute();
    }

    /**
     * Loads a selected file. The method uses a SwingWorker to
     * handle the task in the background, letting the user interact with the
     * GUI while the operation is underway.
     */
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
                    GlobalValue.setIsLoading(true);
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
                    GlobalValue.setIsLoading(false);
                }
            };
            worker.execute();
        }
    }

    /**
     * Triggers the save tool event. Launches a fileChooser and saves the
     * state of the program in a given file path.
     *
     * The method uses a SwingWorker to
     * handle the task in the background, letting the user interact with the
     * GUI while the operation is underway.
     */
    private void saveEvent()
    {
        if(GlobalValue.isLoading()) {
            PopupWindow.infoBox(null, "Please Wait for the Current Load Process to Complete Before Saving a File!", "File Loading in Progress");
            return;
        }
        if(GlobalValue.isSaving()) {
            PopupWindow.infoBox(null, "Please Wait for the Current Save Process to Complete before Saving a new File", "File Saving in Progress");
            return;
        }
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
                    GlobalValue.setIsSaving(true);
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
                    GlobalValue.setIsSaving(false);
                }
            };
            worker.execute();
        } else if (type == ToolbarType.LARGE) toolbar.getTool(ToolType.SAVE).toggleActivate(false);
    }


    /**
     * Triggers the Settings tool event. Launches the settings window.
     */
    private void settingsEvent()
    {
        toolbar.getTool(ToolType.SETTINGS).toggleActivate(true);
        SettingsWindowController.getInstance().showWindow();
    }

    /**
     * Lets the client know whether the searchbar has focus.
     * @return
     */
    public boolean doesSearchbarHaveFocus()
    {
        return SearchToolController.getInstance().doesSearchbarHaveFocus();
    }

    /**
     * Returns the toolbar to the client.
     * @return the toolbar.
     */
    public Toolbar getToolbar() { return toolbar; }

    /**
     * Toggles the AlwaysOnTop status of the Loading Screen.
     * @param status Loading Screen alwaysOnTop
     */
    public void setLoadingScreenAlwaysOnTopStatus(boolean status) {
        if(loadWindow != null) {
            loadWindow.setAlwaysOnTop(status);
        }
    }

    /**
     * Resets the instance. Only meant to be used for testing purposes.
     */
    public void resetInstance() { instance = null; }

    /**
     * Changes the theme of the toolbar and the tools in it.
     */
    public void themeHasChanged()
    {
        SearchToolController.getInstance().themeHasChanged();
        toolbar.applyTheme();
        for(ToolType toolType : toolbar.getAllTools().keySet()) {
            toolbar.getTool(toolType).toggleActivate(toolbar.getTool(toolType).getActivatedStatus());
        }
    }

    /**
     * Toggles the keybindings of the tools in the toolbar.
     */
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

    /**
     * Closes the searchbar popup list if it is visible.
     */
    public void requestSearchToolHideList() {
        SearchToolController.getInstance().closeSearchToolList();
    }

    /**
     * Changes the color of the tool of the type given as parameter.
     * The new color is "hover-color".
     * @param type the type of the tool to change color.
     * @param e the mouse entered event.
     */
    private void mouseEnteredTool(ToolType type, MouseEvent e) {
        toolbar.getTool(type).toggleHover(true);
    }

    /**
     * Changes the color of the tool of the type given as parameter.
     * The new color is the standard tool color.
     * @param type the type of the tool to change color.
     * @param e the mouse entered event.
     */
    private void mouseExitedTool(ToolType type, MouseEvent e) {
        if(toolbar.getTool(type).getActivatedStatus()) {
            toolbar.getTool(type).toggleHover(false);
            toolbar.getTool(type).toggleActivate(true);
        } else {
            toolbar.getTool(type).toggleHover(false);
        }
    }

    /**
     * Transfers focus to the map canvas.
     * note: to be used to decrease coupling.
     */
    public void transferFocusToCanvas()
    {
        MainWindowController.getInstance().transferFocusToMapCanvas();
    }

    /**
     * Repaints the canvas.
     * note: to be used to decrease coupling.
     */
    public void requestCanvasRepaint()
    {
        MainWindowController.getInstance().requestCanvasRepaint();
    }

    /**
     * Repaint and revalidate the toolbar.
     */
    public void repaintToolbar() {
        toolbar.revalidate();
        toolbar.repaint();
    }

    /**
     * Lets the client know whether the Menu tool popup is visible.
     * @return is the Menu tool popup visible.
     */
    public boolean isMenuToolPopupVisible() {
        return MenuToolController.getInstance().isPopupVisible();
    }

    /**
     * Closes the Menu tool if it is visible.
     */
    public void requestHideMenuToolPopup() {
        MenuToolController.getInstance().hidePopupMenu();
    }

    /**
     * A ToolInteraction handler registers all mouse events of a tool, and
     * registers a keybinding for a tool tool.
     */
    private class ToolInteractionHandler extends MouseAdapter {

        private ToolType toolType;
        private ToolFeature tool;
        private int keyEvent;
        private int activationKey;

        /**
         * Creates a ToolInteractionHandler to be added to a tool.
         * @param toolType the type of the tool.
         * @param keyEvent a key event to be associated with the tool.
         * @param activationKey an activation key to be held down in order to activate the keybinding.
         */
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

        /**
         * Adds the InteractionHandler as a MouseAdapter on the tool.
         */
        private void addMouseListener()
        {
            if (tool != null) {
                tool.addMouseListener(this);
            } else {
                throw new RuntimeException("No such tool found");
            }
        }

        /**
         * Activated when the user clicks the tool. Changes color, transfer
         * focus to the tool, triggers the tool event and closes the searchbar list if it
         * is visible.
         * @param e the mouse event.
         */
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
                        if(toolType != ToolType.ROUTES) tool.setTheme();
                    }
                }
                toolEvent(toolType);
            }
            requestSearchToolHideList();
        }

        /**
         * Called when the user hovers the mouse above the tool. Changes the tool color to "hover color".
         * @param e the mouse event.
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            if(tool != null) {
                mouseEnteredTool(toolType, e);
                if(!doesSearchbarHaveFocus() && !MainWindowController.getInstance().doesJourneyPlannerSearchHaveFocus()) tool.grabFocus();
            }
        }

        /**
         * Called when the user removes the mouse from the tool. Changes the tool color to normal.
         * @param e the mouse event.
         */
        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            if(tool != null) mouseExitedTool(toolType, e);
        }

        /**
         * Adds the keybinding to the tool.
         */
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

    /**
     * A ToolbarInteractionHandler deals with mouse inputs on the toolbar.
     */
    private class ToolbarInteractionHandler extends MouseAdapter {

        /**
         * Constructs the ToolbarInteractionHandler.
         */
        public ToolbarInteractionHandler() { addMouseListener(); }

        /**
         * Adds the ToolbarInteractionHandler as a MouseAdapter to the toolbar.
         */
        private void addMouseListener() { toolbar.addMouseListener(this); }

        /**
         * Activated when the user clicks the mouse on the toolbar.
         * Transfers focus to the toolbar, closes the Menu tool popup if it is visible and
         * closes the searchbar lists in the JourneyPlanner if they are visible.
         * @param e the mouse clicked event
         */
        @Override
        public void mouseClicked(MouseEvent e)
        {
            super.mouseClicked(e);
            if(isMenuToolPopupVisible()) requestHideMenuToolPopup();
            toolbar.grabFocus();
            requestSearchToolHideList();
            MainWindowController.getInstance().requestJourneyPlannerCloseSearchLists();
        }

        /**
         * Activated when the user presses down the mouse on the toolbar.
         * Transfers focus to the toolbar.
         * @param e the mouse pressed event.
         */
        @Override
        public void mousePressed(MouseEvent e)
        {
            super.mousePressed(e);
            if(!doesSearchbarHaveFocus()) toolbar.grabFocus();
        }

        /**
         * Activated when the user releases the mouse key above the toolbar.
         * Transfers focus to the toolbar.
         * @param e the mouse released event.
         */
        @Override
        public void mouseReleased(MouseEvent e)
        {
            super.mouseReleased(e);
            if(!doesSearchbarHaveFocus()) toolbar.grabFocus();
        }

        /**
         * Activated when the user drags the mouse on the toolbar.
         * Transfers focus to the toolbar.
         * @param e the mouse dragged event.
         */
        @Override
        public void mouseDragged(MouseEvent e)
        {
            super.mouseDragged(e);
            if(!doesSearchbarHaveFocus()) toolbar.grabFocus();
        }

        /**
         * Activated when the mouse enters the toolbar.
         * Transfers focus to the toolbar if there are no search popup lists visible.
         * @param e the mouse entered event.
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            if(!doesSearchbarHaveFocus() && !MainWindowController.getInstance().doesJourneyPlannerSearchHaveFocus()) toolbar.grabFocus();
        }
    }
}
