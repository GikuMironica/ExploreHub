package filterComponent;

import authentification.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Account;
import models.Events;
import models.Location;
import org.apache.commons.lang3.ObjectUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class CriteriaTest {
    ObservableList<Events> events;

    @Before
    public void setUp() throws Exception {
        StrategyContext strategyContext;
        String username = "Gleaves@hs-ulm.de";
        String password = "user85";
        strategyContext = new StrategyContext(new UserStrategy());
        strategyContext.executeStrategy(username, password);


        Account connection = CurrentAccountSingleton.getInstance().getAccount();
        EntityManager entityManager = connection.getConnection();
        TypedQuery getEvents = entityManager.createNamedQuery(
                "Events.findAllEvents",
                Events.class);
        events = FXCollections.observableList(getEvents.getResultList());
    }


    @Test
    public void filterByFreePlaces(){

            Criteria places = new FreePlacesCriteria(15);
        ObservableList<Events> filteredEvents = places.meetCriteria(events);
        for (Events event: filteredEvents
             ) {
            assertTrue(filteredEvents.get(0).getAvailablePlaces()>15 );
        }
    }

    @Test
    public void filterByPrice(){

        Criteria price = new PriceCriteria(100);
        ObservableList<Events> filteredEvents = price.meetCriteria(events);
        for (Events event:filteredEvents
             ) {
            assertTrue(event.getPrice() < 100);
        }

        price = new PriceCriteria(95);
        filteredEvents = price.meetCriteria(events);
        for (Events event:filteredEvents
        ) {
            assertTrue(event.getPrice() < 95);
        }

        price = new PriceCriteria(0);
        filteredEvents = price.meetCriteria(events);
        for (Events event:filteredEvents
        ) {
            assertTrue(event.getPrice() <= 0);
        }
    }

    @Test
    public void getCityCoordinatesTest() throws IOException, ParseException {
        Location location = null;
        try{
            RadiusCriteria radius = new RadiusCriteria(50, "Ulm");
            location = radius.getCityCoordinates("Ulm");
            assertEquals(location.getLatitude(),48.4192186,0.1);
            assertEquals(location.getLongitude(), 9.9323005,0.1 );
        }catch (NullPointerException np){
            assertNull(location);
        }catch(Exception e){
            fail();
        }


    }


    @Test
    public void filterByRadius(){
        try{
            Criteria radius = new RadiusCriteria(100, "Ulm");
            ObservableList<Events> newList = radius.meetCriteria(events);
            for (int i = 0; i < newList.size(); i++){
                assertTrue(RadiusCriteria.distance(newList.get(i).getLocation().getLatitude(),newList.get(i).getLocation().getLongitude(), 48.4011, 9.9876) < 100);
            }
        }catch (Exception np){
            assertTrue(np instanceof NullPointerException);
        }
    }

    @Test
    public void multipleFilter(){
        try{
            Criteria radius = new RadiusCriteria(100, "Ulm");
            Criteria price = new PriceCriteria(80);
            Criteria filter = new AndCriteria(radius, price);
            ObservableList<Events> filteredEvents = filter.meetCriteria(events);
            for (Events event:filteredEvents
                 ) {
                assertTrue( (RadiusCriteria.distance(event.getLocation().getLatitude(), event.getLocation().getLongitude(), 48.4010822,9.9876076) < 100) && event.getPrice() < 80);
            }
        }catch (Exception np){
            assertTrue(np instanceof NullPointerException);
        }

    }


    @Test
    public void distance(){

        double distance = RadiusCriteria.distance(47.9837999, 10.1801883,48.4010822,9.9876076);
        System.out.println(distance);
        assertTrue(distance < 50);
    }

}
