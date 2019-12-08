package models;

import authentification.AdminConnectionSingleton;
import authentification.UserConnectionSingleton;
import javax.persistence.*;
import java.util.List;
import java.util.Set;

@SuppressWarnings("JpaQlInspection")
@NamedQueries({
        @NamedQuery(name= "Admin.findAdminByEmailPass", query =	"SELECT u FROM Admin u WHERE u.Email = :email AND u.Password = :password"),
        @NamedQuery(name= "Admin.findAdminbyEmail", query =	"SELECT u FROM Admin u WHERE u.Email = :email"),
        @NamedQuery(name= "Admin.findAdmins", query = "SELECT a FROM Admin a WHERE a.Access = :access")
})

/**
 *Model class which represents the admin entity and encapsulates direct access to it
 *
 * @author: Gheorghe Mironica
 */
@Entity
@DiscriminatorValue(value="1")
public class Admin extends Account{

    public Admin(){

    }
    public Admin(String firstname, String lastname, String email, String password, Courses course) {
        super(firstname, lastname, email, password, course);
        super.Access = 1;
    }

    public Admin(String firstname, String lastname, String email, String password, Courses course, String picture) {
        super(firstname, lastname, email, password, course);
        super.picture = picture;
        super.Access = 1;
    }

    /**
     *Method to access the access the database interface as Administrator
     *@return Entity Manager
     */
    @Override
    public EntityManager getConnection() {
        AdminConnectionSingleton u1 = AdminConnectionSingleton.getInstance();
        return u1.getManager();
    }

    /**
     * This method closes connection to db
     */
    @Override
    public void closeConnection(){
        AdminConnectionSingleton.getInstance().closeConnection();
    }


}
