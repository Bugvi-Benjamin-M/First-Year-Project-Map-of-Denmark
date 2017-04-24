import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

/**
 * Created by Nik on 13/03/17.
 */
public class TestAfter {

    @Before
    public void buildUp()
    {
        System.out.println("Build up");
    }

    @After
    public void tearDown()
    {
        System.out.println("Tear down");
    }

    @Test
    public void test1()
    {
        System.out.println("Test 1");
        assertEquals(true, true);
    }

    @Test
    public void test2()
    {
        System.out.println("Test 2");
        assertEquals(true, true);
    }

    @Test
    public void test3()
    {
        System.out.println("Test 3");
        assertEquals(true, true);
    }
}
