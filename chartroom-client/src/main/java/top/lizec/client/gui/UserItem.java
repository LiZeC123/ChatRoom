package top.lizec.client.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class UserItem {
    @FXML
    public ImageView user_header_img;
    @FXML
    public Label username_label;
    @FXML
    public Label user_message_label;
    @FXML
    public AnchorPane pane;

    public UserItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/user_item.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(String name, String content) {
        Image image = new Image("http://i1.hdslb.com/bfs/face/dd040cec07c9630e14d534fec2292d140f8440b8.jpg");
        user_header_img.setImage(image);
        username_label.setText(name);
        user_message_label.setText(content);
    }

    public AnchorPane getPane() {
        return pane;
    }
}
