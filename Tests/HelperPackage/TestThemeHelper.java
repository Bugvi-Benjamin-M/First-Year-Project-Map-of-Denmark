package HelperPackage;

import Helpers.ThemeHelper;
import Theme.*;

import org.junit.Test;

import static junit.framework.TestCase.*;

public class TestThemeHelper {

    @Test
    public void testGetDefaultTheme()
    {
        assertEquals("Default", ThemeHelper.getCurrentTheme());
    }

    @Test
    public void testChangeTheme()
    {
        ThemeHelper.setTheme("Night");
        assertEquals("Night", ThemeHelper.getCurrentTheme());
    }

    @Test(expected = Exception.class)
    public void testNonexistantTheme()
    {
        ThemeHelper.setTheme("Danskvand");
        fail();
    }

    @Test
    public void testGetColorFromTheme()
    {
        assertEquals((new DefaultTheme()).park(), ThemeHelper.color("park"));
    }

    @Test(expected = Exception.class)
    public void testNonexistantColor()
    {
        ThemeHelper.color("danskvand");
        fail();
    }
}
