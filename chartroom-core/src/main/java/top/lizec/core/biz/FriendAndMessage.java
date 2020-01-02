package top.lizec.core.biz;

public class FriendAndMessage {
    private String friendName;
    private String content;


    public FriendAndMessage() {
    }

    public FriendAndMessage(String friendName, String content) {
        this.friendName = friendName;
        this.content = content;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "FriendAndMessage{" +
                "friendName='" + friendName + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
