package Helpers.Utilities;

import Enums.ZoomLevel;
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

    private DebugWindow(FPSCounter fpsCounter) {
        this.fpsCounter = fpsCounter;

        container = new TextView();
        window = new Window().title(TITLE)
                .dimension(new Dimension(480,480))
                .layout(new BorderLayout())
                .relativeTo(null);
        window.getFrame().pack();
        setupWindow();
    }

    private void setupWindow() {
        container.reset();
        container.addJLabel("loadtime","loadtime");
        container.addJLabel("lonlabel","lonlabel");
        container.addJLabel("latlabel","latlabel");
        container.addJLabel("fpscount","fpscount");
        container.addJLabel("zoomlabel","zoomlabel");
        container.addJLabel("zoomfactor","zoomfactor");
        container.addJLabel("coastlines","coastlines");

        window.addWindowAdapter(this);
        window.addComponent(BorderLayout.CENTER,container,false);
    }

    public static DebugWindow getInstance() {
        if (instance == null) {
            instance = new DebugWindow(Main.FPS_COUNTER);
        }
        return instance;
    }

    public void setFPSLabel() {
        JLabel retrieved = container.getJLabel("fpscount");
        String label = "FPS: " + fpsCounter.getFPS();
        if(retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("fps label not found");
        }
    }

    public void setZoomLabel() {
        JLabel retrieved = container.getJLabel("zoomlabel");
        String label = ZoomLevel.getZoomLevel().toString();
        if (retrieved != null)  retrieved.setText(label);
        else System.out.println("zoom label not found");
    }

    public void setZoomFactorLabel() {
        JLabel retrieved = container.getJLabel("zoomfactor");
        String label = "Zoom Factor: "+ZoomLevel.getZoomFactor();
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("zoom factor label not found");
        }
    }

    public void setLongitudeLabel() {
        JLabel retrieved = container.getJLabel("lonlabel");
        Model model = Model.getInstance();
        String label = "Longitude Bounds: Min='"+model.getMinLongitude()+"' Max='"+model.getMaxLongitude()+"'";
        if (retrieved != null){
            retrieved.setText(label);
        } else {
            System.out.println("lon label not found");
        }
    }

    public void setLatitudeLabel() {
        JLabel retrieved = container.getJLabel("latlabel");
        Model model = Model.getInstance();
        String label = "Latitude Bounds: Min='"+model.getMinLatitude()+"' Max='"+model.getMaxLatitude()+"'";
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("lat label not found");
        }
    }

    public void setLoadtimeLabel() {
        JLabel retrieved = container.getJLabel("loadtime");
        long loadtime = Main.LOAD_TIME;
        long loadtimeMilliseconds = loadtime / 1000000;
        long loadtimeSeconds = loadtimeMilliseconds / 1000;
        long loadtimeMinutes = loadtimeSeconds / 60;
        String label = "Load time: "+loadtimeMinutes+" m, "
                +(loadtimeSeconds - (loadtimeMinutes * 60))+" s, "
                +(loadtimeMilliseconds - (loadtimeSeconds * 1000))+ " ms";
        // System.out.println(label);
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("loadtime label not found");
        }
    }

    public void setCoastlineLabel() {
        JLabel retrieved = container.getJLabel("coastlines");
        CoastlineFactory factory = Model.getInstance().getCoastlineFactory();
        String label = "Total coastlines: "+factory.getNumberOfCoastlines()+
                " with "+factory.getNumberOfCoastlinePoints()+" points";
        if (retrieved != null) {
            retrieved.setText(label);
        } else {
            System.out.println("coastlines label not found");
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        super.windowOpened(e);
        setupWindow();
        setLoadtimeLabel();
        setLongitudeLabel();
        setLatitudeLabel();
        setFPSLabel();
        setZoomLabel();
        setZoomFactorLabel();
        setCoastlineLabel();

        fpsCounter.start();
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
