package View;

import Enums.ToolType;
import Helpers.ThemeHelper;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class details:
 * The Toolbar is a visual component consisting of a collection of tools.
 */
public class Toolbar extends View {

    private static Map<ToolType, ToolComponent> tools;
    private SpringLayout layout;

    public Toolbar()
    {
        tools = new ToolFactory().setupToolbar();
        layout = new SpringLayout();
        setLayout(layout);
        setBorder(BorderFactory.createLineBorder(ThemeHelper.color("border")));
        applyTheme();
    }

    /**
     * Applies the current selected theme to the toolbar
     */
    public void applyTheme()
    {
        setBackground(ThemeHelper.color("toolbar"));
        for (ToolType type : tools.keySet()) {
            if (type == ToolType.SEARCHBAR) {
                SearchTool searchTool = (SearchTool)tools.get(type);
                searchTool.applyTheme();
            } else {
                ToolFeature feature = (ToolFeature)tools.get(type);
                feature.setTheme();
            }
        }
    }

    /**
     * Retrieves the layout manager of the toolbar
     */
    public SpringLayout getLayout() { return layout; }

    /**
   * Returns the ToolComponent of the type given as parameter
   * @param type of the tool to be returned
   * @return a ToolFeature
   */
    public ToolComponent getTool(ToolType type) { return tools.get(type); }

    /**
     * Returns all tools in the toolbar
     */
    public Map<ToolType, ToolComponent> getAllTools() { return tools; }

    /** ToolFactory creates the collection of visual components representing the
   * tools */
    private class ToolFactory {
        private Map<ToolType, ToolComponent> setupToolbar()
        {
            Map<ToolType, ToolComponent> tools = new HashMap<>();

            tools.put(ToolType.LOAD, new ToolFeature("\uf115", ToolType.LOAD));
            tools.put(ToolType.SAVE, new ToolFeature("\uf0c7", ToolType.SAVE));
            tools.put(ToolType.SETTINGS,
                new ToolFeature("\uf085", ToolType.SETTINGS));
            tools.put(ToolType.MENU, new ToolFeature("\uf0c9", ToolType.MENU));
            tools.put(ToolType.SEARCHBAR, new SearchTool());
            tools.put(ToolType.SEARCHBUTTON,
                new ToolFeature("\uf002", ToolType.SEARCHBUTTON));
            tools.put(ToolType.POI, new ToolFeature("\uf041", ToolType.POI));
            tools.put(ToolType.ROUTES, new ToolFeature("\uf018", ToolType.ROUTES));
            return tools;
        }
    }
}
