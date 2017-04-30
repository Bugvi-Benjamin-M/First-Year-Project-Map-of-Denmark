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

    private final int SPACE_BETWEEN_PROFILES = 10;

    private List<PointProfile> points;
    public PointsOfInterestBar() {
        points = new ArrayList<>();
        applyTheme();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    public void addPlaceToList(PointProfile place) {
        points.add(place);
        add(place);
        createVerticalSpace(SPACE_BETWEEN_PROFILES);
        addHorizontalGlue();
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

    public void setPointProfiles(List<PointProfile> pointProfiles) {
        points = pointProfiles;
        for (PointProfile point : points) {
            add(point);
            createVerticalSpace(SPACE_BETWEEN_PROFILES);
            addHorizontalGlue();
        }
    }
}
