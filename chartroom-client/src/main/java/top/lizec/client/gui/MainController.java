package top.lizec.client.gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import top.lizec.client.message.MessagePool;
import top.lizec.client.request.MessageRequester;
import top.lizec.client.request.UserRequester;
import top.lizec.core.biz.FriendAndMessage;
import top.lizec.core.biz.Message;
import top.lizec.core.biz.User;
import top.lizec.core.proxy.Context;

public class MainController implements Initializable {
    public ListView<FriendAndMessage> user_list_view;
    public Button send_button;
    public TextArea txt_message_list;
    public TextArea txt_send_message;
    private Context context;
    private User thisUser;
    private MessagePool pool;
    //private FriendAndMessage currentSelectedUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private ChangeListener<FriendAndMessage> friendChange = (observable, oldValue, newValue) -> {
        if (newValue == null) {
            // 如果是刷新列表触发的事件 那么oldValue就是之前选中的项目
            updateMessageList(oldValue.getFriendName());
        } else {
            // 否则是更换选择项目触发的事件
            pool.updateCurrentUser(newValue);
            newValue.cleanCount(); // 清除未读消息计数
            // 更新用户列表 去除未读消息标记
            ObservableList<FriendAndMessage> friendList = FXCollections.observableArrayList();
            friendList.addAll(pool.getNewFriendList());
            updateFriendList(friendList);

            updateMessageList(newValue.getFriendName());
        }
    };

    void setApp(Context context) {
        this.context = context;
        thisUser = new User(context.getValueByName("name"), null, context.getValueByName("token"));
        pool = context.getObjectByType(MessagePool.class);
    }

    void initList() {
        ObservableList<FriendAndMessage> friendList = FXCollections.observableArrayList();

        List<FriendAndMessage> friends = context.getObjectByType(UserRequester.class).friendList(thisUser);
        //friends.forEach(System.out::println);
        friendList.addAll(friends);

        pool.updateFriendList(friends);

        updateFriendList(friendList);

        user_list_view.getSelectionModel()
                .selectedItemProperty()
                .addListener(friendChange);
        user_list_view.setCellFactory(param -> new ListViewCell());

        context.setObject("MainController", this);
    }

    private void updateFriendList(ObservableList<FriendAndMessage> friendList) {
        user_list_view.setItems(null);
        user_list_view.setItems(friendList);
    }


    public void update() {
        ObservableList<FriendAndMessage> currentItem = user_list_view.getSelectionModel().getSelectedItems();

        ObservableList<FriendAndMessage> friendList = FXCollections.observableArrayList();
        friendList.addAll(pool.getNewFriendList());
        updateFriendList(friendList);

        if (!currentItem.isEmpty()) {
            user_list_view.getSelectionModel().select(currentItem.get(0));
        }
    }

    private void updateMessageList(String currentFriend) {
        List<Message> messages = pool.getMessageOf(currentFriend);
        StringBuilder builder = new StringBuilder();
        for (Message message : messages) {
            builder.append(toMessageStr(message));
        }
        txt_message_list.setText(builder.toString());
    }

    private String toMessageStr(Message message) {
        if (message.getUsername().equals(thisUser.getUsername())) {
            message.setUsername("自己");
        }

        return String.format("<%s>: %s\n", message.getUsername(), message.getContent());
    }


    public void sendButtonClick(ActionEvent actionEvent) {
        if (pool.getCurrentUser() != null) {
            String receiver = pool.getCurrentUser().getFriendName();
            String content = txt_send_message.getText();
            txt_send_message.setText("");
            Message message = new Message(thisUser.getUsername(), thisUser.getToken(), content, receiver);
            context.getObjectByType(MessageRequester.class).addMessage(message);
        } else {
            System.err.println("当前没有选中的用户");
        }

    }
}
