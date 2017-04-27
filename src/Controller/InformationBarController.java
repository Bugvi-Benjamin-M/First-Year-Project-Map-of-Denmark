package Controller;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import View.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import static javax.swing.SpringLayout.NORTH;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public final class InformationBarController extends Controller {

    private final int PROFILE_HEIGHT = 90;

    private static InformationBarController instance;
    private InformationBar informationBar;
    private SpringLayout informationBarLayout;
    private PointsOfInterestBar pointsOfInterestBar;
    private JScrollPane scroll;
    private PointsOfInterestTopButtons poiButtons;

    private List<String> places;

    private InformationBarController() { super();}

    public static InformationBarController getInstance()
    {
        if (instance == null) {
            instance = new InformationBarController();
        }
        return instance;
    }

    public void setupInformationBar() {
        places = new LinkedList<>();
        informationBar = new InformationBar();
        informationBarLayout = (SpringLayout) informationBar.getLayout();
        informationBar.setPreferredSize(new Dimension(400, window.getFrame().getHeight()));
        setupPointsOfInterestBar();

    }

    public void setupPointsOfInterestBar() {
        pointsOfInterestBar = new PointsOfInterestBar();
        pointsOfInterestBar.setOpaque(true);
        pointsOfInterestBar.setMinimumSize(new Dimension(300, PROFILE_HEIGHT));

        setupScrollbar();
        addPointsToPointsOfInterestBar();
        poiButtons = new PointsOfInterestTopButtons();
        poiButtons.setPreferredSize(new Dimension(300, 50));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, poiButtons, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, poiButtons, GlobalValue.getToolbarHeight() + 10, NORTH, informationBar);
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scroll, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, scroll, 60, NORTH, poiButtons);
        informationBar.add(poiButtons);
        informationBar.add(scroll);
        addInteractionHandlersPointsOfInterestButtons();
    }

    private void addInteractionHandlersPointsOfInterestButtons() {
        poiButtons.getNewPointButton().addMouseListener(new PointsOfInterestButtonInteractionHandler(poiButtons.getNewPointButton()));
        poiButtons.getDeleteAllButton().addMouseListener(new PointsOfInterestButtonInteractionHandler(poiButtons.getDeleteAllButton()));
    }

    private void setupScrollbar() {
        scroll = new JScrollPane(pointsOfInterestBar);
        scroll.setOpaque(true);
        //Todo find a proper constant instead of 400, needs to be relative to the window size somehow
        scroll.setPreferredSize(new Dimension(300, window.getFrame().getHeight()- (GlobalValue.getToolbarHeight()+410)));
        scroll.setMinimumSize(new Dimension(300, window.getFrame().getHeight()- (GlobalValue.getToolbarHeight()+410)));
        scroll.setMaximumSize(new Dimension(300, window.getFrame().getHeight()- (GlobalValue.getToolbarHeight()+410)));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        scroll.getVerticalScrollBar().setUI(new CustomScrollbarUI());
    }

    public void themeHasChanged() {
        scroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
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
            pointsOfInterestBar.addPlaceToList(setupPointPanel(s));
            pointsOfInterestBar.createVerticalSpace(10);
            pointsOfInterestBar.addHorizontalGlue();
        }
        pointsOfInterestBar.revalidate();
    }


    private JPanel setupPointPanel(String s) {
        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("border")));
        panel.setPreferredSize(new Dimension(290,PROFILE_HEIGHT));
        panel.setMaximumSize(new Dimension(290, PROFILE_HEIGHT));
        panel.setMinimumSize(new Dimension(290, PROFILE_HEIGHT));
        panel.setBackground(ThemeHelper.color("toolbar"));
        panel.addMouseListener(new PointsInteractionHandler(panel));
        return panel;
    }


    private class PointsInteractionHandler extends MouseAdapter {

        private JPanel point;

        public PointsInteractionHandler(JPanel panel) {
            point = panel;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            point.setBackground(ThemeHelper.color("pointHover"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            point.setBackground(ThemeHelper.color("toolbar"));
        }

    }

    private class PointsOfInterestButtonInteractionHandler extends MouseAdapter {

        private JLabel button;

        public PointsOfInterestButtonInteractionHandler(JLabel button) {
            this.button = button;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setForeground(ThemeHelper.color("toolHover"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setForeground(ThemeHelper.color("icon"));
        }

    }

}
