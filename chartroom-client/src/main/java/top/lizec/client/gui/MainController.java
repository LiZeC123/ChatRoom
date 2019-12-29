package top.lizec.client.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class MainController implements Initializable {
    public ListView<String> user_list_view;

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
        ObservableList<String> strList = FXCollections.observableArrayList();
        for (int i = 0; i < 100; i++) {
            strList.add("Item " + i);
        }

        user_list_view.setItems(strList);
        user_list_view.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    System.out.println("old is " + oldValue + ", new is" + newValue);
                });
        user_list_view.setCellFactory(param -> new ListViewCell());

    }

    public void setApp(ChartroomGUIApplication chartroomGUIApplication) {

    }
}
