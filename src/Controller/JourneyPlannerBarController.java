package Controller;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;
import View.InformationBar;
import View.JourneyPlannerButtons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 03/05/2017
 */
public final class JourneyPlannerBarController extends Controller {

    private static JourneyPlannerBarController instance;

    private final int DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS = GlobalValue.getToolbarHeight() + 10;
    private final int BUTTONS_WIDTH = GlobalValue.getLargeInformationBarWidth() - 40;
    private final int BUTTONS_HEIGHT = 80;

    private InformationBar informationBar;
    private SpringLayout informationBarLayout;
    private JourneyPlannerButtons journeyPlannerButtons;

    private boolean isLargeJourneyPlannerVisible;
    private boolean isSmallJourneyPlannerVisible;

    private JourneyPlannerBarController() {
        super();
    }

    public static JourneyPlannerBarController getInstance() {
        if(instance == null) {
            instance = new JourneyPlannerBarController();
        }
        return instance;
    }


    public void setupInformationBar() {
        informationBar = new InformationBar();
        informationBarLayout = (SpringLayout) informationBar.getLayout();
        InformationBarInteractionHandler handler = new InformationBarInteractionHandler();
        informationBar.addMouseListener(handler);
        isLargeJourneyPlannerVisible = false;
        isSmallJourneyPlannerVisible = false;
    }

    public void setupBaseJourneyPlannerBar() {
        journeyPlannerButtons = new JourneyPlannerButtons();
        journeyPlannerButtons.setOpaque(true);
        addInteractionHandlersToJourneyPlannerButtons();
    }

    public void setupLargeJourneyPlannerBar() {
        isLargeJourneyPlannerVisible = true;
        informationBar.setPreferredSize(new Dimension(GlobalValue.getLargeInformationBarWidth(), window.getFrame().getHeight()));
        journeyPlannerButtons.setPreferredSize(new Dimension(BUTTONS_WIDTH, BUTTONS_HEIGHT));
        informationBarLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, journeyPlannerButtons,
                0, SpringLayout.HORIZONTAL_CENTER, informationBar);
        informationBarLayout.putConstraint(SpringLayout.NORTH, journeyPlannerButtons, DISTANCE_BETWEEN_TOOLBAR_AND_BUTTONS, SpringLayout.NORTH, informationBar);
        informationBar.add(journeyPlannerButtons);
    }

    public void setupSmallJourneyPlannerBar() {
        isSmallJourneyPlannerVisible = true;
    }

    public InformationBar getInformationBar() {
        return informationBar;
    }

    private void addInteractionHandlersToJourneyPlannerButtons() {
        journeyPlannerButtons.getOnFootButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerButtons.getOnFootButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerButtons.getOnFootButton().setForeground(ThemeHelper.color("icon"));
            }
        });
        journeyPlannerButtons.getBicycleButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerButtons.getBicycleButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerButtons.getBicycleButton().setForeground(ThemeHelper.color("icon"));
            }
        });
        journeyPlannerButtons.getCarButton().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                journeyPlannerButtons.getCarButton().setForeground(ThemeHelper.color("toolHover"));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                journeyPlannerButtons.getCarButton().setForeground(ThemeHelper.color("icon"));
            }
        });
    }

    public void clearJourneyPlannerBar() {
        isLargeJourneyPlannerVisible = false;
        isSmallJourneyPlannerVisible = false;
        informationBarLayout.removeLayoutComponent(journeyPlannerButtons);
        informationBar.removeAll();
    }

    private class InformationBarInteractionHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if(MainWindowController.getInstance().isMenuToolPopupVisible()) MainWindowController.getInstance().requestMenuToolHidePopup();
            MainWindowController.getInstance().requestSearchToolCloseList();
        }
    }
}
