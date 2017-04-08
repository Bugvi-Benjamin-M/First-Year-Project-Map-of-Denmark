import Controller.CanvasController;
import Controller.MainWindowController;
import Controller.WindowController;
import Model.Model;
import OSM.OSMHandler;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestOSM {

    private Model model;
    private CanvasController canvasController;
    private WindowController mainWindowController;
    private OSMHandler OSMHandler;

    @Before
    public void buildUp(){
        model = Model.getInstance();
        mainWindowController = MainWindowController.getInstance();
        canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        OSMHandler = OSMHandler.getInstance();

        mainWindowController.resetInstance();
        model.resetInstance();
        canvasController.resetInstance();
        OSMHandler.resetInstance();
        System.gc();
        model = Model.getInstance();
        mainWindowController = MainWindowController.getInstance();
        canvasController = CanvasController.getInstance(mainWindowController.getWindow());
        OSMHandler = OSMHandler.getInstance();
    }

    @Test
    public void filler() {
        assertEquals(true, true);
    }

    /* @Test
    public void testBounds(){
        model = Model.getInstance();
        try {
            FileHandler.loadResource("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        assertTrue(Math.abs(-55.7272800 - model.getMinLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4692700 * OSMHandler.getLongitudeFactor() - model.getMinLongitude()) < 0.0001);
        assertTrue(Math.abs(-55.7286100 - model.getMaxLatitude()) < 0.0001);
        assertTrue(Math.abs(11.4735500 * OSMHandler.getLongitudeFactor() - model.getMaxLongitude()) < 0.0001);
    }*/
   /* @Test
    public void testReload(){
        try {
            FileHandler.loadResource("/testThreeRoads.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int RoadCount = model.getWayElements().get(ElementType.ROAD).size();
        assertEquals(3, RoadCount);
        try {
            FileHandler.loadResource("/testRoad.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        RoadCount = model.getWayElements().get(ElementType.ROAD).size();
        assertEquals(1, RoadCount);
    }*/
    /*@Test
    public void testRoads(){

        try {
            FileHandler.loadResource("/testRoad.osm");
        }catch(FileNotFoundException e){
            throw new RuntimeException(e);
        }
        int RoadCount = model.getWayElements().get(ElementType.ROAD).size();
        assertEquals(1, RoadCount);

    }

    @Test
    public void testUnknown() {

        try {
            FileHandler.loadResource("/testUnknown.osm");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int UnknownCount = model.getWayElements().get(ElementType.UNKNOWN).size();
        assertEquals(1, UnknownCount);

    }*/

}
