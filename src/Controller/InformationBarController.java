package Controller;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import View.InformationBar;
import View.PointsOfInterestBar;

import javax.swing.*;
import java.awt.*;
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

    private static InformationBarController instance;
    private InformationBar informationBar;
    private SpringLayout informationBarLayout;
    private PointsOfInterestBar pointsOfInterestBar;
    private JScrollPane scroll;

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
        pointsOfInterestBar.setMinimumSize(new Dimension(300, 800));

        setupScrollbar();
        addPointsToPointsOfInterestBar();

        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scroll, 0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(NORTH, scroll, GlobalValue.getToolbarHeight() + 30, NORTH, informationBar);
        informationBar.add(scroll);
    }

    private void setupScrollbar() {
        scroll = new JScrollPane(pointsOfInterestBar);
        scroll.setOpaque(true);
        scroll.setPreferredSize(new Dimension(300, 800));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        //Find out how to change color on scroll bar
    }

    public void themeHasChanged() {
        scroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
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
            calculatePointsOfInterestBarSize();
        }
        pointsOfInterestBar.revalidate();
    }


    private JPanel setupPointPanel(String s) {
        JPanel panel = new JPanel();
        SpringLayout layout = new SpringLayout();
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("border")));
        panel.setPreferredSize(new Dimension(300,90));
        panel.setMaximumSize(new Dimension(300, 90));
        panel.setMinimumSize(new Dimension(300, 90));
        panel.setBackground(ThemeHelper.color("toolbar"));
        return panel;
    }

    private void calculatePointsOfInterestBarSize() {
        //Todo // FIXME: 27/04/2017 Create a proper calculation method
        pointsOfInterestBar.setPreferredSize(new Dimension(300, 1200));
    }
}
