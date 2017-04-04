package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;



/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 29/03/2017
 */
public class SearchTool extends ToolComponent {

    private JComboBox<String> field;
    private Dimension searchFieldDimension;
    private JLabel searchLabel;

    public SearchTool(int width) {
        field = new JComboBox<>();
        searchFieldDimension = new Dimension(width, 40);
        setupLayout();
        addSearchLabel();
        applyTheme();
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

    public JComboBox<String> getField() {
        return field;
    }


    public void setText(String text) {
        field.getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
        field.getEditor().setItem(text);
    }

    public String getText() {
        return field.getEditor().getItem().toString();
    }

    private static class NoArrowUI extends BasicComboBoxUI {

        public static NoArrowUI createUI(JComponent c) {
            return new NoArrowUI();
        }

        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton();
            button.setEnabled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setBackground(ThemeHelper.color("toolbar"));
            return button;
        }

        @Override
        public void configureArrowButton() {
            //
        }
    }
}
