package discussionComponent.views;

public enum PostMode {
    REPLY("Replying to "),
    TOPIC("New Topic in "),
    EDIT("Edit Post");

    private String action;

    public String getAction(){
        return this.action;
    }

    private PostMode(String action){
        this.action = action;
    }
}
