package top.lizec.client.gui;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import top.lizec.core.proxy.Context;

public class ChartroomGUIApplication extends Application {
    private static Context context;

    private static final Logger logger = Logger.getLogger(ChartroomGUIApplication.class.getName());
    private Stage stage;

    public static void setContext(Context context) {
        ChartroomGUIApplication.context = context;
    }

    public static void launchApp() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("My Tim");
        gotoLogin();
        stage.show();

    }

    /**
     * 跳转到登录界面
     */
    private void gotoLogin() {
        System.out.println("Goto Login");
        try {
            LoginController login = (LoginController) replaceSceneContent(StaticResourcesConfig.LOGIN_VIEW_PATH);
            login.setApp(this, context);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 跳转到主界面
     */
    void gotoMain() {
        System.out.println("Goto Main");
        try {
            MainController main = (MainController) replaceSceneContent(StaticResourcesConfig.MAIN_VIEW_PATH);
            main.setApp(this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 替换场景
     */
    private Initializable replaceSceneContent(String fxml) {
        System.out.println(fxml);
        System.out.println(ChartroomGUIApplication.class.getResource(fxml));
        FXMLLoader loader = new FXMLLoader();
        try (InputStream in = ChartroomGUIApplication.class.getResourceAsStream(fxml)) {
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            loader.setLocation(ChartroomGUIApplication.class.getResource(fxml));
            AnchorPane page = loader.load(in);
            Scene scene = new Scene(page);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "页面加载异常！");
        }
        return (Initializable) loader.getController();
    }
}
