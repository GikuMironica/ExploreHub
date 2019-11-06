package filterComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
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
            JSONObject jsonObject = getCityCoordinates(city);
            predicate = event -> distance(event.getLocation().getLatitude(),event.getLocation().getLongitude(), Double.valueOf(jsonObject.get("lat").toString()), Double.valueOf(jsonObject.get("lng").toString())) < radius;
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
    public JSONObject getCityCoordinates (String name) {
        try {
            Object object = new JSONParser().parse(new FileReader("res\\europe.json"));
            JSONArray jsonFile =  (JSONArray)object;
            for (Object city :jsonFile
                 ) {
                JSONObject jsonObject = (JSONObject) city;
                if(jsonObject.get("city").toString().equalsIgnoreCase(name)){
                    return jsonObject;
                }
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        return null;
    }

}
