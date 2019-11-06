package filterComponent;

import authentification.AdminConnectionSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
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
        AdminConnectionSingleton connection = AdminConnectionSingleton.getInstance();
        EntityManager entityManager = connection.getManager();
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
            assertTrue(event.getPrice() < 0);
        }
    }

    @Test
    public void getCityCoordinatesTest() throws IOException, ParseException {
        RadiusCriteria radius = new RadiusCriteria(50, "Ulm");
        JSONObject jsonObject = radius.getCityCoordinates("Ulm");
        assertTrue(jsonObject.get("city").toString().equalsIgnoreCase("Ulm"));

    }


    @Test
    public void filterByRadius(){
        Criteria radius = new RadiusCriteria(100, "Ulm");
        ObservableList<Events> newList = radius.meetCriteria(events);
        List<String> companies = new ArrayList<>();
        companies.add("BMW");
        companies.add("Daimler");
        companies.add("Elektrobit");
        companies.add("Hochschule Ulm");
        companies.add("Transporeon");
        for (int i = 0; i < newList.size(); i++){
            assertEquals(newList.get(i).getCompany(), companies.get(i));
        }

    }

    @Test
    public void multipleFilter(){
        Criteria radius = new RadiusCriteria(100, "Ulm");
        Criteria price = new PriceCriteria(80);
        Criteria filter = new AndCriteria(radius, price);
        ObservableList<Events> filteredEvents = filter.meetCriteria(events);
        for (Events event:filteredEvents
             ) {
            assertTrue( (RadiusCriteria.distance(event.getLocation().getLatitude(), event.getLocation().getLongitude(), 48.4010822,9.9876076) < 100) && event.getPrice() < 80);
        }

    }


    @Test
    public void distance(){

        double distance = RadiusCriteria.distance(47.9837999, 10.1801883,48.4010822,9.9876076);
        System.out.println(distance);
        assertTrue(distance < 50);
    }

}