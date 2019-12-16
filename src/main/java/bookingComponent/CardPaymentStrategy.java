package bookingComponent;


import authentification.CurrentAccountSingleton;
import handlers.Convenience;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import models.*;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public class CardPaymentStrategy implements PaymentStrategy {

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
    public void pay() {

        // Card processing
        // Maybe transaction window that just loads for a few seconds

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
                    paymentMethod = 0;

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


                        Invoice invoice = new Invoice(transactions);
                        entityManager.getTransaction().begin();
                        entityManager.persist(invoice);
                        entityManager.getTransaction().commit();
                    } catch (Exception e) {
                        e.printStackTrace();
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

        generateInvoice();
        updateInterestList(evList);

    }

    public void generateInvoice(){
        // Send email to user and maybe provide a pdf invoice or something
    }

    @SuppressWarnings("Duplicates")
    public void updateInterestList(List<Events> eventList){

        interestList = CurrentAccountSingleton.getInstance().getAccount().getEvents();

        List<Events> bookedEvents = new ArrayList<>(eventList); // Makes the list modifiable

        interestList.removeAll(bookedEvents);
        bookedEvents.clear();

        CurrentAccountSingleton.getInstance().getAccount().setEvents(interestList);
        CurrentAccountSingleton.getInstance().getAccount().setBookedEvents(bookedEvents);

        entityManager = user.getConnection();
        entityManager.getTransaction().begin();
        entityManager.merge(user);
        entityManager.getTransaction().commit();

       /* if(interestList != null) {
            ListIterator iterator = interestList.listIterator();

            while (iterator.hasNext()) {

                interestListEvent = (Events) iterator.next();

                if(event.getId() == interestListEvent.getId()){
                    iterator.remove();
                }
            }

        }*/
    }
}
