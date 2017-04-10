package Controller.ToolbarControllers;

import Enums.ToolType;
import Helpers.OSDetector;
import Helpers.ThemeHelper;
import View.MenuTool;
import View.ToolComponent;
import View.Toolbar;
import View.Window;
import Controller.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.WEST;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 05/04/2017
 */
public final class MenuToolController extends Controller {

    private final int POPUP_MARGIN_LEFT = 10;
    private final int POPUP_MARGIN_HEIGHT = 205;
    private final int POPUP_MARGIN_WIDTH = 60;
    private final int POPUP_MARGIN_TOP = 10;
    private final int POPUP_MARGIN_BETWEEN_TOOLS = 63;
    private final int POPUPMENU_LEFT_OFFSET = 10;
    private final int POPUPMENU_YAXIS_OFFSET = 15;

    private static MenuToolController instance;
    private MenuTool popupMenu;
    private Toolbar toolbar;

    private MenuToolController(Window window) {
        super(window);
        toolbar = ToolbarController.getInstance(window).getToolbar();
    }

    public static MenuToolController getInstance(Window window) {
        if(instance == null) {
            instance = new MenuToolController(window);
        }
        return instance;
    }

    protected void setupMenuTool() {
        toolbar = ToolbarController.getInstance(window).getToolbar();
        if(popupMenu != null && popupMenu.isVisible()) popupMenu.hidePopupMenu();
        popupMenu = new MenuTool();
        setupLayoutForMenuTool();
        setToCurrentTheme();
        addFocusListener();
        addActionsToToolsMenu();
    }

    private void setupLayoutForMenuTool() {
        ToolComponent load = toolbar.getTool(ToolType.LOAD);
        popupMenu.getLayout().putConstraint(WEST, load, POPUP_MARGIN_LEFT, WEST, popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(NORTH, load, POPUP_MARGIN_TOP, NORTH, popupMenu.getPopupMenu());
        popupMenu.addTool(load);
        ToolComponent save = toolbar.getTool(ToolType.SAVE);
        popupMenu.getLayout().putConstraint(WEST, save, POPUP_MARGIN_LEFT, WEST, popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(NORTH, save, POPUP_MARGIN_BETWEEN_TOOLS, NORTH, load);
        popupMenu.addTool(toolbar.getTool(ToolType.SAVE));
        ToolComponent settings = toolbar.getTool(ToolType.SETTINGS);
        popupMenu.getLayout().putConstraint(WEST, settings, POPUP_MARGIN_LEFT, WEST, popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(NORTH, settings, POPUP_MARGIN_BETWEEN_TOOLS, NORTH, save);
        popupMenu.addTool(toolbar.getTool(ToolType.SETTINGS));
        toolbar.getTool(ToolType.MENU).add(popupMenu.getPopupMenu());
        popupMenu.getPopupMenu().setPopupSize(POPUP_MARGIN_WIDTH, POPUP_MARGIN_HEIGHT);
        popupMenu.showPopupMenu();
        popupMenu.hidePopupMenu();
    }

    private void setToCurrentTheme() {
        popupMenu.setBackGroundColor(ThemeHelper.color("toolbar"));
        popupMenu.setForeGroundColor(ThemeHelper.color("icon"));
    }

    private void addActionsToToolsMenu() {
        addAction(KeyEvent.VK_L, OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(popupMenu.isVisible()) ToolbarController.getInstance(window).toolEvent(ToolType.LOAD);
            }
        });
        addAction(KeyEvent.VK_S, OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(popupMenu.isVisible()) ToolbarController.getInstance(window).toolEvent(ToolType.SAVE);
            }
        });
        addAction(KeyEvent.VK_COMMA, OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(popupMenu.isVisible()) ToolbarController.getInstance(window).toolEvent(ToolType.SETTINGS);
            }
        });
    }

    private void addAction(int key, int activationKey, AbstractAction action) {
        toolbar.getTool(ToolType.MENU).getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(key, activationKey), action.toString());
        toolbar.getTool(ToolType.MENU).getActionMap()
                .put(action.toString(), action);
    }

    protected void menuToolActivated() {
        if(!popupMenu.isVisible()) {
            popupMenu.showPopupMenu();
            toolbar.getTool(ToolType.MENU).grabFocus();
        }
        else {
            popupMenu.hidePopupMenu();
        }
        popupMenu.setLocation(calculatePosition());
    }

    private void addFocusListener() {
       toolbar.getTool(ToolType.MENU).addFocusListener(new MenuToolFocusHandler());
    }

    protected void windowResizedEvent() {
        if(popupMenu.isVisible()) popupMenu.setLocation(calculatePosition());
    }

    protected void windowMovedEvent() {
        if(popupMenu.isVisible()) popupMenu.setLocation(calculatePosition());
    }

    private Point calculatePosition() {
        return new Point(toolbar.getLocationOnScreen().x + POPUPMENU_LEFT_OFFSET, (toolbar.getLocationOnScreen().y + toolbar.getHeight()) - POPUPMENU_YAXIS_OFFSET);
    }

    private class MenuToolFocusHandler extends FocusAdapter {

        @Override
        public void focusLost(FocusEvent e) {
            super.focusLost(e);
            popupMenu.hidePopupMenu();
        }
    }
}
