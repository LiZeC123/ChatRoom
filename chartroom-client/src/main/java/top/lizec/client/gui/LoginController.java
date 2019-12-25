package top.lizec.client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import top.lizec.client.request.UserRequester;
import top.lizec.core.biz.User;
import top.lizec.core.proxy.Context;

public class LoginController implements Initializable {
    public TextField login_username;
    public TextField login_password;
    public Button login_button;
    private ChartroomGUIApplication app;
    private Context context;


    public void loginButtonClick(ActionEvent actionEvent) {
        User user = new User(login_username.getText(), login_password.getText());
        System.out.println(user);
        String u = context.getObjectByType(UserRequester.class).login(user);
        if (u != null) {
            System.out.println("Token is" + u);
            app.gotoMain();
        }
    }


    void setApp(ChartroomGUIApplication app, Context context) {
        this.app = app;
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
