package top.lizec.core.biz;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = -1278495680803802349L;
    private String username;
    private String token;
    private String content;
    private String receiver;


    public Message() {

    }

    public Message(String username, String token, String content, String receiver) {
        this.username = username;
        this.token = token;
        this.content = content;
        this.receiver = receiver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", content='" + content + '\'' +
                ", receiver='" + receiver + '\'' +
                '}';
    }
}
