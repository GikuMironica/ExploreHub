package filterComponent;

import authentification.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import listComponent.EventListSingleton;
import models.Account;
import models.Events;
import models.Location;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class CriteriaTest {
    ObservableList<Events> events;
    private Random random;

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
        random = new Random();
    }


    @Test
    public void filterByFreePlaces(){

            Criteria places = new FreePlacesCriteria(15);
        ObservableList<Events> filteredEvents = places.meetCriteria(events);
        for (Events event: filteredEvents
             ) {
            assertTrue(event.getAvailablePlaces()>=15 );
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

    /**
     * Tests the search criteria by randomly extracting keywords from the 3 attributes
     * of an event: short description, long description, company name.
     * The expected result should be at least one event, for which at least one of
     * the listed attributes contains the provided keyword.
     */
    @Test
    public void testSearchByRandomKeywords() {
        // short description + long description
        String keyword = getRandomKeyword(TestTargetType.SHORT_DESCRIPTION) + ' ' +
                            getRandomKeyword(TestTargetType.LONG_DESCRIPTION);
        testSearch(keyword);

        // short description + company name
        keyword = getRandomKeyword(TestTargetType.SHORT_DESCRIPTION) + ' ' +
                    getRandomKeyword(TestTargetType.COMPANY_NAME);
        testSearch(keyword);

        // long description + company name
        keyword = getRandomKeyword(TestTargetType.LONG_DESCRIPTION) + ' ' +
                    getRandomKeyword(TestTargetType.COMPANY_NAME);
        testSearch(keyword);

        // short description + long description + company name
        keyword = getRandomKeyword(TestTargetType.SHORT_DESCRIPTION) + ' ' +
                    getRandomKeyword(TestTargetType.LONG_DESCRIPTION) + ' ' +
                    getRandomKeyword(TestTargetType.COMPANY_NAME);
        testSearch(keyword);

        // short description (x2)
        keyword = getRandomKeyword(TestTargetType.SHORT_DESCRIPTION) + ' ' +
                    getRandomKeyword(TestTargetType.SHORT_DESCRIPTION);
        testSearch(keyword);

        // long description (x2)
        keyword = getRandomKeyword(TestTargetType.LONG_DESCRIPTION) + ' ' +
                getRandomKeyword(TestTargetType.LONG_DESCRIPTION);
        testSearch(keyword);

        // company name (x2)
        keyword = getRandomKeyword(TestTargetType.COMPANY_NAME) + ' ' +
                getRandomKeyword(TestTargetType.COMPANY_NAME);
        testSearch(keyword);
    }

    /**
     * Tests the search criteria by providing it the empty string.
     * The expected result should be the list of all the available events in the DB.
     */
    @Test
    public void testSearchByEmptyString() {
        Criteria searchCriteria = new SearchCriteria("");
        ObservableList<Events> filteredEvents = searchCriteria.meetCriteria(events);

        assertArrayEquals(events.toArray(), filteredEvents.toArray());
    }

    /**
     * Tests the search criteria by some random meaningless string.
     * The expected result should be an empty list, unless there is an event
     * in the DB with the provided search string for any reason.
     */
    @Test
    public void testSearchByNonsenseString() {
        String searchString = "This is a nonsense sentence, it's useless, it should not give any results";
        Criteria searchCriteria = new SearchCriteria(searchString);
        ObservableList<Events> filteredEvents = searchCriteria.meetCriteria(events);
        assertTrue(filteredEvents.isEmpty());

        searchString = "Поиск на русском по идее тоже не должен выдавать никаких результатов";
        searchCriteria = new SearchCriteria(searchString);
        filteredEvents = searchCriteria.meetCriteria(events);
        assertTrue(filteredEvents.isEmpty());
    }

    /**
     * Tests if the given keyword filters the events as expected.
     * According to {@link SearchCriteria} class, all the keywords in the search string
     * have to be present at least in one of the following attributes of an event for it to appear in the search result:
     *     1. Short description
     *     2. Long description
     *     3. Company name
     * @param keyword - the given search string
     */
    private void testSearch(String keyword) {
        Criteria searchCriteria = new SearchCriteria(keyword);
        ObservableList<Events> filteredEvents = searchCriteria.meetCriteria(events);

        for (Events event : filteredEvents) {
            boolean containsKeywords = true;
            for (String word : keyword.toLowerCase().strip().split("\\W+")) {
                if (!event.getShortDescription().toLowerCase().strip().contains(word) &&
                        !event.getLongDescription().toLowerCase().strip().contains(word) &&
                        !event.getCompany().toLowerCase().strip().contains(word)) {
                    containsKeywords = false;
                    break;
                }
            }
            assertTrue(containsKeywords);
        }
    }

    /**
     * Returns a random keyword from the given test target type.
     *
     * @param targetType - an event's attribute, which a random keyword has to be extracted from.
     *                   Possible attributes: short description, long description, company name
     * @return {@link String} object containing a random keyword
     */
    private String getRandomKeyword(TestTargetType targetType) {
        int randomIndex = random.nextInt(events.size());
        String randomEventTestTarget = getTestTarget(events.get(randomIndex), targetType);
        String[] testTargetWords = randomEventTestTarget.split("\\W+");

        randomIndex = random.nextInt(testTargetWords.length);
        return testTargetWords[randomIndex];
    }

    /**
     * Returns one of the 3 attributes of an event based on the test target type.
     *
     * @param event - {@link Events} object, whose attribute should be returned
     * @param targetType - an event's attribute, which a random keyword has to be extracted from.
     *                   Possible attributes: short description, long description, company name
     * @return {@link String} object containing a short description, long description or a company name
     */
    private String getTestTarget(Events event, TestTargetType targetType) {
        if (targetType == TestTargetType.SHORT_DESCRIPTION) {
            return event.getShortDescription();
        } else if (targetType == TestTargetType.LONG_DESCRIPTION) {
            return event.getLongDescription();
        } else {
            return event.getCompany();
        }
    }

    private enum TestTargetType {
        SHORT_DESCRIPTION,
        LONG_DESCRIPTION,
        COMPANY_NAME
    }
}
