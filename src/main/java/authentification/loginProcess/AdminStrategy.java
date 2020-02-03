package authentification.loginProcess;

import models.Admin;
import persistenceComponent.AdminConnectionSingleton;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * Class part of the strategy pattern serving as a concrete strategy to login as Admin
 * @author Gheorghe Mironica
 */
public class AdminStrategy implements Strategy {
    private CurrentAccountSingleton currentAccount;
    private EntityManager entityManager;
    private AdminConnectionSingleton con;

    /**
     * This method is validating the credentials and resets the current Account to an Admin
     * @param email this is username
     * @param pass this is the password
     */
    @Override
    public void getAccount(String email, String pass) {
        currentAccount = CurrentAccountSingleton.getInstance();
        con = AdminConnectionSingleton.getInstance();
        entityManager = con.getManager();

        @SuppressWarnings("JpaQueryApiInspection")
        TypedQuery<Admin> tq2 = entityManager.createNamedQuery(
                "Admin.findAdminByEmailPass",
                Admin.class);
        tq2.setParameter("email", email);
        tq2.setParameter("password", pass);
        Admin a1 = tq2.getSingleResult();
        currentAccount.setAccount(a1);
    }
}
