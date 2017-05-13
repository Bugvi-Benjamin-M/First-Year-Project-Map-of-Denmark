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
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 03/05/2017
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
    private final int SEARCHTOOL_SMALL_HEIGHT = 50;

    private final int DESCRIPTION_BUTTON_WIDTH = 60;
    private final int DESCRIPTION_BUTTON_HEIGHT = 60;

    private final int SMALL_CLEAR_SEARCH_BUTTONS_WIDTH = CLEAR_SEARCH_BUTTONS_WIDTH-85;
    private final int SMALL_CLEAR_SEARCH_BUTTONS_HEIGHT = CLEAR_SEARCH_BUTTONS_HEIGHT-16;

    private final int SMALL_VERTICAL_DISTANCE_BETWEEN_TRANSPORTBUTTONS_AND_SEARCHBARS = 5;

    private final int SMALL_NORTH_DISTANCE_BETWEEN_CLEARSEARCH_BUTTONS_AND_JOURNEYPLANNERBAR = 33;
    private final int SMALL_VERTICAL_DISTANCE_BETWEEN_CLEARSEARCH_BUTTONS_AND_SEARCHBARS = 5;

    private final int DESCRIPTION_BUTTON_NORTH_DISTANCE = 25;
    private final int DESCRIPTION_BUTTON_EAST_DISTANCE = -20;

    private final int LARGE_SEARCH_FONT = 17;
    private final int SMALL_SEARCH_FONT = 11;

    private final int DESCRIPTION_BUTTON_FONT_SIZE = 40;
    private boolean isFirstDownAction = true;



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

    private JourneyPlannerBarController() {
        super();
        fromSearcher = new ToFromController();
        toSearcher = new ToFromController();
    }

    public static JourneyPlannerBarController getInstance() {
        if(instance == null) {
            instance = new JourneyPlannerBarController();
        }
        return instance;
    }


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
    }

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
        type = TravelType.WALK;
    }

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

    public void setupSmallJourneyPlannerBar() {
        isSmallJourneyPlannerVisible = true;
        MainWindowController.getInstance().requestCanvasToggleRouteVisualization(true);
        MainWindowController.getInstance().requestCanvasRepaint();
        informationBar.setPreferredSize(new Dimension(window.getFrame().getWidth(), GlobalValue.getSmallInformationBarHeight()));
        journeyPlannerBar.setPreferredSize(new Dimension(window.getFrame().getWidth() - SMALL_JOURNEY_PLANNERBAR_WIDTH_DECREASE, GlobalValue.getSmallInformationBarHeight() - SMALL_JOURNEY_PLANNERBAR_HEIGHT_DECREASE));
        journeyPlannerTransportTypeButtons.setPreferredSize(new Dimension(SMALL_TRANSPORT_BUTTONS_WIDTH, SMALL_TRANSPORT_BUTTONS_HEIGHT));
        journeyPlannerTransportTypeButtons.applySmallerState();
        fromSearcher.getSearchTool().setPreferredSize(new Dimension(SEARCHTOOL_WIDTH, SEARCHTOOL_SMALL_HEIGHT));
        fromSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH,SEARCHBAR_HEIGHT));
        fromSearcher.setBarBorder(SMALL_TITLE_FONT_SIZE);
        fromSearcher.getSearchTool().getField().setFont(new Font("Verdana", Font.PLAIN, SMALL_SEARCH_FONT));
        toSearcher.getSearchTool().setPreferredSize(new Dimension(SEARCHTOOL_WIDTH, SEARCHTOOL_SMALL_HEIGHT));
        toSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH,SEARCHBAR_HEIGHT));
        toSearcher.setBarBorder(SMALL_TITLE_FONT_SIZE);
        toSearcher.getSearchTool().getField().setFont(new Font("Verdana", Font.PLAIN, SMALL_SEARCH_FONT));
        journeyPlannerSearchClearButtons.setPreferredSize(new Dimension(SMALL_CLEAR_SEARCH_BUTTONS_WIDTH, SMALL_CLEAR_SEARCH_BUTTONS_HEIGHT));
        journeyPlannerSearchClearButtons.applySmallState();
        descriptionButton.setPreferredSize(new Dimension(DESCRIPTION_BUTTON_WIDTH,DESCRIPTION_BUTTON_HEIGHT));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerBar, DISTANCE_BETWEEN_JOURNEY_PLANNERBAR_AND_SMALL_INFORMATIONBAR, SpringLayout.NORTH, informationBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerTransportTypeButtons, NORTH_DISTANCE_SMALL_JOURNEY_PLANNERBAR_TRANSPORT_BUTTONS, SpringLayout.NORTH, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.WEST, journeyPlannerTransportTypeButtons, WEST_DISTANCE_SMALL_JOURNEY_PLANNERBAR_TRANSPORT_BUTTONS, SpringLayout.WEST, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, fromSearcher.getSearchTool(), 0, SpringLayout.NORTH, journeyPlannerBar);
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

    public InformationBar getInformationBar() {
        return informationBar;
    }

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

    public void addInteractionHandlerToDescriptionButton() {
        descriptionButton.addMouseListener(new MouseAdapter() {
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

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                if(isSearch) descriptionButton.setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(isSearch) {
                    if(isDescriptionFieldOpen)  descriptionButton.setForeground(ThemeHelper.color("toolActivated"));
                    else descriptionButton.setForeground(ThemeHelper.color("icon"));
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(isSearch) descriptionButton.setForeground(ThemeHelper.color("toolActivated"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if(isSearch) descriptionButton.setForeground(ThemeHelper.color("icon"));
            }
        });
    }

    private void descriptionActivationEvent() {
        if(!isSmallJourneyPlannerVisible) return;
            travelDescription.setPreferredSize(new Dimension(300, 400));
            travelDescription.setBounds(window.getFrame().getWidth() - 300, window.getFrame().getHeight() - GlobalValue.getSmallInformationBarHeight() - 400, 300, 400);
            window.getFrame().getLayeredPane().add(travelDescription, new Integer(6));
            isDescriptionFieldOpen = true;
    }

    private void descriptionDeactivationEvent() {
        window.getFrame().getLayeredPane().remove(travelDescription);
        isDescriptionFieldOpen = false;
        MainWindowController.getInstance().requestCanvasRepaint();
    }

    private void searchInitialised() {
        isSearch = true;
        descriptionButton.setForeground(ThemeHelper.color("icon"));
    }

    private void noSearchInitialised() {
        isSearch = false;
        descriptionButton.setForeground(ThemeHelper.color("inactiveButton"));
    }


    public void printRouteDescription() {
        java.util.List<String> route = JourneyPlannerBarController.getInstance().getRouteDescription();
        if (route == null) return;
        travelDescription.addLine(route.get(route.size()-1));
        for (int i = 0; i < route.size()-2; i++) {
            travelDescription.addLine(route.get(i));
        }
    }

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
                        description.add(last.describe(distance));
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

    private void addInteractionHandlerToClearSearchButtons() {
        journeyPlannerSearchClearButtons.getClearButton().addMouseListener(new MouseAdapter() {
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

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("icon"));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("toolActivated"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                journeyPlannerSearchClearButtons.getClearButton().setForeground(ThemeHelper.color("icon"));
            }
        });
        journeyPlannerSearchClearButtons.getSearchButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
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
                                searchUnderway = true;
                                factory = Model.getInstance().getGraphFactory();

                                //Road start = MainWindowController.getInstance().requestCalculateNearestNeighbour((float) fromPoint.getX(), (float) fromPoint.getY());
                                //Road end = MainWindowController.getInstance().requestCalculateNearestNeighbour((float) fromPoint.getX(), (float) fromPoint.getY());
                                Road start = CanvasController.calculateNearestNeighbour((float) fromPoint.getX(), (float) fromPoint.getY());
                                Road end = CanvasController.calculateNearestNeighbour((float) toPoint.getX(), (float) toPoint.getY());

                                dijk = new RouteSearch.RouteDijkstra(
                                        factory.getGraph(), start.getNearestPoint(fromPoint), end.getNearestPoint(toPoint), type);
                                MainWindowController.getInstance().requestCanvasRepaint();
                                return "Done";
                            }

                            @Override
                            protected void done() {
                                if (dijk.path() == null) {
                                    PopupWindow.infoBox(null, "No Route Found Between " + fromSearcher.getSearchTool().getText() + " and " + toSearcher.getSearchTool().getText() + "!", "No Route Found");
                                    System.out.println("No route");
                                    informationBar.grabFocus();
                                    journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
                                    fromSearcher.getSearchTool().getField().setFocusable(true);
                                    toSearcher.getSearchTool().getField().setFocusable(true);
                                    return;
                                }

                                factory.setRoute(dijk.path());
                                //MainWindowController.getInstance().requestCanvasSetRoute(dijk.path());
                                CanvasController.getInstance().getMapCanvas().setRoute(dijk.path());

                                printRouteDescription();
                                MainWindowController.getInstance().requestCanvasRepaint();
                                searchUnderway = false;
                                informationBar.grabFocus();
                                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
                                fromSearcher.getSearchTool().getField().setFocusable(true);
                                toSearcher.getSearchTool().getField().setFocusable(true);
                            }
                        };
                        worker.execute();
                        MainWindowController.getInstance().transferFocusToMapCanvas();
                    }
                } else {
                    fromSearcher.getSearchTool().getField().setFocusable(true);
                    toSearcher.getSearchTool().getField().setFocusable(true);
                    informationBar.grabFocus();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseMoved(e);
                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("toolActivated"));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                journeyPlannerSearchClearButtons.getSearchButton().setForeground(ThemeHelper.color("icon"));
            }
        });
    }

    private void addInteractionHandlersToJourneyPlannerTransportButtons() {
        journeyPlannerTransportTypeButtons.getOnFootButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                type = TravelType.WALK;
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(type != TravelType.WALK) journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
                else journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));

            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolActivated"));
            }

        });
        journeyPlannerTransportTypeButtons.getBicycleButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                type = TravelType.BICYCLE;
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolActivated"));
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(type != TravelType.BICYCLE) journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
                else journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolActivated"));
            }
        });
        journeyPlannerTransportTypeButtons.getCarButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                type = TravelType.VEHICLE;
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolActivated"));
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                if(type != TravelType.VEHICLE) journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
                else journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolActivated"));
            }
        });
    }

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
            MainWindowController.getInstance().requestCanvasResetRoute();
            MainWindowController.getInstance().requestCanvasUpateToAndFrom(toPoint, fromPoint);
        }else {
            noSearchInitialised();
            PopupWindow.infoBox(null, "Could not find an address", "Mismatch");
        }
    }

    private class InformationBarInteractionHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
            MainWindowController.getInstance().requestSearchToolCloseList();
            fromSearcher.closeSearchToolList();
            toSearcher.closeSearchToolList();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if(!fromSearcher.doesSearchbarHaveFocus() && !toSearcher.doesSearchbarHaveFocus()) informationBar.grabFocus();
        }
    }

    public boolean isASearchListOpen() {
        return fromSearcher.doesSearchbarHaveFocus() || toSearcher.doesSearchbarHaveFocus();
    }

    public void closeSearchLists() {
        fromSearcher.closeSearchToolList();
        toSearcher.closeSearchToolList();
    }

    private class ToFromController extends SearchController {

        private String title;

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
                @Override
                public void focusGained(FocusEvent e) {
                    super.focusGained(e);
                    showMatchingResults();
                    searchTool.setText(currentQuery);
                    allowSearch = true;
                }

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

        @Override
        protected void themeHasChanged() {
            searchTool.applyTheme();
            searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
            searchTool.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(searchTool.getFont().getName(), searchTool.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
        }

        @Override
        public void closeSearchToolList() {
            if(searchTool.getField().isPopupVisible()) {
                allowSearch = false;
                searchTool.getField().hidePopup();
            }
        }

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
                                System.out.println(searchTool.getField().getSelectedIndex());
                                if(searchTool.getField().getSelectedIndex() == 0) isFirstDownAction = true;
                                if (searchTool.getField().getSelectedIndex() > 0) {
                                    searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() - 1);
                                    return;
                                } else return;
                            }
                            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                                System.out.println(searchTool.getField().getSelectedIndex());
                                if (searchTool.getField().getSelectedIndex() < searchTool.getField().getModel().getSize() - 1) {
                                    if(isFirstDownAction){
                                        searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() + 1);
                                        searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() - 1);
                                        isFirstDownAction = false;
                                        System.out.println("this works");
                                    }else {
                                        searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() + 1);
                                        System.out.println("this dosnt works");
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

        public SearchTool getSearchTool() {
            return searchTool;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBarBorder(int fontSize) {
            Font font = new Font("Verdana", Font.PLAIN, fontSize);
            searchTool.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, font, ThemeHelper.color("icon")));
        }
    }
}
