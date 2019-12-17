package models;

import javax.persistence.*;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="ForumCategory.getCategories", query = "SELECT f FROM ForumCategory f  WHERE f.Name = :fname OR f.Type NOT LIKE \"course\" AND f.Type NOT LIKE \"event\""),
        @NamedQuery(name="ForumCategory.getCategoryByName", query = "SELECT f FROM ForumCategory f WHERE f.Name = :fname"),
        @NamedQuery(name="ForumCategory._getCategories", query = "SELECT f FROM ForumCategory f"),
        @NamedQuery(name="ForumCategory.getCat", query = "SELECT f FROM ForumCategory f JOIN Events e ON f.Name = e.ShortDescription JOIN Transactions t ON t.event = e WHERE t.user = :sid AND t.Completed = 1")
})
@Entity
@Table(name="category")
public class ForumCategory {
    public ForumCategory(){

    }

    public ForumCategory(int categoryid, String categoryName, String type){
        Id = categoryid;
        Name = categoryName;
        Type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int Id;

    @Basic(optional = false)
    private String Name;

    @Basic
    private String Type;

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
}
