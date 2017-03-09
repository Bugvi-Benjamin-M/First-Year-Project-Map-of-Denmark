package Controller;

import Enums.ToolType;
import Helpers.FileHandler;
import Helpers.OSDetector;
import Model.Model;
import View.Toolbar;
import View.Window;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Class details:
 *
 * @author Andreas Blanke, blan@itu.dk
 * @version 06-03-2017.
 * @project BFST
 */
public class ToolbarController extends Controller {
    private Window window;
    private Toolbar toolbar;

    public ToolbarController(Window window) {
        toolbar = new Toolbar();
        this.window = window;
        this.window.addComponent(BorderLayout.PAGE_START,toolbar);
        addMouseListenersToTools();
    }

    private void addMouseListenersToTools() {
        addMouseListenerToLoadTool();
    }

    private void addMouseListenerToLoadTool() {
        toolbar.addMouseListenerToTool(ToolType.LOAD, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "OSM files", "osm");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);
                int returnVal = chooser.showOpenDialog(null);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    System.out.println("You chose to open this file: " +
                            file.getName());
                    Model.getInstance().clear();
                    if(OSDetector.isIsWindows()) {
                        FileHandler.load("file:" + file.toString());
                    } else if(OSDetector.isIsMac()) {
                        FileHandler.load("file://" + file.toString());
                    } else {
                        FileHandler.load(file.getAbsolutePath());
                    }
                }

            }
        });
    }

}
