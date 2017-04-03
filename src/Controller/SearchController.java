package Controller;

import Enums.ToolType;
import Helpers.GlobalValue;
import View.SearchTool;
import View.Toolbar;
import View.Window;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public final class SearchController extends Controller {

    private static SearchController instance;
    private SearchTool searchTool;
    private Toolbar toolbar;
    private String currentText;

    private SearchController(Window window) {
        super(window);
        searchTool = (SearchTool) ToolbarController.getInstance(this.window).getToolbar().getTool(ToolType.SEARCH);
        searchTool.addFocusListener();
        toolbar = ToolbarController.getInstance(MainWindowController.getInstance().getWindow()).getToolbar();
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

    public void saveCurrentText() {
        currentText = searchTool.getText();
    }

    public void setToCurrentText() {
        searchTool.setText(currentText);
    }

    public void searchToolThemeChangeEvent() {
        toolbar = ToolbarController.getInstance(window).getToolbar();
        searchTool = (SearchTool) ToolbarController.getInstance(window).getToolbar().getTool(ToolType.SEARCH);
        searchTool.addFocusListener();
        setToCurrentText();
    }

    public void searchToolResizeEvent() {
        saveCurrentText();
        toolbar.rebuildSearchTool(GlobalValue.getSearchFieldSize());
        searchTool = (SearchTool) ToolbarController.getInstance(window).getToolbar().getTool(ToolType.SEARCH);
        searchTool.addFocusListener();
        setToCurrentText();
    }



}
