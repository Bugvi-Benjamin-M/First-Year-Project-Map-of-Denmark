package View;

import Helpers.GlobalValue;
import Helpers.ScreenScaler;
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
    private final int SEARCHBAR_HEIGHT = 40 * ScreenScaler.getHeightScaleFactor();

    public SearchTool() {
        field = new JComboBox<>();
        setupLayout();
        adaptSizeToLargeToolbar();
        applyTheme();
        add(field);
    }

    public void adaptSizeToLargeToolbar() {
        searchFieldDimension = new Dimension(ScreenScaler.getSearchFieldLargeSize(), SEARCHBAR_HEIGHT);
        field.setPreferredSize(searchFieldDimension);
        field.setBounds(GlobalValue.getSearchFieldStartX(), 30, GlobalValue.getSearchFieldStartX() + ScreenScaler.getSearchFieldLargeSize(), 70);
    }

    public void adaptSizeToSmallToolbar() {
        searchFieldDimension = new Dimension(ScreenScaler.getSearchFieldSmallSize(), SEARCHBAR_HEIGHT);
        field.setPreferredSize(searchFieldDimension);
    }

    @Override
    public void setupLayout() {
        field.setUI(NoArrowUI.createUI(field));
        field.setEditable(true);
        field.setFont(new Font(field.getFont().getName(), field.getFont().getStyle(), 20 * ScreenScaler.getWidthScaleFactor()));
        field.setRequestFocusEnabled(true);
    }


    public void applyTheme() {
        setBackground(ThemeHelper.color("toolbar"));
        field.getEditor().getEditorComponent().setBackground(ThemeHelper.color("searchfield"));
    }


    public JComboBox<String> getField() {
        return field;
    }

    public void setText(String text) {
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
