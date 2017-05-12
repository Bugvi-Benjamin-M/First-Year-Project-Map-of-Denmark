package Helpers.Utilities;

import Enums.BoundType;
import Enums.ZoomLevel;
import Helpers.HelperFunctions;
import Main.Main;
import Model.Coastlines.CoastlineFactory;
import Model.Model;
import View.Window;
import View.TextView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

    private DebugWindow(FPSCounter fpsCounter)
    {
        this.fpsCounter = fpsCounter;

        container = new TextView();
        window = new Window()
                     .title(TITLE)
                     .dimension(new Dimension(480, 480))
                     .layout(new BorderLayout())
                     .relativeTo(null);
        window.getFrame().pack();
        setupWindow();
    }

    private void setupWindow()
    {
        container.reset();
        container.addJLabel("loadtime", "loadtime");
        container.addJLabel("lonlabel", "lonlabel");
        container.addJLabel("latlabel", "latlabel");
        container.addJLabel("camera", "camera");
        container.addJLabel("fpscount", "fpscount");
        container.addJLabel("zoomlabel", "zoomlabel");
        container.addJLabel("zoomfactor", "zoomfactor");
        container.addJLabel("coastlines", "coastlines");

        window.addWindowAdapter(this);
        window.addBorderLayoutComponent(BorderLayout.CENTER, container, false);
    }

    public static DebugWindow getInstance()
    {
        if (instance == null) {
            instance = new DebugWindow(Main.FPS_COUNTER);
        }
        return instance;
    }

    public void setFPSLabel()
    {
        JLabel retrieved = container.getJLabel("fpscount");
        String label = "FPS: " + fpsCounter.getFPS();
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("fps label not found");
        }
    }

    public void setZoomLabel()
    {
        JLabel retrieved = container.getJLabel("zoomlabel");
        String label = ZoomLevel.getZoomLevel().toString();
        if (retrieved != null)
            retrieved.setText(label);
        else
            System.out.println("zoom label not found");
    }

    public void setZoomFactorLabel()
    {
        JLabel retrieved = container.getJLabel("zoomfactor");
        String label = "Zoom Factor: " + ZoomLevel.getZoomFactor();
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("zoom factor label not found");
        }
    }

    public void setLongitudeLabel()
    {
        JLabel retrieved = container.getJLabel("lonlabel");
        Model model = Model.getInstance();
        String label = "Longitude Bounds: Min='" + model.getMinLongitude(false) + "' Max='" + model.getMaxLongitude(false) + "'";
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("lon label not found");
        }
    }

    public void setLatitudeLabel()
    {
        JLabel retrieved = container.getJLabel("latlabel");
        Model model = Model.getInstance();
        String label = "Latitude Bounds: Min='" + model.getMinLatitude(false) + "' Max='" + model.getMaxLatitude(false) + "'";
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("lat label not found");
        }
    }

    public void setLoadtimeLabel()
    {
        JLabel retrieved = container.getJLabel("loadtime");
        long loadtime = Main.LOAD_TIME;
        String label = "Load time: "+
                HelperFunctions.simplifyNanoTime(loadtime);
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("loadtime label not found");
        }
    }

    public void setCoastlineLabel()
    {
        JLabel retrieved = container.getJLabel("coastlines");
        CoastlineFactory factory = Model.getInstance().getCoastlineFactory();
        String label = "Total coastlines: " + factory.getNumberOfCoastlines() + " with " + factory.getNumberOfCoastlinePoints() + " points";
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("coastlines label not found");
        }
    }

    public void setCameraBoundsLabel()
    {
        JLabel retrieved = container.getJLabel("camera");
        Model model = Model.getInstance();
        String label = "Camera: (" + model.getCameraBound(BoundType.MIN_LONGITUDE) + "; " + model.getCameraBound(BoundType.MAX_LONGITUDE) + ") - (" + model.getCameraBound(BoundType.MIN_LATITUDE) + "; " + model.getCameraBound(BoundType.MAX_LATITUDE) + ")";
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("camera label not found");
        }
    }

    @Override
    public void windowOpened(WindowEvent e)
    {
        super.windowOpened(e);
        setupWindow();
        setLoadtimeLabel();
        setLongitudeLabel();
        setLatitudeLabel();
        setCameraBoundsLabel();
        setFPSLabel();
        setZoomLabel();
        setZoomFactorLabel();
        setCoastlineLabel();

        fpsCounter.start();
    }

    @Override
    public void windowClosed(WindowEvent e)
    {
        super.windowClosed(e);
        fpsCounter.stopThread();
    }

    public void show() { window.show(); }

    public void hide() { window.hide(); }
}
