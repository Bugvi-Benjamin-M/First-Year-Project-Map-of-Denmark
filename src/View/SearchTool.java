package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;


/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 29/03/2017
 */
public class SearchTool extends ToolComponent {

    private static final String defaultText = " - - Search for points of interest - - ";

    private JComboBox<String> field;

    private Dimension searchFieldDimension;
    private JLabel searchLabel;

    //Todo fix why search bar is not being set up properly when theme is changed
    public SearchTool(int width) {
        field = new JComboBox<>();
        searchFieldDimension = new Dimension(width, 40);
        setupLayout();
        addSearchLabel();
        applyTheme();
        setDefaultText(defaultText);
        add(field);
    }

    @Override
    public void setupLayout() {
        field.setPreferredSize(searchFieldDimension);
        field.setEditable(true);
        field.setFont(new Font(field.getFont().getName(), field.getFont().getStyle(), 20));
        field.setRequestFocusEnabled(true);
        for (Component component : field.getComponents()) if (component instanceof JButton) field.remove(component);
    }

    private void addSearchLabel() {
        searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font(getFont().getName(), getFont().getStyle(), 20));
        add(searchLabel);
    }

    private void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        searchLabel.setForeground(ThemeHelper.color("icon"));
        field.getEditor().getEditorComponent().setBackground(ThemeHelper.color("searchfield"));
        field.getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
    }

    public void setDefaultText(String text) {
        field.getEditor().getEditorComponent().setForeground(ThemeHelper.color("defaulttext"));
        field.getEditor().setItem(text);
    }

    public void addFocusListener() {
        field.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                setDefaultText("");
                field.getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                setDefaultText(defaultText);
            }
        });
    }

    public static String getDefaultText() {
        return defaultText;
    }

}
