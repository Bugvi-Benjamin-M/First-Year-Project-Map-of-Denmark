package Model;

import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Enums.OSMEnums.WayType;
import Enums.RoadType;
import org.junit.Test;

import java.awt.geom.Path2D;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestAddWayElement {

    @Test
    public void testAddWayElement() {
        Model model = Model.getInstance();
        WindowController mainWindowController = MainWindowController.getInstance();
        CanvasController canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        assertEquals(0, model.getWayElements().get(WayType.ROAD).size());
        Path2D path = new Path2D.Float();
        model.addWayElement(WayType.ROAD, new Road(RoadType.SERVICE, path));
        assertEquals(1, model.getWayElements().get(WayType.ROAD).size());


        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
    }
}
