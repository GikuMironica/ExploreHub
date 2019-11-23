package bookingComponent;


import authentification.CurrentAccountSingleton;
import models.Events;
import models.Transactions;
import models.User;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;

public class CardPaymentStrategy implements PaymentStrategy {

    private List<Events> evList;
    private Events currentEvent;
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
        evList = ((User) (CurrentAccountSingleton.getInstance().getAccount())).getBookedEvents();

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
                        entityManager.persist(transactions);
                        entityManager.getTransaction().commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else {
                    // Warning telling the user no more spaces available for this certain event and cutting the price down for the total (in confirmation screen)
                }
            }
        }

        generateInvoice();
        resetInterestList();

    }

    public void generateInvoice(){
        // Send email to user and maybe provide a pdf invoice or something
    }
    public void resetInterestList(){

    }
}
