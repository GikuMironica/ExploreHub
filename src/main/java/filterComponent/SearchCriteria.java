package filterComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A class that implements the Search criteria for the search bar.
 * @author Hidayat Rzayev
 */
public class SearchCriteria implements Criteria {

    private List<String> keywords;

    public SearchCriteria(String keyword) {
        keyword = keyword.toLowerCase().trim();
        this.keywords = Arrays.asList(keyword.split("\\W+"));

        if (this.keywords.isEmpty() && !keyword.trim().isEmpty()) {
            this.keywords = Arrays.asList(keyword);
        }
    }

    @Override
    public ObservableList<Events> meetCriteria(ObservableList<Events> events) {
        Predicate<Events> predicate = this::containsKeyword;

        return FXCollections.observableList(events.stream()
                .filter(predicate)
                .collect(Collectors.toList())
        );
    }

    /**
     * Checks if any of the short description, long description or company name
     * of an event contains the search keywords typed in the search bar.
     *
     * @param event - current event which is being checked against the keyword
     * @return {@code true}, if the event contains the keywords or if the keyword is blank,
     *         {@code false} otherwise.
     */
    private boolean containsKeyword(Events event) {
        String shortDescription = event.getShortDescription().toLowerCase();
        String longDescription = event.getLongDescription().toLowerCase();
        String companyName = event.getCompany().toLowerCase();

        for (String word : keywords) {
            if (!shortDescription.contains(word) &&
                    !longDescription.contains(word) &&
                    !companyName.contains(word)) {
                return false;
            }
        }

        return true;
    }
}
