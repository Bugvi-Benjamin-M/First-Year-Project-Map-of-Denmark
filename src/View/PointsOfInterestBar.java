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

    public void specifyLayout(int layout) {
        setLayout(new BoxLayout(this, layout));
    }

    public void addPlaceToVerticaList(PointProfile place) {
        add(place);
        createVerticalSpace(SPACE_BETWEEN_PROFILES);
        addHorizontalGlue();
    }

    public void addPlaceToHorizontalList(PointProfile place) {
        add(place);
        createHorizontalSpace(SPACE_BETWEEN_PROFILES);
        addVerticalGlue();
    }

    public void createVerticalSpace(int size) {
        add(Box.createRigidArea(new Dimension(0,size)));
    }

    public void createHorizontalSpace(int size) { add(Box.createRigidArea(new Dimension(size, 0)));}

    public void addVerticalGlue() {add(Box.createVerticalGlue());}

    public void addHorizontalGlue() {
        add(Box.createHorizontalGlue());
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        for(PointProfile panel : points) {
           panel.applyTheme();
        }
    }

    public void setPointProfilesVertically(List<PointProfile> pointProfiles) {
        points = pointProfiles;
        for (PointProfile point : points) {
            add(point);
            createVerticalSpace(SPACE_BETWEEN_PROFILES);
            addHorizontalGlue();
        }
    }

    public void setPointProfilesHorizontally(List<PointProfile> pointProfiles) {
        points = pointProfiles;
        for(PointProfile point : points) {
            add(point);
            createHorizontalSpace(SPACE_BETWEEN_PROFILES);
            addVerticalGlue();
        }
    }

}
