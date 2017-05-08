package Controller;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import View.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        //toBar = toSearcher.getSearchTool();
        toSearcher.setToolTip("Type End Destination");
        toSearcher.setTitle("To:");
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
        //fromBar.applyTheme();
        //toBar.applyTheme();
        journeyPlannerBar.applyTheme();
        //fromBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "From:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(fromBar.getFont().getName(), fromBar.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
        //fromBar.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
        //toBar.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
        //toBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "To:", TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(fromBar.getFont().getName(), fromBar.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
        journeyPlannerSearchClearButtons.applyTheme();
        travelDescription.applyTheme();
    }

    private void addInteractionHandlerToClearSearchButtons() {
        journeyPlannerSearchClearButtons.getClearButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
        });
        journeyPlannerSearchClearButtons.getSearchButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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

    private class InformationBarInteractionHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
            MainWindowController.getInstance().requestSearchToolCloseList();
        }
    }

    private class ToFromController extends SearchController {

        private String title;

        @Override
        protected void setupSearchTool() {
            searchTool = new SearchTool();
            searchTool.setOpaque(true);
        }

        @Override
        protected void themeHasChanged() {
            searchTool.applyTheme();
            searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
            searchTool.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), title, TitledBorder.LEFT, TitledBorder.ABOVE_TOP, new Font(searchTool.getFont().getName(), searchTool.getFont().getStyle(), TITLE_FONT_SIZE), ThemeHelper.color("icon")));
        }

        @Override
        public void closeSearchToolList() {

        }

        @Override
        protected void specifyKeyBindings() {

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
