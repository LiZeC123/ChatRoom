package top.lizec.client.gui;

import javafx.scene.control.ListCell;
import top.lizec.core.biz.FriendAndMessage;

public class ListViewCell extends ListCell<FriendAndMessage> {


    @Override
    protected void updateItem(FriendAndMessage item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            UserItem userItem = new UserItem();
            userItem.setInfo(item.getFriendName(), item.getContent());
            setGraphic(userItem.getPane());
        }
    }
}
