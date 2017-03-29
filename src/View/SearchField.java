package View;

import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.metal.MetalComboBoxButton;
import javax.swing.plaf.metal.MetalComboBoxUI;
import java.awt.*;
import java.util.List;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 29/03/2017
 */
public class SearchField extends View {

    private JComboBox<String> field;
    private static MetalComboBoxButton button;
    private java.util.List<String> addresses;
    private DefaultComboBoxModel<String> model;

    public SearchField() {
        field = new JComboBox<>();
        setupComboBox();
        add(field);
    }

    private void setupComboBox() {
        field.setPreferredSize(new Dimension(600, 40));
        field.setMinimumSize(new Dimension(600, 40));
        field.setEditable(true);
        field.setFont(new Font(field.getFont().getName(), field.getFont().getStyle(), 20));
        field.setRequestFocusEnabled(true);
        field.setUI(SearchField.Arrow.createUI(field));
        //Todo force focus loss
        //Todo add implementation for theme change
    }

    public void addListToSearchField(List<String> list) {
        addresses = list;
        model = new DefaultComboBoxModel(list.toArray());
    }

    private static class Arrow extends MetalComboBoxUI {


        public static ComboBoxUI createUI(JComponent component) {
            return new SearchField.Arrow();
        }

        @Override
        protected JButton createArrowButton() {
            button = (MetalComboBoxButton) super.createArrowButton();
            return button;
        }

    }
}
