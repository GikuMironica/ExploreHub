package models;

import javax.persistence.*;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Post.getPostById", query = "Select p from Post p WHERE p.Id = :pid"),
        @NamedQuery(name="Post.getPostbyThread", query = "SELECT p FROM Post p WHERE p.topic = :t")
})

@Entity
@Table(name="post")
public class Post {
    public Post(){

    }

    public Post(Account postAuthor, String postContent, String postTime){
        this.postAuthor = postAuthor;
        this.postContent = postContent;
        this.postTime = postTime;
        this.postLastEdited = postTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.DETACH)
    @JoinColumn(name = "threadID", nullable=false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postAuthor", nullable=false)
    private Account postAuthor;

    @Basic(optional = false)
    private String postContent;

    @Basic(optional = false)
    private String postTime;

    public int getPostID() {
        return Id;
    }

    public void setPostID(int postID) {
        this.Id = postID;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Account getAuthor() {
        return postAuthor;
    }

    public void setUser(Account author) {
        this.postAuthor = author;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostLastEdited() {
        return postLastEdited;
    }

    public void setPostLastEdited(String postLastEdited) {
        this.postLastEdited = postLastEdited;
    }

    @Basic(optional = false)
    private String postLastEdited;
}
