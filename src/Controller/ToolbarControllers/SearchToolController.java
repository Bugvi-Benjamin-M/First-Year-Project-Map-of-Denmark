package Controller.ToolbarControllers;

import Controller.MainWindowController;
import Controller.SearchController;
import Enums.ToolType;
import Helpers.OSDetector;
import Helpers.ThemeHelper;
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
import java.util.Iterator;


public final class SearchToolController extends SearchController {

    private static final String defaultText = "Search for an address or a city...";

    private static SearchToolController instance;
    private JSONParser parser = new JSONParser();
    private JSONArray searchHistory;
    private boolean isFirstDownAction = true;


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

    public void closeSearchToolList() {
        if(searchTool.getField().isPopupVisible()) {
            setToDefaultText();
            allowSearch = false;
            searchTool.getField().hidePopup();
            ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBUTTON).toggleActivate(false);
            ToolbarController.getInstance().requestCanvasRepaint();
        }
    }

    private void addFocusListenerToSearchTool() {
        searchTool.getField().getEditor().getEditorComponent().addFocusListener(new SearchToolFocusHandler());
    }

    protected Point2D.Float searchActivatedEvent() {
        Point2D.Float point = super.searchActivatedEvent();
        if(isValidSearch()) {
            saveHistory(currentQuery);
            ToolbarController.getInstance().transferFocusToCanvas();
        }
        return point;
    }

    private void showHistory(){
        if(searchHistory.isEmpty()) {
            return;
        }
        searchTool.getField().removeAllItems();
        Iterator<String> iterator = searchHistory.iterator();
        while (iterator.hasNext()) {
            searchTool.getField().addItem(iterator.next());
        }
        searchTool.getField().setSelectedIndex(-1);
        //searchTool.getField().hidePopup();
        searchTool.getField().showPopup();
    }

    private void saveHistory(String query){
        if(searchHistory.contains(query)){
            return;
        }


        searchHistory.add(query);

        try {
            File file=new File(OSDetector.getTemporaryPath()  + "searchHistory.json");
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

    protected void specifyKeyBindings() {
        searchTool.getField().getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (checkForProhibitedKey(e)) {
                    return;
                }
                if (OSDetector.isMac()) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) return;
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) return;
                }

                switch (e.getKeyChar()) {
                    case KeyEvent.VK_ENTER:
                        currentQuery = searchTool.getText();
                            Point2D.Float selectedAddress = searchActivatedEvent();
                            MainWindowController.getInstance().requestCanvasUpdateAddressMarker(selectedAddress);
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
                if(OSDetector.isMac()) {
                    if (searchTool.getField().isPopupVisible()) {
                        if (e.getKeyCode() == KeyEvent.VK_UP) {
                            if(searchTool.getField().getSelectedIndex() == 0) isFirstDownAction = true;
                            if (searchTool.getField().getSelectedIndex() > 0) {
                                searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() - 1);
                                return;
                            } else return;
                        }
                        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                            if (searchTool.getField().getSelectedIndex() < searchTool.getField().getModel().getSize() - 1) {
                                if(isFirstDownAction){
                                    searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() + 1);
                                    searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() - 1);
                                    isFirstDownAction = false;
                                }else {
                                    searchTool.getField().setSelectedIndex(searchTool.getField().getSelectedIndex() + 1);
                                }
                                return;
                            } else return;
                        }
                    }
                }
                if (e.getKeyChar() != KeyEvent.VK_ENTER && e.getKeyChar() != KeyEvent.VK_ESCAPE) {
                    if(queryTimer == null) {
                        queryTimer = new Timer(QUERY_DELAY, ae -> {
                            queryTimer.stop();
                            queryTimer = null;
                            currentQuery = searchTool.getText();
                            if(!currentQuery.equals("")) showMatchingResults();
                            searchTool.setText(currentQuery);
                            if(searchTool.getField().getModel().getSize() <= 8) {
                                searchTool.getField().setMaximumRowCount(searchTool.getField().getModel().getSize());
                            } else if(searchTool.getField().getModel().getSize() > 8)
                            {
                                searchTool.getField().hidePopup();
                                searchTool.getField().setMaximumRowCount(8);
                                searchTool.getField().showPopup();
                            }

                            if(searchTool.getField().getModel() == null || searchTool.getField().getModel().getSize() == 0) searchTool.getField().hidePopup();
                        });
                        queryTimer.start();
                    } else queryTimer.restart();
                }

                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    if (!searchTool.getText().isEmpty()) {
                        allowSearch = true;
                    }
                }
                if (searchTool.getText().isEmpty()) {
                    searchTool.getField().setMaximumRowCount(8);
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
            ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBUTTON).toggleActivate(true);
        }

        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            currentQuery = searchTool.getText();
            setToDefaultText();
            allowSearch = false;
            searchTool.getField().hidePopup();
            ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBUTTON).toggleActivate(false);
            ToolbarController.getInstance().requestCanvasRepaint();
        }
    }


}
