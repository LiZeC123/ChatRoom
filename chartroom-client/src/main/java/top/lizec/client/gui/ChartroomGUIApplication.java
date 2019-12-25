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

public class ChartroomGUIApplication extends Application {

    private static final Logger logger = Logger.getLogger(ChartroomGUIApplication.class.getName());
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        stage.setTitle("XXXX系统");
        gotoLogin();
        stage.show();

    }

    /**
     * 跳转到登录界面
     */
    public void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent(StaticResourcesConfig.LOGIN_VIEW_PATH);
            login.setApp(this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 跳转到主界面
     */
    public void gotoMain() {
        try {
            MainController main = (MainController) replaceSceneContent(StaticResourcesConfig.MAIN_VIEW_PATH);
            main.setApp(this);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 替换场景
     *
     * @param fxml
     * @return
     * @throws Exception
     */
    private Initializable replaceSceneContent(String fxml) throws Exception {
        System.out.println(fxml);
        System.out.println(ChartroomGUIApplication.class.getResource(fxml));
        FXMLLoader loader = new FXMLLoader();
        InputStream in = ChartroomGUIApplication.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(ChartroomGUIApplication.class.getResource(fxml));
        try {
            AnchorPane page = loader.load(in);
            Scene scene = new Scene(page, StaticResourcesConfig.STAGE_WIDTH, StaticResourcesConfig.STAGE_HEIGHT);
            stage.setScene(scene);
            stage.sizeToScene();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "页面加载异常！");
        } finally {
            in.close();
        }
        return (Initializable) loader.getController();
    }
}
