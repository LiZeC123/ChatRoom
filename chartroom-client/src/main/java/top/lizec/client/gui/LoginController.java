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
    public Button signUp_button;
    private ChartroomGUIApplication app;
    private Context context;


    public void loginButtonClick(ActionEvent actionEvent) {
        User user = new User(login_username.getText(), login_password.getText(), null);
        doRequest(user, true);
    }

    public void loginButtonKey() {
        User user = new User(login_username.getText(), login_password.getText(), null);
        doRequest(user, true);
    }

    private void doRequest(User user, boolean isLogin) {
        if (isLogin) {
            System.out.println(user);
            String token = context.getObjectByType(UserRequester.class).login(user);
            if (token != null) {
                System.out.println("Token is" + token);
                context.setValue("name", user.getUsername());
                context.setValue("token", token);
                app.gotoMain();
            } else {
                System.out.println("用户名或密码错误");
            }
        }
    }

    public void singUpButtonClick(ActionEvent actionEvent) {
        User user = new User(login_username.getText(), login_password.getText(), null);
        doRequest(user, false);
    }


    void setApp(ChartroomGUIApplication app, Context context) {
        this.app = app;
        this.context = context;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
