package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 26/04/2017
 */
public class PointsOfInterestBar extends View {

    private List<PointProfile> points;
    public PointsOfInterestBar() {
        points = new ArrayList<>();
        applyTheme();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    public void addPlaceToList(PointProfile place) {
        points.add(place);
        add(place);
    }

    public void createVerticalSpace(int size) {
        add(Box.createRigidArea(new Dimension(0,size)));
    }

    public void addHorizontalGlue() {
        add(Box.createHorizontalGlue());
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        for(PointProfile panel : points) {
           panel.applyTheme();
        }
    }

    public void updateListOfPoints(List<PointProfile> list) {
        points = list;
    }


}
