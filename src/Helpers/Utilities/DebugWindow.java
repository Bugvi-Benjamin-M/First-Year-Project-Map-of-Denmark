package Helpers.Utilities;

import Controller.CanvasController;
import Controller.MainWindowController;
import Main.Main;
import Model.Model;
import View.Window;
import View.View;
import View.TextView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.TextEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 28-03-2017.
 * @project BFST
 */
public class DebugWindow extends WindowAdapter {

    private static DebugWindow instance;

    private FPSCounter fpsCounter;
    private Window window;
    private TextView container;
    private static final String TITLE = "DEBUG WINDOW";

    private DebugWindow(FPSCounter fpsCounter) {
        container = new TextView();
        window = new Window().title(TITLE)
                .dimension(new Dimension(640,480))
                .layout(new BorderLayout())
                .relativeTo(null);
        window.addComponent(BorderLayout.CENTER,container);
        this.fpsCounter = fpsCounter;
    }

    public static DebugWindow getInstance() {
        if (instance == null) instance = new DebugWindow(Main.FPS_COUNTER);
        return instance;
    }

    public void setFPSLabel() {
        JLabel retrieved = container.getJLabel("fpscount");
        String label = "FPS: " + fpsCounter.getFPS();
        if (retrieved == null) container.addJLabel("fpscount",label);
        else retrieved.setText(label);
    }

    public void setZoomLabel(double zoom_factor) {
        JLabel retrieved = container.getJLabel("zoomlabel");
        String label = "Zoom factor: " + zoom_factor;
        if (retrieved == null) container.addJLabel("zoomlabel",label);
        else retrieved.setText(label);
    }

    public void setBoundsLabel() {
        JLabel retrieved = container.getJLabel("boundslabel");
        Model model = Model.getInstance();
        String label = "Bounds: minlon: "+model.getMinLongitude()+"; maxlon: "+model.getMaxLongitude()+
                "\n     minlat: "+model.getMinLatitude()+"; maxlat: "+model.getMaxLatitude();
        if (retrieved == null) container.addJLabel("boundslabel",label);
        else retrieved.setText(label);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        fpsCounter.start();
        setZoomLabel(CanvasController.getInstance(MainWindowController.getInstance().getWindow())
                .getMapCanvas().getZoomLevel());
        setFPSLabel();
        setBoundsLabel();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        super.windowClosed(e);
        fpsCounter.stopThread();
    }

    public void show() {
        window.show();
    }

    public void hide() {
        window.hide();
    }
}
