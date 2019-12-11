package filterComponent;

import authentification.CurrentAccountSingleton;
import authentification.UserConnectionSingleton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import models.Location;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *Class that implements the radius criteria for filter.
 * @author Aleksejs Marmiss
 *
 */
public class RadiusCriteria implements Criteria {

    private int radius;
    private String city;

    /**
     * Constructor fro Radius Criteria.
     * @param radius radius in kilometers as integer.
     * @param city city which is the center of search area. Must be a String.
     */
    public RadiusCriteria(int radius, String city){
        this.radius = radius;
        this.city = city;
    }

    /**
     *Method that checks which events meet the criteria. If event does not meet criteria, it is removed from the list.
     * @param events Observable list of event.
     * @return Observable list of event that meet this criteria.
     */
    @Override
    public ObservableList<Events> meetCriteria(ObservableList<Events> events) {
        Predicate<Events> predicate;
        if(radius != 0 && city != null) {
            Location location = getCityCoordinates(city);
            predicate = event -> distance(event.getLocation().getLatitude(),event.getLocation().getLongitude(), location.getLatitude(), location.getLongitude()) < radius;
        }else if(radius == 0 && city != null){
            predicate = event-> event.getLocation().getCity().equalsIgnoreCase(this.city);
        }else {return events;}
            return FXCollections.observableList(events.stream()
                .filter( predicate )
                .collect(Collectors.<Events>toList()));
    }

    /**
     *  Method which calculates the distance between two places based on their coordinates.
     * @param lat1 latitude of first place. Must be double.
     * @param lon1 longitude of first place. Must be double.
     * @param lat2 latitude of second place. Must be double.
     * @param lon2 longitude of second place. Must be double.
     * @return distance in km as double.
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
            return (dist);
        }
    }

    /**
     *  Methods that analyzes json file and gets an information about certain city.
     * @param name name of the city as String.
     * @return json Object which contains the information about the city.
     */
    private Location getCityCoordinates (String name) {
        ObservableList<Location> locations = FXCollections.observableArrayList();
        EntityManager entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
        TypedQuery<Location> locationQuery;
        locationQuery = entityManager.createNamedQuery(
                "Location.findAllLocation",
                Location.class);
        locations.addAll(locationQuery.getResultList());
        for (Location location:locations
             ) {
            if(location.getCity().equalsIgnoreCase(name)){
                return location;
            }
        }
        return null;
    }
}
