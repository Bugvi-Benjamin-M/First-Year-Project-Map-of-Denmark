package Controller.ToolbarControllers;

import Controller.Controller;
import Enums.ToolType;
import Helpers.ThemeHelper;
import View.PopupWindow;
import View.SearchTool;
import View.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public final class SearchToolController extends Controller {

    private static final String defaultText = "Addresses, points of interest...";

    private static SearchToolController instance;
    private SearchTool searchTool;
    private String currentText;
    private boolean allowSearch;

    private SearchToolController(Window window) {
        super(window);
        searchTool = (SearchTool) ToolbarController.getInstance(this.window).getToolbar().getTool(ToolType.SEARCHBAR);
        addFocusListenerToSearchTool();
        setToDefaultText();
        specifyKeyBindings();
    }

    public static SearchToolController getInstance(Window window) {
        if (instance == null) {
            instance = new SearchToolController(window);
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
        if(currentText.equals(defaultText)) setToDefaultText();
        else searchTool.setText(currentText);
    }

    protected void searchToolReplacedEvent() {
        searchTool = (SearchTool) ToolbarController.getInstance(window).getToolbar().getTool(ToolType.SEARCHBAR);
        addFocusListenerToSearchTool();
        setToCurrentText();
        specifyKeyBindings();
    }

    protected void searchToolResizeEvent() {
        searchTool = (SearchTool) ToolbarController.getInstance(window).getToolbar().getTool(ToolType.SEARCHBAR);
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
            System.out.println("case 1");
            searchTool.getField().requestFocus();
        }
        else if(allowSearch && searchTool.getField().getEditor().getItem().equals("")) {
            System.out.println("case 2");
            searchTool.getField().requestFocus();
        }
        else if(allowSearch) {
            System.out.println("case 3");
            PopupWindow.infoBox(null, searchTool.getText(), "Search Test");
            searchTool.getField().requestFocus();
            allowSearch = true;
        }


    }

    private void specifyKeyBindings() {
        addKeyBinding(KeyEvent.VK_ENTER, KeyEvent.VK_UNDEFINED);
    }

    private void addKeyBinding(int key, int activationKey) {
        searchTool.getField().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if(e.getKeyChar() == key) searchActivatedEvent();
            }
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyChar() == key) {
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
            if (editor.getItem().equals("")) setToDefaultText();
                allowSearch = false;
        }
    }


}


