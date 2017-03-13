import Enums.WayType;
import Helpers.FileHandler;
import junit.framework.TestCase;
import Model.*;

/**
 * Created by Nik on 13/03/17.
 */
public class TestOSMHandler extends TestCase {

    public void testOSMHandler(){
        Main.main(null);
        Model model = Model.getInstance();
        FileHandler.loadDefault("/test.osm");
        int RoadCount = model.getWayElements().get(WayType.ROAD).size();
        assertEquals(1, RoadCount);
    }
}
