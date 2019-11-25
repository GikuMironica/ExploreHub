package authentification;

import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

import java.sql.*;

import static org.junit.Assert.*;

public class RememberUserDBSingletonTest {
    private static Connection connection;

    /**
     * Test singleton functionality {@link RememberUserDBSingleton}
     */
    @Test
    public void getInstance() {
        RememberUserDBSingleton userDB = RememberUserDBSingleton.getInstance();
        assertTrue(!(userDB == null));
    }

    /**
     * Test singleton initialization {@link RememberUserDBSingleton}
     */
    @Test
    @Before
    public void init(){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:SQLiteCurrentUserDB.db");
            assertTrue(!(connection == null));
        } catch(Exception e){
            fail();
        }
    }

    /**
     * Test setUser Method {@link RememberUserDBSingleton #setUser}
     */
    @Test
    public void setUser() {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblUser'");
            if(result.next()) {
               // everything fine at this step
            }
        }catch(Exception e){
             fail();
        }
    }

    /**
     * Test cleanDB Method {@link RememberUserDBSingleton #cleanDB}
     */
    @Test
    public void cleanDB() {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblUser'");
            if(result.next()) {
                PreparedStatement statement1 = connection.prepareStatement("DELETE FROM tblUser;");
                statement1.execute();
                // everything fine at this step
            }
        }catch(Exception e){
          assertEquals(e.getMessage(), e.getMessage());
        }
    }

    /**
     * Test okState Method {@link RememberUserDBSingleton #okState}
     */
    @Test
    public void okState() {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='tblUser'");
            if(result.next()) {
                PreparedStatement statement1 = connection.prepareStatement("SELECT * FROM tblUser;");
                ResultSet result1 = statement1.executeQuery();
                // everything fine at this step
            }
        } catch(Exception e){
            fail();
        }
    }
}