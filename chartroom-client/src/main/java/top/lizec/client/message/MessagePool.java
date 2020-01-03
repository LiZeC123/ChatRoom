package top.lizec.client.message;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    @Automatique
    MessageRequester messageRequester;

    private HashMap<String, List<Message>> messageMap;
    private List<FriendAndMessage> friendList;
    private FriendAndMessage currentUser;
    private String thisUserName;
    private boolean hasInit = false;

    public MessagePool() {

    }

    synchronized private void init() {
        thisUserName = context.getValueByName("name");
        if (!hasInit) {
            synchronized (this) {
                if (!hasInit) {
                    try {
                        File folder = new File(thisUserName);
                        if (!folder.exists() && folder.mkdir()) {
                            messageMap = new HashMap<>();
                            friendList = new ArrayList<>();
                        } else if (folder.exists()) {
                            messageMap = loadData("./" + thisUserName + "/message");
                            friendList = loadData("./" + thisUserName + "/friend");
                        } else {
                            throw new RuntimeException("目录创建失败");
                        }
                    } catch (FileNotFoundException e) {
                        messageMap = new HashMap<>();
                        friendList = new ArrayList<>();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new RuntimeException("数据加载失败");
                    }
                    hasInit = true;
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T loadData(String name) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(name + ".data")))) {
            return (T) in.readObject();
        }
    }

    private void saveDate() {
        String name = "./" + thisUserName + "/message.data";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(name)))) {
            out.writeObject(messageMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        name = "./" + thisUserName + "/friend.data";
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(name)))) {
            out.writeObject(friendList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMessage(String name, Message message, boolean isSelf) {
        init();
        if (!messageMap.containsKey(name)) {
            messageMap.put(name, new ArrayList<>());
        }
        messageMap.get(name).add(message);
        Optional<FriendAndMessage> var = friendList.stream().filter(friendAndMessage -> friendAndMessage.getFriendName().equals(name))
                .findFirst();


        if (var.isPresent()) {
            FriendAndMessage item = var.get();
            item.setContent(message.getContent());
            // 自己的消息不计数, 当前用户的消息不计数
            if (!isSelf && !name.equals(currentUser.getFriendName())) {
                item.incCount();
            }
        } else {
            friendList.add(new FriendAndMessage(name, message.getContent()));
        }
        saveDate();
    }

    public void updateCurrentUser(FriendAndMessage currentUser) {
        this.currentUser = currentUser;
    }

    public FriendAndMessage getCurrentUser() {
        return currentUser;
    }

    public void updateFriendList(List<FriendAndMessage> friends) {
        init();
        friends.forEach(friend -> {
            FriendAndMessage old = findSameUser(friend);

            if (old == null) {
                // 如果没有则更新
                updateMessageFor(friend);
            } else if (!Objects.equals(friend.getContent(), old.getContent())) {
                // 如果有且与当前的本地内容有区别 并根据比较结果拉取远程的数据
                System.out.println("Update Message List For " + friend.getFriendName());
                updateMessageFor(friend);
            }
        });
        this.friendList = friends;
        saveDate();
    }

    private void updateMessageFor(FriendAndMessage friend) {
        Message relationship = new Message(thisUserName, context.getValueByName("token"), null, friend.getFriendName());
        List<Message> list = messageRequester.getMessageByUser(relationship);
        if (messageMap.containsKey(friend.getFriendName())) {
            friend.setUnreadCount(list.size() - messageMap.get(friend.getFriendName()).size());
        }

        messageMap.put(friend.getFriendName(), list);
        saveDate();
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
        init();
        return friendList;
    }

    /**
     * 获得指定用户的消息列表
     */
    public List<Message> getMessageOf(String username) {
        init();
        if (messageMap.get(username) == null) {
            return new ArrayList<>();
        } else {
            return messageMap.get(username);
        }
    }
}
