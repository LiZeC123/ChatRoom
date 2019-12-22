package top.lizec.core.biz;

public class Message {
    private String username;
    private String content;


    public Message() {

    }

    public Message(String username, String content) {
        this.username = username;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
