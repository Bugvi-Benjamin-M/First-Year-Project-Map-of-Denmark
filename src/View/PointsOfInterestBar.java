package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;

/**
 * The PointsOfInterestBar is a retractable visual component, which contains all
 * Points of Interest as well as functionalities for adding or removing POI's.
 */
public class PointsOfInterestBar extends View {

    private final int SPACE_BETWEEN_PROFILES = 10;
    private final int HORIZONTAL_NOPOI_LABEL_NORTH_OFFSET = 35;
    private final int NOPOI_LARGE_OFFSET = -20;
    private final int NOPOI_SMALL_OFFSET = -10;

    private final PointProfile NO_POI_SAVED = new PointProfile("No Points Of Interest Saved!", 0, 0);

    private int orientation;

    public PointsOfInterestBar() {
        applyTheme();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        orientation = BoxLayout.PAGE_AXIS;
        setupNoPOIPanel();
    }

    /**
     * Sets up the POIBar with no currently loaded POI's
     */
    public void setupNoPOIPanel() {
        NO_POI_SAVED.setBorder(BorderFactory.createEmptyBorder());
        NO_POI_SAVED.remove(NO_POI_SAVED.getDeleteButton());
        for(MouseListener listener : NO_POI_SAVED.getMouseListeners()) {
            NO_POI_SAVED.removeMouseListener(listener);
        }
    }

    /**
     * Specifies the layout of the POIbar
     */
    public void specifyLayout(int layout) {
        orientation = layout;
        setLayout(new BoxLayout(this, layout));
    }

    /**
     * Adds a POI to the vertical bar
     * @param place The PointProfile matching the POI
     */
    public void addPlaceToVerticaList(PointProfile place) {
        add(place);
        createVerticalSpace(SPACE_BETWEEN_PROFILES);
        addHorizontalGlue();
    }

    /**
     * Adds a POI to the horizontal bar
     * @param place The PointProfile matching the POI
     */
    public void addPlaceToHorizontalList(PointProfile place) {
        add(place);
        createHorizontalSpace(SPACE_BETWEEN_PROFILES);
        addVerticalGlue();
    }

    /**
     * Creates some vertical space between elements
     */
    public void createVerticalSpace(int size) {
        add(Box.createRigidArea(new Dimension(0,size)));
    }

    /**
     * Creates some horizontal space between elements
     */
    public void createHorizontalSpace(int size) { add(Box.createRigidArea(new Dimension(size, 0)));}

    /**
     * Creates vertical glue for the bar, e.g. instead of just adding components
     * directly below each other, they uses the space of the bar evenly (spaced out)
      */
    public void addVerticalGlue() {add(Box.createVerticalGlue());}

    /**
     * Creates horizontal glue for the bar, e.g. instead of just adding components
     * directly next to each other, they uses the space of the bar evenly (spaced out)
     */
    public void addHorizontalGlue() {
        add(Box.createHorizontalGlue());
    }

    /**
     * Updates and applies the currently selected theme onto the toolbar
     */
    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        NO_POI_SAVED.applyTheme();
    }

    /**
     * Adds the panel with no POI's present
     */
    public void addNoPoiPanel() {
        if(orientation == BoxLayout.LINE_AXIS){
            SpringLayout spl = (SpringLayout) NO_POI_SAVED.getLayout();
            spl.removeLayoutComponent(NO_POI_SAVED.getDescription());
            spl.putConstraint(SpringLayout.NORTH, NO_POI_SAVED.getDescription(), HORIZONTAL_NOPOI_LABEL_NORTH_OFFSET, SpringLayout.NORTH, NO_POI_SAVED);
            spl.putConstraint(SpringLayout.HORIZONTAL_CENTER, NO_POI_SAVED.getDescription(), NOPOI_SMALL_OFFSET, SpringLayout.HORIZONTAL_CENTER, NO_POI_SAVED);
            add(NO_POI_SAVED);
        } else {
            SpringLayout spl = (SpringLayout) NO_POI_SAVED.getLayout();
            spl.removeLayoutComponent(NO_POI_SAVED.getDescription());
            spl.putConstraint(SpringLayout.HORIZONTAL_CENTER, NO_POI_SAVED.getDescription(), NOPOI_SMALL_OFFSET, SpringLayout.HORIZONTAL_CENTER, NO_POI_SAVED);
            spl.putConstraint(SpringLayout.VERTICAL_CENTER, NO_POI_SAVED.getDescription(), NOPOI_LARGE_OFFSET, SpringLayout.VERTICAL_CENTER, NO_POI_SAVED);
            add(NO_POI_SAVED);
        }
    }

    /**
     * Removes the panel with not POI's
     */
    public void removeNoPoiPanel() {
        remove(NO_POI_SAVED);
    }
}
