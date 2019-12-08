package models;

import authentification.UserConnectionSingleton;

import javax.persistence.*;
import java.util.List;
import java.util.Set;



@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name= "User.findUserbyEmailPass", query =	"SELECT u FROM User u WHERE u.Email = :email AND u.Password = :password"),
        @NamedQuery(name= "User.findUserbyEmail", query =	"SELECT u FROM User u WHERE u.Email = :email"),
        @NamedQuery(name= "User.findAllUser", query = "SELECT u FROM User u")
})
@NamedNativeQuery(name="checkIfEventInWishList", query ="SELECT Count(*) FROM wishlist WHERE StudentID = ? AND EventID = ?;")

/**
 *Model class which represents the user entity and encapsulates direct access to it
 *
 *@Author: Gheorghe Mironica
 */
@Entity
@DiscriminatorValue(value="0")
public class User extends Account{

    public User(){

    }
    public User(String firstname, String lastname, String email, String password, Courses course) {
        super(firstname, lastname, email, password, course);
    }

    public User(String firstname, String lastname, String email, String password, Courses course, String picture) {
        super(firstname, lastname, email, password, course);
        super.picture = picture;
    }

    /**
     *  Method to access the access the database interface as User
     * @return Entity Manager
     */
    @Override
    public EntityManager getConnection() {
        UserConnectionSingleton u1 = UserConnectionSingleton.getInstance();
        return u1.getManager();
    }

    /**
     * This method closes connection to db
     */
    @Override
    public void closeConnection(){
        UserConnectionSingleton.getInstance().closeConnection();
    }

}
