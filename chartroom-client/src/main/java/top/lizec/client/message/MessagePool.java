package top.lizec.client.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import top.lizec.client.request.MessageRequester;
import top.lizec.core.annotation.Automatique;
import top.lizec.core.annotation.Component;
import top.lizec.core.biz.FriendAndMessage;
import top.lizec.core.biz.Message;
import top.lizec.core.proxy.Context;

@Component
public class MessagePool {
    @Automatique
    private Context context;

    private HashMap<String, List<Message>> messageMap;
    private List<FriendAndMessage> friendList;

    public MessagePool() {
        try {
            messageMap = loadData("message");
            friendList = loadData("friend");
        } catch (FileNotFoundException e) {
            messageMap = new HashMap<>();
            friendList = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("数据加载失败");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T loadData(String name) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(name + ".data")));
        return (T) in.readObject();
    }

    public void addMessage(String name, Message message) {
        if (!messageMap.containsKey(name)) {
            messageMap.put(name, new ArrayList<>());
        }
        messageMap.get(name).add(message);
        Optional<FriendAndMessage> var = friendList.stream().filter(friendAndMessage -> friendAndMessage.getFriendName().equals(name))
                .findFirst();


        if (var.isPresent()) {
            var.get().setContent(message.getContent());
        } else {
            friendList.add(new FriendAndMessage(name, message.getContent()));
        }
    }


    public void updateFriendList(List<FriendAndMessage> friends) {
        friends.forEach(friend -> {
            FriendAndMessage old = findSameUser(friend);

            if (old == null) {
                // 如果没有则更新
                updateMessageFor(friend);
            } else if (!Objects.equals(friend.getContent(), old.getContent())) {
                // 如果有则与当前的本地内容有区别 并根据比较结果拉取远程的数据
                System.out.println("Update Message List For " + friend.getFriendName());
                updateMessageFor(friend);
            }
        });
        this.friendList = friends;
    }

    private void updateMessageFor(FriendAndMessage friend) {
        Message relationship = new Message(context.getValueByName("name"), context.getValueByName("token"), null, friend.getFriendName());
        List<Message> list = context.getObjectByType(MessageRequester.class).getMessageByUser(relationship);
        messageMap.put(friend.getFriendName(), list);
    }

    private FriendAndMessage findSameUser(FriendAndMessage origin) {
        return friendList.stream()
                .filter(friend -> friend.getFriendName().equals(origin.getFriendName()))
                .findFirst().orElse(null);
    }

    /**
     * 获得最新状态的用户好友列表
     *
     * @return 用户好友列表
     */
    public List<FriendAndMessage> getNewFriendList() {
        return friendList;
    }

    /**
     * 获得指定用户的消息列表
     */
    public List<Message> getMessageOf(String username) {
        if (messageMap.get(username) == null) {
            return new ArrayList<>();
        } else {
            return messageMap.get(username);
        }
    }
}
