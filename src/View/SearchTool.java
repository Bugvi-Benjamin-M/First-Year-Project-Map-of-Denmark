package View;

import Helpers.GlobalValue;
import Helpers.ThemeHelper;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

/**
 * The SearchTool is a visual component that contains a combobox of strings and
 * is used for searching.
 */
public class SearchTool extends ToolComponent {

    private JComboBox<String> field;
    private Dimension searchFieldDimension;
    private final int SEARCHBAR_HEIGHT = 40;

    public SearchTool()
    {
        field = new JComboBox<>();
        setupLayout();
        adaptSizeToLargeToolbar();
        applyTheme();
        add(field);
    }

    /**
     * Adapts the size of the search tool to the size of the large toolbar
     */
    public void adaptSizeToLargeToolbar()
    {
        searchFieldDimension = new Dimension(GlobalValue.getSearchFieldLargeSize(), SEARCHBAR_HEIGHT);
        field.setPreferredSize(searchFieldDimension);
        field.setBounds(GlobalValue.getSearchFieldStartX(), 30,
            GlobalValue.getSearchFieldStartX() + GlobalValue.getSearchFieldLargeSize(),
            70);
    }

    /**
     * Adapts the size of the search tool to the size of the smaller toolbar
     */
    public void adaptSizeToSmallToolbar()
    {
        searchFieldDimension = new Dimension(GlobalValue.getSearchFieldSmallSize(), SEARCHBAR_HEIGHT);
        field.setPreferredSize(searchFieldDimension);
    }

    /**
     * Set ups the layout of the search tool
     */
    @Override
    public void setupLayout()
    {
        field.setUI(NoArrowUI.createUI(field));
        field.setEditable(true);
        field.setFont(
            new Font(field.getFont().getName(), field.getFont().getStyle(), 20));
        field.setRequestFocusEnabled(true);
    }

    /**
     * Updates and applies the currently selected theme to the search tool
     */
    public void applyTheme()
    {
        setBackground(ThemeHelper.color("toolbar"));
        field.getEditor().getEditorComponent().setBackground(
            ThemeHelper.color("searchfield"));
        field.setRenderer(new DefaultListCellRenderer() {
            @Override
            public void setBackground(Color bg)
            {
                super.setBackground(ThemeHelper.color("toolbar"));
            }

            @Override
            public void setForeground(Color fg)
            {
                super.setForeground(ThemeHelper.color("icon"));
            }
        });
        JTextField textField = (JTextField)field.getEditor().getEditorComponent();
        textField.setCaretColor(ThemeHelper.color("icon"));
    }

    /**
     * Retrieves the search field (combobox) from the search tool
     */
    public JComboBox<String> getField() { return field; }

    /**
     * Sets the text of the search field
     */
    public void setText(String text) { field.getEditor().setItem(text); }

    /**
     * Retrieves the text written inside the search field
     */
    public String getText() { return field.getEditor().getItem().toString(); }

    /**
     * Private class responsible of making sure that no arrows appear on or
     * next to the search field
     */
    private static class NoArrowUI extends BasicComboBoxUI {

        public static NoArrowUI createUI(JComponent c) { return new NoArrowUI(); }

        @Override
        protected JButton createArrowButton()
        {
            JButton button = new JButton() {
                @Override
                public int getWidth()
                {
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
        public void configureArrowButton()
        {
            //
        }

        @Override
        protected ComboPopup createPopup() {
            return new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroller = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    scroller.getVerticalScrollBar().setUI(new CustomScrollbarUI());
                    return scroller;
                }
            };
        }
    }
}
