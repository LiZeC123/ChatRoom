package top.lizec.server.entity;

public class Friend {
    private String user1;
    private String user2;

    public Friend() {
    }

    public Friend(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "user1='" + user1 + '\'' +
                ", user2='" + user2 + '\'' +
                '}';
    }
}
