package Controller.ToolbarControllers;

import Controller.Controller;
import Enums.ToolType;
import Helpers.ThemeHelper;
import Helpers.OSDetector;
import View.SearchTool;


import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import com.sun.xml.internal.bind.v2.TODO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by Búgvi Magnussen on 02-04-2017.
 */
public final class SearchToolController extends Controller {

    private static final String defaultText = "Type an address, point of interest...";

    private static SearchToolController instance;
    private SearchTool searchTool;
    private boolean allowSearch;
    private JSONParser parser = new JSONParser();
    private JSONArray searchHistory;

    private String currentQuery;
    private final int[] prohibitedKeys = new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
    KeyEvent.VK_UP, KeyEvent.VK_DOWN};

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
        else if(allowSearch && searchTool.getText().isEmpty()) {
            searchTool.getField().requestFocus();
        }
        else if(allowSearch) {
            this.saveHistory(searchTool.getText());


            ToolbarController.getInstance().transferFocusToCanvas();
            allowSearch = true;
        }
    }

    private void showMatchingResults(){
        searchTool.getField().removeAllItems();
        searchTool.getField().addItem("Cat");
        searchTool.getField().addItem("Horse");
        searchTool.getField().showPopup();
    }

    private void showHistory(){
        if(searchHistory.isEmpty()) return;
        searchTool.getField().removeAllItems();
        Iterator<String> iterator = searchHistory.iterator();
        while (iterator.hasNext()) {
            searchTool.getField().addItem(iterator.next());
        }
        searchTool.getField().setSelectedIndex(-1);
        searchTool.getField().showPopup();
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

    private boolean checkForProhibitedKey(KeyEvent e) {
        for(int key : prohibitedKeys) {
            if(e.getKeyCode() == key) return true;
        }
        return false;
    }
    //Todo fix bug regarding the list appearing when pressing the down key
    private void specifyKeyBindings() {
        searchTool.getField().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (checkForProhibitedKey(e)) return;

                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ENTER:
                        searchActivatedEvent();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (searchTool.getField().getEditor().getEditorComponent().hasFocus())
                            ToolbarController.getInstance().transferFocusToCanvas();
                        break;
                    default:
                        if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE) {
                            currentQuery = searchTool.getText();
                            showMatchingResults();
                            searchTool.setText(currentQuery);
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (checkForProhibitedKey(e)) return;

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (!searchTool.getText().isEmpty()) {
                        allowSearch = true;
                    }
                }
                if (searchTool.getText().isEmpty()) {
                    ToolbarController.getInstance().requestCanvasRepaint();
                    searchTool.getField().hidePopup();
                    showHistory();
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
            } else {
                showMatchingResults();
                searchTool.setText(currentQuery);
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
            ToolbarController.getInstance().requestCanvasRepaint();

        }
    }


}
