package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComboBox;



/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 29/03/2017
 */
public class SearchTool extends ToolComponent {

    private static final String defaultText = "Addresses, points of interest...";

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
        field.setUI(NoArrowUI.createUI(field));
        field.setPreferredSize(searchFieldDimension);
        field.setEditable(true);
        field.setFont(new Font(field.getFont().getName(), field.getFont().getStyle(), 20));
        field.setRequestFocusEnabled(true);
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
                if(!((field.getEditor().getItem().equals(defaultText)))) return;
                else {
                    setDefaultText("");
                    field.getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(field.getEditor().getItem().equals("")) setDefaultText(defaultText);
            }
        });
    }

    public void setText(String text) {
        field.getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
        field.getEditor().setItem(text);
    }

    public String getText() {
        return field.getEditor().getItem().toString();
    }
}
