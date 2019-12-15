package authentification;

import models.Admin;
import models.Owner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class OwnerStrategy implements Strategy {
    private CurrentAccountSingleton currentAccount;
    private EntityManager entityManager;
    private AdminConnectionSingleton con;

    /**
     * This method is validating the credentials and resets the current Account to the owner
     * @param email this is username
     * @param pass this is the password
     */
    @Override
    public void getAccount(String email, String pass) {
        currentAccount = CurrentAccountSingleton.getInstance();
        con = AdminConnectionSingleton.getInstance();
        entityManager = con.getManager();

        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Owner> tq2 = entityManager.createNamedQuery(
                "Owner.findOwnerByEmailPass",
                Owner.class);
        tq2.setParameter("email", email);
        tq2.setParameter("password", pass);
        Owner o1 = tq2.getSingleResult();
        currentAccount.setAccount(o1);
    }
}
