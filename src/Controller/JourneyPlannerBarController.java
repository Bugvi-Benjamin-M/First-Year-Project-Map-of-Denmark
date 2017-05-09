package Controller;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import Model.Elements.RoadEdge;
import Model.Model;
import RouteSearch.RoadGraphFactory;
import View.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    //private final int JOURNEY_PLANNERBAR_HEIGHT = 810;
    private final int JOURNEY_PLANNERBAR_HEIGHT_DECREASE = 167;
    private final int TITLE_FONT_SIZE = 15;
    private final int DISTANCE_BETWEEN_SEARCHBARS = 4;
    private final int DISTANCE_BETWEEN_TOBAR_TO_CLEARSEARCH_BUTTONS = 10;
    private final int JOURNEY_PLANNER_DESCRIPTION_FIELD_WIDTH = 328;
    private final int JOURNEY_PLANNER_DESCRIPTION_FIELD_HEIGHT_DECREASE = 333;
    //private final int JOURNEY_PLANNER_DESCRIPTION_FIELD_HEIGHT = 477;
    private final int DISTANCE_BETWEEN_SEARCHCLEAR_BUTTONS_AND_DESCRIPTION_FIELD = 30;
    private final int SMALL_JOURNEY_PLANNERBAR_WIDTH_DECREASE = 20;
    private final int SMALL_JOURNEY_PLANNERBAR_HEIGHT_DECREASE = 40;
    private final int SMALL_TRANSPORT_BUTTONS_WIDTH = 150;
    private final int SMALL_TRANSPORT_BUTTONS_HEIGHT = 60;

    private InformationBar informationBar;
    private SpringLayout journeyPlannerBarLayout;
    private SpringLayout informationBarLayout;
    private JourneyPlannerTransportTypeButtons journeyPlannerTransportTypeButtons;
    private JourneyPlannerBar journeyPlannerBar;
    //private SearchTool fromBar;
    //private SearchTool toBar;
    private JourneyPlannerSearchClearButtons journeyPlannerSearchClearButtons;
    private JourneyDescriptionField travelDescription;
    private ToFromController fromSearcher;
    private ToFromController toSearcher;

    private boolean isLargeJourneyPlannerVisible;
    private boolean isSmallJourneyPlannerVisible;

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
    }

    public void setupBaseJourneyPlannerBar() {
        journeyPlannerTransportTypeButtons = new JourneyPlannerTransportTypeButtons();
        journeyPlannerTransportTypeButtons.setOpaque(true);
        journeyPlannerBar = new JourneyPlannerBar();
        journeyPlannerBar.setOpaque(true);
        journeyPlannerBarLayout = (SpringLayout) journeyPlannerBar.getLayout();
        //fromBar = fromSearcher.getSearchTool();
        fromSearcher.setToolTip("Type Departure Destination");
        fromSearcher.setTitle("From:");
        fromSearcher.specifyKeyBindings();
        //toBar = toSearcher.getSearchTool();
        toSearcher.setToolTip("Type End Destination");
        toSearcher.setTitle("To:");
        toSearcher.specifyKeyBindings();
        journeyPlannerSearchClearButtons = new JourneyPlannerSearchClearButtons();
        journeyPlannerSearchClearButtons.setOpaque(true);
        travelDescription = new JourneyDescriptionField();
        travelDescription.setOpaque(true);
        addInteractionHandlersToJourneyPlannerTransportButtons();
        addInteractionHandlerToClearSearchButtons();
    }

    public void setupLargeJourneyPlannerBar() {
        isLargeJourneyPlannerVisible = true;
        informationBar.setPreferredSize(new Dimension(GlobalValue.getLargeInformationBarWidth(), window.getFrame().getHeight()));
        int journeyPlannerBarHeight = window.getFrame().getHeight() - JOURNEY_PLANNERBAR_HEIGHT_DECREASE;
        journeyPlannerBar.setPreferredSize(new Dimension(JOURNEY_PLANNERBAR_WIDTH, journeyPlannerBarHeight));
        journeyPlannerTransportTypeButtons.setPreferredSize(new Dimension(TRANSPORT_BUTTONS_WIDTH, TRANSPORT_BUTTONS_HEIGHT));
        journeyPlannerTransportTypeButtons.applyLargeState();
        journeyPlannerSearchClearButtons.setPreferredSize(new Dimension(CLEAR_SEARCH_BUTTONS_WIDTH, CLEAR_SEARCH_BUTTONS_HEIGHT));
        fromSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH,SEARCHBAR_HEIGHT));
        fromSearcher.setBarBorder();
        //fromSearcher.getSearchTool().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "From:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(fromBar.getFont().getName(), fromBar.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
        toSearcher.getSearchTool().getField().setPreferredSize(new Dimension(SEARCHBAR_WIDTH, SEARCHBAR_HEIGHT));
        toSearcher.setBarBorder();
        //toSearcher.getSearchTool().setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "To:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(toBar.getFont().getName(), toBar.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
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
    }

    public void setupSmallJourneyPlannerBar() {
        isSmallJourneyPlannerVisible = true;
        informationBar.setPreferredSize(new Dimension(window.getFrame().getWidth(), GlobalValue.getSmallInformationBarHeight()));
        journeyPlannerBar.setPreferredSize(new Dimension(window.getFrame().getWidth() - SMALL_JOURNEY_PLANNERBAR_WIDTH_DECREASE, GlobalValue.getSmallInformationBarHeight() - SMALL_JOURNEY_PLANNERBAR_HEIGHT_DECREASE));
        journeyPlannerTransportTypeButtons.setPreferredSize(new Dimension(SMALL_TRANSPORT_BUTTONS_WIDTH, SMALL_TRANSPORT_BUTTONS_HEIGHT));
        journeyPlannerTransportTypeButtons.applySmallerState();
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, journeyPlannerBar, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerBar, 10, SpringLayout.NORTH, informationBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerTransportTypeButtons, 20, SpringLayout.NORTH, journeyPlannerBar);
        journeyPlannerBarLayout.putConstraint(SpringLayout.WEST, journeyPlannerTransportTypeButtons, 5, SpringLayout.WEST, journeyPlannerBar);
        themeHasChanged();
        journeyPlannerBar.add(journeyPlannerTransportTypeButtons);
        informationBar.add(journeyPlannerBar);
    }

    public InformationBar getInformationBar() {
        return informationBar;
    }

    public void themeHasChanged() {
        informationBar.applyTheme();
        journeyPlannerTransportTypeButtons.applyTheme();
        journeyPlannerBar.applyTheme();
        journeyPlannerSearchClearButtons.applyTheme();
        travelDescription.applyTheme();
        fromSearcher.getSearchTool().applyTheme();
        toSearcher.getSearchTool().applyTheme();
    }

    public void printRouteDescription() {
        java.util.List<String> route = JourneyPlannerBarController.getInstance().getRouteDescription();
        for (String string : route) {
            travelDescription.addLine(string);
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
                        if (compare == -1) {
                            description.add("Turn to the left unto "+next.getName());
                        } else if (compare == 1) {
                            description.add("Turn to the right unto "+next.getName());
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
                description.add("Total travel time: "+time+" secs");
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
                super.mouseClicked(e);
                fromSearcher.getSearchTool().getField().getEditor().setItem("");
                toSearcher.getSearchTool().getField().getEditor().setItem("");
                travelDescription.getField().setText("");
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
                super.mouseClicked(e);
                searchActivatedEvent();
                informationBar.grabFocus();
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
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerTransportTypeButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
            }
        });
        journeyPlannerTransportTypeButtons.getBicycleButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerTransportTypeButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
            }
        });
        journeyPlannerTransportTypeButtons.getCarButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerTransportTypeButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
            }
        });
    }

    public void clearJourneyPlannerBar() {
        isLargeJourneyPlannerVisible = false;
        isSmallJourneyPlannerVisible = false;
        journeyPlannerBarLayout.removeLayoutComponent(journeyPlannerTransportTypeButtons);
        journeyPlannerBarLayout.removeLayoutComponent(fromSearcher.getSearchTool());
        journeyPlannerBarLayout.removeLayoutComponent(toSearcher.getSearchTool());
        journeyPlannerBar.removeAll();
        informationBar.removeAll();
    }

    private void searchActivatedEvent() {
        if(fromSearcher.getSearchTool().getText().equals("")) {
            PopupWindow.infoBox(null, "Please Specify Departure Location!", "No Departure Location Selected!");
            return;
        }
        fromSearcher.searchActivatedEvent();
        if(toSearcher.getSearchTool().getText().equals("")) {
            PopupWindow.infoBox(null, "Please Specify End Destination!", "No End Destination Selected");
            return;
        }
        toSearcher.searchActivatedEvent();
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

        @Override
        public void mouseExited(MouseEvent e) {
            //Maybe set theme
        }
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
                    if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
                    MainWindowController.getInstance().requestSearchToolCloseList();
                }
            });
            searchTool.getField().getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    super.focusGained(e);
                    if(searchTool.getText().equals("")) return;
                    else {
                        showMatchingResults();
                        searchTool.getField().getEditor().selectAll();
                        searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
                        allowSearch = true;
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    currentQuery = searchTool.getText();
                    searchTool.getField().hidePopup();
                    allowSearch = false;
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
                    if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE && e.getKeyChar() != KeyEvent.VK_ENTER && e.getKeyChar() != KeyEvent.VK_ESCAPE) {
                        currentQuery = searchTool.getText();
                        showMatchingResults();
                        searchTool.getField().showPopup();
                        searchTool.setText(currentQuery);
                    }

                    if (searchTool.getText().isEmpty()) {
                        searchTool.getField().hidePopup();
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

        public void setBarBorder() {
            searchTool.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(searchTool.getFont().getName(), searchTool.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
        }
    }
}
