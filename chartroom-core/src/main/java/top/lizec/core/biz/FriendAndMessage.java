package top.lizec.core.biz;

import java.io.Serializable;

public class FriendAndMessage implements Serializable {
    private static final long serialVersionUID = -1526986696217487186L;
    private String friendName;
    private String content;
    private Integer unreadCount;


    public FriendAndMessage() {
    }

    public FriendAndMessage(String friendName, String content) {
        this.friendName = friendName;
        this.content = content;
        this.unreadCount = 0;
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

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void incCount() {
        unreadCount += 1;
    }

    public void cleanCount() {
        unreadCount = 0;
    }

    @Override
    public String toString() {
        return "FriendAndMessage{" +
                "friendName='" + friendName + '\'' +
                ", content='" + content + '\'' +
                ", unreadCount=" + unreadCount +
                '}';
    }
}
