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
import java.awt.geom.Point2D;
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
    private final int SMALL_SCROLLBAR_WIDTH = 770;
    private final int DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS = GlobalValue.getToolbarHeight() + 10;
    private final int DISTANCE_BETWEEN_BUTTONS_AND_SCROLLPANE = 60;
    private final int SCROLLBAR_SPEED = 14;
    private final int SMALL_POINTS_OF_INTERESTBAR_HEIGHT = 150;
    private final int DISTANCE_FROM_SMALLINFORMATIONBAR_LEFT_EDGE_TO_BUTTONS = 25;
    private final int DISTANCE_FROM_SMALLPOIBARTOP_TO_BUTTONS = 35;
    private final int SMALL_SCROLLBAR_HEIGHT = 100;
    private final int DISTANCE_FROM_SMALLPOIBARTOP_TO_SCROLLPANE = 10;

    private static PointsOfInterestController instance;
    private InformationBar informationBar;
    private SpringLayout informationBarLayout;
    private PointsOfInterestBar pointsOfInterestBar;
    private JScrollPane largeScroll;
    private JScrollPane smallScroll;
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
        pointsOfInterest = Model.getInstance().getPointsOfInterest();
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
        addPointsToVerticalPointsOfInterestBar();
        poiButtons.setPreferredSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, BUTTONS_HEIGHT));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, poiButtons, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, poiButtons, DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS, NORTH, informationBar);
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, largeScroll, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, largeScroll, DISTANCE_BETWEEN_BUTTONS_AND_SCROLLPANE, NORTH, poiButtons);
        informationBar.add(poiButtons);
        informationBar.add(largeScroll);
    }

    public void setupSmallPointsOfInterestBar() {
        informationBar.setPreferredSize(new Dimension(window.getFrame().getWidth(), SMALL_POINTS_OF_INTERESTBAR_HEIGHT));
        pointsOfInterestBar.specifyLayout(BoxLayout.LINE_AXIS);
        pointsOfInterestBar.setMinimumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, PROFILE_HEIGHT+10));
        setupSmallScrollbar();
        addPointsToHorizontalPointsOfInterestBar();
        poiButtons.setPreferredSize(new Dimension(150, BUTTONS_HEIGHT));
        informationBarLayout.putConstraint(WEST, poiButtons, DISTANCE_FROM_SMALLINFORMATIONBAR_LEFT_EDGE_TO_BUTTONS, WEST, informationBar);
        informationBarLayout.putConstraint(NORTH, poiButtons, DISTANCE_FROM_SMALLPOIBARTOP_TO_BUTTONS, NORTH, informationBar);
        informationBarLayout.putConstraint(NORTH, smallScroll, DISTANCE_FROM_SMALLPOIBARTOP_TO_SCROLLPANE, NORTH, informationBar);
        informationBarLayout.putConstraint(WEST, smallScroll, 30, EAST, poiButtons);
        informationBar.add(poiButtons);
        informationBar.add(smallScroll);
    }

    public void clearPointsOfInterestBar() {
        if(largeScroll != null) largeScroll.remove(pointsOfInterestBar);
        if(smallScroll != null) smallScroll.remove(pointsOfInterestBar);
        informationBarLayout.removeLayoutComponent(poiButtons);
        informationBarLayout.removeLayoutComponent(pointsOfInterestBar);
        informationBarLayout.removeLayoutComponent(largeScroll);
        informationBarLayout.removeLayoutComponent(smallScroll);
        informationBar.removeAll();
        pointsOfInterestBar.removeAll();
    }

    public void poiModeOn(){
        poiButtons.getNewPointButton().setForeground(ThemeHelper.color("toolActivated"));
        MainWindowController.getInstance().changeCanvasMouseCursorToPoint();
        GlobalValue.setIsAddNewPointActive(true);
    }

    public void poiModeOff(){
        poiButtons.getNewPointButton().setForeground(ThemeHelper.color("icon"));
        MainWindowController.getInstance().changeCanvasMouseCursorToNormal();
        GlobalValue.setIsAddNewPointActive(false);
    }

    private void addInteractionHandlersPointsOfInterestButtons() {
        poiButtons.getNewPointButton().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(!GlobalValue.isAddNewPointActive()) {
                    poiModeOn();
                } else {
                    poiModeOff();
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
        smallScroll = new JScrollPane(pointsOfInterestBar);
        smallScroll.setOpaque(true);
        smallScroll.setPreferredSize(new Dimension(SMALL_SCROLLBAR_WIDTH, SMALL_SCROLLBAR_HEIGHT+10));
        smallScroll.setMinimumSize(new Dimension(SMALL_SCROLLBAR_WIDTH, SMALL_SCROLLBAR_HEIGHT+10));
        smallScroll.setMaximumSize(new Dimension(SMALL_SCROLLBAR_WIDTH, SMALL_SCROLLBAR_HEIGHT+10));
        smallScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        smallScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        smallScroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        smallScroll.getHorizontalScrollBar().setUI(new CustomScrollbarUI());
        smallScroll.getHorizontalScrollBar().setUnitIncrement(SCROLLBAR_SPEED);
    }



    public void themeHasChanged() {

        if(largeScroll != null) largeScroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        if(smallScroll != null) smallScroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        if(poiButtons != null) poiButtons.applyTheme();
        if(informationBar != null) informationBar.applyTheme();
        if(pointsOfInterestBar != null) pointsOfInterestBar.applyTheme();
    }

    public InformationBar getInformationBar() {
        return informationBar;
    }
    //Todo figure out why elements are added multiple times
    private void addPointsToVerticalPointsOfInterestBar() {
        for(POI poi : pointsOfInterest) {
            addPOI(poi);
        }
        pointsOfInterestBar.setPointProfilesVertically(POIpanels);
        pointsOfInterestBar.revalidate();
    }

    public void addPointsToHorizontalPointsOfInterestBar() {
        for(POI poi : pointsOfInterest) {
            addPOI(poi);
        }
        pointsOfInterestBar.setPointProfilesHorizontally(POIpanels);
        pointsOfInterestBar.revalidate();
    }

    public void addPOI(POI poi) {
        PointProfile pointProfile = new PointProfile(poi.getDescription(), poi.x, poi.y);
        pointProfile.addMouseListener(new PointsInteractionHandler(pointProfile));
        pointProfile.getDeleteButton().addMouseListener(new PointDeleteButtonInteractionHandler(pointProfile.getDeleteButton()));
        POIpanels.add(pointProfile);
        pointsOfInterestBar.add(pointProfile);
    }

    public void repaintPointsOfInterestBar() {
        if(pointsOfInterestBar != null) pointsOfInterestBar.applyTheme();
    }

    public void updatePointsOfInterestBar(){
        pointsOfInterestBar.revalidate();
        pointsOfInterestBar.repaint();
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
            MainWindowController.getInstance().requestSearchToolCloseList();
            CanvasController.getInstance().getMapCanvas().panToPoint(new Point2D.Float(point.getPOIX(), point.getPOIY()));
            CanvasController.getInstance().repaintCanvas();
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
            MainWindowController.getInstance().requestSearchToolCloseList();
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
            MainWindowController.getInstance().requestSearchToolCloseList();
        }

    }

}
