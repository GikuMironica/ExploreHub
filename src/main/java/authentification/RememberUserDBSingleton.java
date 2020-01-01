package authentification;

import models.Account;
import models.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.*;

/**
 * Class which serves as an interface to the local sqlLite database
 *
 * @author Gheorghe Mironica
 */
public class RememberUserDBSingleton {
    private static RememberUserDBSingleton ourInstance;
    private static Connection connection;
    private static boolean hasDB = false;
    private Strategy loginStrategy;
    private int Id;

    /**
     * Method which returns instance of this class
     *
     * @return {@link RememberUserDBSingleton}
     */
    public static RememberUserDBSingleton getInstance() {
        if(ourInstance == null){
            try {
                ourInstance = new RememberUserDBSingleton();
            }catch (Exception e){
            }
        }
        return ourInstance;
    }

    /**
     * Constructor
     */
    private RememberUserDBSingleton() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:CurrentUser.db");
        } catch(Exception e){

        }
    }

    /**
     * Initialize Class with current user
     * @param accountID {@link Integer} id
     */
    public void init(int accountID){
        Id = accountID;
        initialise();
        initialise(accountID);
    }

    /**
     * Method which creates the Local Database once
     */
    private void initialise() {
        if(!hasDB){
            hasDB = true;
            try {
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblUser'");

                if(!result.next()){
                    Statement statement1 = connection.createStatement();
                    statement1.execute("CREATE TABLE tblUser(Id INTEGER , PRIMARY KEY(Id));");
                }

            } catch(Exception e){

            }
        }
    }

    /**
     * Initialize Database
     * @param accountID {@link Integer} account iD
     */
    private void initialise(int accountID) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM tblUser WHERE (tblUser.Id = ?);");
                statement.setString(1,String.valueOf(accountID));
                ResultSet result = statement.executeQuery();

                if(!result.next()){
                    PreparedStatement preparedStatement= connection.prepareStatement("INSERT into tblUser values(?);");
                    preparedStatement.setString(1,String.valueOf(accountID));
                    preparedStatement.execute();
                } else{
                }
            } catch(Exception e){

            }

    }

    /**
     * Get registered users
     * @return {@link ResultSet} result
     */
    public ResultSet getUser(){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tblUser;");
            ResultSet result = statement.executeQuery();
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Method which captures the reference of the current logged in user into a unique singleton class {@link CurrentAccountSingleton}
     */
    @SuppressWarnings("JpaQueryApiInspection")
    public void setUser(){
        String accountID = "0";

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tblUser;");
            ResultSet result = statement.executeQuery();

            while(result.next()){
                accountID = result.getString("Id");
            }

            EntityManager entityManager = GuestConnectionSingleton.getInstance().getManager();
            Account account = entityManager.find(Account.class, Integer.valueOf(accountID));

            int access = account.getAccess();
            String lastUser = account.getEmail();
            String lastPass = account.getPassword();

            if(access==0) {
                loginStrategy = new UserStrategy();
                loginStrategy.getAccount(lastUser, lastPass);
            }else {
                loginStrategy = new AdminStrategy();
                loginStrategy.getAccount(lastUser, lastPass);
            }
        } catch(Exception e){

        }
    }

    /**
     * Method which drops all records from local SQLite database
     */
    public void cleanDB(){
        try {
            PreparedStatement statement1 = connection.prepareStatement("DELETE FROM tblUser;");
            statement1.execute();
        } catch(Exception e){
        }
    }

    /**
     * Method which checks state of DB
     */
    public boolean okState(){
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tblUser;");
            ResultSet result = statement.executeQuery();

            if(result.next()){
               return true;
            } else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}
