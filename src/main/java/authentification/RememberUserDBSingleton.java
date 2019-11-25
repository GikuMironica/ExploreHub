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
    private String user;
    private String pass;

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
                System.out.println("local sqlLiteDB problems -> RememberUserDBSingleton");
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
            connection = DriverManager.getConnection("jdbc:sqlite:SQLiteCurrentUserDB.db");
            System.out.println("local db connection established -> RememberUserDBSingleton");
        } catch(Exception e){
            System.out.println("local sqlLiteDB problems -> C-tor -RememberUserDBSingleton");
            e.printStackTrace();
        }
    }

    /**
     * Initialize Class with current user
     * @param User user email {@link String}
     * @param Pass user password {@link String}
     */
    public void init(String User, String Pass){
        this.user = User;
        this.pass = Pass;

        initialise();
        initialise(user, pass);
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
                    System.out.println("DB empty - building DB 1 time");
                    Statement statement1 = connection.createStatement();
                    statement1.execute("CREATE TABLE tblUser(Email VARCHAR, Pass VARCHAR, PRIMARY KEY(Email));");
                }

            } catch(Exception e){
                e.printStackTrace();
                System.out.println("local sqlLiteDB problems -> C-tor -Initialize");
            }
        }
    }

    /**
     * Initialize Database
     * @param userEmail user email {@link String}
     * @param userPass ser password {@link String}
     */
    private void initialise(String userEmail, String userPass) {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM tblUser WHERE (tblUser.Email = ? AND tblUser.Pass =?);");
                statement.setString(1,userEmail);
                statement.setString(2,userPass);
                ResultSet result = statement.executeQuery();

                if(!result.next()){
                    PreparedStatement preparedStatement= connection.prepareStatement("INSERT into tblUser values(?, ?);");
                    preparedStatement.setString(1, userEmail);
                    preparedStatement.setString(2, userPass);
                    preparedStatement.execute();
                    System.out.println("User successfully saved - RememberUserDBCSingleton - "+userEmail+" "+userPass);
                } else{
                    System.out.println("this user already exists "+userEmail+" "+userPass);
                }
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("local sqlLiteDB problems -> C-tor -Initialize");
            }

    }

    /**
     * Method which captures the reference of the current logged in user into a unique singleton class {@link CurrentAccountSingleton}
     */
    @SuppressWarnings("JpaQueryApiInspection")
    public void setUser(){
        String lastUser = "";
        String lastPass = "";

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tblUser;");
            ResultSet result = statement.executeQuery();

            while(result.next()){
                lastUser = result.getString("Email");
                lastPass = result.getString("Pass");
            }

            EntityManager entityManager = UserConnectionSingleton.getInstance().getManager();
            Query query = entityManager.createNamedQuery("User.determineAccess",
                    User.class);
            query.setParameter("email", lastUser);
            query.setParameter("password", lastPass);
            int access = (Integer)query.getSingleResult();

            if(access==0) {
                loginStrategy = new UserStrategy();
                loginStrategy.getAccount(lastUser, lastPass);
            }else {
                loginStrategy = new AdminStrategy();
                loginStrategy.getAccount(lastUser, lastPass);
            }
        } catch(Exception e){
            System.out.println("user not found - rememberUserDBSingleton.returnLastUser()");
            e.printStackTrace();
        }
    }

    /**
     * Method which drops all records from local SQLite database
     */
    public void cleanDB(){
        try {
            PreparedStatement statement1 = connection.prepareStatement("DELETE FROM tblUser;");
            statement1.execute();
            System.out.println("RememberOption unchecked, local data dropped");

        } catch(Exception e){
            System.out.println(" Probs droping users -> RememberUserDBSingleton.cleanDB()");
            e.printStackTrace();
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
            // db doesn't exist
            return false;
        }
    }
}
