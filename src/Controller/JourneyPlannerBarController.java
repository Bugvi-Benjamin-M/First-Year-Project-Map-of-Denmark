package Controller;

import View.InformationBar;

import javax.swing.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 03/05/2017
 */
public final class JourneyPlannerBarController extends Controller {

    private static JourneyPlannerBarController instance;

    private InformationBar informationBar;
    private SpringLayout informationBarLayout;

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
    }

    public InformationBar getInformationBar() {
        return informationBar;
    }
}
