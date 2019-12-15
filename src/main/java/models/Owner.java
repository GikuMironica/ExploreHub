package models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

@SuppressWarnings("JpaQlInspection")
@NamedQuery(name= "Owner.findOwnerByEmailPass", query =	"SELECT u FROM Owner u WHERE u.Email = :email AND u.Password = :password")

@Entity
@DiscriminatorValue(value="2")
public class Owner extends Admin{

    public Owner(){

    }

    public Owner(String firstname, String lastname, String email, String password, Courses course) {
        super(firstname, lastname, email, password, course);
    }

}
