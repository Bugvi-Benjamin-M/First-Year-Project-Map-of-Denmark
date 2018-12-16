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

/**
 * This class is responsible for handling the interaction with the address searchbar.
 * It extends SearchController which implements the autocompletion functionality.
 */
public final class SearchToolController extends SearchController {

    private static final String defaultText = "Search for an address or a city...";

    private static SearchToolController instance;
    private JSONParser parser = new JSONParser();
    private JSONArray searchHistory;
    private boolean isFirstDownAction = true;


    private SearchToolController() {
        super();
    }

    /**
     * This method replaces the contructor, and is called when a
     * reference to the object is needed. (Singleton)
     * @return an Instance of the class.
     */
    public static SearchToolController getInstance() {
        if (instance == null) {
            instance = new SearchToolController();
        }
        return instance;
    }

    /**
     * Sets up the search tool by adding a focus listener to the searchbar, setting
     * the searchbar to a default text, specifying keybindings for the searchbar, creating a JSON object which is used
     * to save the history of the user input.
     */
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

    /**
     * Resets the singleton.
     * If getInstance is called afterwards, a new instance is created.
     */
    public void resetInstance() {
        instance = null;
    }

    /**
     * Changes the visual appearance of the components dependent on the current theme.
     */
    protected void themeHasChanged() {
        setToDefaultText();
        if(!searchTool.getText().equals(defaultText)) {
            String text = searchTool.getText();
            searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("icon"));
            searchTool.setText(text);
        }
    }

    /**
     * Resizes the search tool appropriate when a resize event is started.
     */
    protected void searchToolResizeEvent() {
        setToDefaultText();
        searchTool.adaptSizeToLargeToolbar();
        ToolbarController.getInstance().transferFocusToCanvas();
    }

    /**
     * Assures that the searchbar stops resizing when a given minimum size is reached.
     */
    protected void searchToolFixedSizeEvent() {
        setToDefaultText();
        searchTool.adaptSizeToSmallToolbar();
        ToolbarController.getInstance().transferFocusToCanvas();
    }

    /**
     * Sets the searchtool to a default text which gives the user an idea of what
     * the purpose of the searchtool is.
     */
    protected void setToDefaultText() {
        if (searchTool.getText().equals("") || searchTool.getText().equals(defaultText)) {
            searchTool.getField().getEditor().getEditorComponent().setForeground(ThemeHelper.color("defaulttext"));
            searchTool.setText(defaultText);
        }
    }

    /**
     * Closes the searchtool popup.
     * Sets the default text.
     * The user is currently not able to press enter and get any output (allowSearch = false)
     */
    public void closeSearchToolList() {
        if(searchTool.getField().isPopupVisible()) {
            setToDefaultText();
            allowSearch = false;
            searchTool.getField().hidePopup();
            ToolbarController.getInstance().getToolbar().getTool(ToolType.SEARCHBUTTON).toggleActivate(false);
            ToolbarController.getInstance().requestCanvasRepaint();
        }
    }

    /**
     * Adds a focus listener to the searchtool.
     */
    private void addFocusListenerToSearchTool() {
        searchTool.getField().getEditor().getEditorComponent().addFocusListener(new SearchToolFocusHandler());
    }

    /**
     * Calls the searchActivatedEvent method of the superclass and saves it
     * as history if it is a valid search. (If the address exists)
     * @return
     */
    protected Point2D.Float searchActivatedEvent() {
        Point2D.Float point = super.searchActivatedEvent();
        if(isValidSearch()) {
            saveHistory(searchTool.getText());
            ToolbarController.getInstance().transferFocusToCanvas();
        }
        return point;
    }

    /**
     * Shows the history of recent user input.
     * The history is only shown when the searchtool is empty.
     */
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

    /**
     * Saves the history to a JSON file.
     * @param query The current string in the searchtool field.
     */
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

    /**
     * Adds keybindings to the searchtool.
     * Enter is used to activate a search.
     * Escape is used to exit the searchtool field.
     * Any key except the two above are used to start an autocompletion search in the TST.
     * Up and down are used to navigate through the autocompletion popup.
     */
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

    /**
     * Manages the focus of the searchtool.
     * If the searchtool loses focus, the autocompletion popup gets hidden,
     * searching is disabled and the field of the searchtool is set to the default text.
     * If the searchtool gains focus, the history of the recent user inputs is shown and searching
     * is enabled.
     */
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
