package View;

import Helpers.GlobalValue;
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
    private final int SEARCHBAR_HEIGHT = 40;

    public SearchTool(int width) {
        field = new JComboBox<>();
        //searchFieldDimension = new Dimension(width, SEARCHBAR_HEIGHT);
        //todo use set bounds to adjust searchfield size instead of removing and adding
        setupLayout();
        adaptSize();
        applyTheme();
        add(field);
    }

    public void adaptSize() {
        searchFieldDimension = new Dimension(GlobalValue.getSearchFieldLargeSize(), SEARCHBAR_HEIGHT);
        field.setPreferredSize(searchFieldDimension);
        field.setBounds(GlobalValue.getSearchFieldStartX(), 30, GlobalValue.getSearchFieldStartX() + GlobalValue.getSearchFieldLargeSize(), 70);
    }

    @Override
    public void setupLayout() {
        field.setUI(NoArrowUI.createUI(field));
        //field.setPreferredSize(searchFieldDimension);
        field.setEditable(true);
        field.setFont(new Font(field.getFont().getName(), field.getFont().getStyle(), 20));
        field.setRequestFocusEnabled(true);
    }


    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
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
            JButton button = new JButton() {
                @Override
                public int getWidth() {
                    return 0;
                }
            };
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
