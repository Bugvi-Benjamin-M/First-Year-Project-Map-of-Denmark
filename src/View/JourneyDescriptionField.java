package View;

import Helpers.ThemeHelper;

import javax.swing.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 05/05/2017
 */
public class JourneyDescriptionField extends View {

    private JLabel field;
    private JLabel noSearchLabel;
    private final String NO_SEARCH_TEXT = "No Search Initialised!";
    private String description;

    public JourneyDescriptionField() {
        field = new JLabel();
        field.setPreferredSize(this.getSize());
        noSearchLabel = new JLabel(NO_SEARCH_TEXT);
    }

    public void removeField() {
        remove(field);
    }

    public void removeNoSearchText() {
        remove(noSearchLabel);
    }

    public void setFieldText(String description) {
        this.description = description.replace("\n", "<br>");
        this.description = "<html><font size='10" + this.description + "</font></html";
        field.setText(this.description);
    }

    public void addNoSearchText() {
        add(noSearchLabel);
    }

    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        field.setBackground(ThemeHelper.color("searchField"));
        field.setForeground(ThemeHelper.color("icon"));
        noSearchLabel.setBackground(ThemeHelper.color("toolbar"));
        noSearchLabel.setForeground(ThemeHelper.color("icon"));
    }
}
