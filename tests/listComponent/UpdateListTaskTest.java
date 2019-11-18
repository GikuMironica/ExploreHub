package listComponent;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class which tests the functionality of the {@link UpdateListTask}
 *
 * @author Gheorghe Mironica
 */
public class UpdateListTaskTest {

    /**
     * This method checks the functionality of the run method, should start, execute succesfuly the thread
     *
     */
    @Test
    public void run() {
        EventListSingleton eventList = EventListSingleton.getInstance();
        assertTrue(!(eventList==null));
        System.out.println(Thread.currentThread().getName()+" scheduled job triggered");
        eventList.refreshList();
    }
}