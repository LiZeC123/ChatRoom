package top.lizec.client.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

class UserItem {
    @FXML
    public ImageView user_header_img;
    @FXML
    public Label username_label;
    @FXML
    public Label user_message_label;
    @FXML
    public Label user_message_count;
    @FXML
    public AnchorPane pane;

    UserItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/user_item.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void setInfo(String name, String content, int unreadCount) {
        Image image = new Image("http://i1.hdslb.com/bfs/face/dd040cec07c9630e14d534fec2292d140f8440b8.jpg");
        user_header_img.setImage(image);
        username_label.setText(name);
        user_message_label.setText(content);
        if (unreadCount == 0) {
            user_message_count.setText("");
        } else {
            user_message_count.setText(String.valueOf(unreadCount));
        }
    }

    AnchorPane getPane() {
        return pane;
    }
}
