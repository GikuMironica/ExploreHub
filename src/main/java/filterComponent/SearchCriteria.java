package filterComponent;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Events;

import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A class that implements the Search criteria for the search bar.
 * @author Hidayat Rzayev
 */
public class SearchCriteria implements Criteria {

    private String keyword;

    public SearchCriteria(String keyword) {
        this.keyword = keyword.toLowerCase().strip();
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
     * of an event contain the search keyword typed in the search bar.
     *
     * @param event - current event which is being checked against the keyword
     * @return {@code true}, if the event contains the keyword or if the keyword is blank,
     *         {@code false} otherwise.
     */
    private boolean containsKeyword(Events event) {
        if (keyword.isBlank()) {
            return true;
        }

        String shortDescription = event.getShortDescription().toLowerCase();
        String longDescription = event.getLongDescription().toLowerCase();
        String companyName = event.getCompany().toLowerCase();

        return shortDescription.contains(keyword) ||
                longDescription.contains(keyword) ||
                companyName.contains(keyword);
    }
}
