package Controller.ToolbarControllers;

import Controller.Controller;
import Controller.MainWindowController;
import Enums.ToolType;
import Helpers.ThemeHelper;
import View.PopupWindow;
import View.SearchTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public final class SearchToolController extends Controller {

    private static final String defaultText = "Addresses, points of interest...";

    private static SearchToolController instance;
    private SearchTool searchTool;
    private String currentText;
    private boolean allowSearch;

    private SearchToolController() {
        super();
        specifyWindow(MainWindowController.getInstance().getWindow());
        searchTool = (SearchTool) ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBAR);
        addFocusListenerToSearchTool();
        setToDefaultText();
        specifyKeyBindings();
    }

    public static SearchToolController getInstance() {
        if (instance == null) {
            instance = new SearchToolController();
        }
        return instance;
    }

    public void resetInstance() {
        instance = null;
    }

    protected void saveCurrentText() {
        currentText = searchTool.getText();
    }

    protected void setToCurrentText() {
        if(currentText.equals(defaultText) || currentText.equals("")) setToDefaultText();
        else searchTool.setText(currentText);
    }

    protected void searchToolReplacedEvent() {
        searchTool = (SearchTool) ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBAR);
        addFocusListenerToSearchTool();
        setToCurrentText();
        specifyKeyBindings();
    }

    protected void themeHasChanged() {
        currentText = searchTool.getText();
        setToCurrentText();
    }

    protected void searchToolResizeEvent() {
        searchTool = (SearchTool) ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBAR);
        addFocusListenerToSearchTool();
        if(currentText.equals("")) currentText = defaultText;
        setToCurrentText();
        specifyKeyBindings();
    }

    protected void setToDefaultText() {
        searchTool.setDefaultText(defaultText);
    }

    private void addFocusListenerToSearchTool() {
        searchTool.getField().getEditor().getEditorComponent().addFocusListener(new SearchToolFocusHandler());
    }

    protected void searchActivatedEvent() {
        if(!allowSearch) {
            searchTool.getField().requestFocus();
        }
        else if(allowSearch && searchTool.getField().getEditor().getItem().equals("")) {
            searchTool.getField().requestFocus();
        }
        else if(allowSearch) {
            PopupWindow.infoBox(null, searchTool.getText(), "Search Test");
            searchTool.getField().requestFocus();
            allowSearch = true;
        }
    }

    protected boolean doesSearchbarHaveFocus() {
        return searchTool.getField().getEditor().getEditorComponent().hasFocus();
    }


    private void specifyKeyBindings() {
        searchTool.getField().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ENTER:
                        searchActivatedEvent();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if(searchTool.getField().getEditor().getEditorComponent().hasFocus()) ToolbarController.getInstance().transferFocusToCanvas();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (!searchTool.getText().isEmpty()) {
                        allowSearch = true;
                    }
                }
            }
        });
    }


    private class SearchToolFocusHandler extends FocusAdapter {

        private JComboBox<String> field;
        private ComboBoxEditor editor;
        private Component editorComponent;

        private SearchToolFocusHandler() {
            field = searchTool.getField();
            editor = field.getEditor();
            editorComponent = editor.getEditorComponent();
        }

        @Override
        public void focusGained(FocusEvent e) {
            super.focusGained(e);
            if (!((editor.getItem().equals(defaultText)))) return;
            else {
                searchTool.setText("");
                editorComponent.setForeground(ThemeHelper.color("icon"));
                allowSearch = true;
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            if (editor.getItem().equals("")) setToCurrentText();
                allowSearch = false;
        }
    }


}


