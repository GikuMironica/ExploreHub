package discussionComponent;

public class ThreadListObject {
    private int threadId;
    private String threadTitle;
    private String threadAuthor;
    private String threadStartedOn;
    private int threadResponseCount;
    private String threadLastResponseAuthor;
    private String threadLastResponseDate;

    public ThreadListObject(int threadId, String threadTitle, String threadAuthor, String threadStartedOn) {
        this.threadId = threadId;
        this.threadTitle = threadTitle;
        this.threadAuthor = threadAuthor;
        this.threadStartedOn = threadStartedOn;
        this.threadResponseCount = 0;
        this.threadLastResponseAuthor = "";
        this.threadLastResponseDate = "";
    }

    public int getThreadId() {
        return threadId;
    }

    public String getThreadAuthor() {
        return threadAuthor;
    }

    public String getThreadStartedOn() {
        return threadStartedOn;
    }

    public String getThreadTitle(){
        return threadTitle;
    }

    public int getThreadResponseCount() {
        return threadResponseCount;
    }

    public String getThreadLastResponseAuthor() {
        if(this.threadLastResponseAuthor == ""){
            return this.getThreadAuthor();
        }
        return threadLastResponseAuthor;
    }

    public String getThreadLastResponseDate() {
        if(this.threadLastResponseDate == ""){
            return this.getThreadStartedOn();
        }
        return threadLastResponseDate;
    }

    public void setThreadResponseCount(int threadResponseCount) {
        this.threadResponseCount = threadResponseCount;
    }

    public void setThreadLastResponseAuthor(String threadLastResponseAuthor) {
        this.threadLastResponseAuthor = threadLastResponseAuthor;
    }

    public void setThreadLastResponseDate(String threadLastResponseDate) {
        this.threadLastResponseDate = threadLastResponseDate;
    }
}
