package models;

import javax.persistence.*;

@SuppressWarnings("ALL")
@NamedQueries({
        @NamedQuery(name="Thread.getThreads", query = "SELECT t FROM Thread t"),
        @NamedQuery(name="Thread.getThreadsbyForum", query = "SELECT t FROM Thread t JOIN ForumCategory f ON t.category = f JOIN Post p on t.threadLastPost = p WHERE f.Name = :fName ORDER BY p.postTime DESC"),
        @NamedQuery(name="Thread.getReplyCount", query = "SELECT Count(p) FROM Thread t JOIN Post p on t = p.thread WHERE (NOT p = t.threadFirstPost) AND t.Id = :tid")
})
@Entity
@Table(name="thread")
public class Thread {
    public Thread(){

    }

    public Thread(ForumCategory category, String threadTitle, User threadAuthor, int threadLocked, int threadType) {
        this.category = category;
        this.threadTitle = threadTitle;
        this.threadAuthor = threadAuthor;
        this.threadLocked = threadLocked;
        this.threadType = threadType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "categoryID", nullable=false)
    private ForumCategory category;


    @Basic(optional=false)
    private String threadTitle;

    @ManyToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "threadAuthor", nullable=false)
    private User threadAuthor;

    @OneToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "threadFirstPost")
    private Post threadFirstPost;

    @OneToOne(fetch = FetchType.LAZY, cascade= CascadeType.ALL)
    @JoinColumn(name = "threadLastPost")
    private Post threadLastPost;

    @Basic(optional=false)
    private int threadLocked;

    @Basic(optional=false)
    private int threadType;

    public ForumCategory getCategory() {
        return category;
    }

    public void setCategory(ForumCategory category) {
        this.category = category;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public User getThreadAuthor() {
        return threadAuthor;
    }

    public void setThreadAuthor(User threadAuthor) {
        this.threadAuthor = threadAuthor;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

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