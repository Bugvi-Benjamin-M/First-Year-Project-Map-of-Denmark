package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 26/04/2017
 */
public class InformationBar extends View {

    private Dimension dimension;

    public InformationBar() {
        setPreferredSize(dimension = new Dimension(400, 1000));
        setLayout(new SpringLayout());
        applyTheme();
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
    }

}
