package Controller.ToolbarControllers;

import Controller.Controller;
import Enums.ToolType;
import Enums.ToolbarType;
import Helpers.OSDetector;
import View.MenuTool;
import View.ToolComponent;
import View.Toolbar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.WEST;

public final class MenuToolController extends Controller {

    private final int POPUP_MARGIN_LEFT = 10;
    private final int POPUP_MARGIN_HEIGHT = 340;
    private final int POPUP_MARGIN_WIDTH = 60;
    private final int POPUP_MARGIN_TOP = 10;
    private final int POPUP_MARGIN_BETWEEN_TOOLS = 63;
    private final int POPUP_MARGIN_LARGER_BETWEEN_TOOLS = 70;
    private final int POPUPMENU_LEFT_OFFSET = 10;
    private final int POPUPMENU_YAXIS_OFFSET = 15;

    private static MenuToolController instance;
    private MenuTool popupMenu;
    private Toolbar toolbar;

    private MenuToolController() { super(); }

    public static MenuToolController getInstance()
    {
        if (instance == null) {
            instance = new MenuToolController();
        }
        return instance;
    }

    protected void setupMenuTool()
    {
        toolbar = ToolbarController.getInstance().getToolbar();
        popupMenu = new MenuTool();
        addActionsToToolsMenu();
    }

    protected void hidePopupMenu()
    {
        if (popupMenu != null && popupMenu.isVisible()) {
            popupMenu.hidePopupMenu();
            if (ToolbarController.getInstance().getType() == ToolbarType.SMALL)
                ToolbarController.getInstance()
                        .getToolbar()
                        .getTool(ToolType.MENU)
                        .toggleActivate(false);
        }
    }

    protected boolean isPopupVisible() {
        if(popupMenu != null) return popupMenu.isVisible();
        else return false;
    }

    protected void setupLayoutForMenuTool()
    {
        ToolComponent load = toolbar.getTool(ToolType.LOAD);
        popupMenu.getLayout().putConstraint(WEST, load, POPUP_MARGIN_LEFT, WEST,
            popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(NORTH, load, POPUP_MARGIN_TOP, NORTH,
            popupMenu.getPopupMenu());
        popupMenu.addTool(load);
        ToolComponent save = toolbar.getTool(ToolType.SAVE);
        popupMenu.getLayout().putConstraint(WEST, save, POPUP_MARGIN_LEFT, WEST,
            popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(NORTH, save, POPUP_MARGIN_BETWEEN_TOOLS,
            NORTH, load);
        popupMenu.addTool(toolbar.getTool(ToolType.SAVE));
        ToolComponent poi = toolbar.getTool(ToolType.POI);
        popupMenu.getLayout().putConstraint(WEST, poi, POPUP_MARGIN_LEFT, WEST,
            popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(NORTH, poi, POPUP_MARGIN_BETWEEN_TOOLS,
            NORTH, save);
        popupMenu.addTool(toolbar.getTool(ToolType.POI));
        ToolComponent routes = toolbar.getTool(ToolType.ROUTES);
        popupMenu.getLayout().putConstraint(WEST, routes, POPUP_MARGIN_LEFT, WEST,
                popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(NORTH, routes, POPUP_MARGIN_BETWEEN_TOOLS,
                NORTH, poi);
        popupMenu.addTool(routes);
        ToolComponent settings = toolbar.getTool(ToolType.SETTINGS);
        popupMenu.getLayout().putConstraint(WEST, settings, POPUP_MARGIN_LEFT, WEST,
            popupMenu.getPopupMenu());
        popupMenu.getLayout().putConstraint(
            NORTH, settings, POPUP_MARGIN_LARGER_BETWEEN_TOOLS, NORTH, routes);
        popupMenu.addTool(toolbar.getTool(ToolType.SETTINGS));
        toolbar.getTool(ToolType.MENU).add(popupMenu.getPopupMenu());
        popupMenu.getPopupMenu().setPopupSize(POPUP_MARGIN_WIDTH,
            POPUP_MARGIN_HEIGHT);
        popupMenu.showPopupMenu();
        popupMenu.hidePopupMenu();
        popupMenu.getPopupMenu().repaint();
    }

    private void addActionsToToolsMenu()
    {
        addAction(KeyEvent.VK_L, OSDetector.getActivationKey(),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (popupMenu.isVisible()) {
                        ToolbarController.getInstance().toolEvent(ToolType.LOAD);
                        hidePopupMenu();
                    }
                }
            });
        addAction(KeyEvent.VK_S, OSDetector.getActivationKey(),
            new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (popupMenu.isVisible()) {
                        ToolbarController.getInstance().toolEvent(ToolType.SAVE);
                        hidePopupMenu();
                    }
                }
            });
        addAction(
            KeyEvent.VK_COMMA, OSDetector.getActivationKey(), new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    if (popupMenu.isVisible()) {
                        ToolbarController.getInstance().toolEvent(ToolType.SETTINGS);
                        hidePopupMenu();
                    }
                }
            });
        addAction(KeyEvent.VK_P, OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(popupMenu.isVisible()) {
                    ToolbarController.getInstance().toolEvent(ToolType.POI);
                    hidePopupMenu();
                }
            }
        });
        addAction(KeyEvent.VK_R, OSDetector.getActivationKey(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(popupMenu.isVisible()) {
                    ToolbarController.getInstance().toolEvent(ToolType.ROUTES);
                    hidePopupMenu();
                }
            }
        });
    }

    private void addAction(int key, int activationKey, AbstractAction action)
    {
        toolbar.getTool(ToolType.MENU)
            .getInputMap(JComponent.WHEN_FOCUSED)
            .put(KeyStroke.getKeyStroke(key, activationKey), action.toString());
        toolbar.getTool(ToolType.MENU)
            .getActionMap()
            .put(action.toString(), action);
    }

    protected void menuToolActivated()
    {
        if (!popupMenu.isVisible()) {
            popupMenu.showPopupMenu();
            toolbar.getTool(ToolType.MENU).grabFocus();
        } else {
            popupMenu.hidePopupMenu();
            ToolbarController.getInstance()
                .getToolbar()
                .getTool(ToolType.MENU)
                .toggleActivate(false);
        }
        popupMenu.setLocation(calculatePosition());
    }

    protected void windowResizedEvent()
    {
        if (popupMenu.isVisible())
            popupMenu.setLocation(calculatePosition());
    }

    protected void windowMovedEvent()
    {
        if (popupMenu.isVisible())
            popupMenu.setLocation(calculatePosition());
    }

    private Point calculatePosition()
    {
        return new Point(toolbar.getLocationOnScreen().x + POPUPMENU_LEFT_OFFSET,
            (toolbar.getLocationOnScreen().y + toolbar.getHeight()) - POPUPMENU_YAXIS_OFFSET);
    }

}
