import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import Parser.Address;

/**
 * Created by Nik on 13/03/17.
 */
public class TestParser {


    @Test
    public void fullAddress(){
        Address addr = Address.parse("Kongevej 4, 1234 Frederiksby");
        assertEquals("Kongevej", addr.street());
        assertEquals("4", addr.house());
        assertEquals("1234", addr.postcode());
        assertEquals("Frederiksby", addr.city());
    }

    public void city(){
        Address addr = Address.parse("1234 Frederiksby");
        assertEquals("1234", addr.postcode());
        assertEquals("Frederiksby", addr.city());
    }

    public void road(){
        Address addr = Address.parse("Kongevej 4");
        assertEquals("Kongevej", addr.street());
        assertEquals("4", addr.house());
    }
}
