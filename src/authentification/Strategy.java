package authentification;

/**
 * Interface for the Strategy Pattern used for the login process
 * @author Gheorghe Mironica
 */
public interface Strategy {
    /**
     * This method is validating the credentials and resets the current Account
     * @param email this is username
     * @param pass this is the password
     */
    void getAccount(String email, String pass);
}
