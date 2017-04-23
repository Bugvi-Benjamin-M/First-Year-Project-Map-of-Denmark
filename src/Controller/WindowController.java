package Controller;

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

    protected WindowController() { super(); }

    protected void addInteractionHandlerToWindow()
    {
        handler = new WindowInteractionHandler();
        window.getFrame().addComponentListener(handler);
        specifyKeyBindings();
    }

    protected abstract void specifyKeyBindings();

    public void showWindow() { window.show(); }

    public void hideWindow() { window.hide(); }

    public Window getWindow() { return window; }

    public abstract void resetInstance();

    public void toggleKeyBindings()
    {
        JPanel content = (JPanel)window.getFrame().getContentPane();
        for (Object key : content.getActionMap().keys()) {
            content.getActionMap().get(key).setEnabled(
                PreferencesController.getInstance().getKeyBindingsSetting());
        }
    }

    protected class WindowInteractionHandler extends ComponentAdapter {

        protected JPanel content;

        protected WindowInteractionHandler()
        {
            this.content = (JPanel)window.getFrame().getContentPane();
        }

        @Override
        public void componentResized(ComponentEvent e)
        {
            super.componentResized(e);
        }

        @Override
        public void componentHidden(ComponentEvent e)
        {
            super.componentHidden(e);
        }

        protected void addKeyBinding(int key, int activationKey,
            AbstractAction event)
        {
            content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            content.getActionMap().put(event.toString(), event);
        }
    }
}
