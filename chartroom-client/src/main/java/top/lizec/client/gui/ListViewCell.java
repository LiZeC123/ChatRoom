package top.lizec.client.gui;

import javafx.scene.control.ListCell;

public class ListViewCell extends ListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            UserItem userItem = new UserItem();
            userItem.setInfo("User:" + item, item + "This is some message ANd very very long");
            setGraphic(userItem.getPane());
        }
    }
}
