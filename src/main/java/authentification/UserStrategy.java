package authentification;

import models.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Class part of the strategy pattern serving as a concrete strategy to login as User
 *
 * @author Gheorghe Mironica
 */
public class UserStrategy implements Strategy {

    private CurrentAccountSingleton currentAccount;
    private EntityManager entityManager;
    private UserConnectionSingleton con;

    /**
     * This method is validating the credentials and resets the current Account to an User
     *
     * @param email this is username
     * @param pass this is the password
     */
    @Override
    public void getAccount(String email, String pass) {
        currentAccount = CurrentAccountSingleton.getInstance();
        con = UserConnectionSingleton.getInstance();
        entityManager = con.getManager();

        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<User> tq2 = entityManager.createNamedQuery(
                "User.findUserbyEmailPass",
                User.class);
        tq2.setParameter("email", email);
        tq2.setParameter("password", pass);
        User u1 = tq2.getSingleResult();
        currentAccount.setAccount(u1);
    }
}
