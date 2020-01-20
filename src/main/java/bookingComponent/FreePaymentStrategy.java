package bookingComponent;

import alerts.CustomAlertType;
import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import handlers.HandleNet;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import mainUI.MainPane;
import models.Account;
import models.Events;
import models.Transactions;
import models.User;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FreePaymentStrategy implements PaymentStrategy {

    private List<Events> evList;
    private List<Events> interestList;
    private Events currentEvent;
    private Account user = CurrentAccountSingleton.getInstance().getAccount();
    private LocalDate localDate;
    private Date date;
    private int completed;
    private int paymentMethod;
    private EntityManager entityManager;

    @Override @SuppressWarnings("Duplicates")
    public boolean pay() {
        boolean ok = true;
        entityManager = CurrentAccountSingleton.getInstance().getAccount().getConnection();
        evList = CurrentAccountSingleton.getInstance().getAccount().getBookedEvents();

        if(evList != null) {
            ListIterator iterator = evList.listIterator();
            while (iterator.hasNext()) {
                currentEvent = (Events) iterator.next();

                if(!(currentEvent.getAvailablePlaces() <= 0)) {

                    localDate = LocalDate.now();
                    date = Date.valueOf(localDate);
                    completed = 1;
                    paymentMethod = 2;

                    // New Transaction entry
                    Transactions transactions = new Transactions();

                    transactions.setUser((User) (CurrentAccountSingleton.getInstance().getAccount()));
                    transactions.setEvent(currentEvent);
                    transactions.setDate(date);
                    transactions.setCompleted(completed);
                    transactions.setPaymentMethod(paymentMethod);

                    currentEvent.setAvailablePlaces(currentEvent.getAvailablePlaces() - 1);

                    try {
                        entityManager.getTransaction().begin();
                        entityManager.merge(currentEvent);
                        entityManager.persist(transactions);
                        entityManager.getTransaction().commit();

                        user.getTransactions().add(transactions);
                    } catch (Exception e) {
                        // e.printStackTrace();
                        if(!HandleNet.hasNetConnection()){
                            Convenience.closePreviousDialog();
                            handleConnection();
                            return false;
                        }
                        Convenience.closePreviousDialog();
                        Convenience.showAlert(CustomAlertType.WARNING, "Booking this event is impossible right, for more information contact customer support service.");
                        return false;
                    }
                }

                else {
                    // Warning telling the user no more spaces available for this certain event and cutting the price down for the total (in confirmation screen)
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR,"", ButtonType.OK);
                        alert.setTitle("Booking error!");
                        alert.setHeaderText("Booking failed");
                        alert.setContentText("Booking failed because there are not enough available places. \nFor " + currentEvent.getCompany() + "\n" + currentEvent.getShortDescription());
                        alert.showAndWait();
                    });

                }

            }

        }

        if(ok=true){
            updateInterestList(evList);
        }
        return ok;
    }
    private void handleConnection() {
        try {
            Convenience.showAlert(CustomAlertType.WARNING, "Booking this event is impossible right, for more information contact customer support service.");
        }catch(Exception e) { /**/ }
    }

    @SuppressWarnings("Duplicates")
    public void updateInterestList(List<Events> eventList) {
        interestList = CurrentAccountSingleton.getInstance().getAccount().getEvents();

        List<Events> bookedEvents = new ArrayList<>(eventList); // Makes the list modifiable

        interestList.removeAll(bookedEvents);
        bookedEvents.clear();

        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }
}
