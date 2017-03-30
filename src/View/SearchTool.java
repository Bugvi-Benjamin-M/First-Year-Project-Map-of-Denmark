package View;

import javax.swing.*;
import java.awt.*;
import java.util.List;


/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 29/03/2017
 */
public class SearchTool extends ToolComponent {

    private JComboBox<String> field;
    private java.util.List<String> addresses;
    private DefaultComboBoxModel<String> model;
    private Dimension dimension;
    private JLabel searchLabel;


    public SearchTool() {
        field = new JComboBox<>();
        dimension = new Dimension(600, 40);
        setupLayout();
        addSearchLabel();
        add(field);
    }

    @Override
    public void setupLayout() {
        field.setPreferredSize(dimension);
        field.setMinimumSize(dimension);
        field.setEditable(true);
        field.setFont(new Font(field.getFont().getName(), field.getFont().getStyle(), 20));
        field.setRequestFocusEnabled(true);
        for(Component component : field.getComponents()) if(component instanceof JButton) field.remove(component);
        /*field.getEditor().getEditorComponent().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Rectangle r = field.getBounds();
                if(r.contains(e.getPoint())) field.getEditor().getEditorComponent().setEnabled(true);
                else field.getEditor().getEditorComponent().setEnabled(false);
            //Todo this only works sometimes. Fix
            }
        });*/
        //Todo force focus loss
        //Todo add implementation for theme change
    }

    public void addListToSearchField(List<String> list) {
        addresses = list;
        model = new DefaultComboBoxModel(list.toArray());
    }

    public void setWidth(int width) {
        setPreferredSize(dimension = new Dimension(width, (int) dimension.getHeight()));
    }

    private void addSearchLabel() {
        searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font(getFont().getName(), getFont().getStyle(), 20));
        add(searchLabel);
    }
}
