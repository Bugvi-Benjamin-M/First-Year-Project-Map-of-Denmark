package View;

import Helpers.ThemeHelper;

import javax.swing.*;
import java.awt.*;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 05/05/2017
 */
public class JourneyDescriptionField extends View {

    private JTextArea field;
    private JScrollPane scroll;


    public JourneyDescriptionField() {
        field = new JTextArea();
        scroll = new JScrollPane(field);
        field.setOpaque(true);
        field.setEditable(false);
        scroll.setOpaque(true);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createLineBorder(ThemeHelper.color("toolbar")));
        scroll.getVerticalScrollBar().setUI(new CustomScrollbarUI());
        scroll.getVerticalScrollBar().setUnitIncrement(18);
        add(scroll);
        field.setBackground(ThemeHelper.color("searchfield"));
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");
        addLine("hello");


    }

    public void addLine(String line) {
        field.append(line + "\n");
    }

    public JTextArea getField() {
        return field;
    }


    public void setScrollSize(Dimension dimension) {
        scroll.setPreferredSize(dimension);
        scroll.setMinimumSize(dimension);
        scroll.setMaximumSize(dimension);
    }

}
