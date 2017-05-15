package Controller;

import View.Window;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * The WindowController is a superclass of other window controllers.
 */
public abstract class WindowController extends Controller {

    protected WindowInteractionHandler handler;

    /**
     * Creates a WindowController Object.
     */
    protected WindowController() { super(); }

    /**
     * Adds an interactionHandler to the window. The interactionHandler is
     * a MouseAdapter.
     */
    protected void addInteractionHandlerToWindow()
    {
        handler = new WindowInteractionHandler();
        window.getFrame().addComponentListener(handler);
        specifyKeyBindings();
    }

    protected abstract void specifyKeyBindings();

    /**
     * Shows the window.
     */
    public void showWindow() { window.show(); }

    /**
     * Hides the window.
     */
    public void hideWindow() { window.hide(); }

    /**
     * Returns the window to the client.
     * @return the window.
     */
    public Window getWindow() { return window; }

    public abstract void resetInstance();

    /**
     * Toggles keybindings associated with the window based on the current
     * user setting.
     */
    public void toggleKeyBindings()
    {
        JPanel content = (JPanel)window.getFrame().getContentPane();
        for (Object key : content.getActionMap().keys()) {
            content.getActionMap().get(key).setEnabled(
                PreferencesController.getInstance().getKeyBindingsSetting());
        }
    }

    /**
     * A WindowInteractionHandler registers events related to the window, as well
     * as adds keybindings to it. The WindowInteractionHandler is a ComponentAdapter.
     */
    protected class WindowInteractionHandler extends ComponentAdapter {

        protected JPanel content;

        /**
         * Creates a WindowInteractionHandler Object.
         */
        protected WindowInteractionHandler()
        {
            this.content = (JPanel)window.getFrame().getContentPane();
        }

        /**
         * Registers ComponentResized events.
         * @param e the component resized event.
         */
        @Override
        public void componentResized(ComponentEvent e)
        {
            super.componentResized(e);
        }

        /**
         * Registers ComponentHidden events.
         * @param e the component hidden event.
         */
        @Override
        public void componentHidden(ComponentEvent e)
        {
            super.componentHidden(e);
        }

        /**
         * Adds a key binding to the window.
         * @param key the keybinding.
         * @param activationKey a key to be pressed down in order to activate the main key (if any).
         * @param event the event to be triggered on a the given keybinding.
         */
        protected void addKeyBinding(int key, int activationKey,
            AbstractAction event)
        {
            content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(key, activationKey), event.toString());
            content.getActionMap().put(event.toString(), event);
        }
    }
}
