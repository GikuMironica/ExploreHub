package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@SuppressWarnings("JpaQlInspection")
@NamedQuery(name= "Owner.findOwnerByEmailPass", query =	"SELECT u FROM Owner u WHERE u.Email = :email AND u.Password = :password")

/**
 * Model class which represents the owner entity and encapsulates direct access to it
 *
 * @author: Gheorghe Mironica
 */
@Entity
@DiscriminatorValue(value="2")
public class Owner extends Admin{

    /**
     * Default constructor
     */
    public Owner(){

    }

    /**
     * Custom Constructor
     * @param firstname {@link String} input param
     * @param lastname {@link String} input param
     * @param email {@link String} input param
     * @param password {@link String} input param
     * @param course {@link Courses} input param
     */
    public Owner(String firstname, String lastname, String email, String password, Courses course) {
        super(firstname, lastname, email, password, course);
    }

}
