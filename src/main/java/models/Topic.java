package models;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Topic.getThreads", query = "SELECT t FROM Topic t"),
        @NamedQuery(name="Topic.getThreadsbyForum", query = "SELECT t FROM Topic t JOIN ForumCategory f " +
                "ON t.category = f JOIN Post p on t.threadLastPost = p WHERE f.Name = :fName ORDER BY p.postTime DESC"),
        @NamedQuery(name="Topic.getReplyCount", query = "SELECT Count(p) FROM Topic t JOIN Post p on t = p.topic" +
                " WHERE (NOT p = t.threadFirstPost) AND t.Id = :tid")
})
@Entity
@Table(name="thread")
public class Topic {
    public Topic(){

    }


    public Topic(ForumCategory category, String threadTitle, Account threadAuthor, int threadLocked, int threadType) {
        this.category = category;
        this.threadTitle = threadTitle.getBytes();
        this.threadAuthor = threadAuthor;
        this.threadLocked = threadLocked;
        this.threadType = threadType;
    }

    @Id
    @Column(length=6)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID", nullable=false)
    private ForumCategory category;


    @Basic(optional=false)
    private byte[] threadTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "threadAuthor", nullable=false)
    private Account threadAuthor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> threadPosts;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "threadFirstPost")
    private Post threadFirstPost;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "threadLastPost", nullable=true)
    private Post threadLastPost;

    @Basic(optional=false)
    @Column(length=1)
    private int threadLocked;

    @Basic(optional=false)
    @Column(length=5)
    private int threadType;

    public ForumCategory getCategory() {
        return category;
    }

    public void setCategory(ForumCategory category) {
        this.category = category;
    }

    public String getThreadTitle() {
        return new String(threadTitle, StandardCharsets.UTF_8);
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle.getBytes();
    }

    public Account getThreadAuthor() {
        return threadAuthor;
    }

    public void setThreadAuthor(Account threadAuthor) {
        this.threadAuthor = threadAuthor;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public List<Post> getPosts() { return threadPosts; }

    public void setPosts(List<Post> posts) { this.threadPosts = posts; }

    public Post getThreadFirstPost() {
        return threadFirstPost;
    }

    public void setThreadFirstPost(Post threadFirstPost) {
        this.threadFirstPost = threadFirstPost;
    }

    public Post getThreadLastPost() {
        return threadLastPost;
    }

    public void setThreadLastPost(Post threadLastPost) {
        this.threadLastPost = threadLastPost;
    }

    public int getThreadLocked() {
        return threadLocked;
    }

    public void setThreadLocked(int threadLocked) {
        this.threadLocked = threadLocked;
    }

    public int getThreadType() {
        return threadType;
    }

    public void setThreadType(int threadType) {
        this.threadType = threadType;
    }
}