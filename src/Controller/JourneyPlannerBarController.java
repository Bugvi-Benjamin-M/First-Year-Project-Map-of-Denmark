package Controller;

import Enums.TravelType;
import Helpers.FontAwesome;
import Helpers.GlobalValue;
import Helpers.OSDetector;
import Helpers.ThemeHelper;
import Model.Elements.Road;
import Model.Elements.RoadEdge;
import Model.Model;
import RouteSearch.RoadGraphFactory;
import View.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Controls the Journeyplanner.
 * Handles layout and all logic related to user input in the Journeyplanner, as well
 * as passing on information to the model and requesting relevant information from the model.
 */
public final class JourneyPlannerBarController extends Controller {

    private static JourneyPlannerBarController instance;

    private final int DISTANCE_BETWEEN_TOOLBAR_AND_LARGE_JOURNEYPLANNERBAR = GlobalValue.getToolbarHeight() + 10;
    private final int TRANSPORT_BUTTONS_WIDTH = GlobalValue.getLargeInformationBarWidth() - 72;
    private final int CLEAR_SEARCH_BUTTONS_WIDTH = TRANSPORT_BUTTONS_WIDTH - 3;
    private final int TRANSPORT_BUTTONS_HEIGHT = 90;
    private final int CLEAR_SEARCH_BUTTONS_HEIGHT = 60;
    private final int DISTANCE_BETWEEN_BUTTONS_AND_FROMBAR = 10;
    private final int SEARCHBAR_WIDTH = 312;
    private final int SEARCHBAR_HEIGHT = 30;
    private final int SMALL_SEARCHBAR_HEIGHT = 20;
    private final int JOURNEY_PLANNERBAR_WIDTH = 325;
    private final int JOURNEY_PLANNERBAR_HEIGHT_DECREASE = 167;
    private final int TITLE_FONT_SIZE = 15;
    private final int SMALL_TITLE_FONT_SIZE = 10;
    private final int DISTANCE_BETWEEN_SEARCHBARS = 4;
    private final int DISTANCE_BETWEEN_TOBAR_TO_CLEARSEARCH_BUTTONS = 10;
    private final int JOURNEY_PLANNER_DESCRIPTION_FIELD_WIDTH = 328;
    private final int JOURNEY_PLANNER_DESCRIPTION_FIELD_HEIGHT_DECREASE = 333;
    private final int DISTANCE_BETWEEN_SEARCHCLEAR_BUTTONS_AND_DESCRIPTION_FIELD = 30;
    private final int SMALL_JOURNEY_PLANNERBAR_WIDTH_DECREASE = 20;
    private final int SMALL_JOURNEY_PLANNERBAR_HEIGHT_DECREASE = 40;
    private final int SMALL_TRANSPORT_BUTTONS_WIDTH = 150;
    private final int SMALL_TRANSPORT_BUTTONS_HEIGHT = 60;
    private final int DISTANCE_BETWEEN_JOURNEY_PLANNERBAR_AND_SMALL_INFORMATIONBAR = 10;
    private final int NORTH_DISTANCE_SMALL_JOURNEY_PLANNERBAR_TRANSPORT_BUTTONS = 20;
    private final int WEST_DISTANCE_SMALL_JOURNEY_PLANNERBAR_TRANSPORT_BUTTONS = 5;
    private final int SEARCHTOOL_WIDTH = 330;
    private final int SEARCHTOOL_LARGE_HEIGHT = 65;
    private final int SEARCHTOOL_SMALL_HEIGHT = 45;

    private final int DESCRIPTION_BUTTON_WIDTH = 60;
    private final int DESCRIPTION_BUTTON_HEIGHT = 60;

    private final int SMALL_CLEAR_SEARCH_BUTTONS_WIDTH = CLEAR_SEARCH_BUTTONS_WIDTH-85;
    private final int SMALL_CLEAR_SEARCH_BUTTONS_HEIGHT = CLEAR_SEARCH_BUTTONS_HEIGHT-16;

    private final int SMALL_VERTICAL_DISTANCE_BETWEEN_TRANSPORTBUTTONS_AND_SEARCHBARS = 5;

    private final int SMALL_NORTH_DISTANCE_BETWEEN_CLEARSEARCH_BUTTONS_AND_JOURNEYPLANNERBAR = 33;
    private final int SMALL_VERTICAL_DISTANCE_BETWEEN_CLEARSEARCH_BUTTONS_AND_SEARCHBARS = 5;

    private final int SMALL_NORTH_DISTANCE_BETWEEN_FROM_SEARCHBAR_TO_JOURNEYPLANNERBAR = 5;

    private final int DESCRIPTION_BUTTON_NORTH_DISTANCE = 25;
    private final int DESCRIPTION_BUTTON_EAST_DISTANCE = -20;

    private final int LARGE_SEARCH_FONT = 17;
    private final int SMALL_SEARCH_FONT = 11;

    private final int DESCRIPTION_BUTTON_FONT_SIZE = 40;
    private boolean isFirstDownAction;



    private InformationBar informationBar;
    private SpringLayout journeyPlannerBarLayout;
    private SpringLayout informationBarLayout;
    private JourneyPlannerTransportTypeButtons journeyPlannerTransportTypeButtons;
    private JourneyPlannerBar journeyPlannerBar;
    private JourneyPlannerSearchClearButtons journeyPlannerSearchClearButtons;
    private JourneyDescriptionField travelDescription;
    private ToFromController fromSearcher;
    private ToFromController toSearcher;
    private Point2D.Float fromPoint;
    private Point2D.Float toPoint;
    private JLabel descriptionButton;

    private TravelType type;

    private boolean isLargeJourneyPlannerVisible;
    private boolean isSmallJourneyPlannerVisible;

    private boolean isDescriptionFieldOpen;

    private boolean isSearch;

    private boolean searchUnderway;

    private RouteSearch.RouteDijkstra dijk;
    private RoadGraphFactory factory;

    private JWindow loadWindow;

    /**
     * Private constructor, called by getInstance.
     * Creates the two search controllers.
     */
    private JourneyPlannerBarController() {
        super();
        fromSearcher = new ToFromController();
        toSearcher = new ToFromController();
    }

    /**
     * Returns to the client the singleton instance object.
     * @return the singleton object.
     */
    public static JourneyPlannerBarController getInstance() {
        if(instance == null) {
            instance = new JourneyPlannerBarController();
        }
        return instance;
    }

    /**
     * Sets up the base information bar.
     * Adds mouse listener to the informationbar, specifies the main window
     * as the winow of the informationbar and initiates setup of the two search
     * tools.
     * Initiates all boolean variables.
     */
    public void setupInformationBar() {
        informationBar = new InformationBar();
        informationBarLayout = (SpringLayout) informationBar.getLayout();
        InformationBarInteractionHandler handler = new InformationBarInteractionHandler();
        informationBar.addMouseListener(handler);
        fromSearcher.specifyWindow(window);
        toSearcher.specifyWindow(window);
        fromSearcher.setupSearchTool();
        toSearcher.setupSearchTool();
        isLargeJourneyPlannerVisible = false;
        isSmallJourneyPlannerVisible = false;
        isDescriptionFieldOpen = false;
        searchUnderway = false;
        isFirstDownAction = true;
        isSearch = false;
    }

    /**
     * Sets up the base JourneyPlannerbar.
     * Specifies keybindings, sets borders, adds interaction handlers to components.
     */
    public void setupBaseJourneyPlannerBar() {
        journeyPlannerTransportTypeButtons = new JourneyPlannerTransportTypeButtons();
        journeyPlannerTransportTypeButtons.setOpaque(true);
        journeyPlannerBar = new JourneyPlannerBar();
        journeyPlannerBar.setOpaque(true);
        journeyPlannerBarLayout = (SpringLayout) journeyPlannerBar.getLayout();
        fromSearcher.setToolTip("Type Departure Destination");
        fromSearcher.setTitle("From:");
        fromSearcher.specifyKeyBindings();
        toSearcher.setToolTip("Type End Destination");
        toSearcher.setTitle("To:");
        toSearcher.specifyKeyBindings();
        journeyPlannerSearchClearButtons = new JourneyPlannerSearchClearButtons();
        journeyPlannerSearchClearButtons.setOpaque(true);
        travelDescription = new JourneyDescriptionField();
        travelDescription.setOpaque(true);
        descriptionButton = new JLabel("\uf15c");
        descriptionButton.setFont(FontAwesome.getFontAwesome().deriveFont(DESCRIPTION_BUTTON_FONT_SIZE));
        descriptionButton.setOpaque(true);
        descriptionButton.setToolTipText("View Travel Description");
        addInteractionHandlersToJourneyPlannerTransportButtons();
        addInteractionHandlerToClearSearchButtons();
        addInteractionHandlerToDescriptionButton();
        type = TravelType.VEHICLE;
    }

    /**
     * Sets up the large version of the JourneyPlanner.
     * Specifies relations between components and sets font sizes.
     */
    public void setupLargeJourneyPlannerBar() {
        isLargeJourneyPlannerVisible = true;
        MainWindowController.getInstance().requestCanvasToggleRouteVisualization(true);
        MainWindowController.getInstance().requestCanvasRepaint();
        informationBar.setPreferredSize(new Dimension(GlobalValue.getLargeInformationBarWidth(), window.getFrame().getHeight()));
        int journeyPlannerBarHeight = window.getFrame().getHeight() - JOURNEY_PLANNERBAR_HEIGHT_DECREASE;
        journeyPlannerBar.setPreferredSize(new Dimension(JOURNEY_PLANNERBAR_WIDTH, journeyPlannerBarHeight));
        journeyPlannerTransportTypeButtons.setPreferredSize(new Dimension(TRANSPORT_BUTTONS_WIDTH, TRANSPORT_BUTTONS_HEIGHT));
        journeyPlannerTransportTypeButtons.applyLargeState();
        journeyPlannerSearchClearButtons.setPreferredSize(new Dimension(CLEAR_SEARCH_BUTTONS_WIDTH, CLEAR_SEARCH_BUTTONS_HEIGHT));
        journeyPlannerSearchClearButtons.applyLargeState();
        fromSearcher.getSearchTool().setPreferredSize(new Dimension(SEARCHTOOL_WIDTH, SEARCHTOOL_LARGE_HEIGHT));
        fromSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH,SEARCHBAR_HEIGHT));
        fromSearcher.setBarBorder(TITLE_FONT_SIZE);
        fromSearcher.getSearchTool().getField().setFont(new Font("Verdana", Font.PLAIN, LARGE_SEARCH_FONT));
        toSearcher.getSearchTool().setPreferredSize(new Dimension(SEARCHTOOL_WIDTH, SEARCHTOOL_LARGE_HEIGHT));
        toSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH, SEARCHBAR_HEIGHT));
        toSearcher.setBarBorder(TITLE_FONT_SIZE);
        toSearcher.getSearchTool().getField().setFont(new Font("Verdana", Font.PLAIN, LARGE_SEARCH_FONT));
        int journeyPlannerDescriptionFieldHeight = journeyPlannerBarHeight - JOURNEY_PLANNER_DESCRIPTION_FIELD_HEIGHT_DECREASE;
        travelDescription.setPreferredSize(new Dimension(JOURNEY_PLANNER_DESCRIPTION_FIELD_WIDTH, journeyPlannerDescriptionFieldHeight));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerBar, DISTANCE_BETWEEN_TOOLBAR_AND_LARGE_JOURNEYPLANNERBAR, SpringLayout.NORTH, informationBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, journeyPlannerTransportTypeButtons, 0, SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerTransportTypeButtons, 0, SpringLayout.NORTH, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, fromSearcher.getSearchTool(), 0, SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, fromSearcher.getSearchTool(), DISTANCE_BETWEEN_BUTTONS_AND_FROMBAR, SpringLayout.SOUTH, journeyPlannerTransportTypeButtons);
        journeyPlannerBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, toSearcher.getSearchTool(), 0, SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, toSearcher.getSearchTool(), DISTANCE_BETWEEN_SEARCHBARS, SpringLayout.SOUTH, fromSearcher.getSearchTool());
        journeyPlannerBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, journeyPlannerSearchClearButtons, 0, SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerSearchClearButtons, DISTANCE_BETWEEN_TOBAR_TO_CLEARSEARCH_BUTTONS, SpringLayout.SOUTH, toSearcher.getSearchTool());
        journeyPlannerBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, travelDescription, 0, SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, travelDescription, DISTANCE_BETWEEN_SEARCHCLEAR_BUTTONS_AND_DESCRIPTION_FIELD, SpringLayout.SOUTH, journeyPlannerSearchClearButtons);
        themeHasChanged();
        journeyPlannerBar.add(journeyPlannerTransportTypeButtons);
        journeyPlannerBar.add(fromSearcher.getSearchTool());
        journeyPlannerBar.add(toSearcher.getSearchTool());
        journeyPlannerBar.add(journeyPlannerSearchClearButtons);
        journeyPlannerBar.add(travelDescription);
        informationBar.add(journeyPlannerBar);
        setToCurrentTravelType();
    }

    /**
     * Sets up the small version of the JourneyPlanner.
     * Specifies relations between components and sets font sizes.
     */
    public void setupSmallJourneyPlannerBar() {
        isSmallJourneyPlannerVisible = true;
        MainWindowController.getInstance().requestCanvasToggleRouteVisualization(true);
        MainWindowController.getInstance().requestCanvasRepaint();
        informationBar.setPreferredSize(new Dimension(window.getFrame().getWidth(), GlobalValue.getSmallInformationBarHeight()));
        journeyPlannerBar.setPreferredSize(new Dimension(window.getFrame().getWidth() - SMALL_JOURNEY_PLANNERBAR_WIDTH_DECREASE, GlobalValue.getSmallInformationBarHeight() - SMALL_JOURNEY_PLANNERBAR_HEIGHT_DECREASE));
        journeyPlannerTransportTypeButtons.setPreferredSize(new Dimension(SMALL_TRANSPORT_BUTTONS_WIDTH, SMALL_TRANSPORT_BUTTONS_HEIGHT));
        journeyPlannerTransportTypeButtons.applySmallerState();
        fromSearcher.getSearchTool().setPreferredSize(new Dimension(SEARCHTOOL_WIDTH, SEARCHTOOL_SMALL_HEIGHT));
        fromSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH, SMALL_SEARCHBAR_HEIGHT));
        fromSearcher.setBarBorder(SMALL_TITLE_FONT_SIZE);
        fromSearcher.getSearchTool().getField().setFont(new Font("Verdana", Font.PLAIN, SMALL_SEARCH_FONT));
        toSearcher.getSearchTool().setPreferredSize(new Dimension(SEARCHTOOL_WIDTH, SEARCHTOOL_SMALL_HEIGHT));
        toSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH, SMALL_SEARCHBAR_HEIGHT));
        toSearcher.setBarBorder(SMALL_TITLE_FONT_SIZE);
        toSearcher.getSearchTool().getField().setFont(new Font("Verdana", Font.PLAIN, SMALL_SEARCH_FONT));
        journeyPlannerSearchClearButtons.setPreferredSize(new Dimension(SMALL_CLEAR_SEARCH_BUTTONS_WIDTH, SMALL_CLEAR_SEARCH_BUTTONS_HEIGHT));
        journeyPlannerSearchClearButtons.applySmallState();
        descriptionButton.setPreferredSize(new Dimension(DESCRIPTION_BUTTON_WIDTH,DESCRIPTION_BUTTON_HEIGHT));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerBar, DISTANCE_BETWEEN_JOURNEY_PLANNERBAR_AND_SMALL_INFORMATIONBAR, SpringLayout.NORTH, informationBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerTransportTypeButtons, NORTH_DISTANCE_SMALL_JOURNEY_PLANNERBAR_TRANSPORT_BUTTONS, SpringLayout.NORTH, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.WEST, journeyPlannerTransportTypeButtons, WEST_DISTANCE_SMALL_JOURNEY_PLANNERBAR_TRANSPORT_BUTTONS, SpringLayout.WEST, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, fromSearcher.getSearchTool(), SMALL_NORTH_DISTANCE_BETWEEN_FROM_SEARCHBAR_TO_JOURNEYPLANNERBAR, SpringLayout.NORTH, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.WEST, fromSearcher.getSearchTool(), SMALL_VERTICAL_DISTANCE_BETWEEN_TRANSPORTBUTTONS_AND_SEARCHBARS, SpringLayout.EAST, journeyPlannerTransportTypeButtons);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, toSearcher.getSearchTool(), SMALL_VERTICAL_DISTANCE_BETWEEN_TRANSPORTBUTTONS_AND_SEARCHBARS, SpringLayout.SOUTH, fromSearcher.getSearchTool());
        journeyPlannerBarLayout.putConstraint(SpringLayout.WEST, toSearcher.getSearchTool(), SMALL_VERTICAL_DISTANCE_BETWEEN_TRANSPORTBUTTONS_AND_SEARCHBARS, SpringLayout.EAST, journeyPlannerTransportTypeButtons);

        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerSearchClearButtons, SMALL_NORTH_DISTANCE_BETWEEN_CLEARSEARCH_BUTTONS_AND_JOURNEYPLANNERBAR, SpringLayout.NORTH, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.WEST, journeyPlannerSearchClearButtons, SMALL_VERTICAL_DISTANCE_BETWEEN_CLEARSEARCH_BUTTONS_AND_SEARCHBARS, SpringLayout.EAST, fromSearcher.getSearchTool());
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, descriptionButton, DESCRIPTION_BUTTON_NORTH_DISTANCE, SpringLayout.NORTH, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.EAST, descriptionButton, DESCRIPTION_BUTTON_EAST_DISTANCE , SpringLayout.EAST, journeyPlannerBar);
        themeHasChanged();
        journeyPlannerBar.add(journeyPlannerTransportTypeButtons);
        journeyPlannerBar.add(fromSearcher.getSearchTool());
        journeyPlannerBar.add(toSearcher.getSearchTool());
        journeyPlannerBar.add(journeyPlannerSearchClearButtons);
        journeyPlannerBar.add(descriptionButton);
        informationBar.add(journeyPlannerBar);
        setToCurrentTravelType();
    }

    /**
     * Returns the informationbar to the client.
     * @return the informationbar.
     */
    public InformationBar getInformationBar() {
        return informationBar;
    }

    /**
     * Sets the active transport button to the current travel type.
     */
    private void setToCurrentTravelType() {
        switch (type) {
            case WALK:
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));
                break;
            case BICYCLE:
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolActivated"));
                break;
            case VEHICLE:
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolActivated"));
                break;
        }
    }

    /**
     * Lets the client know if the large version of the JourneyPlanner is visible.
     * @return is the large JourneyPlanner visible.
     */
    public boolean isLargeJourneyPlannerVisible() {
        return isLargeJourneyPlannerVisible;
    }


    /**
     * Lets the client know if the small version of the JourneyPlanner is visible.
     * @return is the small JourneyPlanner visible.
     */
    public boolean isSmallJourneyPlannerVisible() {
        return isSmallJourneyPlannerVisible;
    }

    /**
     * Adapts the JourneyPlanner to the size of the window.
     * Adjusts bounds and sizes.
     */
    public void resizeEvent() {
        if(isLargeJourneyPlannerVisible) {
            informationBar.setBounds(0,0,GlobalValue.getLargeInformationBarWidth(), window.getFrame().getHeight());
            int journeyPlannerBarHeight = window.getFrame().getHeight() - JOURNEY_PLANNERBAR_HEIGHT_DECREASE;
            journeyPlannerBar.setPreferredSize(new Dimension(JOURNEY_PLANNERBAR_WIDTH, journeyPlannerBarHeight));
            int journeyPlannerDescriptionFieldHeight = journeyPlannerBarHeight - JOURNEY_PLANNER_DESCRIPTION_FIELD_HEIGHT_DECREASE;
            travelDescription.setPreferredSize(new Dimension(JOURNEY_PLANNER_DESCRIPTION_FIELD_WIDTH, journeyPlannerDescriptionFieldHeight));
        } else if(isSmallJourneyPlannerVisible) {
            informationBar.setBounds(0, window.getFrame().getHeight() - GlobalValue.getSmallInformationBarHeight(), window.getFrame().getWidth(), window.getFrame().getHeight());
            journeyPlannerBar.setPreferredSize(new Dimension(window.getFrame().getWidth() - SMALL_JOURNEY_PLANNERBAR_WIDTH_DECREASE, GlobalValue.getSmallInformationBarHeight() - SMALL_JOURNEY_PLANNERBAR_HEIGHT_DECREASE));
            if(isDescriptionFieldOpen) {
                travelDescription.setBounds(window.getFrame().getWidth() - 300, window.getFrame().getHeight() - GlobalValue.getSmallInformationBarHeight() - 400, 300, 400);
            }
        }
        if(loadWindow != null) calculateLoadingScreenPosition();
    }

    /**
     * Changes the colors of the JourneyPlanner and Informationbar to reflect the
     * current theme.
     */
    public void themeHasChanged() {
        informationBar.applyTheme();
        journeyPlannerTransportTypeButtons.applyTheme();
        journeyPlannerBar.applyTheme();
        journeyPlannerSearchClearButtons.applyTheme();
        travelDescription.applyTheme();
        fromSearcher.themeHasChanged();
        toSearcher.themeHasChanged();
        if(descriptionButton != null) descriptionButton.setBackground(ThemeHelper.color("toolbar"));
        if(descriptionButton != null) {
            if(isSearch) descriptionButton.setForeground(ThemeHelper.color("icon"));
            else descriptionButton.setForeground(ThemeHelper.color("inactiveButton"));

            if(isDescriptionFieldOpen) descriptionButton.setForeground(ThemeHelper.color("toolActivated"));
        }
        switch (type) {
            case WALK:
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));
                break;
            case BICYCLE:
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolActivated"));
                break;
            case VEHICLE:
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolActivated"));
                break;
        }
    }

    /**
     * Adds an interactionHandler to the description button.
     * The interactionHandler is a MouseAdapter that registers mouse events
     * and triggers actions.
     */
    public void addInteractionHandlerToDescriptionButton() {
        descriptionButton.addMouseListener(new MouseAdapter() {

            /**
             * Handles the mouse clicked event.
             * If no search has been made previously, or the last search
             * has been cleared, this method call is ignored.
             * Activates the description text field in the small JourneyPlanner.
             * Changes the relevant colors.
             * @param e the mouse clicked event.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(isSearch) {
                    if (!isDescriptionFieldOpen) {
                        descriptionButton.setForeground(ThemeHelper.color("toolActivated"));
                        descriptionActivationEvent();
                    } else {
                        descriptionButton.setForeground(ThemeHelper.color("icon"));
                        descriptionDeactivationEvent();
                    }
                }
            }

            /**
             * Handles MouseEntered events.
             * If no search has been made previously, or the last search
             * has been cleared, this method call is ignored.
             * Changes the relevant colors.
             * @param e the mouse entered event.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(isSearch) descriptionButton.setForeground(ThemeHelper.color("toolHover"));
            }

            /**
             * Handles MouseExited events.
             * If no search has been made previously, or the last search
             * has been cleared, this method call is ignored.
             * Changes the relevant colors.
             * @param e the mouse exited event.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(isSearch) {
                    if(isDescriptionFieldOpen)  descriptionButton.setForeground(ThemeHelper.color("toolActivated"));
                    else descriptionButton.setForeground(ThemeHelper.color("icon"));
                }
            }

            /**
             * Handles MousePressed events.
             * If no search has been made previously, or the last search
             * has been cleared, this method call is ignored.
             * Changes the relevant colors.
             * @param e the mouse pressed event.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(isSearch) descriptionButton.setForeground(ThemeHelper.color("toolActivated"));
            }

            /**
             * Handles MouseReleased events.
             * If no search has been made previously, or the last search
             * has been cleared, this method call is ignored.
             * Changes the relevant colors.
             * @param e the mouse released event.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(isSearch) descriptionButton.setForeground(ThemeHelper.color("icon"));
            }
        });
    }

    /**
     * Opens the description text field in the small version of the JourneyPlanner.
     */
    private void descriptionActivationEvent() {
        if(!isSmallJourneyPlannerVisible) return;
            travelDescription.setPreferredSize(new Dimension(300, 400));
            if(!OSDetector.isWindows())travelDescription.setBounds(window.getFrame().getWidth() - 300, window.getFrame().getHeight() - GlobalValue.getSmallInformationBarHeight() - 400, 300, 400);
            else travelDescription.setBounds(window.getFrame().getWidth() - 310, window.getFrame().getHeight() - GlobalValue.getSmallInformationBarHeight() - 400, 300, 400);
            window.getFrame().getLayeredPane().add(travelDescription, new Integer(6));
            isDescriptionFieldOpen = true;
    }

    /**
     * Closes the description text field of the small version of the JourneyPlanner.
     */
    private void descriptionDeactivationEvent() {
        window.getFrame().getLayeredPane().remove(travelDescription);
        isDescriptionFieldOpen = false;
        MainWindowController.getInstance().requestCanvasRepaint();
    }

    /**
     * Used to specify that a valid search has been made.
     */
    private void searchInitialised() {
        isSearch = true;
        descriptionButton.setForeground(ThemeHelper.color("icon"));
    }

    /**
     * Used to specify that no valid search has been made.
     */
    private void noSearchInitialised() {
        isSearch = false;
        descriptionButton.setForeground(ThemeHelper.color("inactiveButton"));
    }


    /**
     * Prints the route description in the description text field.
     */
    public void printRouteDescription() {
        java.util.List<String> route = JourneyPlannerBarController.getInstance().getRouteDescription();
        if (route == null) return;
        travelDescription.addLine(route.get(route.size()-1));
        for (int i = 0; i < route.size()-1; i++) {
            travelDescription.addLine(route.get(i));
        }
        travelDescription.getField().setCaretPosition(0);
    }

    /**
     * Retrieves a route description from the RoadGraphFactory and
     * gives the list of strings the correct format.
     * @return the formatted description of a route.
     */
    private java.util.List<String> getRouteDescription() {
        RoadGraphFactory factory = Model.getInstance().getGraphFactory();
        java.util.List<String> description = new ArrayList<>();
        java.util.List<RoadEdge> route = factory.getRoute();
        if (route != null ) {
            if (route.size() != 0) {
                RoadEdge last = route.get(0), next;
                float time = last.getTime();
                float distance = last.getLength();
                for (int i = 1; i < route.size(); i++) {
                    next = route.get(i);
                    int compare = last.compareToRoad(next);
                    if (compare != 0){
                        if (distance == 0) description.add(last.describe(last.getLength()));
                        else description.add(last.describe(distance));
                        distance = 0;
                        String name = next.getName();
                        if (name.equals("")) name = next.getRoadType().name();
                        if (compare == -1) {
                            description.add("Turn to the left unto "+name);
                        } else if (compare == 1) {
                            description.add("Turn to the right unto "+name);
                        }
                    } else {
                        distance += next.getLength();
                        if (i == route.size()-1) {
                            description.add(last.describe(distance));
                        }
                    }
                    time += last.getTime();
                    last = next;
                }
                time /= 60;
                time = Math.round(time);
                description.add("Total travel time: "+(int)time+" minutes");
            } else {
                description.add("No route was found...");
            }
            return description;
        }
        return null;
    }

    /**
     * Adds an interactionHandler to the search and clear buttons. The
     * interactionHandler is a MouseAdapter.
     */
    private void addInteractionHandlerToClearSearchButtons() {
        journeyPlannerSearchClearButtons.getClearButton().addMouseListener(new MouseAdapter() {

            /**
             * Handles MouseClicked events on the clear button.
             * Checks whether a search is already underway, if so this method call is ignored.
             * Resets all relevant components and changes the colors of relevant components.
             * @param e the mouse clicked event.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if(searchUnderway) return;
                super.mouseClicked(e);
                fromSearcher.getSearchTool().getField().getEditor().setItem("");
                toSearcher.getSearchTool().getField().getEditor().setItem("");
                fromSearcher.setCurrentQuery("");
                toSearcher.setCurrentQuery("");
                travelDescription.getField().setText("");
                MainWindowController.getInstance().requestCanvasResetRoute();
                descriptionButton.setForeground(ThemeHelper.color("inactiveButton"));
                if(isDescriptionFieldOpen && isSmallJourneyPlannerVisible) descriptionDeactivationEvent();
                noSearchInitialised();
                MainWindowController.getInstance().requestCanvasRepaint();
            }

            /**
             * Handles MouseEntered events on the clear button.
             * Changes the color of the clear button.
             * @param e the mouse entered event.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("toolHover"));
            }

            /**
             * Handles MouseExited events on the clear button.
             * Changes the color of the clear button.
             * @param e the mouse exited event.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("icon"));
            }

            /**
             * Handles MousePressed events on the clear button.
             * Changes the color of the clear button.
             * @param e the mouse pressed event.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("toolActivated"));
            }

            /**
             * Handles MouseReleased events on the clear button.
             * Changes the color of the clear button.
             * @param e the mouse released event.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("icon"));
            }
        });
        journeyPlannerSearchClearButtons.getSearchButton().addMouseListener(new MouseAdapter() {
            /**
             * Handles MouseClicked events on the search button.
             * Checks whether a search is already underway, if so this method call is ignored.
             * Initialises a new search for a route.
             * Uses a SwingWorker to handle the task in the background.
             * @param e the mouse clicked event.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if(GlobalValue.isLoading()) {
                    PopupWindow.infoBox(null, "Please Wait for the Current Load Process to Finish Before Searching for a Route!", "Loading in Progress");
                    return;
                }
                if(GlobalValue.isSaving()) {
                    PopupWindow.infoBox(null, "Please Wait for the Current Save Process to Finish Before Searching for a Route!", "Saving in Progress");
                    return;
                }
                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("toolActivated"));
                if(!searchUnderway) {
                    super.mouseClicked(e);
                    fromSearcher.getSearchTool().getField().setFocusable(false);
                    toSearcher.getSearchTool().getField().setFocusable(false);
                    searchActivatedEvent();
                    informationBar.grabFocus();
                    if (isSearch) {
                        journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("toolActivated"));
                        travelDescription.getField().setText("");
                        if (isSmallJourneyPlannerVisible) descriptionButton.setForeground(ThemeHelper.color("icon"));
                        SwingWorker worker = new SwingWorker() {
                            @Override
                            protected Object doInBackground() throws Exception {
                                activateNewRouteLoadingScreen();
                                searchUnderway = true;
                                factory = Model.getInstance().getGraphFactory();

                                Road start = MainWindowController.getInstance().requestCalculateNearestNeighbour((float) fromPoint.getX(), (float) fromPoint.getY(), type);
                                Road end = MainWindowController.getInstance().requestCalculateNearestNeighbour((float) toPoint.getX(), (float) toPoint.getY(), type);

                                dijk = new RouteSearch.RouteDijkstra(
                                        factory.getGraph(), start.getNearestPoint(fromPoint), end.getNearestPoint(toPoint), type);
                                MainWindowController.getInstance().requestCanvasRepaint();
                                return "Done";
                            }

                            @Override
                            protected void done() {
                                if (dijk.path() == null) {
                                    PopupWindow.infoBox(null, "No Route Found Between " + fromSearcher.getSearchTool().getText() + " and " + toSearcher.getSearchTool().getText() + "!", "No Route Found");
                                    searchUnderway = false;
                                    noSearchInitialised();
                                    journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
                                    fromSearcher.getSearchTool().getField().setFocusable(true);
                                    toSearcher.getSearchTool().getField().setFocusable(true);
                                    deactivateNewRouteLoadingScreen();
                                    return;
                                }

                                factory.setRoute(dijk.path());
                                MainWindowController.getInstance().requestCanvasResetRoute();
                                MainWindowController.getInstance().requestCanvasResetLocationMarker();
                                MainWindowController.getInstance().requestCanvasSetRoute(dijk.path());
                                MainWindowController.getInstance().requestCanvasUpateToAndFrom(toPoint, fromPoint);
                                printRouteDescription();
                                MainWindowController.getInstance().requestCanvasRepaint();
                                searchUnderway = false;
                                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
                                fromSearcher.getSearchTool().getField().setFocusable(true);
                                toSearcher.getSearchTool().getField().setFocusable(true);
                                deactivateNewRouteLoadingScreen();
                            }
                        };
                        worker.execute();
                        worker = null;
                    } else {
                        fromSearcher.getSearchTool().getField().setFocusable(true);
                        toSearcher.getSearchTool().getField().setFocusable(true);
                    }
                } else {
                    fromSearcher.getSearchTool().getField().setFocusable(true);
                    toSearcher.getSearchTool().getField().setFocusable(true);
                }
            }

            /**
             * Handles MouseExited events on the search button.
             * Checks whether a search is already underway, if so this method call is ignored.
             * Changes the color of the search button.
             * @param e the mouse exited event.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(!searchUnderway) journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
            }

            /**
             * Handles MouseEntered events on the search button.
             * Checks whether a search is already underway, if so this method call is ignored.
             * Changes the color of the search button.
             * @param e the mouse entered event.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseMoved(e);
                if(!searchUnderway) journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("toolHover"));
            }

            /**
             * Handles MousePressed events on the search button.
             * Checks whether a search is already underway, if so this method call is ignored.
             * Changes the color of the search button.
             * @param e the mouse pressed event.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("toolActivated"));
            }

            /**
             * Handles MouseReleased events on the search button.
             * Checks whether a search is already underway, if so this method call is ignored.
             * Changes the color of the search button.
             * @param e the mouse released event.
             */
            @Override
            public void mouseReleased(MouseEvent e) {
                if(!searchUnderway) journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
            }
        });
    }

    /**
     * Adds an interactionHandler to the transport type buttons.
     * The interactionHandler is a MouseAdapter.
     */
    private void addInteractionHandlersToJourneyPlannerTransportButtons() {
        journeyPlannerTransportTypeButtons.getOnFootButton().addMouseListener(new MouseAdapter() {
            /**
             * Handles MouseClicked events on the walk button.
             * Changes colors and sets the transportation type to walk.
             * @param e the mouse clicked event.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if(searchUnderway) return;
                super.mouseClicked(e);
                type = TravelType.WALK;
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
            }
            /**
             * Handles MouseEntered events on the walk button.
             * Changes colors.
             * @param e the mouse entered event.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolHover"));
            }

            /**
             * Handles MouseExited events on the walk button.
             * Changes colors.
             * @param e the mouse exited event.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(type != TravelType.WALK) journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
                else journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));
            }

            /**
             * Handles MousePressed events on the walk button.
             * Changes colors.
             * @param e the mouse pressed event.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if(searchUnderway) return;
                super.mousePressed(e);
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));
            }

        });
        journeyPlannerTransportTypeButtons.getBicycleButton().addMouseListener(new MouseAdapter() {
            /**
             * Handles MouseClicked events on the bicycle button.
             * Changes colors and sets the transportation type to bicycle.
             * @param e the mouse clicked event.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if(searchUnderway) return;
                super.mouseClicked(e);
                type = TravelType.BICYCLE;
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolActivated"));
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
            }

            /**
             * Handles MouseEntered events on the bicycle button.
             * Changes colors.
             * @param e the mouse entered event.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolHover"));
            }

            /**
             * Handles MouseExited events on the bicycle button.
             * Changes colors.
             * @param e the mouse exited event.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(type != TravelType.BICYCLE) journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
                else journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolActivated"));
            }

            /**
             * Handles MousePressed events on the bicycle button.
             * Changes colors.
             * @param e the mouse pressed event.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if(searchUnderway) return;
                super.mousePressed(e);
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolActivated"));
            }
        });
        journeyPlannerTransportTypeButtons.getCarButton().addMouseListener(new MouseAdapter() {
            /**
             * Handles MouseClicked events on the car button.
             * Changes colors and sets the transportation type to car.
             * @param e the mouse clicked event.
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (searchUnderway) return;
                super.mouseClicked(e);
                type = TravelType.VEHICLE;
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolActivated"));
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
            }

            /**
             * Handles MouseEntered events on the car button.
             * Changes colors.
             * @param e the mouse entered event.
             */
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolHover"));
            }

            /**
             * Handles MouseExited events on the car button.
             * Changes colors.
             * @param e the mouse exited event.
             */
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(type != TravelType.VEHICLE) journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
                else journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolActivated"));
            }

            /**
             * Handles MousePressed events on the car button.
             * Changes colors.
             * @param e the mouse pressed event.
             */
            @Override
            public void mousePressed(MouseEvent e) {
                if(searchUnderway) return;
                super.mousePressed(e);
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolActivated"));
            }
        });
    }

    /**
     * Clears the JourneyPlanner and informationbar.
     * Removes all layout specifications and removes all components.
     */
    public void clearJourneyPlannerBar() {
        if (isDescriptionFieldOpen) descriptionDeactivationEvent();
        isLargeJourneyPlannerVisible = false;
        isSmallJourneyPlannerVisible = false;
        MainWindowController.getInstance().requestCanvasToggleRouteVisualization(false);
        journeyPlannerBarLayout.removeLayoutComponent(journeyPlannerTransportTypeButtons);
        journeyPlannerBarLayout.removeLayoutComponent(fromSearcher.getSearchTool());
        journeyPlannerBarLayout.removeLayoutComponent(toSearcher.getSearchTool());
        journeyPlannerBarLayout.removeLayoutComponent(journeyPlannerSearchClearButtons);
        journeyPlannerBarLayout.removeLayoutComponent(travelDescription);
        journeyPlannerBarLayout.removeLayoutComponent(descriptionButton);
        journeyPlannerBar.removeAll();
        informationBarLayout.removeLayoutComponent(journeyPlannerBar);
        informationBar.removeAll();
    }

    /**
     * Called when a search is initialised. Checks whether the user
     * inputs are valid. Sets the JourneyPlanner to search mode in case
     * the input is valid, and not if that is not the case.
     */
    private void searchActivatedEvent() {
        if(fromSearcher.getSearchTool().getText().equals("")) {
            PopupWindow.infoBox(null, "Please Specify Departure Location!", "No Departure Location Selected!");
            noSearchInitialised();
            return;
        }
        fromPoint = fromSearcher.searchActivatedEvent();
        if(toSearcher.getSearchTool().getText().equals("")) {
            PopupWindow.infoBox(null, "Please Specify End Destination!", "No End Destination Selected");
            noSearchInitialised();
            return;
        }
        toPoint = toSearcher.searchActivatedEvent();
        if(toPoint != null && fromPoint != null) {
            searchInitialised();
            informationBar.grabFocus();
        }else {
            noSearchInitialised();
            PopupWindow.infoBox(null, "Could not find an address", "Mismatch");
        }
    }

    /**
     * Activates a route loading screen to be shown while a new route is calculated.
     */
    private void activateNewRouteLoadingScreen() {
        loadWindow = PopupWindow.routeLoadingScreen("Loading Route!");
        loadWindow.setAlwaysOnTop(true);
        calculateLoadingScreenPosition();
    }

    /**
     * Deactivates the route loading screen.
     */
    private void deactivateNewRouteLoadingScreen() {
        if(loadWindow != null) {
            loadWindow.setAlwaysOnTop(false);
            loadWindow.setVisible(false);
            loadWindow = null;
        }
    }

    /**
     * Calculates the position of the route loading screen.
     */
    private void calculateLoadingScreenPosition() {
        if(isLargeJourneyPlannerVisible) loadWindow.setLocation(journeyPlannerSearchClearButtons.getLocationOnScreen().x + 10, journeyPlannerSearchClearButtons.getLocationOnScreen().y + 10);
        else if(isSmallJourneyPlannerVisible) loadWindow.setLocation(journeyPlannerSearchClearButtons.getLocationOnScreen().x + 10, journeyPlannerSearchClearButtons.getLocationOnScreen().y - 40);
    }

    /**
     * Handles a window hidden event. Removes the loading screen always on top requirement.
     * Can also be used when the window loses focus.
     */
    public void hiddenEvent() {
        if(loadWindow != null) {
            loadWindow.setAlwaysOnTop(false);
        }
    }

    /**
     * Handles a window moved event. Re-calculates loading screen position.
     */
    public void movedEvent() {
        if(loadWindow != null) calculateLoadingScreenPosition();
    }

    /**
     * Handles a window shown event. Sets the loading screen to be always on top.
     * Can also be used when the window gains focus.
     */
    public void shownEvent() {
        if(loadWindow != null) {
            calculateLoadingScreenPosition();
            loadWindow.setAlwaysOnTop(true);
        }
    }

    /**
     * Lets the client know if a search is underway.
     * @return is a search underway.
     */
    public boolean isSearchUnderway() {
        return searchUnderway;
    }

    /**
     * The informationBarInteractionHandler is a MouseAdapter that deals with
     * all mouse events related to the informationBar.
     */
    private class InformationBarInteractionHandler extends MouseAdapter {

        /**
         * Handles all mouse clicked events on the informationbar.
         * Closes the Menu tool popup and all search tool popup lists.
         * @param e the mouse clicked event.
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
            MainWindowController.getInstance().requestSearchToolCloseList();
            fromSearcher.closeSearchToolList();
            toSearcher.closeSearchToolList();
        }

        /**
         * Handles mouse entered events.
         * Tranfers focus to the informationbar if no search list popups are visible.
         * @param e the mouse entered event
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            if(!fromSearcher.doesSearchbarHaveFocus() && !toSearcher.doesSearchbarHaveFocus() && !MainWindowController.getInstance().doesSearchToolHaveFocus()) informationBar.grabFocus();
        }
    }

    /**
     * Lets the client know whether one of the search fields has a visible popup.
     * @return is a search field popup visible.
     */
    public boolean isASearchListOpen() {
        return fromSearcher.doesSearchbarHaveFocus() || toSearcher.doesSearchbarHaveFocus();
    }

    /**
     * closes the search field popups.
     */
    public void closeSearchLists() {
        fromSearcher.closeSearchToolList();
        toSearcher.closeSearchToolList();
    }

    /**
     * The ToFromController controls the a search field and the input given
     * in it.
     */
    private class ToFromController extends SearchController {

        private String title;

        /**
         * Sets up a search tool. Adds mouse listener to it, specifies look and feel and adds a
         * focus listener to it.
         */
        @Override
        protected void setupSearchTool() {
            searchTool = new SearchTool();
            searchTool.setOpaque(true);
            searchTool.getField().getEditor().getEditorComponent().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (MainWindowController.getInstance().isMenuToolPopupVisible())
                        MainWindowController.getInstance().requestMenuToolHidePopup();
                    MainWindowController.getInstance().requestSearchToolCloseList();
                }
            });
            searchTool.getField().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
                /**
                 * Handles FocusGained events.
                 * Initiates matching results, enables allow search mode and sets the text to currentQuery.
                 * @param e the focus gained event.
                 */
                @Override
                public void focusGained(FocusEvent e) {
                    super.focusGained(e);
                    showMatchingResults();
                    searchTool.setText(currentQuery);
                    allowSearch = true;
                }

                /**
                 * Handles FocusLost events.
                 * sets the currentquery to the text in the search field.
                 * Disables allow search and hides search popup list.
                 * @param e the focus lost event.
                 */
                @Override
                public void focusLost(FocusEvent e) {
                    super.focusLost(e);
                    currentQuery = searchTool.getText();
                    allowSearch = false;
                    isFirstDownAction = true;
                    searchTool.getField().hidePopup();
                }
            });
        }

        /**
         * Changes the colors of the search field to match the current theme.
         */
        @Override
        protected void themeHasChanged() {
            searchTool.applyTheme();
            searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
            if(isLargeJourneyPlannerVisible) searchTool.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(searchTool.getFont().getName(), searchTool.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
            else if(isSmallJourneyPlannerVisible) searchTool.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(searchTool.getFont().getName(), searchTool.getFont().getStyle(), SMALL_TITLE_FONT_SIZE), ThemeHelper.color("icon")));
        }

        /**
         * Closes the search fields popup list.
         */
        @Override
        public void closeSearchToolList() {
            if(searchTool.getField().isPopupVisible()) {
                allowSearch = false;
                searchTool.getField().hidePopup();
            }
        }

        /**
         * Specifies the keybindings for the searchfield.
         * Adjusts the size of the search list based on matching items.
         */
        @Override
        protected void specifyKeyBindings() {
            searchTool.getField().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    super.keyReleased(e);
                    if (checkForProhibitedKey(e)) {
                        return;
                    }
                    if(OSDetector.isMac()) {
                        if (searchTool.getField().isPopupVisible()) {
                            if (e.getKeyCode() == KeyEvent.VK_UP) {
                                if(searchTool.getField().getSelectedIndex() == 0) isFirstDownAction = true;
                                if (searchTool.getField().getSelectedIndex() > 0) {
                                    searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() - 1);
                                    return;
                                } else return;
                            }
                            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                if (searchTool.getField().getSelectedIndex() < searchTool.getField().getModel().getSize() - 1) {
                                    if(isFirstDownAction){
                                        searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() + 1);
                                        searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() - 1);
                                        isFirstDownAction = false;
                                    }else {
                                        searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() + 1);
                                    }
                                    return;
                                } else return;
                            }
                        }
                    }
                    isFirstDownAction = true;
                    if (e.getKeyChar() != KeyEvent.VK_ENTER && e.getKeyChar() != KeyEvent.VK_ESCAPE && searchTool.getText().length() > 0) {
                        if(queryTimer == null) {
                            queryTimer = new Timer(QUERY_DELAY, ae -> {
                                queryTimer.stop();
                                queryTimer = null;
                                currentQuery = searchTool.getText();
                                if(!currentQuery.equals("")) showMatchingResults();
                                if(currentQuery != null) searchTool.setText(currentQuery);
                                if(searchTool.getField().getModel().getSize() < 8) {
                                    searchTool.getField().setMaximumRowCount(searchTool.getField().getModel().getSize());
                                } else {
                                    searchTool.getField().setMaximumRowCount(8);
                                }
                                if(searchTool.getField().getModel() == null || searchTool.getField().getModel().getSize() == 0) searchTool.getField().hidePopup();
                            });
                            queryTimer.start();
                        } else queryTimer.restart();
                    }
                    if (searchTool.getText().isEmpty()) {
                        searchTool.getField().hidePopup();
                    }
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    if(OSDetector.isMac()) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) return;
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) return;
                    }
                }
            });
        }

        /**
         * Returns the search tool to the client.
         * @return the search tool.
         */
        public SearchTool getSearchTool() {
            return searchTool;
        }

        /**
         * Sets the title to be used in the titledborder.
         * @param title the title of the titledborder.
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * Sets the border of the searchTool with a given font size.
         * @param fontSize the size of the font of the titleborder.
         */
        public void setBarBorder(int fontSize) {
            Font font = new Font("Verdana", Font.PLAIN, fontSize);
            searchTool.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, font, ThemeHelper.color("icon")));
        }
    }
}
