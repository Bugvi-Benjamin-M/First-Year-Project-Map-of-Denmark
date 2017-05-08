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
    private final int BUTTONS_HEIGHT = 60;
    //private final int LARGE_SCROLLBAR_HEIGHT = (int) (0.976316*Toolkit.getDefaultToolkit().getScreenSize().getHeight() + (-292.289));
    private final double LARGE_SCROLLBAR_HEIGHT_DECREASE = -252.289;
    private final double SMALL_SCROLLBAR_WIDTH_DECREASE = -219.421;
    //private final int SMALL_SCROLLBAR_WIDTH = 790;
    private final int DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS = GlobalValue.getToolbarHeight() + 10;
    private final int DISTANCE_BETWEEN_BUTTONS_AND_SCROLLPANE = 60;
    private final int SCROLLBAR_SPEED = 14;
    private final int DISTANCE_FROM_SMALLINFORMATIONBAR_LEFT_EDGE_TO_BUTTONS = 25;
    private final int DISTANCE_FROM_SMALLPOIBARTOP_TO_BUTTONS = 35;
    private final int SMALL_SCROLLBAR_HEIGHT = 110;
    private final int DISTANCE_FROM_SMALLPOIBARTOP_TO_SCROLLPANE = 10;
    private final int SMALL_POINTS_OF_INTERESTBAR_HEIGHT = 100;
    private final int POI_BUTTONS_SMALL_POINTS_OF_INTERESTBAR_WIDTH = 150;
    private final int DISTANCE_BETWEEN_POI_BUTTONS_AND_SMALL_SCROLLPANE = 30;

    private static PointsOfInterestController instance;
    private InformationBar informationBar;
    private SpringLayout informationBarLayout;
    private PointsOfInterestBar pointsOfInterestBar;
    private JScrollPane largeScroll;
    private JScrollPane smallScroll;
    private PointsOfInterestButtons poiButtons;

    private List<POI> pointsOfInterest;
    private List<PointProfile> POIpanels;
    private boolean isLargePOIVisible;
    private boolean isSmallPOIVisible;

    private PointsOfInterestController() { super();}

    public static PointsOfInterestController getInstance()
    {
        if (instance == null) {
            instance = new PointsOfInterestController();
        }
        return instance;
    }

    public void setupInformationBar() {
        informationBar = new InformationBar();
        informationBarLayout = (SpringLayout) informationBar.getLayout();
        InformationBarInteractionHandler handler = new InformationBarInteractionHandler();
        informationBar.addMouseListener(handler);
        isLargePOIVisible = false;
        isSmallPOIVisible = false;
    }

    public void setupBasePointsOfInterestBar() {
        pointsOfInterest = Model.getInstance().getPointsOfInterest();
        POIpanels = new ArrayList<>();
        pointsOfInterestBar = new PointsOfInterestBar();
        pointsOfInterestBar.setOpaque(true);
        poiButtons = new PointsOfInterestButtons();
        poiButtons.setOpaque(true);
        addInteractionHandlersPointsOfInterestButtons();
    }

    public void setupLargePointsOfInterestBar() {
        isLargePOIVisible = true;
        informationBar.setPreferredSize(new Dimension(GlobalValue.getLargeInformationBarWidth(), window.getFrame().getHeight()));
        pointsOfInterestBar.specifyLayout(BoxLayout.PAGE_AXIS);
        pointsOfInterestBar.setMinimumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, PROFILE_HEIGHT));
        setupLargeScrollbar();
        addPointsToVerticalPointsOfInterestBar();
        poiButtons.setPreferredSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, BUTTONS_HEIGHT));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, poiButtons, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, poiButtons, DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS, NORTH, informationBar);
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, largeScroll, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, largeScroll, DISTANCE_BETWEEN_BUTTONS_AND_SCROLLPANE+10, NORTH, poiButtons);
        informationBar.add(poiButtons);
        informationBar.add(largeScroll);
    }

    public void setupSmallPointsOfInterestBar() {
        isSmallPOIVisible = true;
        informationBar.setPreferredSize(new Dimension(window.getFrame().getWidth(), GlobalValue.getSmallInformationBarHeight()));
        pointsOfInterestBar.specifyLayout(BoxLayout.LINE_AXIS);
        pointsOfInterestBar.setMinimumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, SMALL_POINTS_OF_INTERESTBAR_HEIGHT));
        setupSmallScrollbar();
        addPointsToHorizontalPointsOfInterestBar();
        poiButtons.setPreferredSize(new Dimension(POI_BUTTONS_SMALL_POINTS_OF_INTERESTBAR_WIDTH, BUTTONS_HEIGHT));
        informationBarLayout.putConstraint(WEST, poiButtons, DISTANCE_FROM_SMALLINFORMATIONBAR_LEFT_EDGE_TO_BUTTONS, WEST, informationBar);
        informationBarLayout.putConstraint(NORTH, poiButtons, DISTANCE_FROM_SMALLPOIBARTOP_TO_BUTTONS, NORTH, informationBar);
        informationBarLayout.putConstraint(NORTH, smallScroll, DISTANCE_FROM_SMALLPOIBARTOP_TO_SCROLLPANE, NORTH, informationBar);
        informationBarLayout.putConstraint(WEST, smallScroll, DISTANCE_BETWEEN_POI_BUTTONS_AND_SMALL_SCROLLPANE, EAST, poiButtons);
        informationBar.add(poiButtons);
        informationBar.add(smallScroll);
    }

    public void clearPointsOfInterestBar() {
        isLargePOIVisible = false;
        isSmallPOIVisible = false;
        if(largeScroll != null) largeScroll.remove(pointsOfInterestBar);
        if(smallScroll != null) smallScroll.remove(pointsOfInterestBar);
        informationBarLayout.removeLayoutComponent(poiButtons);
        informationBarLayout.removeLayoutComponent(pointsOfInterestBar);
        informationBarLayout.removeLayoutComponent(largeScroll);
        informationBarLayout.removeLayoutComponent(smallScroll);
        informationBar.removeAll();
        pointsOfInterestBar.removeAll();
        POIpanels.clear();
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
                    pointsOfInterestBar.addNoPoiPanel();
                    pointsOfInterestBar.revalidate();
                    pointsOfInterestBar.repaint();
                    MainWindowController.getInstance().requestCanvasRepaint();
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
        int largeScrollbarHeight = (int) (window.getFrame().getHeight() + LARGE_SCROLLBAR_HEIGHT_DECREASE);
        largeScroll.setPreferredSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, largeScrollbarHeight));
        largeScroll.setMinimumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, largeScrollbarHeight));
        largeScroll.setMaximumSize(new Dimension(LARGE_POINTS_OF_INTERESTBAR_WIDTH, largeScrollbarHeight));
        largeScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        largeScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        largeScroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        largeScroll.getVerticalScrollBar().setUI(new CustomScrollbarUI());
        largeScroll.getVerticalScrollBar().setUnitIncrement(SCROLLBAR_SPEED);
    }

    private void setupSmallScrollbar() {
        smallScroll = new JScrollPane(pointsOfInterestBar);
        smallScroll.setOpaque(true);
        int SMALL_SCROLLBAR_WIDTH = (int) (window.getFrame().getWidth() + SMALL_SCROLLBAR_WIDTH_DECREASE);
        smallScroll.setPreferredSize(new Dimension(SMALL_SCROLLBAR_WIDTH, SMALL_SCROLLBAR_HEIGHT));
        smallScroll.setMinimumSize(new Dimension(SMALL_SCROLLBAR_WIDTH, SMALL_SCROLLBAR_HEIGHT));
        smallScroll.setMaximumSize(new Dimension(SMALL_SCROLLBAR_WIDTH, SMALL_SCROLLBAR_HEIGHT));
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
        if(POIpanels != null) {
            for (PointProfile point : POIpanels) {
                point.applyTheme();
            }
        }
    }

    public InformationBar getInformationBar() {
        return informationBar;
    }
    private void addPointsToVerticalPointsOfInterestBar() {
        for(POI poi : pointsOfInterest) {
            addPOI(poi);
        }
        if(POIpanels != null && POIpanels.size() == 0) pointsOfInterestBar.addNoPoiPanel();
        pointsOfInterestBar.revalidate();
    }

    public void addPointsToHorizontalPointsOfInterestBar() {
        for(POI poi : pointsOfInterest) {
            addPOI(poi);
        }
        if(POIpanels != null && POIpanels.size() == 0) pointsOfInterestBar.addNoPoiPanel();
        pointsOfInterestBar.revalidate();
    }


    public void addPOI(POI poi) {
        if(isSmallPOIVisible || isLargePOIVisible) {
            PointProfile pointProfile = new PointProfile(poi.getDescription(), poi.x, poi.y);
            pointProfile.addMouseListener(new PointsInteractionHandler(pointProfile));
            pointProfile.getDeleteButton().addMouseListener(new PointDeleteButtonInteractionHandler(pointProfile.getDeleteButton()));
            POIpanels.add(pointProfile);
            pointsOfInterestBar.removeNoPoiPanel();
            if (isLargePOIVisible) pointsOfInterestBar.addPlaceToVerticaList(pointProfile);
            else if (isSmallPOIVisible) pointsOfInterestBar.addPlaceToHorizontalList(pointProfile);
        }
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
            MainWindowController.getInstance().requestCanvasPanToPoint(new Point2D.Float(point.getPOIX(), point.getPOIY()));
            MainWindowController.getInstance().requestCanvasRepaint();
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
            if(POIpanels.isEmpty()) {
                pointsOfInterestBar.removeAll();
                pointsOfInterestBar.addNoPoiPanel();
            } else pointsOfInterestBar.remove(point);
            pointsOfInterestBar.revalidate();
            pointsOfInterestBar.repaint();
            MainWindowController.getInstance().requestSearchToolCloseList();
            MainWindowController.getInstance().requestCanvasRepaint();
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
            themeHasChanged();
            if(GlobalValue.isAddNewPointActive()) {
                poiButtons.getNewPointButton().setForeground(ThemeHelper.color("toolActivated"));
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
            MainWindowController.getInstance().requestSearchToolCloseList();
        }

    }

}
