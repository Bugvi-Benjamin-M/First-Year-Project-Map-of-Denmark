package Controller.ToolbarControllers;

import Controller.Controller;
import Enums.ToolType;
import Helpers.OSDetector;
import Helpers.ThemeHelper;
import Model.Model;
import View.SearchTool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
    //Todo accept down and up key when the list is not empty
    private final int[] prohibitedKeys = new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
            KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_META, KeyEvent.VK_WINDOWS, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_UNDEFINED};

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
        if(!searchTool.getText().equals(defaultText)) {
            String text = searchTool.getText();
            searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
            searchTool.setText(text);
        }
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

    protected void setToolTip() {
        searchTool.getField().setToolTipText("Search");
    }

    protected void searchActivatedEvent() {
        if(!allowSearch) {
            searchTool.getField().requestFocus();
        }
        else if(allowSearch && searchTool.getText().isEmpty()) {
            searchTool.getField().requestFocus();
        }
        else if(allowSearch) {
            //TODO Does not work as intended
            this.saveHistory(searchTool.getText());
            Point2D p = Model.getInstance().getTst().get(searchTool.getText());
            System.out.println(Model.getInstance().getTst().get(searchTool.getText()));

            ToolbarController.getInstance().transferFocusToCanvas();
            allowSearch = true;
        }
    }

    private void showMatchingResults(){
        //Todo implement proper search
        if(searchTool.getField().isPopupVisible() && searchTool.getField().getItemCount() == 0) searchTool.getField().hidePopup();
        searchTool.getField().removeAllItems();
        ArrayList<String> list = Model.getInstance().getTst().keysThatMatch(currentQuery);
        for(String s : list) {
            searchTool.getField().addItem(s);
        }
        searchTool.getField().showPopup();
    }

    private void showHistory(){
        //Todo does not work as intended
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
    //Todo, make sure that the up and down arrows can be used when history is not empty
    //Todo fix bug regarding the list appearing when pressing the down key. Seems to be a Windows issue.
    private void specifyKeyBindings() {
        searchTool.getField().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (checkForProhibitedKey(e)) {
                    return;
                }


                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ENTER:
                        searchActivatedEvent();
                        break;
                    case KeyEvent.VK_ESCAPE:
                        if (searchTool.getField().getEditor().getEditorComponent().hasFocus())
                            ToolbarController.getInstance().transferFocusToCanvas();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (checkForProhibitedKey(e)) {
                    return;
                }

                if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE && e.getKeyChar() != KeyEvent.VK_ENTER && e.getKeyChar() != KeyEvent.VK_ESCAPE) {
                    currentQuery = searchTool.getText();
                    showMatchingResults();
                    searchTool.setText(currentQuery);
                }

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
                //showHistory();
                searchTool.setText("");
            } else {
                showMatchingResults();
                searchTool.setText(currentQuery);
                editor.selectAll();
            }
            editorComponent.setForeground(ThemeHelper.color("icon"));
            allowSearch = true;
            ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBUTTON).toggleActivate(true);
        }

        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            setToDefaultText();
            allowSearch = false;
            searchTool.getField().hidePopup();
            ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBUTTON).toggleActivate(false);
            ToolbarController.getInstance().requestCanvasRepaint();
        }
    }


}