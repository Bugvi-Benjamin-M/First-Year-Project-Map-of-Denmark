package Controller;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import View.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.WEST;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public final class PointsOfInterestController extends Controller {

    private final int PROFILE_HEIGHT = 90;
    private final int LARGE_POINTS_OF_INTERESTBAR_WIDTH = 300;
    private final int BUTTONS_HEIGHT = 50;
    private final int LARGE_SCROLLBAR_HEIGHT = (int) (0.956316*Toolkit.getDefaultToolkit().getScreenSize().getHeight() + (-292.289));
    private final int DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS = GlobalValue.getToolbarHeight() + 10;
    private final int DISTANCE_BETWEEN_BUTTONS_AND_SCROLLPANE = 60;
    private final int SCROLLBAR_SPEED = 14;
    private final int SMALL_POINTS_OF_INTERESTBAR_HEIGHT = 200;
    private final int DISTANCE_FROM_LEFT_EDGE_TO_BUTTONS = 50;

    private static PointsOfInterestController instance;
    private InformationBar informationBar;
    private SpringLayout informationBarLayout;
    private PointsOfInterestBar pointsOfInterestBar;
    private JScrollPane largeScroll;
    private PointsOfInterestButtons poiButtons;

    private List<String> places;
    private List<PointProfile> panelPlaces;

    private PointsOfInterestController() { super();}

    public static PointsOfInterestController getInstance()
    {
        if (instance == null) {
            instance = new PointsOfInterestController();
        }
        return instance;
    }

    public void setupInformationBar() {
        places = new LinkedList<>();
        panelPlaces = new ArrayList<>();
        informationBar = new InformationBar();
        informationBarLayout = (SpringLayout) informationBar.getLayout();
        InformationBarInteractionHandler handler = new InformationBarInteractionHandler();
        informationBar.addMouseListener(handler);
    }

    public void setupBasePointsOfInterestBar() {
        pointsOfInterestBar = new PointsOfInterestBar();
        pointsOfInterestBar.setOpaque(true);
        poiButtons = new PointsOfInterestButtons();
        poiButtons.setOpaque(true);
        addInteractionHandlersPointsOfInterestButtons();
    }

    public void setupLargePointsOfInterestBar() {
        informationBar.setPreferredSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, window.getFrame().getHeight()));
        pointsOfInterestBar.specifyLayout(BoxLayout.PAGE_AXIS);
        pointsOfInterestBar.setMinimumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, PROFILE_HEIGHT));
        setupLargeScrollbar();
        addPointsToPointsOfInterestBar();
        //poiButtons = new PointsOfInterestButtons();
        poiButtons.setPreferredSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, BUTTONS_HEIGHT));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, poiButtons, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, poiButtons, DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS, NORTH, informationBar);
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, largeScroll, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, largeScroll, DISTANCE_BETWEEN_BUTTONS_AND_SCROLLPANE, NORTH, poiButtons);
        informationBar.add(poiButtons);
        informationBar.add(largeScroll);
        //InformationBarInteractionHandler handler = new InformationBarInteractionHandler();
        //informationBar.addMouseListener(handler);
        //addInteractionHandlersPointsOfInterestButtons();
    }

    public void setupSmallPointsOfInterestBar() {
        informationBar.setPreferredSize(new Dimension(window.getFrame().getWidth(), SMALL_POINTS_OF_INTERESTBAR_HEIGHT));
        pointsOfInterestBar.specifyLayout(BoxLayout.LINE_AXIS);
        pointsOfInterestBar.setMinimumSize(new Dimension(window.getFrame().getWidth(), SMALL_POINTS_OF_INTERESTBAR_HEIGHT));
        setupSmallScrollbar();
        addPointsToPointsOfInterestBar();
        poiButtons.setPreferredSize(new Dimension(150, BUTTONS_HEIGHT));
        informationBarLayout.putConstraint(WEST, poiButtons, DISTANCE_FROM_LEFT_EDGE_TO_BUTTONS, WEST, informationBar);
        informationBarLayout.putConstraint(NORTH, poiButtons, 75, NORTH, informationBar);
        //informationBarLayout.putConstraint(SOUTH, poiButtons, -75, SOUTH, informationBar);
        informationBar.add(poiButtons);
    }

    public void clearPointsOfInterestBar() {
        informationBarLayout.removeLayoutComponent(poiButtons);
        informationBarLayout.removeLayoutComponent(pointsOfInterestBar);
        informationBar.removeAll();
        pointsOfInterestBar.removeAll();
    }

    private void addInteractionHandlersPointsOfInterestButtons() {
        poiButtons.getNewPointButton().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!GlobalValue.isAddNewPointActive()) {
                    poiButtons.getNewPointButton().setForeground(ThemeHelper.color("toolActivated"));
                    MainWindowController.getInstance().changeCanvasMouseCursorToPoint();
                    GlobalValue.setIsAddNewPointActive(true);
                } else {
                    poiButtons.getNewPointButton().setForeground(ThemeHelper.color("icon"));
                    MainWindowController.getInstance().changeCanvasMouseCursorToNormal();
                    GlobalValue.setIsAddNewPointActive(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if(!GlobalValue.isAddNewPointActive()) poiButtons.getNewPointButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if(!GlobalValue.isAddNewPointActive()) poiButtons.getNewPointButton().setForeground(ThemeHelper.color("icon"));
            }

        });
        poiButtons.getDeleteAllButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                poiButtons.getDeleteAllButton().setForeground(ThemeHelper.color("toolActivated"));
                PopupWindow.confirmBox(null, "Do you Wish to Delete All Points of Interest?", "Confirm Deletion", JOptionPane.YES_OPTION);
                //Clear all points
                poiButtons.getDeleteAllButton().setForeground(ThemeHelper.color("icon"));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                poiButtons.getDeleteAllButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                poiButtons.getDeleteAllButton().setForeground(ThemeHelper.color("icon"));
            }
        });
    }

    private void setupLargeScrollbar() {
        largeScroll = new JScrollPane(pointsOfInterestBar);
        largeScroll.setOpaque(true);
        largeScroll.setPreferredSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, LARGE_SCROLLBAR_HEIGHT));
        largeScroll.setMinimumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, LARGE_SCROLLBAR_HEIGHT));
        largeScroll.setMaximumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, LARGE_SCROLLBAR_HEIGHT));
        largeScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        largeScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        largeScroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        largeScroll.getVerticalScrollBar().setUI(new CustomScrollbarUI());
        largeScroll.getVerticalScrollBar().setUnitIncrement(SCROLLBAR_SPEED);
    }

    private void setupSmallScrollbar() {

    }



    public void themeHasChanged() {
        largeScroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        poiButtons.applyTheme();
        informationBar.applyTheme();
        pointsOfInterestBar.applyTheme();
    }

    public InformationBar getInformationBar() {
        return informationBar;
    }

    private void addPointsToPointsOfInterestBar() {
        places.add("København");
        places.add("Århus, Det Jyske Musikkonservatorium");
        places.add("hello");
        places.add("world");
        places.add("testing");
        places.add("scrollbar");
        places.add("more");
        places.add("Jpanels");
        places.add("are");
        places.add("apparently");
        places.add("needed");
        places.add("oh lord");
        places.add("pls");
        places.add("is");
        places.add("anything");
        places.add("happening");


        for(String s : places) {
            PointProfile profile = new PointProfile(s);
            profile.addMouseListener(new PointsInteractionHandler(profile));
            profile.getDeleteButton().addMouseListener(new PointDeleteButtonInteractionHandler(profile.getDeleteButton()));
            panelPlaces.add(profile);
            //pointsOfInterestBar.addPlaceToList(profile);
            //pointsOfInterestBar.createVerticalSpace(10);
            //pointsOfInterestBar.addHorizontalGlue();
        }
        pointsOfInterestBar.setPointProfiles(panelPlaces);
        pointsOfInterestBar.revalidate();
    }

    public void repaintPointsOfInterestBar() {
        if(pointsOfInterestBar != null) pointsOfInterestBar.applyTheme();
    }

    private class PointsInteractionHandler extends MouseAdapter {

        private PointProfile point;

        public PointsInteractionHandler(PointProfile panel) {
            point = panel;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            point.setBackground(ThemeHelper.color("pointHover"));
            point.getDeleteButton().setBackground(ThemeHelper.color("pointHover"));

        }

        @Override
        public void mouseExited(MouseEvent e) {
            point.setBackground(ThemeHelper.color("toolbar"));
            point.getDeleteButton().setBackground(ThemeHelper.color("toolbar"));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //
        }

    }

    private class PointDeleteButtonInteractionHandler extends MouseAdapter {

        private PointProfile point;
        private JLabel button;

        public PointDeleteButtonInteractionHandler(JLabel button) {
            point = (PointProfile) button.getParent();
            this.button = button;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            point.setBackground(ThemeHelper.color("pointHover"));
            button.setForeground(ThemeHelper.color("toolHover"));
            button.setBackground(ThemeHelper.color("pointHover"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setForeground(ThemeHelper.color("icon"));
            button.setBackground(ThemeHelper.color("toolbar"));
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //
        }


    }

    private class InformationBarInteractionHandler extends MouseAdapter {

        @Override
        public void mouseEntered(MouseEvent e) {
            pointsOfInterestBar.applyTheme();
            informationBar.grabFocus();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            informationBar.applyTheme();
            pointsOfInterestBar.applyTheme();

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
        }

    }

}
