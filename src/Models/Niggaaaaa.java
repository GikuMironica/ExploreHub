package Models;

import javax.persistence.*;

@Entity
public class Niggaaaaa {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Basic(optional = false)
    int Id;

    @Basic(optional = false)
    String login;

    @Basic(optional = false)
    String password;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
