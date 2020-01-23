package aboutComponent;
/**
 * @author Abdul Basit
 */

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class AboutController implements Initializable {
    @FXML
    private Text version, teamMembers, organisation, copyright;

    /**
     * Method that loads the information from a json on initialization.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new InputStreamReader(getClass().getResourceAsStream("/about.json")));

            JSONObject jsonObject = (JSONObject) obj;

            String jsonVersion = (String) jsonObject.get("version");
            String jsonOrganisation = (String) jsonObject.get("universityName");
            JSONArray jsonTeamMembers = (JSONArray) jsonObject.get("teamMembers");
            String jsonBuildDate = (String) jsonObject.get("build-date");
            String jsonAppName = (String) jsonObject.get("appName");
            String teamMembersList = "";

            version.setText(jsonVersion);
            organisation.setText(jsonOrganisation);
            copyright.setText("Copyright " + jsonBuildDate + " " + jsonAppName);

            Iterator<String> iterator = jsonTeamMembers.iterator();
            while (iterator.hasNext()) {
                teamMembersList += iterator.next();
                if(iterator.hasNext()) teamMembersList += ", ";
            }
            teamMembers.setText(teamMembersList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
