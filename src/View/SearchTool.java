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
    private java.util.List<String> history;

    private Dimension searchFieldDimension;
    private JLabel searchLabel;


    public SearchTool(int width) {
        field = new JComboBox<>();
        searchFieldDimension = new Dimension(width, 40);
        setupLayout();
        addSearchLabel();
        applyTheme();
        field.getEditor().setItem(defaultText);
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

    public void addFocusListener() {
        field.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                field.getEditor().setItem("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                field.getEditor().setItem(defaultText);
            }
        });
    }
}
