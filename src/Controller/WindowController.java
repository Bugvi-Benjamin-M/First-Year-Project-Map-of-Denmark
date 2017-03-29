package Controller;

import View.PopupWindow;
import View.Window;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 20/03/2017
 */
public abstract class WindowController extends Controller {

    protected WindowInteractionHandler handler;

    protected WindowController(Window window) {
        super(window);
        addInteractionHandlerToWindow();
        specifyKeyBindings();
    }

    protected void addInteractionHandlerToWindow() {
        handler = new WindowInteractionHandler();
        window.getFrame().addComponentListener(handler);
    }

    protected abstract void specifyKeyBindings();


    public Window getWindow() {
        return window;
    }

    public abstract void resetInstance();

    public void toggleKeyBindings(boolean status) {
        JPanel content = (JPanel) window.getFrame().getContentPane();
        for(Object key : content.getActionMap().keys()) {
            content.getActionMap().get(key).setEnabled(status);
        }
    }

    protected class WindowInteractionHandler extends ComponentAdapter {

        protected JPanel content;

        protected WindowInteractionHandler() {
            this.content = (JPanel) window.getFrame().getContentPane();
        }

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            if(window.getFrame().getWidth() < 1100) {
                PopupWindow.infoBox(null, "Window has reached event size", "Resize");
            }
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            super.componentHidden(e);
            //Todo maybe use this method instead of window listener in subclass
        }


        protected void addKeyBinding(int key, int activationKey, AbstractAction event) {
            content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                    put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            content.getActionMap().put(event.toString(), event);

        }

    }
}
