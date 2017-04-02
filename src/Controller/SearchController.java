package Controller;

import Enums.ToolType;
import View.SearchTool;
import View.Toolbar;
import View.Window;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public final class SearchController extends Controller {

    private static SearchController instance;
    private SearchTool searchTool;

    private SearchController(Window window) {
        super(window);
        searchTool = (SearchTool) ToolbarController.getInstance(this.window).getToolbar().getTool(ToolType.SEARCH);
        searchTool.addFocusListener();
    }

    public static SearchController getInstance(Window window) {
        if (instance == null) {
            instance = new SearchController(window);
        }
        return instance;
    }

    public void resetInstance() {
        instance = null;
    }


    public void searchFieldResized() {
        searchTool = (SearchTool) ToolbarController.getInstance(window).getToolbar().getTool(ToolType.SEARCH);
        searchTool.addFocusListener();
    }


}
