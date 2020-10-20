package top.lizec.server.entity;

import top.lizec.core.biz.Message;

public class MessageR {
    private String username;
    private String content;
    private String receiver;
    private boolean send;

    public MessageR() {
    }

    public MessageR(Message message) {
        this.username = message.getUsername();
        this.content = message.getContent();
        this.receiver = message.getReceiver();
        this.send = false;
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

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public boolean isSend() {
        return send;
    }

    public void setSend(boolean send) {
        this.send = send;
    }
}
