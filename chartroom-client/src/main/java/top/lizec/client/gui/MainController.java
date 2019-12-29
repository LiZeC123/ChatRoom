package top.lizec.client.gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import top.lizec.client.request.UserRequester;
import top.lizec.core.biz.User;
import top.lizec.core.proxy.Context;

public class MainController implements Initializable {
    public ListView<UserAndMessage> user_list_view;
    private Context context;
    private User thisUser;
//
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        primaryStage.setTitle("My Tim");
//        String fxml = "/main.fxml";
//        FXMLLoader loader = new FXMLLoader();
//        try (InputStream in = ChartroomGUIApplication.class.getResourceAsStream(fxml)) {
//            loader.setBuilderFactory(new JavaFXBuilderFactory());
//            loader.setLocation(ChartroomGUIApplication.class.getResource(fxml));
//            AnchorPane page = loader.load(in);
//            Scene scene = new Scene(page);
//            scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
//            primaryStage.setScene(scene);
//            primaryStage.sizeToScene();
//        }
//        primaryStage.show();
//    }

//
//    public static void main(String[] args) {
//        launch(args);
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


    void setApp(Context context) {
        this.context = context;
        thisUser = new User(context.getValueByName("name"), null, context.getValueByName("token"));
    }

    void initList() {
        ObservableList<UserAndMessage> strList = FXCollections.observableArrayList();

        List<String> friends = context.getObjectByType(UserRequester.class).friendList(thisUser);
        List<UserAndMessage> users = friends.stream().map(s -> new UserAndMessage(s, "我觉得不行")).collect(Collectors.toList());
        strList.addAll(users);

        user_list_view.setItems(null);
        user_list_view.setItems(strList);
        user_list_view.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    System.out.println("old is " + oldValue + ", new is" + newValue);
                });
        user_list_view.setCellFactory(param -> new ListViewCell());
    }
}
