package Controller.ToolbarControllers;

import Controller.Controller;
import Enums.ToolType;
import Helpers.ThemeHelper;
import Helpers.OSDetector;
import Theme.Theme;
import View.PopupWindow;
import View.SearchTool;

import java.util.Iterator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public final class SearchToolController extends Controller {

    private static final String defaultText = "Addresses, points of interest...";

    private static SearchToolController instance;
    private SearchTool searchTool;
    private boolean allowSearch;
    private JSONParser parser = new JSONParser();
    private JSONArray searchHistory;

    private SearchToolController() {
        super();
    }

    public static SearchToolController getInstance() {
        if (instance == null) {
            instance = new SearchToolController();
        }
        return instance;
    }

    protected void setupSearchTool() {
        searchTool = (SearchTool) ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBAR);
        addFocusListenerToSearchTool();
        setToDefaultText();
        specifyKeyBindings();

        try {
            Object obj = parser.parse(new FileReader(OSDetector.getTemporaryPath()  + "searchHistory.json"));
            JSONObject jsonObject = (JSONObject) obj;
            searchHistory = (JSONArray) jsonObject.get("history");
        }catch(Exception e){
            searchHistory = new JSONArray();
        }
    }

    public void resetInstance() {
        instance = null;
    }


    protected void themeHasChanged() {
        setToDefaultText();
    }

    protected void searchToolResizeEvent() {

        setToDefaultText();
        searchTool.adaptSizeToLargeToolbar();
        ToolbarController.getInstance().transferFocusToCanvas();
    }

    protected void searchToolFixedSizeEvent() {
        setToDefaultText();
        searchTool.adaptSizeToSmallToolbar();
        ToolbarController.getInstance().transferFocusToCanvas();
    }

    protected void setToDefaultText() {
        if (searchTool.getText().equals("") || searchTool.getText().equals(defaultText)) {
            searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("defaulttext"));
            searchTool.setText(defaultText);
        }
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
            this.saveHistory(searchTool.getText());

            searchTool.getField().getEditor().selectAll();

            searchTool.getField().requestFocus();
            allowSearch = true;
        }
    }

    private void showMatchingResults(){
        searchTool.getField().removeAllItems();
        searchTool.getField().addItem("");
        searchTool.getField().addItem("Cat");
        searchTool.getField().addItem("Horse");
    }

    private void showHistory(){
        searchTool.getField().removeAllItems();
        searchTool.getField().addItem("");
        Iterator<String> iterator = searchHistory.iterator();
        while (iterator.hasNext()) {
            searchTool.getField().addItem((String)iterator.next());
        }
    }

    private void saveHistory(String query){
        searchHistory.add(query);

        try {
           File file=new File("/tmp/searchHistory.json");
           file.createNewFile();
           FileWriter fileWriter = new FileWriter(file);

           JSONObject jsonObj = new JSONObject();
           jsonObj.put("history", searchHistory);
           fileWriter.write(jsonObj.toJSONString());
           fileWriter.flush();
           fileWriter.close();

       } catch (IOException e) {
           e.printStackTrace();
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
                        break;
                    default:
                        showMatchingResults();
                        break;
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
            if(editor.getItem().equals(defaultText)) {
                showHistory();
                searchTool.setText("");
                searchTool.getField().showPopup();
            } else {
                editor.selectAll();
            }
            editorComponent.setForeground(ThemeHelper.color("icon"));
            allowSearch = true;
        }

        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            setToDefaultText();
            allowSearch = false;
            searchTool.getField().hidePopup();

        }
    }


}
