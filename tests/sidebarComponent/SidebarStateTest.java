package sidebarComponent;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A test class for testing the functionality of the {@link SidebarState} class.
 * @author Hidayat Rzayev
 */
public class SidebarStateTest {

    /**
     * Tests the functionality to save the state of the sidebar to the file.
     */
    @Test
    public void testSaveStateHidden() {
        SidebarState.saveStateHidden(true);
        assertTrue(SidebarState.getStateHidden());

        SidebarState.saveStateHidden(false);
        assertFalse(SidebarState.getStateHidden());
    }
}
