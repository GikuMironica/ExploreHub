package Models;

import javax.persistence.*;

@Entity
public class My {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    int Id;

    @Basic(optional=true)
    String Job;

    @Basic(optional = true)
    int Age;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getJob() {
        return Job;
    }

    public void setJob(String job) {
        Job = job;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }
}
