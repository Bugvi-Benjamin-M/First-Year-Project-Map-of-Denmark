package Controller;


import Model.Addresses.Value;
import Model.Model;
import View.PopupWindow;
import View.SearchTool;

import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 08/05/2017
 */
public abstract class SearchController extends Controller {

        protected SearchTool searchTool;
        protected boolean allowSearch;
        private boolean validSearch;
        protected String currentQuery;
        protected final int[] prohibitedKeys = new int[] {KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT, KeyEvent.VK_ALT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT,
                KeyEvent.VK_META, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_WINDOWS, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_UNDEFINED};
        protected javax.swing.Timer queryTimer;
        protected final int QUERY_DELAY = 1000;

        protected SearchController() {
            super();
        }

        protected abstract void setupSearchTool();

        protected abstract void themeHasChanged();

        public abstract void closeSearchToolList();

        public void setToolTip(String tip) {
            searchTool.getField().setToolTipText(tip);
        }

        protected Point2D.Float searchActivatedEvent() {
            validSearch = false;
            if(!allowSearch) {
                System.out.println("Allow search is false");
                searchTool.getField().requestFocus();
            }
            else if(allowSearch && searchTool.getText().isEmpty()) {
                System.out.println("textbox is empty");
                searchTool.getField().requestFocus();
            }
           // else if(allowSearch) {
                Point2D.Float point = null;
                ArrayList<Value> list = Model.getInstance().getTst().get(searchTool.getText());
                //if there exists a value
                if(list != null) {
                    validSearch = true;
                    if (list.size() > 1) {
                        String[] cities = buildCityNameList(list);
                        int resultIndex = selectCity(cities);
                        if(resultIndex >= 0) {
                            point = new Point2D.Float(list.get(resultIndex).getX(), list.get(resultIndex).getY());
                        }
                    } else {
                        point = new Point2D.Float(list.get(0).getX(), list.get(0).getY());
                    }
                }else{
                    String[] matches = manageSearchResults();
                    if(matches.length > 0) {
                        selectAddress(matches);
                        ArrayList<Value> selectedStrings = Model.getInstance().getTst().get(currentQuery);
                        if(selectedStrings != null) {
                            point = new Point2D.Float(selectedStrings.get(0).getX(), selectedStrings.get(0).getY());
                        }
                    }
                }
                allowSearch = true;
                return point;
            //}
            //return null;
        }

        protected String[] buildCityNameList(ArrayList<Value> list){
            String[] cities = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getCitynameindex() != 0) {
                    cities[i] = Model.getInstance().getIndexToCity(list.get(i).getCitynameindex());
                } else {
                    cities[i] = searchTool.getText() + " " + i;
                }
            }
            return cities;
        }

        protected int selectCity(String[] cities){
            Arrays.sort(cities);
            String result = PopupWindow.confirmBox(null, "Select a City:", "Multiple Search Results!", cities);
            if(result != null) {
                int resultIndex = 0;
                for (int i = 0; i < cities.length; i++) {
                    if (cities[i].equals(result)) {
                        resultIndex = i;
                        break;
                    }
                }
                return resultIndex;
            }else return -1;
        }

        protected void selectAddress(String[] matches){
            Arrays.sort(matches);
            String result = PopupWindow.confirmBox(null, "Select an Address:", "Multiple Search Results!", matches);
            if(result != null) {
                validSearch = true;
                currentQuery = result;
                searchTool.setText(currentQuery);
            }
        }

        protected void showMatchingResults() {
            if (searchTool.getField().isPopupVisible() && searchTool.getField().getItemCount() == 0)
            searchTool.getField().hidePopup();
            searchTool.getField().removeAllItems();
            if(currentQuery == null || currentQuery.equals("")) {
                searchTool.getField().removeAllItems();
                return;
            } else {
            String[] listToShow = manageSearchResults();
                if(listToShow == null) return;
                for (String s : listToShow) {
                searchTool.getField().addItem(s);
                }
            }
            //searchTool.getField().hidePopup();
            searchTool.getField().showPopup();
        }

        private String[] manageSearchResults(){
            if(currentQuery == null) return null;
            HashMap<Boolean, ArrayList<String>> map = Model.getInstance().getTst().keysThatMatch(currentQuery.toLowerCase());
            ArrayList<String> listToShow = new ArrayList<>();
            for (String s : map.get(true)) {
                listToShow.add(s);
            }
            int i = 0;
            if(currentQuery.length() < 4) {
                while (listToShow.size() <= 10 && i < map.get(false).size()) {
                    listToShow.add(map.get(false).get(i));
                    i++;
                }
            }else{
                while (i < map.get(false).size()) {
                    listToShow.add(map.get(false).get(i));
                    i++;
                }
            }
            String[] matchesArray = new String[listToShow.size()];
            for (int j = 0; j < matchesArray.length ; j++) {
                matchesArray[j] = listToShow.get(j);
            }
            return matchesArray;
        }


        public boolean doesSearchbarHaveFocus() {
            return searchTool.getField().getEditor().getEditorComponent().hasFocus();
        }

        protected boolean checkForProhibitedKey(KeyEvent e) {
            for (int key : prohibitedKeys) {
                if (e.getKeyCode() == key) return true;
            }
            return false;
        }

        protected abstract void specifyKeyBindings();

    protected boolean isValidSearch() {
        return validSearch;
    }

    protected void setCurrentQuery(String query) {
        currentQuery = query;
    }

}
