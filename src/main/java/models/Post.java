package models;

import jdk.jfr.Name;

import javax.persistence.*;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Post.getPostById", query = "Select p from Post p WHERE p.Id = :pid"),
        @NamedQuery(name="Post.getPostbyThread", query = "SELECT p FROM Post p WHERE p.thread = :t")
})

@Entity
@Table(name="post")
public class Post {
    public Post(){

    }

    public Post(User postAuthor, String postContent, String postTime){
        this.postAuthor = postAuthor;
        this.postContent = postContent;
        this.postTime = postTime;
        this.postLastEdited = postTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "threadID", nullable=false)
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "postAuthor", nullable=false)
    private User postAuthor;

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

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public User getAuthor() {
        return postAuthor;
    }

    public void setUser(User author) {
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
