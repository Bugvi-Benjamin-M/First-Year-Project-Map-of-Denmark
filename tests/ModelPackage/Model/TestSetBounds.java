package ModelPackage.Model;

import org.junit.Test;
import Model.Model;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by  on .
 *
 * @author bugvimagnussen
 * @version 22/03/2017
 */
public class TestSetBounds {

    @Test
    public void testSetBounds() {
        Model model = Model.getInstance();
        model.setBounds(2.3f,2.4f,2.5f, 2.6f);
        assertEquals(2.3f, model.getMinLatitude());
        assertEquals(2.4f, model.getMaxLatitude());
        assertEquals(2.5f, model.getMinLongitude());
        assertEquals(2.6f, model.getMaxLongitude());

        model.resetInstance();
    }
}
