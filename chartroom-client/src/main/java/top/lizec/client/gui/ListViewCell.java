package top.lizec.client.gui;

import javafx.scene.control.ListCell;

public class ListViewCell extends ListCell<UserAndMessage> {


    @Override
    protected void updateItem(UserAndMessage item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            UserItem userItem = new UserItem();
            userItem.setInfo(item.getUsername(), item.getContent());
            setGraphic(userItem.getPane());
        }
    }
}
