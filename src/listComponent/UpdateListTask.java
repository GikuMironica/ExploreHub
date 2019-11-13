package listComponent;

import javafx.application.Platform;

import java.util.TimerTask;

/**
 * Class which extends {@link TimerTask} which serves as a scheduled job to refresh the Event List
 *
 * @author Gheorghe Mironica
 */
public class UpdateListTask extends TimerTask {

    @Override
    public void run() {
        EventListSingleton eventList = EventListSingleton.getInstance();
        eventList.refreshList();
    }

}
