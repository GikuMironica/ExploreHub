package sidebarComponent;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A test class for testing some of the functionality of the {@link SidebarController} class.
 * @author Hidayat Rzayev
 */
public class SidebarControllerTest {

    private SidebarController sidebarController;

    @Before
    public void setUp() {
        sidebarController = new SidebarController();
    }

    /**
     * Tests the functionality to show the sidebar to the user.
     */
    @Test
    public void testShowSidebar() {
        sidebarController.show();
        assertFalse(sidebarController.isHidden());
    }

    /**
     * Tests the functionality to hide the sidebar from the user.
     */
    @Test
    public void testHideSidebar() {
        sidebarController.hide();
        assertTrue(sidebarController.isHidden());
    }
}
