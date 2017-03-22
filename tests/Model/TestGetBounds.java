package Model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestGetBounds {

    @Test
    public void testGetBounds() {
        Model model = Model.getInstance();
        assertTrue(model.getMinLatitude() == 0.0f);
        assertTrue(model.getMaxLatitude() == 0.0f);
        assertTrue(model.getMinLongitude() == 0.0f);
        assertTrue(model.getMaxLongitude() == 0.0f);

        model.resetInstance();
    }

}
