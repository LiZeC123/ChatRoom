package top.lizec.client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController implements Initializable {
    public TextField login_username;
    public TextField login_password;
    public Button login_button;
    private ChartroomGUIApplication app;

    public void loginButtonClick(ActionEvent actionEvent) {
        System.out.println(login_username.getText());
        System.out.println(login_password.getText());
        app.gotoMain();
    }

    public ChartroomGUIApplication getApp() {
        return app;
    }

    public void setApp(ChartroomGUIApplication app) {
        this.app = app;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
