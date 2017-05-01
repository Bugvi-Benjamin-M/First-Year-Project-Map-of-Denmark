package Controller;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import Model.Elements.POI;
import Model.Model;
import View.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.SpringLayout.*;

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
    private final int LARGE_SCROLLBAR_HEIGHT = (int) (0.976316*Toolkit.getDefaultToolkit().getScreenSize().getHeight() + (-292.289));
    private final int DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS = GlobalValue.getToolbarHeight() + 10;
    private final int DISTANCE_BETWEEN_BUTTONS_AND_SCROLLPANE = 60;
    private final int SCROLLBAR_SPEED = 14;
    private final int SMALL_POINTS_OF_INTERESTBAR_HEIGHT = 200;
    private final int DISTANCE_FROM_LEFT_EDGE_TO_BUTTONS = 50;
    private final int DISTANCE_FROM_SMALLPOIBARTOP_TO_BUTTONS = 30;

    private static PointsOfInterestController instance;
    private InformationBar informationBar;
    private SpringLayout informationBarLayout;
    private PointsOfInterestBar pointsOfInterestBar;
    private JScrollPane largeScroll;
    private PointsOfInterestButtons poiButtons;

    private List<POI> pointsOfInterest;
    private List<PointProfile> POIpanels;

    private PointsOfInterestController() { super();}

    public static PointsOfInterestController getInstance()
    {
        if (instance == null) {
            instance = new PointsOfInterestController();
        }
        return instance;
    }

    public void setupInformationBar() {
        pointsOfInterest = new ArrayList<>();
        POIpanels = new ArrayList<>();
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
        informationBarLayout.putConstraint(NORTH, poiButtons, DISTANCE_FROM_SMALLPOIBARTOP_TO_BUTTONS, NORTH, informationBar);
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
                if(PopupWindow.confirmBox(null, "Do you Wish to Delete All Points of Interest?", "Confirm Deletion", JOptionPane.YES_OPTION) == JOptionPane.YES_OPTION) {
                    Model.getInstance().removeAllPOI();
                    POIpanels.clear();
                    pointsOfInterestBar.removeAll();
                    pointsOfInterestBar.revalidate();
                    pointsOfInterestBar.repaint();
                }
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

        if(largeScroll != null) largeScroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        if(poiButtons != null) poiButtons.applyTheme();
        if(informationBar != null) informationBar.applyTheme();
        if(pointsOfInterestBar != null) pointsOfInterestBar.applyTheme();
    }

    public InformationBar getInformationBar() {
        return informationBar;
    }

    private void addPointsToPointsOfInterestBar() {
        /*pointsOfInterest.add("København");
        pointsOfInterest.add("Århus, Det Jyske Musikkonservatorium");
        pointsOfInterest.add("hello");
        pointsOfInterest.add("world");
        pointsOfInterest.add("testing");
        pointsOfInterest.add("scrollbar");
        pointsOfInterest.add("more");
        pointsOfInterest.add("Jpanels");
        pointsOfInterest.add("are");
        pointsOfInterest.add("apparently");
        pointsOfInterest.add("needed");
        pointsOfInterest.add("oh lord");
        pointsOfInterest.add("pls");
        pointsOfInterest.add("is");
        pointsOfInterest.add("anything");
        pointsOfInterest.add("happening");*/


        for(POI poi : pointsOfInterest) {
            addPOI(poi);
            //pointsOfInterestBar.addPlaceToList(profile);
            //pointsOfInterestBar.createVerticalSpace(10);
            //pointsOfInterestBar.addHorizontalGlue();
        }
        pointsOfInterestBar.setPointProfiles(POIpanels);
        pointsOfInterestBar.revalidate();
    }

    public void addPOI(POI poi) {
        PointProfile pointProfile = new PointProfile(poi.getDescription(), poi.x, poi.y);
        pointProfile.addMouseListener(new PointsInteractionHandler(pointProfile));
        pointProfile.getDeleteButton().addMouseListener(new PointDeleteButtonInteractionHandler(pointProfile.getDeleteButton()));
        POIpanels.add(pointProfile);
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
            int index = POIpanels.indexOf(point);
            Model.getInstance().removePOI(index);
            POIpanels.remove(point);
            pointsOfInterestBar.remove(point);
            pointsOfInterestBar.revalidate();
            pointsOfInterestBar.repaint();
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
