package Controller;

import View.Window;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 20/03/2017
 */
public abstract class WindowController extends Controller {

    protected WindowInteractionHandler handler;

    public WindowController(Window window) {
        super(window);
        addInteractionHandlerToWindow();
        specifyKeyBindings();
    }

    private void addInteractionHandlerToWindow() {
        handler = new WindowInteractionHandler();
        window.getFrame().addWindowListener(handler);
    }

    protected abstract void specifyKeyBindings();


    public Window getWindow() {
        return window;
    }

    public abstract void resetInstance();

    protected class WindowInteractionHandler extends WindowAdapter {

        protected JPanel content;


        protected WindowInteractionHandler() {
            this.content = (JPanel) window.getFrame().getContentPane();
        }


        protected void addKeyBinding(int key, int activationKey, AbstractAction event) {
            content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).
                    put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            content.getActionMap().put(event.toString(), event);

        }

        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
        }
    }

}
