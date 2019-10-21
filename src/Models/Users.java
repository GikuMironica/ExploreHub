package Models;

import javax.persistence.*;

@NamedQuery (name=	"Users.SelectUsers",
             query = "SELECT u   FROM Users u")                         // ignore

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Basic(optional = true)
    private int Id;

    @Basic(optional = false)
    private String Name;

    @Basic(optional = false)
    private String Email;

    @Basic(optional = false)
    private int Access;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAccess() {
        return Access;
    }

    public void setAccess(int access) {
        Access = access;
    }
}
